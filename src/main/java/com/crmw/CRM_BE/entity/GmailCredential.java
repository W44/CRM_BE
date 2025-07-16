package com.crmw.CRM_BE.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "gmail_credentials")
public class GmailCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;

    private String userEmail;

    @Column(length = 2048)
    private String encryptedAccessToken;

    @Column(length = 2048)
    private String encryptedRefreshToken;

    private Instant tokenGeneratedAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getEncryptedAccessToken() {
        return encryptedAccessToken;
    }

    public void setEncryptedAccessToken(String encryptedAccessToken) {
        this.encryptedAccessToken = encryptedAccessToken;
    }

    public String getEncryptedRefreshToken() {
        return encryptedRefreshToken;
    }

    public void setEncryptedRefreshToken(String encryptedRefreshToken) {
        this.encryptedRefreshToken = encryptedRefreshToken;
    }

    public Instant getTokenGeneratedAt() {
        return tokenGeneratedAt;
    }

    public void setTokenGeneratedAt(Instant tokenGeneratedAt) {
        this.tokenGeneratedAt = tokenGeneratedAt;
    }
}

