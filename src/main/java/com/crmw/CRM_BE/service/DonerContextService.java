package com.crmw.CRM_BE.service;


import com.crmw.CRM_BE.dto.DonerContextResponseDto;
import com.crmw.CRM_BE.entity.Doner;
import com.crmw.CRM_BE.entity.DonerContext;
import com.crmw.CRM_BE.mapper.DonerContextMapper;
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


    public DonerContextResponseDto createDonerContext(Integer donerId, DonerContext donerContext) {
        Doner doner = iDonerRepository.findById(donerId)
                .orElseThrow(() -> new RuntimeException("Doner not found with ID: " + donerId));

        donerContext.setDoner(doner);
        DonerContext saved = donerContextRepository.save(donerContext);
        DonerContextResponseDto responseDto = DonerContextMapper.mapToDto(saved);
        return responseDto;
    }

    public List<DonerContextResponseDto> getAllDonerContexts() {
        List<DonerContext> entityResponse = donerContextRepository.findAll();
        List<DonerContextResponseDto> responseDtos = entityResponse.stream()
                .map(saved -> DonerContextMapper.mapToDto(saved)).toList();

        return responseDtos;
    }

    public DonerContextResponseDto getDonerContextById(Integer id) {
        Optional<DonerContext> entityResponse = donerContextRepository.findById(id);
        DonerContextResponseDto responseDto = entityResponse
                .map(DonerContextMapper::mapToDto)
                .orElseThrow(() -> new RuntimeException("DonerContext not found with ID: " + id));

        return responseDto;
    }

    public List<DonerContextResponseDto> getDonerContextsByDonerId(Integer donerId) {
        List<DonerContext> entityResponse = donerContextRepository.findByDoner_Id(donerId);
        List<DonerContextResponseDto> responseDtos = entityResponse.stream()
                .map(saved -> DonerContextMapper.mapToDto(saved)).toList();

        return responseDtos;
    }

    public DonerContextResponseDto updateDonerContext(Integer id, DonerContext updatedContext) {
        DonerContext existing = donerContextRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("DonerContext not found with ID: " + id));

    if (updatedContext.getTextContent() != null) {
        existing.setTextContent(updatedContext.getTextContent());
    }

    if (updatedContext.getNotes() != null) {
        existing.setNotes(updatedContext.getNotes());
    }

    DonerContext saved = donerContextRepository.save(existing);
    return DonerContextMapper.mapToDto(saved);
    }

    public void deleteDonerContext(Integer id) {
        if (!donerContextRepository.existsById(id)) {
            throw new RuntimeException("DonerContext not found with ID: " + id);
        }
        donerContextRepository.deleteById(id);
    }


}
