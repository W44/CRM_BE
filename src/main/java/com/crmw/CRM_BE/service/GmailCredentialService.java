package com.crmw.CRM_BE.service;

import com.crmw.CRM_BE.entity.GmailCredential;
import com.crmw.CRM_BE.repository.IGmailCredentialRepository;
import com.crmw.CRM_BE.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class GmailCredentialService {

    @Autowired
    private IGmailCredentialRepository repository;

    @Autowired
    private GoogleOAuthService googleOAuthService;

    public GmailCredential getOrRefreshToken() throws Exception {
        //TODO: in future do this with User ID to make it user centric
        //TODO: move the temporary saving logic to redis.
        GmailCredential credential = repository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new RuntimeException("No Gmail credential found found Activate Gmail Authentication"));

        if (credential.getTokenGeneratedAt().isBefore(Instant.now().minus(50, ChronoUnit.MINUTES))) {
            String decryptedRefreshToken = EncryptionUtil.decrypt(credential.getEncryptedRefreshToken());
            String newAccessToken = googleOAuthService.refreshAccessToken(decryptedRefreshToken);

            credential.setEncryptedAccessToken(EncryptionUtil.encrypt(newAccessToken));
            credential.setTokenGeneratedAt(Instant.now());
            repository.save(credential);
        }
        return credential;
    }

    public String getDecryptedAccessToken() throws Exception {
        GmailCredential credential = getOrRefreshToken();
        return EncryptionUtil.decrypt(credential.getEncryptedAccessToken());
    }

    public void saveTokens(String userEmail, String accessToken, String refreshToken) throws Exception {
        GmailCredential credential = repository.findByUserEmail(userEmail).orElse(new GmailCredential());
        credential.setUserEmail(userEmail);
        credential.setEncryptedAccessToken(EncryptionUtil.encrypt(accessToken));
        credential.setEncryptedRefreshToken(EncryptionUtil.encrypt(refreshToken));
        credential.setTokenGeneratedAt(Instant.now());
        repository.save(credential);
    }

}

