package com.crmw.CRM_BE.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsAppService {

    private final RestTemplate restTemplate;
    @Value("${WHATSAPP_TOKEN}")
    private String TOKEN;

    @Value("${WHATSAPP_PHONEID}")
    private String PHONE_NUMBER_ID;

    public WhatsAppService() {
        this.restTemplate = new RestTemplate();
    }

    public void sendHelloWorldTemplate(String recipientPhoneNumber) {
        String url = "https://graph.facebook.com/v22.0/" + PHONE_NUMBER_ID + "/messages";

        String payload = """
                {
                  "messaging_product": "whatsapp",
                  "to": "%s",
                  "type": "template",
                  "template": {
                    "name": "testing",
                    "language": {
                      "code": "en_US"
                    },
                    "components": [
                      {
                        "type": "body",
                        "parameters": [
                          { "type": "text", "text": "Arslaan 27th birthday party" },
                          { "type": "text", "text": "Arslaan Testing" },
                          { "type": "text", "text": "Jan 1" },
                          { "type": "text", "text": "7 pm" },
                          { "type": "text", "text": "212 Valencia Town Lahore" }
                        ]
                      }
                    ]
                  }
                }
                """.formatted(recipientPhoneNumber);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(TOKEN);

        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            System.out.println("Response: " + response.getBody());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}