import { Component } from '@angular/core';
import { User } from '../../../../types/user';
import { Invoice } from '../../../../types/invoice';
import { InvoiceService } from '../../../../shared/services/invoice.service';
import { AuthService } from '../../../../shared/services/auth.service';
import { CardModule } from 'primeng/card';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-pay-card',
  standalone: true,
  imports: [CardModule, CurrencyPipe, DatePipe, ButtonModule, DialogModule, RouterModule],
  templateUrl: './pay-card.component.html',
  styleUrl: './pay-card.component.css',
})
export class PayCardComponent {
  user: User | null = null;
  latestInvoice: Invoice | null = null;

  constructor(
    private invoiceService: InvoiceService,
    private authService: AuthService
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
        .subscribe((response: any) => {
          this.latestInvoice = response.invoiceData || null;
        });
    } else {
      console.error('Meter number not available for user!');
    }
  }

  downloadInvoicePdf(pdfData: string, invoiceId: string): void {
    this.invoiceService.downloadInvoicePDF(pdfData, invoiceId);
  }

}
