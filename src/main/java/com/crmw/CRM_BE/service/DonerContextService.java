package com.crmw.CRM_BE.service;


import com.crmw.CRM_BE.entity.Doner;
import com.crmw.CRM_BE.entity.DonerContext;
import com.crmw.CRM_BE.repository.IDonerContextRepository;
import com.crmw.CRM_BE.repository.IDonerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DonerContextService {

    @Autowired
    private IDonerContextRepository donerContextRepository;

    @Autowired
    private IDonerRepository iDonerRepository;


    public DonerContext createDonerContext(Integer donerId, DonerContext donerContext) {
        Doner doner = iDonerRepository.findById(donerId)
                .orElseThrow(() -> new RuntimeException("Doner not found with ID: " + donerId));

        donerContext.setDoner(doner);
        return donerContextRepository.save(donerContext);
    }

    public List<DonerContext> getAllDonerContexts() {
        return donerContextRepository.findAll();
    }

    public Optional<DonerContext> getDonerContextById(Integer id) {
        return donerContextRepository.findById(id);
    }

    public List<DonerContext> getDonerContextsByDonerId(Integer donerId) {
        return donerContextRepository.findByDoner_Id(donerId);
    }

    public DonerContext updateDonerContext(Integer id, DonerContext updatedContext) {
        Optional<DonerContext> optional = donerContextRepository.findById(id);
        if (optional.isEmpty()) {
            throw new RuntimeException("DonerContext not found with ID: " + id);
        }

        DonerContext existing = optional.get();
        existing.setTextContent(updatedContext.getTextContent());
        existing.setNotes(updatedContext.getNotes());
        existing.setCreatedAt(updatedContext.getCreatedAt());
        return donerContextRepository.save(existing);
    }

    public void deleteDonerContext(Integer id) {
        if (!donerContextRepository.existsById(id)) {
            throw new RuntimeException("DonerContext not found with ID: " + id);
        }
        donerContextRepository.deleteById(id);
    }


}
