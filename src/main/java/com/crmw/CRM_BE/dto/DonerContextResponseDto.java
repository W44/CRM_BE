package com.crmw.CRM_BE.dto;

import java.time.Instant;

public class DonerContextResponseDto {
    private Integer id;
    private Integer donerId;
    private String textContent;
    private String notes;
    private Instant createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDonerId() {
        return donerId;
    }

    public void setDonerId(Integer donerId) {
        this.donerId = donerId;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
