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
import { InvoiceService } from '../../../../shared/services/invoice.service';
import { Invoice } from '../../../../types/invoice';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { FormatIdPipe } from '../../../../shared/pipes/format-id.pipe';
import { HotToastService } from '@ngxpert/hot-toast';
import { MessageResponse } from '../../../../types/customer';
import { HlmDialogComponent, HlmDialogContentComponent, HlmDialogDescriptionDirective, HlmDialogFooterComponent, HlmDialogHeaderComponent, HlmDialogTitleDirective } from '@spartan-ng/ui-dialog-helm';
import { BrnDialogContentDirective, BrnDialogDescriptionDirective, BrnDialogTitleDirective, BrnDialogTriggerDirective } from '@spartan-ng/ui-dialog-brain';
import { BrnAlertDialogContentDirective, BrnAlertDialogTriggerDirective } from '@spartan-ng/ui-alertdialog-brain';
import { HlmAlertDialogActionButtonDirective, HlmAlertDialogCancelButtonDirective, HlmAlertDialogComponent, HlmAlertDialogContentComponent, HlmAlertDialogDescriptionDirective, HlmAlertDialogFooterComponent, HlmAlertDialogHeaderComponent, HlmAlertDialogOverlayDirective, HlmAlertDialogTitleDirective } from '@spartan-ng/ui-alertdialog-helm';
import { Transaction } from '../../../../types/transaction';
import { PaymentService } from '../../../../shared/services/payment.service';
// import { PayCardComponent } from "../pay-card/pay-card.component";

@Component({
  selector: 'app-invoices',
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
    FormatIdPipe,
    HlmDialogComponent,
    HlmDialogContentComponent,
    HlmDialogDescriptionDirective,
    HlmDialogFooterComponent,
    HlmDialogHeaderComponent,
    HlmDialogTitleDirective,
    BrnDialogContentDirective,
    BrnDialogTriggerDirective,
    BrnDialogTitleDirective,
    BrnDialogDescriptionDirective,
    BrnAlertDialogTriggerDirective,
    BrnAlertDialogContentDirective,

    HlmAlertDialogComponent,
    HlmAlertDialogOverlayDirective,
    HlmAlertDialogHeaderComponent,
    HlmAlertDialogFooterComponent,
    HlmAlertDialogTitleDirective,
    HlmAlertDialogDescriptionDirective,
    HlmAlertDialogCancelButtonDirective,
    HlmAlertDialogActionButtonDirective,
    HlmAlertDialogContentComponent,
  ],
  templateUrl: './invoices.component.html',
  styleUrl: './invoices.component.css',
  host: {
    class: 'flex flex-col gap-2',
  },
})
export class InvoicesComponent {
  invoices: Invoice[] = [];
  totalInvoices: number = 0;

  pageSize: number = 5;
  currentPage: number = 0;

  loading: boolean = true;

  customerName: string = '';

  selectedFile: File | null = null;

  customerNameSubject: Subject<string> = new Subject<string>();

  protected readonly _brnColumnManager = useBrnColumnManager({
    invoiceId: { visible: true, label: 'Invoice ID' },
    customerName: { visible: true, label: 'Customer Name' },
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
    private paymentService: PaymentService,
    private toast: HotToastService
  ) {}

  ngOnInit(): void {
    this.loadInvoices({ first: 0, rows: this.pageSize });

    // Listen to debounced bill duration changes
    this.customerNameSubject
      .pipe(
        debounceTime(300), // wait 300ms after the last event before emitting last event
        distinctUntilChanged() // only emit when the current value is different than the last
      )
      .subscribe((customerName) => {
        this.fetchInvoicesByCustomerName(customerName);
      });
  }

  // Fetch invoices with server-side pagination
  getInvoices(page: number, size: number): void {
    this.loading = true;
    this.invoiceService.getAllInvoices(page, size).subscribe({
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
  
  createInvoice(meterNo: string, unitsConsumed: string, billDuration: string, billDueDate: string, ctx: any): void {
    // Validate input values
  if (!this.validateMeterNo(meterNo)) {
    this.toast.error('Please enter a valid meter number (starts with "MTR" followed by 7 digits)');
    return;
  }
  if (!this.validateUnitsConsumed(unitsConsumed)) {
    this.toast.error('Please enter valid units consumed (a number)');
    return;
  }
  if (!this.validateBillDuration(billDuration)) {
    const trimmedBillDuration = billDuration.trim();
    // Regex pattern to match "Month Year" format (e.g., "January 2024")
    const billDurationPattern = /^(January|February|March|April|May|June|July|August|September|October|November|December) \d{4}$/;
    
    if (!billDurationPattern.test(trimmedBillDuration)) {
      this.toast.error('Please enter the bill duration in the format "Month Year" (e.g., January 2024)');
    } else {
      this.toast.error('Please enter a valid bill duration in the past (before the current month and year)');
    }
    
    return;
  }
  if (!this.validateBillDueDate(billDueDate)) {
    this.toast.error('Please enter the future bill due date');
    return;
  }

    // Create the request payload
    const request = {
      meterNo: meterNo,
      unitsConsumed: Number(unitsConsumed), // Convert unitsConsumed to a number
      billDuration: billDuration,
      billDueDate: billDueDate,
    };

    // Call the service to create the invoice
    this.invoiceService.createInvoice(request).subscribe(
      (response: MessageResponse) => {
        this.toast.success('Invoice created successfully!');
        ctx.close();
        this.loadInvoices({ first: 0, rows: this.pageSize });
      },
      (error) => {
        this.toast.error('Customer not found for this meter number');
      }
    );
  }

    // Meter number validation method
    private validateMeterNo(meterNo: string): boolean {
      const trimmedMeterNo = meterNo.trim();
      const meterNoPattern = /^MTR\d{7}$/;
      return trimmedMeterNo.length > 0 && meterNoPattern.test(trimmedMeterNo);
    }

    // Units consumed validation method
    private validateUnitsConsumed(unitsConsumed: string): boolean {
        const trimmedUnits = unitsConsumed.trim();
        const units = Number(trimmedUnits);
        return trimmedUnits.length > 0 && !isNaN(units) && units > 0;
    }

    private validateBillDuration(billDuration: string): boolean {
      const trimmedBillDuration = billDuration.trim();
      // Regex pattern to match "Month Year" format (e.g., "January 2024")
      const billDurationPattern = /^(January|February|March|April|May|June|July|August|September|October|November|December) \d{4}$/;
      
      // Check if the billDuration matches the pattern
      if (!billDurationPattern.test(trimmedBillDuration)) {
        return false;
      }
    
      // Extract month and year from billDuration
      const [month, year] = trimmedBillDuration.split(' ');
      const monthNames = [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
      ];
      const billMonth = monthNames.indexOf(month);
      const billYear = parseInt(year, 10);
    
      // Get current month and year
      const currentDate = new Date();
      const currentMonth = currentDate.getMonth(); // 0-indexed, January is 0
      const currentYear = currentDate.getFullYear();
    
      // Validate that bill year is less than current year, or if the same year, check that bill month is less than current month
      if (billYear > currentYear || (billYear === currentYear && billMonth >= currentMonth)) {
        return false;
      }
    
      return true;
    }
    


    

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
  
      if (input?.files?.length) {
        this.selectedFile = input.files[0];
      }

      // Check if the file is a CSV
      if (file.type !== 'text/csv' && !file.name.endsWith('.csv')) {
        this.toast.error('Please upload a valid CSV file.');
        input.value = '';
        return;
      }
    }
  }
  

  // File import handler
  importFile(fileInput: HTMLInputElement,billDuration: string, billDueDate: string, ctx: any): void {
    const file = fileInput?.files?.[0]; // Access file from input element
    if (!file) {
      this.toast.error('Please select a file');
      return;
    }
    if (!this.validateBillDuration(billDuration)) {
      const trimmedBillDuration = billDuration.trim();
      // Regex pattern to match "Month Year" format (e.g., "January 2024")
      const billDurationPattern = /^(January|February|March|April|May|June|July|August|September|October|November|December) \d{4}$/;
      
      if (!billDurationPattern.test(trimmedBillDuration)) {
        this.toast.error('Please enter the bill duration in the format "Month Year" (e.g., January 2024)');
      } else {
        this.toast.error('Please enter a valid bill duration in the past (before the current month and year)');
      }
      
      return;
    }
    if (!this.validateBillDueDate(billDueDate)) {
      this.toast.error('Please enter a future bill due date');
      return;
    }    

    this.uploadFile(file, billDuration, billDueDate, ctx);
  }

  uploadFile(file: File, billDuration: string, billDueDate: string, ctx: any) {
    this.invoiceService.bulkUploadInvoice(file,billDuration,billDueDate).subscribe(
      (response: MessageResponse) => {
        this.toast.success(response.message);
        ctx.close();
        this.loadInvoices({ first: 0, rows: this.pageSize });
      },
      (error) => {
        this.toast.error(error);
      }
    );
  }

  // Bill due date validation method
  private validateBillDueDate(billDueDate: string): boolean {
    const currentDate = new Date();
    const dueDate = new Date(billDueDate);

    // Check if billDueDate is a valid date and in the future
    return dueDate > currentDate;
  }

  viewInvoicePdf(pdfData: string, invoiceId: string): void {
    if (!pdfData) {
      console.error('No PDF data found for invoice:', invoiceId);
      return;
    }
    this.invoiceService.viewInvoicePDF(pdfData, invoiceId);
  }

  onPageSizeChange(event: any) {
    this.currentPage = 0;
    this.loadInvoices({ first: 0, rows: this.pageSize });
  }
  trackByPageIndex(index: number, item: any): number {
    return index;
  }

  onCustomerNameChange(value: string): void {
    this.customerNameSubject.next(value);
  }

  fetchInvoicesByCustomerName(customerName: string): void {
    if (!customerName) {
      this.getInvoices(0, this.pageSize);
    } else if (this.invoices.length !== 0) {
      this.loading = true;
      this.invoiceService.getInvoicesByCustomerName(customerName).subscribe({
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
      console.error('No invoices found');
    }
  }



  // Function to mark the invoice as paid
  markAsPaid(invoiceId: string): void {
    this.paymentService.markInvoiceAsPaid(invoiceId, 'CASH').subscribe(
      (response) => {
        // On success, update the paymentStatus of the invoice to PAID
        const invoice = this.invoices.find(i => i.invoiceId === invoiceId);
        if (invoice) {
          invoice.paymentStatus = 'PAID';
        }
      },
      (error) => {
        console.error('Error marking invoice as paid:', error);
      }
    );
  }
}
