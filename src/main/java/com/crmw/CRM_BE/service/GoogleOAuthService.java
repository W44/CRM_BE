package com.crmw.CRM_BE.service;

import com.crmw.CRM_BE.entity.GmailCredential;
import com.crmw.CRM_BE.entity.Users;
import com.crmw.CRM_BE.repository.IGmailCredentialRepository;
import com.crmw.CRM_BE.repository.IUsersRepository;
import com.crmw.CRM_BE.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
public class GoogleOAuthService {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private IGmailCredentialRepository gmailCredentialRepository;

    @Autowired
    IUsersRepository iUsersRepository;

    @Autowired
    JWTService jwtService;

    @Autowired
    UserService userService;

    public Users getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return iUsersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public void handleOAuthCallback(String code, String state) throws Exception {
        String tokenEndpoint = "https://oauth2.googleapis.com/token";

        String username = jwtService.extractUsername(state);
        Users user = userService.findUserByUsernameRaw(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("redirect_uri", redirectUri);
        form.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                tokenEndpoint,
                HttpMethod.POST,
                request,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> tokenResponse = response.getBody();
            String accessToken = (String) tokenResponse.get("access_token");
            String refreshToken = (String) tokenResponse.get("refresh_token");

            GmailCredential credential = gmailCredentialRepository.findFirstByOrderByIdAsc()
                    .orElse(new GmailCredential());
            credential.setUser(user);
            credential.setEncryptedAccessToken(EncryptionUtil.encrypt(accessToken));
            credential.setEncryptedRefreshToken(EncryptionUtil.encrypt(refreshToken));
            credential.setTokenGeneratedAt(Instant.now());
            gmailCredentialRepository.save(credential);

        } else {
            throw new RuntimeException("Failed to fetch token from Google OAuth: " + response.getStatusCode());
        }
    }

    public String refreshAccessToken(String decryptedRefreshToken) throws Exception {
        String tokenEndpoint = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("refresh_token", decryptedRefreshToken);
        form.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                tokenEndpoint,
                HttpMethod.POST,
                request,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> tokenResponse = response.getBody();
            String newAccessToken = (String) tokenResponse.get("access_token");
            return newAccessToken;
        } else {
            throw new RuntimeException("Failed to refresh token: " + response.getStatusCode());
        }
    }

    public String generateStateToken() {
        Users currentUser = getCurrentUser();
        String stateToken = jwtService.generateToken(currentUser.getUsername(), 10 * 60);
        return stateToken;
    }
}

