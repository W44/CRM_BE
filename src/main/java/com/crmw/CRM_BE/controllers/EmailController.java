package com.crmw.CRM_BE.controllers;

import com.crmw.CRM_BE.dto.EmailRequestDto;
import com.crmw.CRM_BE.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    @Autowired
    private EmailService emailService;


    @PostMapping(value = "/notify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> sendStyledEmailAndDocx(@ModelAttribute EmailRequestDto request) {
        emailService.sendStyledEmail(request);
        return ResponseEntity.ok("Email sent to " + request.getTo());
    }

    @PostMapping(value = "/notify", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendStyledEmailJson(@RequestBody EmailRequestDto request) {
        emailService.sendStyledEmail(request);
        return ResponseEntity.ok("Email sent to " + request.getTo());
    }

}

