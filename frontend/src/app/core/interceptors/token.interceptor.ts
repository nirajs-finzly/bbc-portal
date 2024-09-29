import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../../features/auth/services/auth.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  
  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();

    // Define routes where token should NOT be attached
    const excludedUrls = ['/auth/send-otp', '/auth/login']; 

    // Check if the request URL contains any of the excluded paths
    const isExcluded = excludedUrls.some(url => req.url.includes(url));

    // Attach the token only if it's not an excluded route and token exists
    if (token && !isExcluded) {
      const clonedRequest = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
      return next.handle(clonedRequest);
    }

    // If no token is available, pass the request as it is
    return next.handle(req);
  }
}
