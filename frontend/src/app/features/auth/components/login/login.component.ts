import { Component, signal } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HotToastService } from '@ngxpert/hot-toast';
import { CryptoService } from '../../../../shared/services/crypto.service';
import { identifierSchema, otpSchema } from '../../schemas/validation.schema';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
})
export class LoginComponent {
    loginForm: FormGroup;
    isLoading: boolean = false;
    isOtpSent = signal(false);
    currentStep: number = 1;

    constructor(
        private authService: AuthService,
        private cryptoService: CryptoService,
        private fb: FormBuilder,
        private toast: HotToastService
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
        if (this.isOtpSent()) {
            this.currentStep = this.currentStep + 1;
        }
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
                if (response.success) {
                    this.toast.success('OTP sent successfully!');
                    this.isOtpSent.set(true);
                    this.nextStep();
                } else {
                    this.toast.error('Something went wrong!');
                }
                this.setIsLoading(false);
            },
            error: (err: any) => {
                this.toast.error('Invalid ID!');
                console.log(err);
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
                if (response.success) {
                    const encryptedToken = this.cryptoService.encrypt(
                        response.token
                    );
                    const encryptedRole = this.cryptoService.encrypt(
                        response.role
                    );
                    localStorage.setItem('token', encryptedToken);
                    localStorage.setItem('role', encryptedRole);
                    this.toast.success('Login successful');

                    this.loginForm.reset({
                        identifier: '',
                        otp: ''
                    });
                    this.isOtpSent.set(false);
                    this.currentStep = 1;
                } else {
                    this.toast.error('Something went wrong!');
                }
                this.setIsLoading(false);
            },
            error: (err: any) => {
                this.toast.error('Error signing in!');
                console.log(err);
                this.setIsLoading(false);
            },
        });
    }
}
