import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmCardImports } from '@spartan-ng/ui-card-helm';
import {
  HlmFormFieldComponent,
  HlmHintDirective,
} from '@spartan-ng/ui-formfield-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmRadioGroupImports } from '@spartan-ng/ui-radiogroup-helm';
import { FormatIdPipe } from '../../../../shared/pipes/format-id.pipe';
import { AuthService } from '../../../../shared/services/auth.service';
import { InvoiceService } from '../../../../shared/services/invoice.service';
import { PaymentService } from '../../../../shared/services/payment.service';
import { Invoice } from '../../../../types/invoice';
import { User } from '../../../../types/user';
import { HotToastService } from '@ngxpert/hot-toast';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [
    HlmCardImports,
    HlmInputDirective,
    HlmFormFieldComponent,
    HlmHintDirective,
    HlmButtonDirective,
    FormatIdPipe,
    CurrencyPipe,
    HlmIconComponent,
    HlmRadioGroupImports,
    HlmInputDirective,
    ReactiveFormsModule,
    FormsModule,
    CommonModule,
  ],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.css',
  host: {
    class: 'w-[65%]',
  },
})
export class CheckoutComponent {
  cardPaymentForm: FormGroup;
  netBankingPaymentForm: FormGroup;
  upiPaymentForm: FormGroup;
  otpForm: FormGroup;

  isLoading: boolean = false;

  invoice: Invoice | null = null;
  user: User | null = null;

  currentStep: number = 1;

  earlyPaymentDiscount: number = 0;
  onlinePaymentDiscount: number = 0;
  totalDiscount: number = 0;
  finalBillAmount: number = 0;

  paymentMethod: string = 'CREDIT_CARD';

  constructor(
    private invoiceService: InvoiceService,
    private authService: AuthService,
    private paymentService: PaymentService,
    private toast: HotToastService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.cardPaymentForm = this.fb.group({
      cardNumber: ['', Validators.required],
      expiryDate: ['', Validators.required],
      cvv: ['', Validators.required],
    });

    this.netBankingPaymentForm = this.fb.group({
      bankName: ['', [Validators.required]],
      accountNumber: [
        '',
        [
          Validators.required,
          Validators.minLength(9),
          Validators.maxLength(18),
          Validators.pattern(/^[0-9]+$/),
        ],
      ],
      ifscCode: [
        '',
        [Validators.required, Validators.pattern(/^[A-Z]{4}0[A-Z0-9]{6}$/)],
      ],
    });

    this.upiPaymentForm = this.fb.group({
      upiId: [
        '',
        [Validators.required, Validators.pattern(/^[\w.-]+@[a-zA-Z]+$/)],
      ],
    });

    this.otpForm = this.fb.group({
      otp: [
        '',
        [
          Validators.required,
          Validators.minLength(6),
          Validators.maxLength(6),
          Validators.pattern(/^\d{6}$/),
        ],
      ],
    });
  }

  ngOnInit(): void {
    this.user = this.authService.getUserData();

    if (this.user) {
      this.fetchInvoice();
    } else {
      console.error('User not found');
    }
  }

  nextStep() {
    this.currentStep++;
  }

  setIsLoading(isLoading: boolean) {
    this.isLoading = isLoading;
  }

  fetchInvoice(): void {
    if (this.user && this.user.meterNo) {
      this.setIsLoading(true);
      this.invoiceService
        .getLatestUnpaidInvoiceByMeterNo(this.user.meterNo)
        .subscribe({
          next: (response: any) => {
            this.invoice = response.invoiceData || null;

            if (this.invoice && this.invoice.totalAmountDue) {
              this.calculateDiscounts(this.invoice.totalAmountDue);
            }
            this.setIsLoading(false);
          },
          error: (error: any) => {
            console.error('Failed to fetch invoices:', error);
            this.setIsLoading(false);
          },
        });
    }
  }

  calculateDiscounts(billAmount: number): void {
    const isEarlyPayment = this.checkIfEarlyPayment();
    const isOnlinePayment = this.checkIfOnlinePayment();

    if (isEarlyPayment) {
      this.earlyPaymentDiscount = billAmount * 0.05; // 5% early payment discount
    }

    if (isOnlinePayment) {
      this.onlinePaymentDiscount = billAmount * 0.05; // 5% online payment discount
    }

    this.totalDiscount = this.earlyPaymentDiscount + this.onlinePaymentDiscount;
    this.finalBillAmount = billAmount - this.totalDiscount;
  }

  checkIfEarlyPayment(): boolean {
    const dueDate = this.invoice?.billDueDate
      ? new Date(this.invoice.billDueDate)
      : null;
    const today = new Date();
    return dueDate ? today < dueDate : false;
  }

  checkIfOnlinePayment(): boolean {
    return true; // Assuming it's always online payment for now
  }

  onCardNumberInput(event: any): void {
    const input = event.target as HTMLInputElement;
    let value = input.value.replace(/\D/g, ''); // Remove all non-digit characters

    // Format card number in groups of 4 digits (e.g., '1234 5678 9012 3456')
    if (value.length > 4) {
      value = value.match(/.{1,4}/g)?.join(' ') ?? value;
    }

    input.value = value;
    this.cardPaymentForm
      .get('cardNumber')
      ?.setValue(value.replace(/\s+/g, ''), { emitEvent: false });
  }

  // Validator to check for a valid card number length (16 digits)
  cardNumberValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value || '';
    const regex = /^\d{16}$/; // Allows exactly 16 digits

    if (!regex.test(value.replace(/\s+/g, ''))) {
      return { invalidCardNumber: true };
    }

    return null;
  }

  onCardExpiryInput(event: any): void {
    const input = event.target as HTMLInputElement;
    let value = input.value.replace(/\D/g, ''); // Remove all non-digit characters

    if (value.length >= 2) {
      value = value.slice(0, 2) + '/' + value.slice(2);
    }

    if (value.length > 5) {
      value = value.slice(0, 5); // Max length MM/YY
    }

    input.value = value;
    this.cardPaymentForm
      .get('expiryDate')
      ?.setValue(value, { emitEvent: false });
  }

  // Validator to check MM/YY format and valid date
  cardExpiryValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value || '';
    const regex = /^(0[1-9]|1[0-2])\/\d{2}$/;

    if (!regex.test(value)) {
      return { invalidExpiryDate: true };
    }

    const [month, year] = value.split('/');
    const currentYear = new Date().getFullYear() % 100; // Last two digits of current year
    const currentMonth = new Date().getMonth() + 1; // Months are 0-based, add 1

    if (
      parseInt(year, 10) < currentYear ||
      (parseInt(year, 10) === currentYear && parseInt(month, 10) < currentMonth)
    ) {
      return { expired: true };
    }

    return null;
  }

  onCvvInput(event: any): void {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/\D/g, ''); // Remove all non-digit characters
    this.cardPaymentForm
      .get('cvv')
      ?.setValue(input.value, { emitEvent: false });
  }

  // Validator to check for valid CVV length (either 3 or 4 digits)
  cvvValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value || '';
    const regex = /^\d{3,4}$/; // Allows only 3 or 4 digits

    if (!regex.test(value)) {
      return { invalidCvv: true };
    }

    return null;
  }

  makePaymentRequest(): void {
    if (!this.invoice || !this.user) {
      console.error('User or invoice data is missing');
      return;
    }

    this.setIsLoading(true);
    let paymentDetails: string = '';

    // Check the selected payment method and gather form data accordingly
    switch (this.paymentMethod) {
      case 'CREDIT_CARD':
      case 'DEBIT_CARD':
        if (this.cardPaymentForm.valid) {
          const cardNumber = this.cardPaymentForm.get('cardNumber')?.value;
          const expiryDate = this.cardPaymentForm.get('expiryDate')?.value;
          const cvv = this.cardPaymentForm.get('cvv')?.value;
          paymentDetails = `${cardNumber}|${expiryDate}|${cvv}`;
        } else {
          console.error('Credit card form is invalid');
          this.setIsLoading(false);
          return;
        }
        break;

      case 'NET_BANKING':
        if (this.netBankingPaymentForm.valid) {
          const bankName = this.netBankingPaymentForm.get('bankName')?.value;
          const accountNumber =
            this.netBankingPaymentForm.get('accountNumber')?.value;
          const ifscCode = this.netBankingPaymentForm.get('ifscCode')?.value;
          paymentDetails = `${accountNumber}|${ifscCode}`;
        } else {
          console.error('Net banking form is invalid');
          this.setIsLoading(false);
          return;
        }
        break;

      case 'UPI':
        if (this.upiPaymentForm.valid) {
          const upiId = this.upiPaymentForm.get('upiId')?.value;
          paymentDetails = upiId; // Just the UPI ID
        } else {
          console.error('UPI form is invalid');
          this.setIsLoading(false);
          return;
        }
        break;

      default:
        console.error('Invalid payment method selected');
        this.setIsLoading(false);
        return;
    }

    // Call the makePayment method from the PaymentService
    this.paymentService
      .makePayment(
        this.user.meterNo || '',
        this.invoice.invoiceId,
        this.paymentMethod,
        paymentDetails
      )
      .subscribe({
        next: (response: any) => {
          if (response.success) {
            this.toast.success('OTP Sent!');
            console.log('OTP Sent', response);
            this.setIsLoading(false);
            this.nextStep();
          } else {
            this.router.navigateByUrl('/payment-failure/' + response.message);
            console.error('Payment failed:', response.message);
            this.setIsLoading(false);
          }
        },
        error: (error: any) => {
          this.router.navigateByUrl('/payment-failure/' + error.error.message);
          console.error('Payment request failed:', error);
          this.setIsLoading(false);
        },
      });
  }

  confirmPayment(): void {
    if (!this.invoice || !this.user) {
      console.error('User or invoice data is missing');
      return;
    }

    this.setIsLoading(true);

    let paymentDetails: string = '';
    let otp: string = '';

    // Check the selected payment method and gather form data accordingly
    switch (this.paymentMethod) {
      case 'CREDIT_CARD':
      case 'DEBIT_CARD':
        if (this.cardPaymentForm.valid) {
          const cardNumber = this.cardPaymentForm.get('cardNumber')?.value;
          const expiryDate = this.cardPaymentForm.get('expiryDate')?.value;
          const cvv = this.cardPaymentForm.get('cvv')?.value;
          paymentDetails = `${cardNumber}|${expiryDate}|${cvv}`;
        } else {
          console.error('Credit card form is invalid');
          this.setIsLoading(false);
          return;
        }
        break;

      case 'NET_BANKING':
        if (this.netBankingPaymentForm.valid) {
          const accountNumber =
            this.netBankingPaymentForm.get('accountNumber')?.value;
          const ifscCode = this.netBankingPaymentForm.get('ifscCode')?.value;
          paymentDetails = `${accountNumber}|${ifscCode}`;
        } else {
          console.error('Net banking form is invalid');
          this.setIsLoading(false);
          return;
        }
        break;

      case 'UPI':
        if (this.upiPaymentForm.valid) {
          const upiId = this.upiPaymentForm.get('upiId')?.value;
          paymentDetails = upiId; // Just the UPI ID
        } else {
          console.error('UPI form is invalid');
          this.setIsLoading(false);
          return;
        }
        break;

      default:
        console.error('Invalid payment method selected');
        this.setIsLoading(false);
        return;
    }

    if (this.otpForm.valid) {
      otp = this.otpForm.get('otp')?.value;

      const amount = this.finalBillAmount.toFixed(2).toString(); // Format amount to 2 decimal places

      this.paymentService
        .confirmPayment(
          this.user.meterNo || '',
          this.invoice.invoiceId,
          this.paymentMethod,
          paymentDetails,
          otp,
          amount
        )
        .subscribe({
          next: (response: any) => {
            if (response.success) {
              this.setIsLoading(false);
              this.router.navigateByUrl('/payment-success');
            } else {
              console.error('Payment confirmation failed:', response.message);
              this.setIsLoading(false);
              this.router.navigateByUrl('/payment-failure/' + response.message);
            }
          },
          error: (error: any) => {
            console.error(
              'Payment confirmation request failed:',
              error.error.message
            );
            this.setIsLoading(false);
            this.router.navigateByUrl(
              '/payment-failure/' + error.error.message
            );
          },
        });
    } else {
      console.error('Invalid OTP!');
      this.setIsLoading(false);
      this.router.navigateByUrl('/payment-failure/invalid-otp');
      return;
    }
  }
}