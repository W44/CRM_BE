package com.crmw.CRM_BE.service;


import com.crmw.CRM_BE.entity.Doner;
import com.crmw.CRM_BE.repository.IDonerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DonerService {

    @Autowired
    IDonerRepository iDonerRepository;

    public Doner saveDoner(Doner doner) {
        return iDonerRepository.save(doner);
    }

    public List<Doner> getAllDoners() {
        return iDonerRepository.findAll();
    }

    public Optional<Doner> getDonerById(Integer id) {
        return iDonerRepository.findById(id);
    }

    public Doner updateDoner(Integer id, Doner updatedDoner) {
        return iDonerRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedDoner.getName());
                    existing.setEmail(updatedDoner.getEmail());
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
