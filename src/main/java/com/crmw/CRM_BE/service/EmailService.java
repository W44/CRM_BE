package com.crmw.CRM_BE.service;

import com.crmw.CRM_BE.dto.EmailRequestDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.services.gmail.model.Message;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import jakarta.mail.Session;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    GmailCredentialService gmailCredentialService;


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

    public void sendStyledEmailViaGmailApi(EmailRequestDto dto) throws Exception {

        String accessToken = gmailCredentialService.getDecryptedAccessToken();

        AccessToken token = new AccessToken(accessToken, null);
        GoogleCredentials credentials = GoogleCredentials.newBuilder()
                .setAccessToken(token)
                .build()
                .createScoped(List.of("https://www.googleapis.com/auth/gmail.send"));

        HttpCredentialsAdapter adapter = new HttpCredentialsAdapter(credentials);

        Gmail service = new Gmail.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                adapter
        )
                .setApplicationName("CRM_BE")
                .build();

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

        MimeMessage email = createEmail(dto.getTo(), "me", dto.getTitle(), renderedHtml, dto.getAttachedFile());

        Message message = createMessageWithEmail(email);

        service.users().messages().send("me", message).execute();

        System.out.println("Email sent via Gmail API to " + dto.getTo());
    }

    private MimeMessage createEmail(String to, String from, String subject, String bodyText,
                                    MultipartFile attachment) throws Exception {

        Properties props = new Properties();
        Session session = Session.getInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject, "UTF-8");

        MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8"); // enable multipart

        helper.setText(bodyText, true);

        ClassPathResource logoImage = new ClassPathResource("static/images/logo.jpeg");
        helper.addInline("logo", logoImage);

        if (attachment != null && !attachment.isEmpty()) {
            helper.addAttachment(attachment.getOriginalFilename(), attachment);
        }

        return email;
    }

    private Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);

        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

}

