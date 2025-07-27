package com.crmw.CRM_BE.repository;

import com.crmw.CRM_BE.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByStatus(String status);
}
