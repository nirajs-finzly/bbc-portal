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
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { Customer } from '../../../../types/customer';
import { CustomerService } from '../../../../shared/services/customer.service';

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
    private customerService: CustomerService
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
            this.sortCustomersByName();
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
    

  sortCustomersByName(): void {
    this.customers.sort((a: Customer, b: Customer) => {
      const dateA = new Date(a.user.name).getTime();
      const dateB = new Date(b.user.name).getTime();
      return dateB - dateA;
    });
  }

  calculatePageNumbers(): number[] {
    const totalPages = Math.ceil(this.totalCustomers / this.pageSize);
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
    this.loadCustomers({ first: 0, rows: this.pageSize });
  }

  showEllipsisBefore(): boolean {
    const totalPages = Math.ceil(this.totalCustomers / this.pageSize);
    return this.currentPage > 2 && totalPages > 5;
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
        .getCustomerByMeterNo(meterNo)
        .subscribe({
          next: (response: any) => {
            this.customers = response.customers || [];
            this.totalCustomers = response.totalCustomers || 0;
            this.sortCustomersByName();
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
}

