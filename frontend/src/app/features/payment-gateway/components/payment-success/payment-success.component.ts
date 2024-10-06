import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HlmCardImports } from '@spartan-ng/ui-card-helm';

@Component({
  selector: 'app-payment-success',
  standalone: true,
  imports: [HlmCardImports, CommonModule],
  templateUrl: './payment-success.component.html',
  styleUrls: ['./payment-success.component.css'],
  host: {
    class: 'flex flex-col justify-center items-center h-screen w-full',
  },
})
export class PaymentSuccessComponent {
  isPopupVisible: boolean = false;

  constructor(private router: Router) {}

  ngOnInit() {
    setTimeout(() => {
      this.isPopupVisible = true;
    }, 100);

    setTimeout(() => {
      this.fadePopup();
    }, 2000); 
  }

  fadePopup() {
    this.isPopupVisible = false;

    setTimeout(() => {
      this.router.navigate(['/']);
    }, 500); 
  }
}
