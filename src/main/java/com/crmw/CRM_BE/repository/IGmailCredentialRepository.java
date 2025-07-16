package com.crmw.CRM_BE.repository;

import com.crmw.CRM_BE.entity.GmailCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IGmailCredentialRepository extends JpaRepository<GmailCredential, Long> {
    Optional<GmailCredential> findByUserEmail(String userEmail);

    @Query("SELECT g FROM GmailCredential g WHERE g.user.id = :userId")
    Optional<GmailCredential> findByUserId(@Param("userId") Integer userId);

    Optional<GmailCredential> findFirstByOrderByIdAsc();

}

