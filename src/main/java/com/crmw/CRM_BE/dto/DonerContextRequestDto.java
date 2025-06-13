package com.crmw.CRM_BE.dto;

public class DonerContextRequestDto {
    private Integer donerId;
    private String textContent;
    private String notes;

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
}

