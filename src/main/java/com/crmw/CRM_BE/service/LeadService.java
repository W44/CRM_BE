package com.crmw.CRM_BE.service;

import com.crmw.CRM_BE.entity.Lead;
import com.crmw.CRM_BE.enums.LeadStatus;
import com.crmw.CRM_BE.repository.ILeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeadService {

    @Autowired
    private ILeadRepository leadRepository;

    public Lead createLead(Lead lead) {
        return leadRepository.save(lead);
    }

    public List<Lead> getAllLeads() {
        return leadRepository.findAll();
    }

    public Lead getLeadById(Long id) {
        return leadRepository.findById(id).orElseThrow(() -> new RuntimeException("Lead not found"));
    }

    public Lead updateLead(Long id, Lead updatedLead) {
        Lead lead = getLeadById(id);
        if (updatedLead.getName() != null) {
            lead.setName(updatedLead.getName());
        }
        if (updatedLead.getEmail() != null) {
            lead.setEmail(updatedLead.getEmail());
        }
        if (updatedLead.getPhone() != null) {
            lead.setPhone(updatedLead.getPhone());
        }
        if (updatedLead.getIdentifier() != null) {
            lead.setIdentifier(updatedLead.getIdentifier());
        }
        return leadRepository.save(lead);
    }

    public void deleteLead(Long id) {
        leadRepository.deleteById(id);
    }

    public Lead moveLeadStatus(Long id, int newStatus) {
        Lead lead = getLeadById(id);

        // conditions added here
        if (LeadStatus.values()[newStatus] == LeadStatus.COMPLETED && (lead.getIdentifier() == null || lead.getIdentifier().isBlank()
                || LeadStatus.values()[lead.getStatus()] != LeadStatus.IN_PROGRESS)) {
            throw new IllegalArgumentException("Cannot move to Completed without an identifier.");
        }

        lead.setStatus(newStatus);
        return leadRepository.save(lead);
    }
}

