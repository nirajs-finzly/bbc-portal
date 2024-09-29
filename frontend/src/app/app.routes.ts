// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { HomeComponent } from './core/components/home/home.component';
import { LoginComponent } from './features/auth/components/login/login.component';
import { EmployeeDashboardComponent } from './features/employee-dashboard/components/employee-dashboard/employee-dashboard.component';
import { CustomerDashboardComponent } from './features/customer-dashboard/components/customer-dashboard/customer-dashboard.component'; 
import { AuthGuard } from './guards/auth.guard';
import { RedirectGuard } from './guards/redirect.guard'; // Import the new guard
import { DashboardLayoutComponent } from './shared/layouts/dashboard-layout/dashboard-layout.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    canActivate: [RedirectGuard], // Guard applied
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [RedirectGuard], // Guard applied
  },
  {
    path: 'dashboard',
    component: DashboardLayoutComponent,
    canActivate: [AuthGuard], // Guard applied
    children: [
      {
        path: 'o',
        component: EmployeeDashboardComponent,
      },
      {
        path: 'u',
        component: CustomerDashboardComponent,
      }
    ]
  },
  {
    path: '**', // Wildcard route for 404 page
    redirectTo: '',
  }
];
