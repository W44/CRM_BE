package com.crmw.CRM_BE.enums;

public class TasksEnums {

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }

    public enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    public enum Category {
        DONOR_MANAGEMENT,
        DONOR_COMMUNICATION,
        DATA_MANAGEMENT,
        REPORTING,
        EVENT_PLANNING,
        MARKETING,
        SYSTEM_MAINTENANCE
    }
}

