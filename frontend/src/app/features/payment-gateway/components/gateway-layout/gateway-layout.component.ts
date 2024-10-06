import { Component } from '@angular/core';
import { CheckoutComponent } from '../checkout/checkout.component';

@Component({
  selector: 'app-gateway-layout',
  standalone: true,
  imports: [CheckoutComponent],
  templateUrl: './gateway-layout.component.html',
  styleUrl: './gateway-layout.component.css',
})
export class GatewayLayoutComponent {}
