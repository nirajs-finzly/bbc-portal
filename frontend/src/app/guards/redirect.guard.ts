// src/app/guards/redirect.guard.ts
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../features/auth/services/auth.service';

@Injectable({
    providedIn: 'root',
})
export class RedirectGuard implements CanActivate {
    constructor(private authService: AuthService, private router: Router) {}

    canActivate(): boolean {
        const isAuthenticated = this.authService.isAuthenticated();
        const userRole = this.authService.getRole();

        if (isAuthenticated) {
            if (userRole === 'EMPLOYEE') {
                this.router.navigate(['/dashboard/o']);
            } else if (userRole === 'CUSTOMER') {
                this.router.navigate(['/dashboard/u']);
            }
            return false;
        }

        return true;
    }
}
