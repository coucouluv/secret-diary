package com.project.secretdiary.service;

import com.project.secretdiary.dto.request.MailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String adminEmail;

    public void sendMail(final MailRequest mailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailRequest.getAddress());
        message.setFrom(adminEmail);
        message.setSubject(mailRequest.getTitle());
        message.setText(mailRequest.getMessage());
        mailSender.send(message);
    }
}