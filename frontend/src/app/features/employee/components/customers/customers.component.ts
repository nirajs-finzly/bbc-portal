import { Component } from '@angular/core';
import { CustomersTableComponent } from '../customers-table/customers-table.component';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [CustomersTableComponent],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent {

}
