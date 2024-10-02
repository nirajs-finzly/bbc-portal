import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { MenubarModule } from 'primeng/menubar';
import { navItems } from '../../../constants/nav-items';
import { NavItem } from '../../../types/nav-item';
import { User } from '../../../types/user';
import { AuthService } from '../../services/auth.service';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { TitleCasePipe } from '../../pipes/titlecase.pipe';
import { MenuModule } from 'primeng/menu';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-dashboard-layout',
  standalone: true,
  imports: [
    CardModule,
    AvatarModule,
    ButtonModule,
    MenubarModule,
    RouterModule,
    OverlayPanelModule,
    MenuModule,
    TitleCasePipe
  ],
  templateUrl: './dashboard-layout.component.html',
  styleUrl: './dashboard-layout.component.css',
})
export class DashboardLayoutComponent {
  user: User | null = null;
  navItems: NavItem[] = [];
  menuItems: MenuItem[] = [
    { label: 'Logout', icon: 'fa-solid fa-right-from-bracket', command: () => this.logoutUser() },
  ];

  constructor(private authService: AuthService, private router: Router) {}

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

  logoutUser() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
