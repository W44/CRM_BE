package com.crmw.CRM_BE.service;

import com.crmw.CRM_BE.dto.EmailRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;


    public void sendStyledEmail(EmailRequestDto dto) {
        try {
            Context context = new Context();
            context.setVariable("title", dto.getTitle());
            context.setVariable("paragraphs", dto.getParagraphs());

            context.setVariable("date", dto.getDate());
            context.setVariable("recipient_prefix", dto.getRecipientPrefix());
            context.setVariable("recipient_name", dto.getRecipientName());
            context.setVariable("recipient_company", dto.getRecipientCompany());
            context.setVariable("recipient_address_line_1", dto.getRecipientAddressLine1());
            context.setVariable("recipient_address_line_2", dto.getRecipientAddressLine2());
            context.setVariable("recipient_city", dto.getRecipientCity());
            context.setVariable("recipient_salutation", dto.getRecipientSalutation());

            String renderedHtml = templateEngine.process("email-template", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(dto.getTo());
            helper.setSubject(dto.getTitle());
            helper.setText(renderedHtml, true);
            //helper.setFrom("your_email@gmail.com");

            ClassPathResource logoImage = new ClassPathResource("static/images/logo.jpeg");
            helper.addInline("logo", logoImage);

            if (dto.getAttachedFile() != null && !dto.getAttachedFile().isEmpty()) {
                helper.addAttachment(dto.getAttachedFile().getOriginalFilename(), dto.getAttachedFile());
            }
            mailSender.send(message);
            System.out.println("Email sent to " + dto.getTo());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

