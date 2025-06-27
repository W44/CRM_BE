package com.crmw.CRM_BE.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class EmailRequestDto {
    private String to;
    private String title;
    private List<String> paragraphs;

    private String date;
    private String recipientPrefix;
    private String recipientName;
    private String recipientCompany;
    private String recipientAddressLine1;
    private String recipientAddressLine2;
    private String recipientCity;
    private String recipientSalutation;
    private MultipartFile attachedFile;


    public MultipartFile getAttachedFile() {
        return attachedFile;
    }

    public void setAttachedFile(MultipartFile attachedFile) {
        this.attachedFile = attachedFile;
    }

    public String getRecipientPrefix() {
        return recipientPrefix;
    }

    public void setRecipientPrefix(String recipientPrefix) {
        this.recipientPrefix = recipientPrefix;
    }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<String> getParagraphs() { return paragraphs; }
    public void setParagraphs(List<String> paragraphs) { this.paragraphs = paragraphs; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getRecipientCompany() { return recipientCompany; }
    public void setRecipientCompany(String recipientCompany) { this.recipientCompany = recipientCompany; }

    public String getRecipientAddressLine1() { return recipientAddressLine1; }
    public void setRecipientAddressLine1(String recipientAddressLine1) { this.recipientAddressLine1 = recipientAddressLine1; }

    public String getRecipientAddressLine2() { return recipientAddressLine2; }
    public void setRecipientAddressLine2(String recipientAddressLine2) { this.recipientAddressLine2 = recipientAddressLine2; }

    public String getRecipientCity() { return recipientCity; }
    public void setRecipientCity(String recipientCity) { this.recipientCity = recipientCity; }

    public String getRecipientSalutation() { return recipientSalutation; }
    public void setRecipientSalutation(String recipientSalutation) { this.recipientSalutation = recipientSalutation; }
}
