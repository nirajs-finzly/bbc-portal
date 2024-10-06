import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HlmCardImports } from '@spartan-ng/ui-card-helm';

@Component({
  selector: 'app-payment-failure',
  standalone: true,
  imports: [HlmCardImports, CommonModule],
  templateUrl: './payment-failure.component.html',
  styleUrl: './payment-failure.component.css',
  host: {
    class: 'flex flex-col justify-center items-center h-screen w-full',
  },
})
export class PaymentFailureComponent {
  isPopupVisible: boolean = false;
  message: string | null = null;

  constructor(private router: Router, private route: ActivatedRoute) {}

  ngOnInit() {
    // Extract the message from the route parameters
    this.message = this.route.snapshot.paramMap.get('message');

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