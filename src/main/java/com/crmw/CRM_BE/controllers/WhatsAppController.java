package com.crmw.CRM_BE.controllers;

import com.crmw.CRM_BE.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    @Autowired
    private WhatsAppService whatsAppService;

    @PostMapping("/send")
    public String sendMessage(@RequestParam String phone) {
        whatsAppService.sendHelloWorldTemplate(phone);
        return "Message sent (or attempted) to: " + phone;
    }
}

