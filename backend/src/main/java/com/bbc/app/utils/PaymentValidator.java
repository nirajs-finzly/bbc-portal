package com.bbc.app.utils;

import com.bbc.app.model.PaymentMethod;
import com.bbc.app.repository.CreditCardRepository;
import com.bbc.app.repository.DebitCardRepository;
import com.bbc.app.repository.NetBankingRepository;
import com.bbc.app.repository.UPIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
public class PaymentValidator {

    @Autowired
    private DebitCardRepository debitCardRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private NetBankingRepository netBankingRepository;

    @Autowired
    private UPIRepository upiRepository;

    public boolean validatePaymentDetails(PaymentMethod paymentMethod, String paymentDetails) {
        return switch (paymentMethod) {
            case DEBIT_CARD -> validateDebitCard(paymentDetails);
            case CREDIT_CARD -> validateCreditCard(paymentDetails);
            case NET_BANKING -> validateNetBanking(paymentDetails);
            case UPI -> validateUPI(paymentDetails);
            default -> false;
        };
    }

    // Debit card validation (expects "cardNumber|expiryDate|cvv")
    private boolean validateDebitCard(String paymentDetails) {
        String[] details = paymentDetails.split("\\|");
        if (details.length != 3) return false;

        String cardNumber = details[0];
        String expiryDate = details[1];
        String cvv = details[2];

        if (isValidCardNumber(cardNumber) || isValidExpiryDate(expiryDate) || isValidCVV(cvv)) {
            return false;
        }

        // Check if card exists in the database
        return debitCardRepository.existsByCardNumber(cardNumber);
    }

    // Credit card validation (expects "cardNumber|expiryDate|cvv")
    private boolean validateCreditCard(String paymentDetails) {
        String[] details = paymentDetails.split("\\|");
        if (details.length != 3) return false;

        String cardNumber = details[0];
        String expiryDate = details[1];
        String cvv = details[2];

        if (isValidCardNumber(cardNumber) || isValidExpiryDate(expiryDate) || isValidCVV(cvv)) {
            return false;
        }

        // Check if card exists in the database
        return creditCardRepository.existsByCardNumber(cardNumber);
    }

    // Net banking validation (expects "accountNumber|ifscCode")
    private boolean validateNetBanking(String paymentDetails) {
        String[] details = paymentDetails.split("\\|");
        if (details.length != 2) return false;

        String accountNumber = details[0];
        String ifscCode = details[1];

        if (!isValidAccountNumber(accountNumber) || !isValidIfscCode(ifscCode)) {
            return false;
        }

        // Check if account number and IFSC code exist in the database
        return netBankingRepository.existsByAccountNumberAndIfscCode(accountNumber, ifscCode);
    }

    // UPI validation (expects "upiId")
    private boolean validateUPI(String upiId) {
        if (!isValidUPIId(upiId)) {
            return false;
        }

        // Check if UPI ID exists in the database
        return upiRepository.existsByUpiIdValue(upiId);
    }

    // Helper validation methods

    private boolean isValidCardNumber(String cardNumber) {
        return cardNumber == null || !cardNumber.matches("\\d{16}");
    }

    private boolean isValidExpiryDate(String expiryDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiry = YearMonth.parse(expiryDate, formatter);
            YearMonth current = YearMonth.now();
            return !expiry.isAfter(current) && !expiry.equals(current);
        } catch (Exception e) {
            return true;
        }
    }

    private boolean isValidCVV(String cvv) {
        return cvv == null || !cvv.matches("\\d{3}");
    }

    private boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && accountNumber.matches("\\d{9,18}");
    }

    private boolean isValidIfscCode(String ifscCode) {
        return ifscCode != null && ifscCode.matches("^[A-Za-z]{4}0[A-Za-z0-9]{6}$");
    }

    private boolean isValidUPIId(String upiId) {
        return upiId != null && upiId.matches("^[\\w.-]+@[\\w.-]+$");
    }
}