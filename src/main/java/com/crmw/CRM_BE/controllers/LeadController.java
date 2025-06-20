package com.crmw.CRM_BE.controllers;


import com.crmw.CRM_BE.dto.MoveRequestDto;
import com.crmw.CRM_BE.entity.Lead;
import com.crmw.CRM_BE.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leads")
public class LeadController {

    @Autowired
    private LeadService leadService;

    @PostMapping
    public Lead createLead(@RequestBody Lead lead) {
        return leadService.createLead(lead);
    }

    @GetMapping
    public List<Lead> getAllLeads() {
        return leadService.getAllLeads();
    }

    @GetMapping("/{id}")
    public Lead getLead(@PathVariable Long id) {
        return leadService.getLeadById(id);
    }

    @PutMapping("/{id}")
    public Lead updateLead(@PathVariable Long id, @RequestBody Lead updatedLead) {
        return leadService.updateLead(id, updatedLead);
    }

    @DeleteMapping("/{id}")
    public void deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
    }

    @PostMapping("/{id}/move")
    public Lead moveLead(@PathVariable Long id, @RequestBody MoveRequestDto moveRequest) {
        return leadService.moveLeadStatus(id, moveRequest.getNewStatus());
    }
}
