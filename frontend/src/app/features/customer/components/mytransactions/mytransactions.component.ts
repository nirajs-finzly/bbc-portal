import { Component, computed, TrackByFunction } from '@angular/core';
import { Transaction } from '../../../../types/transaction';
import { User } from '../../../../types/user';
import {
  BrnTableImports,
  useBrnColumnManager,
} from '@spartan-ng/ui-table-brain';
import { PaymentService } from '../../../../shared/services/payment.service';
import { AuthService } from '../../../../shared/services/auth.service';
import { HlmTableImports } from '@spartan-ng/ui-table-helm';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { TitleCasePipe } from '../../../../shared/pipes/titlecase.pipe';
import { CommonModule } from '@angular/common';
import { HlmPaginationImports } from '@spartan-ng/ui-pagination-helm';
import { FormsModule } from '@angular/forms';
import { BrnSelectModule } from '@spartan-ng/ui-select-brain';
import { HlmSelectModule } from '@spartan-ng/ui-select-helm';
import { FormatIdPipe } from '../../../../shared/pipes/format-id.pipe';

@Component({
  selector: 'app-mytransactions',
  standalone: true,
  imports: [
    BrnTableImports,
    HlmTableImports,
    HlmButtonDirective,
    HlmIconComponent,
    HlmInputDirective,
    TitleCasePipe,
    FormatIdPipe,
    CommonModule,
    HlmPaginationImports,
    FormsModule,
    BrnSelectModule,
    HlmSelectModule,
  ],
  templateUrl: './mytransactions.component.html',
  styleUrl: './mytransactions.component.css',
})
export class MytransactionsComponent {
  transactions: Transaction[] = [];
  totalTransactions: number = 0;

  pageSize: number = 5;
  currentPage: number = 0;

  user: User | null = null;

  loading: boolean = true;

  protected readonly _brnColumnManager = useBrnColumnManager({
    transactionId: { visible: true, label: 'Transaction ID' },
    invoiceId: { visible: true, label: 'Invoice ID' },
    transactionDate: { visible: true, label: 'Transaction Date' },
    amount: { visible: true, label: 'Amount' },
    paymentMethod: { visible: true, label: 'Payment Method' },
    paymentIdentifier: { visible: true, label: 'Payer ID' },
    transactionStatus: { visible: true, label: 'Transaction Status' },
  });

  protected readonly _allDisplayedColumns = computed(() => [
    ...this._brnColumnManager.displayedColumns(),
  ]);

  protected readonly _trackBy: TrackByFunction<Transaction> = (
    _: number,
    i: Transaction
  ) => i.transactionId;
  Math: any;

  protected readonly _availablePageSizes = [5, 10, 20, 10000];

  constructor(
    private paymentService: PaymentService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getUserData();

    if (this.user) {
      this.getCustomerTransactions(this.currentPage, this.pageSize);
    } else {
      console.error('User not found');
    }

    this.loadTransactions({ first: 0, rows: this.pageSize });
  }

  getCustomerTransactions(page: number, size: number): void {
    if (this.user && this.user.meterNo) {
      this.loading = true;
      this.paymentService
        .getAllTransactionsByMeterNo(this.user.meterNo, page, size)
        .subscribe({
          next: (response: any) => {
            this.transactions = response.transactions || [];
            this.totalTransactions = response.totalTransactions || 0;
            this.sortTransactionsByTransactionDate();
            this.loading = false;
          },
          error: (error: any) => {
            console.error('Failed to fetch transactions:', error);
            this.loading = false;
          },
        });
    } else {
      console.error('Meter number not available for user');
    }
  }

  loadTransactions(event: any): void {
    if (this.pageSize !== event.rows!) {
      this.currentPage = 0;
    } else {
      this.currentPage = event.first! / event.rows!;
    }
    this.pageSize = event.rows!;
    this.getCustomerTransactions(this.currentPage, this.pageSize);
  }

  sortTransactionsByTransactionDate(): void {
    this.transactions.sort((a: Transaction, b: Transaction) => {
      const dateA = new Date(a.transactionDate).getTime();
      const dateB = new Date(b.transactionDate).getTime();
      return dateB - dateA;
    });
  }

  onPageSizeChange(event: any) {
    this.currentPage = 0;
    this.loadTransactions({ first: 0, rows: this.pageSize });
  }

  trackByPageIndex(index: number, item: any): number {
    return index;
  }
}