import { Component } from '@angular/core';

@Component({
  selector: 'app-dashboard-layout',
  templateUrl: './dashboard-layout.component.html',
  styleUrls: ['./dashboard-layout.component.css']
})
export class DashboardLayoutComponent {
  userName: string = 'John Doe';
  email: string = 'john@gmail.com';
  isEmployee: boolean = true; 
  isCustomer: boolean = !this.isEmployee;
  
  constructor() {
    // Here, implement logic to determine the user type and name from authentication service.
  }
}
