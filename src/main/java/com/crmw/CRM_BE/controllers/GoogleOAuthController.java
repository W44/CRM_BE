package com.crmw.CRM_BE.controllers;

import com.crmw.CRM_BE.entity.Users;
import com.crmw.CRM_BE.service.GoogleOAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class GoogleOAuthController {

    @Autowired
    private GoogleOAuthService googleOAuthService;


    @GetMapping("/google/callback")
    public ResponseEntity<Void> handleGoogleCallback(@RequestParam("code") String code, @RequestParam String state) {
        try {

            googleOAuthService.handleOAuthCallback(code, state);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:3000/dashboard"))
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .location(URI.create("http://localhost:3000/dashboard?error=google_oauth_failed"))
                    .build();
        }
    }

    @PostMapping("/google/state-token")
    public ResponseEntity<?> generateStateToken() {
        String stateToken =googleOAuthService.generateStateToken();
        return ResponseEntity.ok(Map.of("stateToken", stateToken));
    }
}

