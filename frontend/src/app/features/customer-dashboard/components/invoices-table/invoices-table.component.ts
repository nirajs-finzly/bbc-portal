import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { AuthService } from '../../../../shared/services/auth.service';
import { InvoiceService } from '../../../../shared/services/invoice.service';
import { Invoice } from '../../../../types/invoice';
import { User } from '../../../../types/user';
import { CardModule } from 'primeng/card';
import { CommonModule } from '@angular/common';
import { FormatIdPipe } from '../../../../shared/pipes/format-id.pipe';
import { TitleCasePipe } from '../../../../shared/pipes/titlecase.pipe';

@Component({
  selector: 'app-invoices-table',
  standalone: true,
  imports: [CardModule, TableModule, ButtonModule, FormsModule, CommonModule, FormatIdPipe, TitleCasePipe],
  templateUrl: './invoices-table.component.html',
  styleUrl: './invoices-table.component.css',
})
export class InvoicesTableComponent {
  invoices: Invoice[] = [];
  totalInvoices: number = 0;

  pageSize: number = 5;
  currentPage: number = 0;

  user: User | null = null;

  loading: boolean = true;

  constructor(
    private invoiceService: InvoiceService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getUserData();

    if (this.user) {
      this.getInvoices(this.currentPage, this.pageSize);
    } else {
      console.error('User not found');
    }

    this.loadInvoices({ first: 0, rows: this.pageSize });
  }

  // Fetch invoices with server-side pagination
  getInvoices(page: number, size: number): void {
    if (this.user && this.user.meterNo) {
      this.loading = true;
      this.invoiceService
        .getInvoicesByMeterNo(this.user.meterNo, page, size)
        .subscribe({
          next: (response: any) => {
            this.invoices = response.invoices || [];
            this.totalInvoices = response.totalInvoices || 0;
            this.sortInvoicesByGeneratedAt();
            this.loading = false;
          },
          error: (error) => {
            console.error('Failed to fetch invoices:', error);
            this.loading = false;
          },
        });
    } else {
      console.error('Meter number not available for user');
    }
  }

  loadInvoices(event: TableLazyLoadEvent): void {
    this.currentPage = event.first! / event.rows!;
    this.pageSize = event.rows!;
    this.getInvoices(this.currentPage, this.pageSize);
  }

  sortInvoicesByGeneratedAt(): void {
    this.invoices.sort((a: Invoice, b: Invoice) => {
      const dateA = new Date(a.generatedAt).getTime();
      const dateB = new Date(b.generatedAt).getTime();
      return dateB - dateA;
    });
  }

  downloadInvoicePdf(pdfData: string, invoiceId: string): void {
    this.invoiceService.downloadInvoicePDF(pdfData, invoiceId);
  }
}
