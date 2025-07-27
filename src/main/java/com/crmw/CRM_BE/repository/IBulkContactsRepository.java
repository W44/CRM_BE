package com.crmw.CRM_BE.repository;

import com.crmw.CRM_BE.entity.BulkContacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBulkContactsRepository extends JpaRepository<BulkContacts, Long> {

}
