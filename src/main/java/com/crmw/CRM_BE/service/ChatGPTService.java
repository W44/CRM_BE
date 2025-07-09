package com.crmw.CRM_BE.service;

import com.crmw.CRM_BE.dto.DonerContextResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ChatGPTService {

    @Value("${OPENAI_API_KEY}")
    private String openaiApiKey;

    @Autowired
    private DonerContextService donorContextService;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ChatGPTService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public ResponseEntity<?> emailGeneration(Integer donorId, String instruction) {
        String url = "https://api.openai.com/v1/chat/completions";

        //TODO: add function to limit records by top 3 or so on..
        List<DonerContextResponseDto> donorContext = donorContextService.getDonerContextsByDonerId(donorId);
        String context = donorContextService.convertDonorContextsToChatGPTContext(donorContext);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role", "system",
                "content", """
                        You are a professional email generator that strictly outputs valid JSON only, in the following structure:

                        {
                            "body": [
                                "<paragraph 1>",
                                "<paragraph 2>",
                                ...
                            ]
                        }

                        Hard rules:
                         - DO NOT include "Dear", "Hi", "Hello", or any greetings.
                         - DO NOT include "Regards", "Sincerely", "Best", or any sign-offs.
                         - ONLY generate the body paragraphs of the email, continuing the previous style and tone.
                         - Keep the email under 500 words in total.
                         - Output only JSON, no explanations, no Markdown, and no additional text.
                         - If instructions conflict, prioritize removing greetings and sign-offs.
                         - If previous context included greetings or sign-offs, IGNORE them and continue with the core body only.
                         - Each paragraph should be clear and direct.
                        """
        ));
        messages.add(Map.of(
                "role", "user",
                "content", """
                        Instruction: %s
                        Context: %s
                        """.formatted(instruction, context)
        ));

        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            JsonNode chatGptJson = objectMapper.readTree(content);

            return ResponseEntity.ok(chatGptJson);

        } catch (Exception e) {
            System.out.println("ChatGPT Error: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(objectMapper.createObjectNode().put("error", e.getMessage()));
        }
    }

    public ResponseEntity<?> generateContextSummary(Integer donorId) {
        String url = "https://api.openai.com/v1/chat/completions";

        //TODO: add function to limit records by top 3 or so on..
        //TODO: add more validations if no context present
        List<DonerContextResponseDto> donorContext = donorContextService.getDonerContextsByDonerId(donorId);
        String context = donorContextService.convertDonorContextsToChatGPTContext(donorContext);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role", "system",
                "content", """
                        You are a professional summary generator that strictly outputs valid JSON only, in the following structure:

                        {
                            "points": [
                                "<point 1>",
                                "<point 2>",
                                ...
                            ]
                        }

                        Rules:
                        - Generate only clear, concise bullet-style points, no paragraphs.
                        - No introduction, no closing statements, no greetings, no sign-offs.
                        - capture the main theme of all the context provided and highlight personal insights.
                        - Keep the summary under 300 words in total.
                        - Output only JSON, no explanations, no Markdown, and no additional text.
                        """
        ));
        messages.add(Map.of(
                "role", "user",
                "content", """
                        Context: %s
                        """.formatted(context)
        ));

        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            JsonNode chatGptJson = objectMapper.readTree(content);

            return ResponseEntity.ok(chatGptJson);

        } catch (Exception e) {
            System.out.println("ChatGPT Error: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(objectMapper.createObjectNode().put("error", e.getMessage()));
        }
    }
}
