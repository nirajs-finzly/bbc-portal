import { Component } from '@angular/core';
import { User } from '../../../../types/user';
import { Invoice } from '../../../../types/invoice';
import { InvoiceService } from '../../../../shared/services/invoice.service';
import { AuthService } from '../../../../shared/services/auth.service';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { HlmCardImports } from '@spartan-ng/ui-card-helm';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { Router, RouterLink } from '@angular/router';
import { PaymentService } from '../../../../shared/services/payment.service';

@Component({
  selector: 'app-pay-card',
  standalone: true,
  imports: [
    CurrencyPipe,
    DatePipe, 
    HlmCardImports,
    HlmButtonDirective,
    HlmIconComponent,
    RouterLink,
  ],
  templateUrl: './pay-card.component.html',
  styleUrl: './pay-card.component.css',
  host: {
    class: 'w-fit block mb-4',
  },
})
export class PayCardComponent {
  user: User | null = null;
  latestInvoice: Invoice | null = null;

  constructor(
    private invoiceService: InvoiceService,
    private authService: AuthService,
    private paymentService: PaymentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getUserData();

    if (this.user) {
      this.getLatestUnpaidInvoice();
    } else {
      console.error('User not found');
    }
  }

  getLatestUnpaidInvoice(): void {
    if (this.user && this.user.meterNo) {
      this.invoiceService
        .getLatestUnpaidInvoiceByMeterNo(this.user.meterNo)
        .subscribe({
          next: (response: any) => {
            this.latestInvoice = response.invoiceData || null;
          },
          error: (error: any) => {
            console.error('Failed to fetch latest unpaid invoice:', error);
          },
        });
    } else {
      console.error('Meter number not available for user!');
    }
  }

  initiateInvoicePayment(): void {
    if (this.user && this.latestInvoice && this.user.meterNo) {
      this.paymentService.initiatePayment(this.user?.meterNo, this.latestInvoice.invoiceId).subscribe({
        next: (response: any) => {
          if(response.success) {
            console.log
            this.router.navigate([response.callbackUrl]);
          }
        },
        error: (error: any) => {
          console.error('Failed to initiate payment:', error);
        },
      });
    } else {
      console.error('Meter number not available for user');
    }
  }
}
