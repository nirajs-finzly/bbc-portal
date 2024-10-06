import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { PaymentService } from '../shared/services/payment.service';

@Injectable({
  providedIn: 'root',
})
export class PaymentGuard implements CanActivate {
  constructor(private paymentService: PaymentService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {
    // Get the full URL path
    const fullPath = route.url.map(segment => segment.path).join('/'); // Join segments to form the full path
    console.log(fullPath); // Log the full path

    // Split the path to get the invoiceId (middle value)
    const segments = fullPath.split('/').filter(segment => segment !== '');
    const invoiceId = segments[1] || null; // Assuming invoiceId is in the second segment

    console.log(invoiceId); // Log the invoiceId
    
    return this.paymentService.validatePaymentInitiation(invoiceId!).pipe(
      map((response: any) => {
        if (response.success) {
          console.log(response);
          return true;
        } else {
          this.router.navigate(['/']);
          return false;
        }
      }),
      catchError(() => {
        this.router.navigate(['/']);
        return new Observable<boolean>(observer => observer.next(false));
      })
    );
  }
}