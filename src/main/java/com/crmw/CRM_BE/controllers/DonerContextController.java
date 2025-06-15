package com.crmw.CRM_BE.controllers;

import com.crmw.CRM_BE.dto.DonerContextRequestDto;
import com.crmw.CRM_BE.dto.DonerContextResponseDto;
import com.crmw.CRM_BE.entity.DonerContext;
import com.crmw.CRM_BE.service.DonerContextService;
import com.crmw.CRM_BE.repository.IDonerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doner-contexts")
public class DonerContextController {

    @Autowired
    private DonerContextService donerContextService;

    @Autowired
    private IDonerRepository donerRepository;


    @PostMapping
    public ResponseEntity<?> createDonerContext(@RequestBody DonerContextRequestDto dto) {

        DonerContext context = new DonerContext();
        context.setTextContent(dto.getTextContent());
        context.setNotes(dto.getNotes());
        context.setCreatedAt(Instant.now());

        return ResponseEntity.ok(donerContextService.createDonerContext(dto.getDonerId(), context));
    }

    @GetMapping
    public ResponseEntity<List<DonerContextResponseDto>> getAllDonerContexts() {
        return ResponseEntity.ok(donerContextService.getAllDonerContexts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDonerContextById(@PathVariable Integer id) {
        DonerContextResponseDto dto = donerContextService.getDonerContextById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/doner/{donerId}")
    public ResponseEntity<List<DonerContextResponseDto>> getByDonerId(@PathVariable Integer donerId) {
        return ResponseEntity.ok(donerContextService.getDonerContextsByDonerId(donerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDonerContext(@PathVariable Integer id, @RequestBody DonerContextRequestDto dto) {

        DonerContext context = new DonerContext();
        context.setTextContent(dto.getTextContent());
        context.setNotes(dto.getNotes());

        return ResponseEntity.ok(donerContextService.updateDonerContext(id, context));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDonerContext(@PathVariable Integer id) {
        donerContextService.deleteDonerContext(id);
        return ResponseEntity.ok().build();
    }
}
