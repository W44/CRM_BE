package com.crmw.CRM_BE.service;

import com.crmw.CRM_BE.entity.BulkContacts;
import com.crmw.CRM_BE.repository.IBulkContactsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BulkContactsService {

    @Autowired
    private IBulkContactsRepository repository;

    public List<BulkContacts> getAllContacts() {
        return repository.findAll();
    }

    public Optional<BulkContacts> getContactById(Long id) {
        return repository.findById(id);
    }

    public BulkContacts createContact(BulkContacts contact) {
        return repository.save(contact);
    }

    public BulkContacts updateContact(Long id, BulkContacts updatedContact) {
        return repository.findById(id).map(existing -> {
            existing.setEmail(updatedContact.getEmail());
            existing.setCategory(updatedContact.getCategory());
            existing.setNote(updatedContact.getNote());
            existing.setReceiveEmails(updatedContact.isReceiveEmails());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
    }

    public void deleteContact(Long id) {
        repository.deleteById(id);
    }
}
