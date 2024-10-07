import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AuthService } from '../../../shared/services/auth.service';
import {
  identifierSchema,
  otpSchema,
} from '../../../validations/validation.schema';
import { HotToastService } from '@ngxpert/hot-toast';
import {
  HlmCardContentDirective,
  HlmCardDescriptionDirective,
  HlmCardDirective,
  HlmCardFooterDirective,
  HlmCardHeaderDirective,
  HlmCardTitleDirective,
} from '@spartan-ng/ui-card-helm';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    HlmCardContentDirective,
    HlmCardDescriptionDirective,
    HlmCardDirective,
    HlmCardFooterDirective,
    HlmCardHeaderDirective,
    HlmCardTitleDirective,
    HlmInputDirective,
    HlmButtonDirective,
    HlmIconComponent,
    CommonModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  host: {
    class: 'flex flex-col justify-center items-center h-screen',
  },
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
    private toast: HotToastService,
    private fb: FormBuilder
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
        this.toast.success('OTP sent! Please check your email.');
        this.setIsLoading(false);
        this.isOtpSent = true;
        this.nextStep();
        this.startCountdown();
      },
      error: (err: any) => {
        this.toast.error("Something went wrong!");
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
        this.toast.error(err.error.message);
        this.setIsLoading(false);
      },
    });
  }
}
