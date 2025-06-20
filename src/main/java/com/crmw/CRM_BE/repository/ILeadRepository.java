package com.crmw.CRM_BE.repository;
import com.crmw.CRM_BE.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILeadRepository extends JpaRepository<Lead, Long> {

}