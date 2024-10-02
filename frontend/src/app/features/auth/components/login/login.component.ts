import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CardModule } from 'primeng/card';
import { identifierSchema, otpSchema } from '../../schemas/validation.schema';
import { AuthService } from '../../../../shared/services/auth.service';
import { CommonModule } from '@angular/common';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { ToastService } from '../../../../shared/services/toast.service';
import { InputOtpModule } from 'primeng/inputotp';
import { AutoFocusModule } from 'primeng/autofocus';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CardModule,
    ReactiveFormsModule,
    CommonModule,
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    InputOtpModule,
    ButtonModule,
    DropdownModule,
    AutoFocusModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  loginForm: FormGroup;
  isLoading: boolean = false;
  currentStep: number = 1;

  resendCountdown: number = 60;
  canResendOtp: boolean = false;
  isOtpSent: boolean = false;

  private interval: any;

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private toast: ToastService
  ) {
    this.loginForm = this.fb.group({
      identifier: ['', Validators.required],
      otp: ['', Validators.required],
    });
  }

  setIsLoading(isLoading: boolean) {
    this.isLoading = isLoading;
  }

  // Move to the next step
  nextStep() {
    this.currentStep += 1;
  }

  startCountdown() {
    this.resendCountdown = 60;
    this.canResendOtp = false;

    // Start the countdown interval
    this.interval = setInterval(() => {
      this.resendCountdown--;
      if (this.resendCountdown === 0) {
        this.canResendOtp = true;
        clearInterval(this.interval);
      }
    }, 1000);
  }

  sendOtp() {
    const identifier = this.loginForm.get('identifier')?.value;

    const validation = identifierSchema.safeParse({ identifier });
    if (!validation.success) {
      this.toast.error(validation.error.errors[0].message);
      return;
    }

    this.setIsLoading(true);
    this.authService.sendOtp(identifier).subscribe({
      next: (response: any) => {
        this.toast.success('OTP sent successfully! Please check your email.');
        this.nextStep();
        this.setIsLoading(false);
        this.startCountdown();
        this.isOtpSent = true;
      },
      error: (err: any) => {
        this.toast.error('Invalid ID!');
        this.setIsLoading(false);
      },
    });
  }

  login() {
    const identifier = this.loginForm.get('identifier')?.value;
    const otp = this.loginForm.get('otp')?.value;

    const validation = otpSchema.safeParse({ otp });
    if (!validation.success) {
      this.toast.error(validation.error.errors[0].message);
      return;
    }

    this.setIsLoading(true);
    this.authService.login(identifier, otp).subscribe({
      next: (response: any) => {
        this.setIsLoading(false);
      },
      error: (err: any) => {
        this.toast.error('Failed to login!');
        this.setIsLoading(false);
      },
    });
  }
}
