package com.bbc.app.service.impl;

import com.bbc.app.dto.data.TransactionData;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.PaymentResponse;
import com.bbc.app.dto.response.TransactionsResponse;
import com.bbc.app.exception.InvalidOtpException;
import com.bbc.app.model.*;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.InvoiceRepository;
import com.bbc.app.repository.PaymentTransactionRepsitory;
import com.bbc.app.repository.UserRepository;
import com.bbc.app.service.OtpService;
import com.bbc.app.service.PaymentService;
import com.bbc.app.service.PdfService;
import com.bbc.app.utils.PaymentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentTransactionRepsitory paymentTransactionRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private PaymentValidator paymentValidator;

    @Override
    public ResponseEntity<TransactionsResponse> getAllTransactions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "paymentDate"));
        Page<PaymentTransaction> transactionPage = paymentTransactionRepository.findAll(pageable);

        List<TransactionData> transactionDataList = transactionPage.getContent().stream()
                .map(transaction -> new TransactionData(
                        transaction.getTransactionId(),
                        transaction.getAmount(),
                        transaction.getCustomer().getUser().getName(),
                        transaction.getInvoice().getInvoiceId().toString(),
                        transaction.getPaymentMethod(),
                        transaction.getTransactionStatus(),
                        transaction.getPaymentDate(),
                        transaction.getPaymentIdentifier(),
                        transaction.getCardType()
                ))
                .toList();

        String message = transactionDataList.isEmpty() ? "No transactions found!" : "Transactions retrieved successfully!";
        boolean success = !transactionDataList.isEmpty();

        TransactionsResponse response = new TransactionsResponse(message, transactionDataList, transactionPage.getTotalElements(), success);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TransactionsResponse> getAllTransactionsByMeterNo(String meterNo, int page, int size) {
        Optional<Customer> customer =  customerRepository.findByMeterNo(meterNo);

        if(customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TransactionsResponse("Customer not found!", null, 0L, false));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "paymentDate"));
        Page<PaymentTransaction> transactionPage = paymentTransactionRepository.findAllByCustomerCustomerId(customer.get().getCustomerId(), pageable);

        List<TransactionData> transactionDataList = transactionPage.getContent().stream()
                .map(transaction -> new TransactionData(
                        transaction.getTransactionId(),
                        transaction.getAmount(),
                        transaction.getCustomer().getUser().getName(),
                        transaction.getInvoice().getInvoiceId().toString(),
                        transaction.getPaymentMethod(),
                        transaction.getTransactionStatus(),
                        transaction.getPaymentDate(),
                        transaction.getPaymentIdentifier(),
                        transaction.getCardType()
                ))
                .toList();

        String message = transactionDataList.isEmpty() ? "No transactions found for this customer!" : "Transactions retrieved successfully!";
        boolean success = !transactionDataList.isEmpty();

        TransactionsResponse response = new TransactionsResponse(message, transactionDataList, transactionPage.getTotalElements(), success);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PaymentResponse> initiatePayment(String meterNo, UUID invoiceId) {
        Optional<Customer> customerOpt = customerRepository.findByMeterNo(meterNo);
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);

        if (customerOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PaymentResponse("Customer not found!" + meterNo, null, null, false));
        }

        if (invoiceOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PaymentResponse("Invoice not found!", null, null, false));
        }

        Invoice invoice = invoiceOpt.get();

        if (invoice.getPaymentStatus() == PaymentStatus.PAID) {
            return ResponseEntity.badRequest().body(new PaymentResponse("Invoice is already marked as paid.", null, null, false));
        }

        invoice.setPaymentInitiated(true);
        invoiceRepository.save(invoice);

        return ResponseEntity.ok(new PaymentResponse("Payment initiated successfully.", "/pay/" + invoice.getInvoiceId() + "/" + customerOpt.get().getMeterNo(), null, true));
    }

    @Override
    public ResponseEntity<MessageResponse> validatePaymentInitiation(UUID invoiceId) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);

        if (invoiceOpt.isPresent()) {
            Invoice invoice = invoiceOpt.get();
            return ResponseEntity.ok(new MessageResponse("Payment initiation status: " + invoice.isPaymentInitiated(), invoice.isPaymentInitiated()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Invoice not found.", false));
    }


    @Override
    public ResponseEntity<MessageResponse> makePayment(String meterNo, UUID invoiceId, PaymentMethod paymentMethod, String paymentDetails) {
        Optional<Customer> customerOpt = customerRepository.findByMeterNo(meterNo);
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);

        Invoice invoice = null;
        if (invoiceOpt.isPresent()) {
            invoice = invoiceOpt.get();
        }

        assert invoice != null;
        if (invoice.getPaymentStatus() == PaymentStatus.PAID) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invoice is already marked as paid.", false));
        }

        if (!invoice.isPaymentInitiated()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Payment not initiated!", false));
        }

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();

            boolean isValid = paymentValidator.validatePaymentDetails(paymentMethod, paymentDetails);

            if (isValid) {
                otpService.sendOtp(customer.getCustomerId());
                return ResponseEntity.ok(new MessageResponse("OTP sent to email address.", true));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid payment details!", false));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Customer or Invoice not found.", false));
    }

    @Override
    public ResponseEntity<PaymentResponse> confirmPayment(String meterNo, UUID invoiceId, PaymentMethod paymentMethod, String paymentDetails, String otp, BigDecimal amount) {
        Optional<Customer> customerOpt = customerRepository.findByMeterNo(meterNo);
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);

        Invoice invoice = null;
        if (invoiceOpt.isPresent()) {
            invoice = invoiceOpt.get();
        }

        assert invoice != null;
        if (invoice.getPaymentStatus() == PaymentStatus.PAID) {
            return ResponseEntity.badRequest().body(new PaymentResponse("Invoice is already marked as paid.", null, null, false));
        }

        if (!invoice.isPaymentInitiated()) {
            return ResponseEntity.badRequest().body(new PaymentResponse("Payment not initiated.", null, null, false));
        }

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();

            boolean isValidOtp = otpService.validateOtp(otp, customer.getUser());

            // Prepare a new transaction object for logging
            PaymentTransaction transaction = new PaymentTransaction();
            transaction.setAmount(amount);
            transaction.setCustomer(customer);
            transaction.setInvoice(invoice);
            transaction.setPaymentMethod(paymentMethod);

            if (paymentMethod == PaymentMethod.UPI) {
                transaction.setPaymentIdentifier(paymentDetails);
            } else if (paymentMethod == PaymentMethod.NET_BANKING || paymentMethod == PaymentMethod.DEBIT_CARD || paymentMethod == PaymentMethod.CREDIT_CARD) {
                String[] details = paymentDetails.split("\\|");
                transaction.setPaymentIdentifier(details[0]);
                if(paymentMethod == PaymentMethod.CREDIT_CARD) {
                    transaction.setCardType("Credit Card");
                } else if (paymentMethod == PaymentMethod.DEBIT_CARD) {
                    transaction.setCardType("Debit Card");
                }
            }

            if (!isValidOtp) {
                transaction.setTransactionStatus(TransactionStatus.FAILED);
                paymentTransactionRepository.save(transaction);
                throw new InvalidOtpException("Invalid OTP.");
            }

            BigDecimal earlyPaymentDiscount = BigDecimal.ZERO;
            BigDecimal onlinePaymentDiscount = BigDecimal.ZERO;
            BigDecimal totalDiscount;
            BigDecimal finalBillAmount = invoice.getTotalAmountDue();

            if (paymentValidator.checkIfEarlyPayment(invoice.getBillDueDate().toString())) {
                earlyPaymentDiscount = invoice.getTotalAmountDue().multiply(BigDecimal.valueOf(0.05));
            }

            if (paymentValidator.checkIfOnlinePayment(paymentMethod)) {
                onlinePaymentDiscount = invoice.getTotalAmountDue().multiply(BigDecimal.valueOf(0.05));
            }

            totalDiscount = earlyPaymentDiscount.add(onlinePaymentDiscount);
            finalBillAmount = finalBillAmount.subtract(totalDiscount);

            finalBillAmount = finalBillAmount.setScale(2, RoundingMode.HALF_UP);

            if (!amount.equals(finalBillAmount)) {
                transaction.setTransactionStatus(TransactionStatus.FAILED);
                paymentTransactionRepository.save(transaction);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Amount mismatch!", null, null, false));
            }

            boolean hasSufficientBalance = paymentValidator.checkPaymentMethodBalance(paymentMethod, amount);
            if (!hasSufficientBalance) {
                transaction.setTransactionStatus(TransactionStatus.FAILED);
                paymentTransactionRepository.save(transaction);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PaymentResponse("Insufficient balance!", null, null, false));
            }

            boolean isBalanceDeducted = paymentValidator.deductPaymentBalance(paymentMethod, amount);
            if (!isBalanceDeducted) {
                transaction.setTransactionStatus(TransactionStatus.FAILED);
                paymentTransactionRepository.save(transaction);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PaymentResponse("Error deducting balance!", null, null, false));
            }

            // Reset OTP and OTP expiry after successful validation
            customer.getUser().setOtp(null);
            customer.getUser().setOtpExpiry(null);
            userRepository.save(customer.getUser());

            // Process payment as successful and save the transaction with success status
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            paymentTransactionRepository.save(transaction);

            invoice.setPaymentInitiated(false);
            invoice.setTotalAmountDue(finalBillAmount);
            invoiceRepository.save(invoice);

            // Mark all unpaid invoices as paid
            List<Invoice> unpaidInvoices = invoiceRepository.findByCustomerAndPaymentStatus(customer, PaymentStatus.UNPAID);

            for (Invoice unpaidInvoice : unpaidInvoices) {
                unpaidInvoice.setPaymentStatus(PaymentStatus.PAID);

                try {
                    byte[] invoicePdf = pdfService.createInvoicePdf(unpaidInvoice, null);
                    unpaidInvoice.setInvoicePdf(invoicePdf);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PaymentResponse("Error generating invoice PDF!", null, null, false));
                }
                    invoiceRepository.save(unpaidInvoice);
            }

            return ResponseEntity.ok(new PaymentResponse("Payment successful!", null, "/pay/success", true));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PaymentResponse("Customer or Invoice not found.", null, null, false));
    }


    @Override
    public ResponseEntity<MessageResponse> markInvoiceAsPaid(UUID invoiceId, PaymentMethod paymentMethod) {
        if (!paymentMethod.name().equalsIgnoreCase("CASH")) {
            return ResponseEntity.badRequest().body(new MessageResponse("Payment method must be cash to mark invoice as paid.", false));
        }

        // Find the invoice by ID
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceId);

        if (optionalInvoice.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invoice not found!", false));
        }

        Invoice invoice = optionalInvoice.get();

        // Check if the invoice is already paid
        if (invoice.getPaymentStatus() == PaymentStatus.PAID) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invoice is already marked as paid.", false));
        }

        // Mark the invoice as paid
        invoice.setPaymentStatus(PaymentStatus.PAID);

        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setAmount(invoice.getTotalAmountDue());
        transaction.setCustomer(invoice.getCustomer());
        transaction.setInvoice(invoice);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);

        paymentTransactionRepository.save(transaction);

        // Generate a new PDF for the updated invoice
        try {
            byte[] updatedInvoicePdf = pdfService.createInvoicePdf(invoice, null);
            invoice.setInvoicePdf(updatedInvoicePdf);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error generating updated invoice PDF!", false));
        }

        // Save the updated invoice back to the repository
        invoiceRepository.save(invoice);

        return ResponseEntity.ok(new MessageResponse("Invoice marked as paid and PDF updated successfully!", true));
    }

}