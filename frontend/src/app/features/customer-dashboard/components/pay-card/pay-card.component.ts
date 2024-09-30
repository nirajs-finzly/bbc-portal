import { Component } from '@angular/core';
import { User } from '../../../../types/user';
import { InvoiceService } from '../../services/invoice.service';
import { AuthService } from '../../../auth/services/auth.service';
import { Invoice } from '../../../../types/invoice';

@Component({
    selector: 'app-pay-card',
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
}
