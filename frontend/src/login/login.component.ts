import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule,FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  employeeId: string = '';
  otp: string = '';
  generatedOtp: string = ''; // Store the generated OTP
  otpGenerated: boolean = false; // Track if OTP is generated

  constructor() {}

  // Handle form submission
  onSubmit() {
    if (!this.employeeId || !this.otp) {
      alert('Please enter both Employee ID and OTP.');
      return;
    }

    if (this.otp === this.generatedOtp) {
      console.log('Login successful!');
      alert('Login Successful!');
      // Add further logic (e.g., redirect to another page)
    } else {
      console.log('Incorrect OTP');
      alert('Incorrect OTP, please try again.');
    }
  }

  // Generate OTP logic
  generateOTP() {
    if (!this.employeeId) {
      alert('Please enter your Employee ID before generating OTP.');
      return;
    }

    this.generatedOtp = Math.floor(100000 + Math.random() * 900000).toString(); // Generate a 6-digit OTP
    this.otpGenerated = true; // Set flag to indicate that OTP is generated

    console.log('Generated OTP:', this.generatedOtp);
  }
}
