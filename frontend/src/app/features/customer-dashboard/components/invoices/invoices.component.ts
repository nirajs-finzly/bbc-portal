import { Component, OnInit } from '@angular/core';
import { Invoice } from '../../../../types/invoice';
import { InvoiceService } from '../../services/invoice.service';
import { AuthService } from '../../../auth/services/auth.service';
import { User } from '../../../../types/user';

@Component({
  selector: 'app-invoices',
  templateUrl: './invoices.component.html',
  styleUrls: ['./invoices.component.css']
})
export class InvoicesComponent implements OnInit {
  user: User | null = null; 

  constructor(private invoiceService: InvoiceService, private authService: AuthService) {}

  ngOnInit(): void {
    this.user = this.authService.getUserData();
  }

}