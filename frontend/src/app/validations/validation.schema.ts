import { z } from 'zod';

export const identifierSchema = z.object({
  identifier: z
    .string()
    .min(1, 'ID is required!')
    .regex(/^(EMP|CRN)\d{7}$/g, 'Invalid ID!')
    .max(10, 'Invalid ID!'),
});

export const otpSchema = z.object({
  otp: z
    .string()
    .min(1, 'OTP is required!')
    .regex(/^\d{6}$/g, { message: 'Invalid OTP!' }),
});
