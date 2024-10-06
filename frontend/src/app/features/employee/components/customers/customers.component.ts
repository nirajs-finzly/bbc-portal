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
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { Customer } from '../../../../types/customer';
import { CustomerService } from '../../../../shared/services/customer.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { BrnAlertDialogContentDirective, BrnAlertDialogTriggerDirective } from '@spartan-ng/ui-alertdialog-brain';
import { HlmAlertDialogActionButtonDirective, HlmAlertDialogCancelButtonDirective, HlmAlertDialogComponent, HlmAlertDialogContentComponent, HlmAlertDialogDescriptionDirective, HlmAlertDialogFooterComponent, HlmAlertDialogHeaderComponent, HlmAlertDialogOverlayDirective, HlmAlertDialogTitleDirective } from '@spartan-ng/ui-alertdialog-helm';

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

  constructor(
    private customerService: CustomerService,
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
      this.customerService
        .getAllCustomers( page, size)
        .subscribe({
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
    } else if (this.customers.length!==0) {
      this.loading = true;
      this.customerService
        .getCustomersByMeterNo(meterNo)
        .subscribe({
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

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
  
      // Check if the file is a CSV
      if (file.type !== 'text/csv' && !file.name.endsWith('.csv')) {
        this.toast.error('Please upload a valid CSV file.');
        input.value = '';
        return;
      }
  
      // Process the file (e.g., read it, upload it, etc.)
      this.uploadFile(file);
    }
  }
  
  uploadFile(file: File) {
    // Implement your upload logic here
  }
  
}

