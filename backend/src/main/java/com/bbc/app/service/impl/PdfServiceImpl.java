package com.bbc.app.service.impl;

import com.bbc.app.model.Invoice;
import com.bbc.app.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public byte[] createInvoicePdf(Invoice invoice, List<Invoice> unpaidInvoices) throws IOException {
        // Prepare the Thymeleaf context
        Context context = new Context();
        context.setVariable("invoice", invoice);
        context.setVariable("unpaidInvoices", unpaidInvoices);

        // Process the HTML content using Thymeleaf
        String htmlContent = templateEngine.process("invoice", context);

        // Convert HTML to PDF using Flying Saucer
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();

            // Set the XHTML content and base URL for relative resources
            renderer.setDocumentFromString(htmlContent, new ClassPathResource("/templates/").getURL().toString());

            // Layout and create the PDF
            renderer.layout();
            renderer.createPDF(byteArrayOutputStream);

            // Return the generated PDF as a byte array
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new IOException("Error generating Invoice PDF", e);
        }
    }
}
