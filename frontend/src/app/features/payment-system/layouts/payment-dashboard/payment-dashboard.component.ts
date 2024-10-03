import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { StepperModule } from 'primeng/stepper';
import { paymentItems } from '../../../../constants/payment-items';
import { PaymentItem } from '../../../../types/payment-item';

@Component({
  selector: 'app-payment-dashboard',
  standalone: true,
  imports: [StepperModule, ButtonModule, RouterOutlet],
  templateUrl: './payment-dashboard.component.html',
  styleUrl: './payment-dashboard.component.css',
})
export class PaymentDashboardComponent {
  items: PaymentItem[] = paymentItems;
}
