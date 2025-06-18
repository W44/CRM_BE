package com.crmw.CRM_BE.service;


import com.crmw.CRM_BE.entity.Doner;
import com.crmw.CRM_BE.repository.IDonerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class DonerService {

    @Autowired
    IDonerRepository iDonerRepository;

    public Doner saveDoner(Doner doner) {

        if (doner.getId() != null && iDonerRepository.existsById(doner.getId())) {
            throw new IllegalArgumentException("A Doner with this ID already exists.");
        }
        return iDonerRepository.save(doner);
    }

    public Page<Doner> getAllDoners(String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return iDonerRepository.findAll(pageable);
        }
        return iDonerRepository.findByNameContainingIgnoreCaseOrId(search, pageable);
    }

    public Optional<Doner> getDonerById(Integer id) {
        return iDonerRepository.findById(id);
    }

    public Doner updateDoner(Integer id, Doner updatedDoner) {
        return iDonerRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedDoner.getName());
                    existing.setEmail(updatedDoner.getEmail());
                    existing.setPhone(updatedDoner.getPhone());
                    return iDonerRepository.save(existing);
                })
                .orElse(null);
    }

    public boolean deleteDoner(Integer id) {
        if (iDonerRepository.existsById(id)) {
            iDonerRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
