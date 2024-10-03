package com.bbc.app.service.impl;

import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.exception.InvalidOtpException;
import com.bbc.app.model.*;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.InvoiceRepository;
import com.bbc.app.repository.PaymentTransactionRepsitory;
import com.bbc.app.repository.UserRepository;
import com.bbc.app.service.OtpService;
import com.bbc.app.service.PaymentService;
import com.bbc.app.utils.PaymentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
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
    private PaymentValidator paymentValidator;

    @Override
    public ResponseEntity<MessageResponse> initiatePayment(String customerId, UUID invoiceId, PaymentMethod paymentMethod, String paymentDetails) {
        Optional<Customer> customerOpt = customerRepository.findByCustomerId(customerId);
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);

        if (customerOpt.isPresent() && invoiceOpt.isPresent()) {
            Customer customer = customerOpt.get();

            boolean isValid = paymentValidator.validatePaymentDetails(paymentMethod, paymentDetails);

            if (isValid) {
                otpService.sendOtp(customer.getCustomerId());
                return ResponseEntity.ok(new MessageResponse("OTP sent to email address.", true));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid payment details.", false));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Customer or Invoice not found.", false));
    }

    @Override
    public ResponseEntity<MessageResponse> confirmPayment(String customerId, UUID invoiceId, PaymentMethod paymentMethod, String otp, BigDecimal amount) {
        Optional<Customer> customerOpt = customerRepository.findByCustomerId(customerId);
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);

        if (customerOpt.isPresent() && invoiceOpt.isPresent()) {
            Customer customer = customerOpt.get();
            Invoice invoice = invoiceOpt.get();

            boolean isValidOtp = otpService.validateOtp(otp, customer.getUser());

            if (!isValidOtp) {
                throw new InvalidOtpException("Invalid OTP.");
            }

            if(!Objects.equals(amount, invoice.getTotalAmountDue())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Incorrect amount.", false));
            }

            // Reset OTP and OTP expiry after successful validation
            customer.getUser().setOtp(null);
            customer.getUser().setOtpExpiry(null);
            userRepository.save(customer.getUser());

            // Process payment and save transaction
            PaymentTransaction transaction = new PaymentTransaction();
            transaction.setAmount(amount);
            transaction.setCustomer(customer);
            transaction.setInvoice(invoice);
            transaction.setPaymentMethod(paymentMethod);

            paymentTransactionRepository.save(transaction);

            List<Invoice> unpaidInvoices = invoiceRepository.findByCustomerAndPaymentStatus(customer, PaymentStatus.UNPAID);

            for (Invoice unpaidInvoice : unpaidInvoices) {
                unpaidInvoice.setPaymentStatus(PaymentStatus.PAID);
                invoiceRepository.save(unpaidInvoice);
            }

            return ResponseEntity.ok(new MessageResponse("Payment successful.", true));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Customer or Invoice not found.", false));
    }
}