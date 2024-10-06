import { Component } from '@angular/core';
import { HlmCardImports } from '@spartan-ng/ui-card-helm';
import { User } from '../../../../types/user';
import { InvoiceService } from '../../../../shared/services/invoice.service';
import { AuthService } from '../../../../shared/services/auth.service';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'app-customer-dashboard',
  standalone: true,
  imports: [HlmCardImports, HlmIconComponent, CurrencyPipe],
  templateUrl: './customer-dashboard.component.html',
  styleUrl: './customer-dashboard.component.css',
})
export class CustomerDashboardComponent {
  user: User | null = null;

  totalInvoices: number = 0;
  unpaidInvoices: number = 0;
  lastPaidAmount: number = 0;
  averageConsumption: number = 0;

  constructor(
    private invoiceService: InvoiceService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getUserData();
    if (this.user && this.user.meterNo) {
      this.fetchInvoiceStatistics(this.user?.meterNo);
    }
  }

  fetchInvoiceStatistics(meterNo: string): void {
    this.invoiceService.getLastPaymentAmount(meterNo).subscribe({
      next: (response) => {
        this.lastPaidAmount = response.data.amount;
      },
      error: (error) => {
        console.error('Error fetching last payment amount', error);
      },
    });

    this.invoiceService.getAllCustomerInvoicesCount(meterNo).subscribe({
      next: (response) => {
        this.totalInvoices = response.count;
      },
      error: (error) => {
        console.error('Error fetching customer invoices count', error);
      },
    });

    this.invoiceService.getCustomerUnpaidInvoicesCount(meterNo).subscribe({
      next: (response) => {
        this.unpaidInvoices = response.count;
      },
      error: (error) => {
        console.error('Error fetching customer unpaid invoices count', error);
      },
    });

    this.invoiceService.getAverageUnitConsumptionOfCustomer(meterNo).subscribe({
      next: (response) => {
        this.averageConsumption = response.data.averageUnitConsumption;
      },
      error: (error) => {
        console.error('Error fetching average unit consumption', error);
      },
    });
  }
}
