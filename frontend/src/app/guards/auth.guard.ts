// auth.guard.ts
import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, CanLoad, Route } from '@angular/router';
import { AuthService } from '../features/auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanLoad {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    return this.checkRoleAndRedirect(state.url);
  }

  canLoad(route: Route): boolean {
    return this.checkRoleAndRedirect(route.path!);
  }

  private checkRoleAndRedirect(url: string): boolean {
    const token = this.authService.getToken(); // Use getToken from AuthService
    const userRole = this.authService.getRole(); // Use getRole from AuthService

    if (!token) {
      this.router.navigate(['/login']);
      return false;
    }

    if (url === '/dashboard') {
      if (userRole === 'EMPLOYEE') {
        this.router.navigate(['/dashboard/o']);
        return false;
      } else if (userRole === 'CUSTOMER'){
        this.router.navigate(['/dashboard/u']);
        return false;
      }
    }

    return true;
  }
}
