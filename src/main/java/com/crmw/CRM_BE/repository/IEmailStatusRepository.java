package com.crmw.CRM_BE.repository;

import com.crmw.CRM_BE.entity.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmailStatusRepository extends JpaRepository<EmailStatus, Long> {
}
