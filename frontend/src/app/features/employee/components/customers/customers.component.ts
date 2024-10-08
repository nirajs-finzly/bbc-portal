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
import {
  BrnDialogContentDirective,
  BrnDialogDescriptionDirective,
  BrnDialogTitleDirective,
  BrnDialogTriggerDirective,
} from '@spartan-ng/ui-dialog-brain';
import {
  HlmDialogComponent,
  HlmDialogContentComponent,
  HlmDialogDescriptionDirective,
  HlmDialogFooterComponent,
  HlmDialogHeaderComponent,
  HlmDialogTitleDirective,
} from '@spartan-ng/ui-dialog-helm';
import { HlmTableImports } from '@spartan-ng/ui-table-helm';
import { TitleCasePipe } from '../../../../shared/pipes/titlecase.pipe';
import { Observable, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { Customer, MessageResponse } from '../../../../types/customer';
import { CustomerService } from '../../../../shared/services/customer.service';
import { HotToastService } from '@ngxpert/hot-toast';
import {
  BrnAlertDialogContentDirective,
  BrnAlertDialogTriggerDirective,
} from '@spartan-ng/ui-alertdialog-brain';
import {
  HlmAlertDialogActionButtonDirective,
  HlmAlertDialogCancelButtonDirective,
  HlmAlertDialogComponent,
  HlmAlertDialogContentComponent,
  HlmAlertDialogDescriptionDirective,
  HlmAlertDialogFooterComponent,
  HlmAlertDialogHeaderComponent,
  HlmAlertDialogOverlayDirective,
  HlmAlertDialogTitleDirective,
} from '@spartan-ng/ui-alertdialog-helm';
import { Transaction } from '../../../../types/transaction';
import { PaymentService } from '../../../../shared/services/payment.service';

@Component({
  selector: 'app-customers',
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
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css',
  host: {
    class: 'flex flex-col gap-2',
  },
})
export class CustomersComponent {
  customers: Customer[] = [];
  totalCustomers: number = 0;

  pageSize: number = 5;
  currentPage: number = 0;

  loading: boolean = true;

  meterNo: string = '';

  customer: any;

  selectedFile: File | null = null;

  transactions: Transaction[] = [];
  showDialog: boolean = false;

  meterNoSubject: Subject<string> = new Subject<string>();

  protected readonly _brnColumnManager = useBrnColumnManager({
    meterNo: { visible: true, label: 'Meter No' },
    name: { visible: true, label: 'Name' },
    email: { visible: true, label: 'Email' },
    phone: { visible: true, label: 'Phone' },
    address: { visible: true, label: 'Address' },
  });

  protected readonly _allDisplayedColumns = computed(() => [
    ...this._brnColumnManager.displayedColumns(),
    'actions',
  ]);

  protected readonly _trackBy: TrackByFunction<Customer> = (
    _: number,
    i: Customer
  ) => i.meterNo;
  Math: any;

  protected readonly _availablePageSizes = [5, 10, 20, 10000];


  protected readonly _brnModalColumnManager = useBrnColumnManager({
    transactionDate: { visible: true, label: 'Date' },
    amount: { visible: true, label: 'Amount' },
    paymentMethod: { visible: true, label: 'Payment Method' },
  });

  protected readonly _allModalDisplayedColumns = computed(() => [
    ...this._brnModalColumnManager.displayedColumns(),
  ]);

  protected readonly _modalTrackBy: TrackByFunction<Transaction> = (
    _: number,
    i: Transaction
  ) => i.transactionId;

  Math1: any;
  Math2: any;


  constructor(
    private customerService: CustomerService,
    private paymentService: PaymentService,
    private toast: HotToastService
  ) {}

  ngOnInit(): void {
    this.loadCustomers({ first: 0, rows: this.pageSize });

    // Listen to debounced bill duration changes
    this.meterNoSubject
      .pipe(
        debounceTime(300), // wait 300ms after the last event before emitting last event
        distinctUntilChanged() // only emit when the current value is different than the last
      )
      .subscribe((meterNo) => {
        this.fetchCustomersByMeterNo(meterNo);
      });
  }

  // Fetch invoices with server-side pagination
  getCustomers(page: number, size: number): void {
    this.loading = true;
    this.customerService.getAllCustomers(page, size).subscribe({
      next: (response: any) => {
        this.customers = response.customers || [];
        this.totalCustomers = response.totalCustomers || 0;
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Failed to fetch customers:', error);
        this.loading = false;
      },
    });
  }

  loadCustomers(event: any): void {
    if (this.pageSize !== event.rows!) {
      this.currentPage = 0;
    } else {
      this.currentPage = event.first! / event.rows!;
    }
    this.pageSize = event.rows!;
    this.getCustomers(this.currentPage, this.pageSize);
  }

  onPageSizeChange(event: any) {
    this.currentPage = 0;
    this.loadCustomers({ first: 0, rows: this.pageSize });
  }

  trackByPageIndex(index: number, item: any): number {
    return index;
  }

  onMeterNoChange(value: string): void {
    this.meterNoSubject.next(value);
  }

  fetchCustomersByMeterNo(meterNo: string): void {
    if (!meterNo) {
      this.getCustomers(0, this.pageSize);
    } else if (this.customers.length !== 0) {
      this.loading = true;
      this.customerService.getCustomersByMeterNo(meterNo).subscribe({
        next: (response: any) => {
          this.customers = response.customers || [];
          this.totalCustomers = response.totalCustomers || 0;
          this.loading = false;
        },
        error: (error: any) => {
          console.error('Failed to fetch customers:', error);
          this.loading = false;
        },
      });
    } else {
      console.error('No customers found');
    }
  }

  viewCustomer(meterNo: string) {
    this.customerService.getCustomersByMeterNo(meterNo).subscribe((data) => {
      this.customer = data;
    });
  }

  // Method to update customer
  updateCustomer(
    meterNo: string,
    name: string,
    email: string,
    phone: string,
    address: string,
    ctx: any
  ): void {
    // Validate input values
  if (!this.validateName(name)) {
    this.toast.error('Please enter a valid customer name (only alphabets and spaces, 3-50 characters long)');
    return;
  }
  if (!this.validateEmail(email)) {
    this.toast.error('Please enter a valid email address');
    return;
  }
  if (!phone || !this.validatePhone(phone)) {
    this.toast.error('Please enter a valid 10-digit phone number');
    return;
  }
  if (!this.validateAddress(address)) {
    this.toast.error('Please enter a valid address (10-100 characters long)');
    return;
  }

    // Create the request payload
    const request = {
      name: name,
      email: email,
      phone: phone,
      address: address,
    };

    // Call the service to update customer
    this.customerService.updateCustomer(meterNo, request).subscribe(
      (response: MessageResponse) => {
        this.toast.success('Customer updated successfully!');
        ctx.close(); // Close the dialog on success
        this.loadCustomers({ first: 0, rows: this.pageSize });
      },
      (error) => {
        this.toast.error('Failed to update the customer');
      }
    );
  }

  // Method to view transactions when user clicks the button
  viewTransactions(meterNo: string): void {
    this.paymentService.getAllTransactionsByMeterNo(meterNo).subscribe({
      next: (response) => {
        this.transactions = response.transactions; // Assuming the API returns a list of transactions
        console.log(response)
        this.showDialog = true; // Show the dialog when data is available
      },
      error: (error) => {
        console.error('Failed to load transactions', error);
      },
    });
  }

  // Method to track transactions by transactionId
  trackByTransactionId(index: number, transaction: Transaction): string {
    return transaction.transactionId;
  }

  // Method to close the dialog
  closeDialog(): void {
    this.showDialog = false;
  }

  // Method to create a new customer
  createCustomer(
    name: string,
    email: string,
    phone: string,
    address: string,
    ctx: any
  ): void {
    // Validate input values
  if (!this.validateName(name)) {
    this.toast.error('Please enter a valid customer name (only alphabets and spaces, 3-50 characters long)');
    return;
  }
  if (!email || !this.validateEmail(email)) {
    this.toast.error('Please enter a valid email address');
    return;
  }
  if (!phone || !this.validatePhone(phone)) {
    this.toast.error('Please enter a valid 10-digit phone number');
    return;
  }
  if (!this.validateAddress(address)) {
    this.toast.error('Please enter a valid address (10-100 characters long)');
    return;
  }


    // Create the request payload
    const request = {
      name: name,
      email: email,
      phone: phone,
      address: address,
    };

    // Call the service to create customer
    this.customerService.createCustomer(request).subscribe(
      (response: MessageResponse) => {
        this.toast.success('Customer created successfully!');
        ctx.close(); // Close the dialog on success
        this.loadCustomers({ first: 0, rows: this.pageSize });
      },
      (error) => {
        this.toast.error('Failed to create the customer');
      }
    );
  }

  // Name validation method
  private validateName(name: string): boolean {
    const trimmedName = name.trim();
    const namePattern = /^[a-zA-Z\s]{3,50}$/;
    return trimmedName.length >= 3 && trimmedName.length <= 50 && namePattern.test(trimmedName);
  }

  // Address validation method
  private validateAddress(address: string): boolean {
    const trimmedAddress = address.trim();
    return trimmedAddress.length >= 10 && trimmedAddress.length <= 100;
  }

  // Email validation method
  private validateEmail(email: string): boolean {
    const trimmedEmail = email.trim();
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return emailPattern.test(trimmedEmail);
  }

  // Phone number validation method
  private validatePhone(phone: string): boolean {
    const trimmedPhone = phone.trim();
  const phonePattern = /^[0-9]{10}$/;
  return phonePattern.test(trimmedPhone);
  }

  // Delete customer method
deleteCustomer(meterNo: string, ctx: any): void {
  if (!meterNo) {
    this.toast.error('Meter number is required');
    return;
  }

  // First, check if there are any unpaid invoices for this customer
  this.customerService.hasUnpaidInvoices(meterNo).subscribe(
    (hasUnpaid: boolean) => {
      if (hasUnpaid) {
        // If unpaid invoices exist, show an error message and stop the process
        this.toast.error('Cannot delete customer. Unpaid invoices are pending.');
        return;
      }

      // If no unpaid invoices, proceed with customer deletion
      this.customerService.deleteCustomer(meterNo).subscribe(
        (response) => {
          this.toast.success(response.message);
          ctx.close();
          this.loadCustomers({ first: 0, rows: this.pageSize });
        },
        (error) => {
          this.toast.error('Failed to delete customer');
        }
      );
    },
    (error) => {
      this.toast.error('Failed to check unpaid invoices');
    }
  );
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
  importFile(fileInput: HTMLInputElement, ctx: any): void {
    const file = fileInput?.files?.[0]; // Access file from input element
    if (!file) {
      this.toast.error('Please select a file');
      return;
    }

    this.uploadFile(file, ctx);
  }

  uploadFile(file: File, ctx: any) {
    this.customerService.bulkUploadCustomer(file).subscribe(
      {
        next: (response: MessageResponse) => {
        this.toast.success(response.message);
        console.log(response);
        ctx.close();
        this.loadCustomers({ first: 0, rows: this.pageSize });
      },
      error: (error) => {
        this.toast.error(error);
      }

      }
      
    );
  }
}
