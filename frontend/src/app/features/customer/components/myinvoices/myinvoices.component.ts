import { CommonModule } from '@angular/common';
import { Component, computed, TrackByFunction } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmPaginationImports } from '@spartan-ng/ui-pagination-helm';
import { BrnSelectModule } from '@spartan-ng/ui-select-brain';
import { HlmSelectModule } from '@spartan-ng/ui-select-helm';
import {
  BrnTableImports,
  useBrnColumnManager,
} from '@spartan-ng/ui-table-brain';
import { HlmTableImports } from '@spartan-ng/ui-table-helm';
import { TitleCasePipe } from '../../../../shared/pipes/titlecase.pipe';
import { AuthService } from '../../../../shared/services/auth.service';
import { InvoiceService } from '../../../../shared/services/invoice.service';
import { Invoice } from '../../../../types/invoice';
import { User } from '../../../../types/user';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { PayCardComponent } from "../pay-card/pay-card.component";


@Component({
  selector: 'app-myinvoices',
  standalone: true,
  imports: [
    BrnTableImports,
    HlmTableImports,
    HlmButtonDirective,
    HlmIconComponent,
    HlmInputDirective,
    TitleCasePipe,
    CommonModule,
    HlmPaginationImports,
    FormsModule,
    BrnSelectModule,
    HlmSelectModule,
    PayCardComponent
],
  templateUrl: './myinvoices.component.html',
  styleUrl: './myinvoices.component.css',
  host: {
    class: 'flex flex-col gap-2',
  },
})
export class MyinvoicesComponent {
  invoices: Invoice[] = [];
  totalInvoices: number = 0;

  pageSize: number = 5;
  currentPage: number = 0;

  user: User | null = null;

  loading: boolean = true;

  billDuration: string = '';

  billDurationSubject: Subject<string> = new Subject<string>();

  protected readonly _brnColumnManager = useBrnColumnManager({
    invoiceId: { visible: true, label: 'Invoice ID' },
    unitConsumption: { visible: true, label: 'Unit Consumption' },
    billDuration: { visible: true, label: 'Bill Duration' },
    billDueDate: { visible: true, label: 'Bill Due Date' },
    amountDue: { visible: true, label: 'Amount Due' },
    status: { visible: true, label: 'Status' },
  });

  protected readonly _allDisplayedColumns = computed(() => [
    ...this._brnColumnManager.displayedColumns(),
    'actions',
  ]);

  protected readonly _trackBy: TrackByFunction<Invoice> = (
    _: number,
    i: Invoice
  ) => i.invoiceId;
  Math: any;

  protected readonly _availablePageSizes = [5, 10, 20, 10000];

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
  
    // Listen to debounced bill duration changes
    this.billDurationSubject
      .pipe(
        debounceTime(300), // wait 300ms after the last event before emitting last event
        distinctUntilChanged() // only emit when the current value is different than the last
      )
      .subscribe((duration) => {
        this.fetchInvoicesByBillDuration(duration);
      });
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
            this.loading = false;
          },
          error: (error: any) => {
            console.error('Failed to fetch invoices:', error);
            this.loading = false;
          },
        });
    } else {
      console.error('Meter number not available for user');
    }
  }

  loadInvoices(event: any): void {
    if (this.pageSize !== event.rows!) {
      this.currentPage = 0;
    } else {
      this.currentPage = event.first! / event.rows!;
    }
    this.pageSize = event.rows!;
    this.getInvoices(this.currentPage, this.pageSize);
  }

  downloadInvoicePdf(pdfData: string, invoiceId: string): void {
    this.invoiceService.downloadInvoicePDF(pdfData, invoiceId);
  }

  calculatePageNumbers(): number[] {
    const totalPages = Math.ceil(this.totalInvoices / this.pageSize);
    const pageNumbers = [];
    const maxVisiblePages = 3; // Adjust as necessary

    if (totalPages <= maxVisiblePages) {
      // Show all pages if less than or equal to maxVisiblePages
      for (let i = 0; i < totalPages; i++) {
        pageNumbers.push(i);
      }
    } else {
      if (this.currentPage <= 1) {
        // Show the first few pages
        pageNumbers.push(0, 1);
      } else if (this.currentPage >= totalPages - 2) {
        // Show the last few pages
        pageNumbers.push(totalPages - 2, totalPages - 1);
      } else {
        // Show current page, with neighbors
        pageNumbers.push(
          this.currentPage - 1,
          this.currentPage,
          this.currentPage + 1
        );
      }
      // Always show first page
      pageNumbers.unshift(0);
      // Always show last page
      pageNumbers.push(totalPages - 1);
    }
    return pageNumbers;
  }

  onPageSizeChange(event: any) {
    this.currentPage = 0;
    this.loadInvoices({ first: 0, rows: this.pageSize });
  }

  showEllipsisBefore(): boolean {
    const totalPages = Math.ceil(this.totalInvoices / this.pageSize);
    return this.currentPage > 2 && totalPages > 5;
  }

  trackByPageIndex(index: number, item: any): number {
    return index;
  }

  onBillDurationChange(value: string): void {
    this.billDurationSubject.next(value);
  }
  
  fetchInvoicesByBillDuration(duration: string): void {
    if (!duration) {
      this.getInvoices(0, this.pageSize); 
    } else if (this.user && this.user.meterNo) {
      this.loading = true;
      this.invoiceService
        .getInvoiceByCustomerBillDuration(this.user.meterNo, duration)
        .subscribe({
          next: (response: any) => {
            this.invoices = response.invoices || [];
            this.totalInvoices = response.totalInvoices || 0;
            this.loading = false;
          },
          error: (error: any) => {
            console.error('Failed to fetch invoices:', error);
            this.loading = false;
          },
        });
    } else {
      console.error('Meter number not available for user');
    }
  }
  
}
