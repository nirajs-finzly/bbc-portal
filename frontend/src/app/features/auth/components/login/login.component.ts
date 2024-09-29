import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HotToastService } from '@ngxpert/hot-toast';
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
    currentStep: number = 1;

    constructor(
        private authService: AuthService,
        private fb: FormBuilder,
        private toast: HotToastService,
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
                this.toast.success('OTP sent successfully!');
                this.nextStep();
                this.setIsLoading(false);
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
                this.toast.error('Login failed!');
                this.setIsLoading(false);
            },
        });
    }
    
}
