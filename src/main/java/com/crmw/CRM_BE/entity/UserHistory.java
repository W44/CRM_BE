package com.crmw.CRM_BE.entity;

import com.crmw.CRM_BE.enums.HistoryTypes;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class UserHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Integer getId() {
        return id;
    }

    public Users getUser() {
        return user;
    }

    public String getDetails() {
        return details;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
