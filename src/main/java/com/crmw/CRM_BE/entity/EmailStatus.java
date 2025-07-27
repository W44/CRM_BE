package com.crmw.CRM_BE.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class EmailStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String status;

    private int campaignIteration;

    private Instant timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCampaignIteration() {
        return campaignIteration;
    }

    public void setCampaignIteration(int campaignIteration) {
        this.campaignIteration = campaignIteration;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}

