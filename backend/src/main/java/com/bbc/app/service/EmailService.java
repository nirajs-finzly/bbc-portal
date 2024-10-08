package com.bbc.app.service;

import com.bbc.app.model.Invoice;

public interface EmailService {
    void sendOtpEmail(String to, String otp);

    void sendInvoiceEmail(String recipientEmail, Invoice invoice, byte[] invoicePdf);
}
