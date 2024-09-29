import { Component } from '@angular/core';
import { AuthService } from '../../../features/auth/services/auth.service';
import { Router } from '@angular/router';
import { User } from '../../../types/user';
import { NavItem } from '../../../types/nav-item';
import { navItems } from '../../../constants/nav-items';

@Component({
  selector: 'app-dashboard-layout',
  templateUrl: './dashboard-layout.component.html',
  styleUrls: ['./dashboard-layout.component.css']
})
export class DashboardLayoutComponent {
  user: User | null = null;
  navItems: NavItem[] = [];
  
  constructor(private authService: AuthService, private router: Router) {
  }

  ngOnInit(): void {
    this.user = this.authService.getUserData();
    if (this.user) {
      this.navItems = this.getNavItems(this.user.role);
    }
  }

  getNavItems(role: string): NavItem[] {
    if (role === 'EMPLOYEE') {
      return navItems.EMPLOYEE;
    } else if (role === 'CUSTOMER') {
      return navItems.CUSTOMER;
    }
    return [];
  }

  logoutUser(){
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
