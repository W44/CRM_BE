package com.crmw.CRM_BE.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class DonerContext {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "doner_id", nullable = false)
    private Doner doner;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String textContent;

    @Column
    private String notes;

    @Column
    private Instant createdAt = Instant.now();

    public Integer getId() {
        return id;
    }

    public Doner getDoner() {
        return doner;
    }

    public String getTextContent() {
        return textContent;
    }

    public String getNotes() {
        return notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDoner(Doner doner) {
        this.doner = doner;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
