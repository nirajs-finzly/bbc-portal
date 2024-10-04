import { Component } from '@angular/core';
import { AuthService } from '../../../../shared/services/auth.service';
import { User } from '../../../../types/user';
import { InvoicesTableComponent } from "../invoices-table/invoices-table.component";
import { PayCardComponent } from "../pay-card/pay-card.component";

@Component({
  selector: 'app-invoices',
  standalone: true,
  imports: [PayCardComponent, InvoicesTableComponent],
  templateUrl: './invoices.component.html',
  styleUrl: './invoices.component.css'
})
export class InvoicesComponent {
}
