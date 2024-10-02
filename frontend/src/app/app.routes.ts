import { Routes } from '@angular/router';
import { HomeComponent } from './core/components/home/home.component';
import { LoginComponent } from './features/auth/components/login/login.component';
import { CustomerDashboardComponent } from './features/customer-dashboard/components/customer-dashboard/customer-dashboard.component';
import { AuthGuard } from './guards/auth.guard';
import { RedirectGuard } from './guards/redirect.guard';
import { DashboardLayoutComponent } from './shared/layouts/dashboard-layout/dashboard-layout.component';
import { EmployeeDashboardComponent } from './features/employee/components/employee-dashboard/employee-dashboard.component';
import { InvoicesComponent } from './features/customer-dashboard/components/invoices/invoices.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    canActivate: [RedirectGuard],
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [RedirectGuard],
  },
  {
    path: 'dashboard',
    component: DashboardLayoutComponent, // Use DashboardLayoutComponent as the parent
    canActivate: [AuthGuard], // Guard applied
    children: [
      {
        path: 'o',
        component: EmployeeDashboardComponent, // Employee dashboard
      },
      {
        path: 'u',
        component: CustomerDashboardComponent, // Customer dashboard
      },
      {
        path: 'u/invoices',
        component: InvoicesComponent, // Route for Invoices
      },
    ]
  },
  {
    path: '**', // Wildcard route for 404
    redirectTo: '',
  }
];
