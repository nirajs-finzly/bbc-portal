package com.bbc.app.service.impl;

import com.bbc.app.exception.SomethingWrongException;
import com.bbc.app.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpEmail(String to, String otp) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(to);
            helper.setSubject("Your OTP Code");
            helper.setText("Your OTP code is: " + otp);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new SomethingWrongException("Something went wrong!");
        }
    }
}