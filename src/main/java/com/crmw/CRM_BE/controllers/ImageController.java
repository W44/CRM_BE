package com.crmw.CRM_BE.controllers;

import com.crmw.CRM_BE.entity.UserHistory;
import com.crmw.CRM_BE.entity.Users;
import com.crmw.CRM_BE.enums.HistoryTypes;
import com.crmw.CRM_BE.repository.IUserHistoryRepository;
import com.crmw.CRM_BE.repository.IUsersRepository;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

    @Autowired
    IUserHistoryRepository iUserHistoryRepository;
    @Autowired
    IUsersRepository iUsersRepository;

    public Users getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return iUsersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }


    @PostMapping("/translate")
    public ResponseEntity<String> translateImageToText(@RequestParam("image") MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body("No image uploaded.");
        }

        ByteString imgBytes = ByteString.readFrom(imageFile.getInputStream());

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();

        List<AnnotateImageRequest> requests = new ArrayList<>();
        requests.add(request);


        String json = System.getenv("GOOGLE_CREDENTIALS").replace("\\n", "\n");
        GoogleCredentials credentials = ServiceAccountCredentials
                .fromStream(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(() -> credentials)
                .build();


        List<String> results = new ArrayList<>();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create(settings)) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            for (AnnotateImageResponse res : response.getResponsesList()) {
                if (res.hasError()) {
                    results.add("Error: " + res.getError().getMessage());
                    continue;
                }
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    results.add(annotation.getDescription());
                }
            }
        } catch (Exception e) {
            System.out.println("Error in translateImageToText Function : " + e);
            return ResponseEntity.badRequest().body("Error: " + e);
        }
        //TODO: Return Text to the FE. Should Populate the text field in Add context Window
        // TODO: Save logs to users History--- add details


        Users currentUser = getCurrentUser();

        UserHistory history = new UserHistory();
        history.setUser(currentUser);
        history.setType(HistoryTypes.TRANSLATIONS);
        //history.setDetails(String.join("\n", results));
        history.setCreatedAt(Instant.now());
        iUserHistoryRepository.save(history);


        String translatedText = "Response from translation API";
        return ResponseEntity.ok(translatedText);
    }

}
