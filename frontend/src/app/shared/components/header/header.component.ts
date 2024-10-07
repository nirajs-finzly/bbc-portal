import { Component, Input } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import {
  HlmMenuBarComponent,
  HlmMenuComponent,
  HlmMenuGroupComponent,
  HlmMenuItemDirective,
  HlmMenuLabelComponent,
  HlmMenuSeparatorComponent,
} from '@spartan-ng/ui-menu-helm';
import { navItems } from '../../../constants/nav-items';
import { NavItem } from '../../../types/nav-item';
import { User } from '../../../types/user';
import { BrnMenuTriggerDirective } from '@spartan-ng/ui-menu-brain';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { AuthService } from '../../services/auth.service';
import { TitleCasePipe } from '../../pipes/titlecase.pipe';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    RouterModule,
    HlmMenuBarComponent,
    HlmButtonDirective,
    BrnMenuTriggerDirective,
    HlmIconComponent,
    HlmMenuComponent,
    HlmMenuItemDirective,
    HlmMenuGroupComponent,
    HlmMenuSeparatorComponent,
    HlmMenuLabelComponent,
    HlmButtonDirective,
    TitleCasePipe
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  host: {
    class:
      'w-full h-[64px] flex-col justify-between items-center shadow-md sticky top-0',
  },
})
export class HeaderComponent {
  user: User | null = null;
  items: NavItem[] = [];

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.user = this.authService.getUserData();
    if (this.user) {
      this.items = this.getNavItems(this.user.role);
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
