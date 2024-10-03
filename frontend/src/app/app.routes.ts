import { Routes } from '@angular/router';
import { HomeComponent } from './core/components/home/home.component';
import { LoginComponent } from './features/auth/components/login/login.component';
import { CustomerDashboardComponent } from './features/customer-dashboard/components/customer-dashboard/customer-dashboard.component';
import { AuthGuard } from './guards/auth.guard';
import { RedirectGuard } from './guards/redirect.guard';
import { DashboardLayoutComponent } from './shared/layouts/dashboard-layout/dashboard-layout.component';
import { EmployeeDashboardComponent } from './features/employee/components/employee-dashboard/employee-dashboard.component';
import { InvoicesComponent } from './features/customer-dashboard/components/invoices/invoices.component';
import { PaymentDashboardComponent } from './features/payment-system/layouts/payment-dashboard/payment-dashboard.component';
import { PaymentDetailsComponent } from './features/payment-system/components/payment-details/payment-details.component';

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
    component: DashboardLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'o',
        component: EmployeeDashboardComponent,
      },
      {
        path: 'u',
        component: CustomerDashboardComponent,
      },
      {
        path: 'u/invoices',
        component: InvoicesComponent,
      },
    ],
  },
  {
    path: 'pay',
    component: PaymentDashboardComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: ':invoiceId/:meterNo',
        component: PaymentDetailsComponent,
      },
    ],
  },
  {
    path: '**',
    redirectTo: '',
  },
];
