package com.bbc.app.service;

import com.bbc.app.model.Invoice;

import java.io.IOException;
import java.util.List;

public interface PdfService {
    byte[] createInvoicePdf(Invoice invoice, List<Invoice> unpaidInvoices) throws IOException;
}
