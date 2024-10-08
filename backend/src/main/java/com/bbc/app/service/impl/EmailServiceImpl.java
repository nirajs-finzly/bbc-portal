package com.bbc.app.service.impl;

import com.bbc.app.exception.SomethingWrongException;
import com.bbc.app.model.Invoice;
import com.bbc.app.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendOtpEmail(String to, String otp) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message, true); // true for multipart message
            helper.setTo(to);
            helper.setSubject("Your OTP Code");

            Context context = new Context();
            context.setVariable("otp", otp);

            String htmlContent = templateEngine.process("otp-email", context);

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new SomethingWrongException("Something went wrong while sending the email!");
        }
    }

    public void sendInvoiceEmail(String recipientEmail, Invoice invoice, byte[] invoicePdf) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientEmail);
            helper.setSubject("Invoice Generated for " + invoice.getBillDuration());
            helper.setText("Dear Customer,\n\nYour invoice for the billing period "
                    + invoice.getBillDuration() + " has been generated.\n\nPlease find the attached PDF for your invoice.\n\nRegards,\nBharat Bijili Corporation");

            // Attach the PDF
            helper.addAttachment("Invoice-" + invoice.getBillDuration() + ".pdf", new ByteArrayDataSource(invoicePdf, "application/pdf"));

            // Send the email
            mailSender.send(message);

        } catch (MessagingException e) {
            // Handle exceptions related to email sending
            e.printStackTrace();
        }
    }

}