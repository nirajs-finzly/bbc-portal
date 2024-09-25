package com.bbc.app.utils;

import java.security.SecureRandom;

public class OtpGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static String generateOtp() {
        int otp = 100000 + random.nextInt(900000); // Ensures 6 digits
        return String.valueOf(otp);
    }
}
