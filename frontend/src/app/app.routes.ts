import { Routes } from '@angular/router';
import { HomeComponent } from './core/components/home/home.component';
import { RedirectGuard } from './guards/redirect.guard';
import { LoginComponent } from './core/components/login/login.component';
import { DashboardLayoutComponent } from './shared/layouts/dashboard-layout/dashboard-layout.component';
import { AuthGuard } from './guards/auth.guard';
import { EmployeeDashboardComponent } from './features/employee/components/employee-dashboard/employee-dashboard.component';
import { CustomersComponent } from './features/employee/components/customers/customers.component';
import { InvoicesComponent } from './features/employee/components/invoices/invoices.component';
import { CustomerDashboardComponent } from './features/customer/components/customer-dashboard/customer-dashboard.component';
import { MyinvoicesComponent } from './features/customer/components/myinvoices/myinvoices.component';
import { MytransactionsComponent } from './features/customer/components/mytransactions/mytransactions.component';

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
        path: 'o/customers',
        component: CustomersComponent,
      },
      {
        path: 'o/invoices',
        component: InvoicesComponent,
      },
      {
        path: 'u',
        component: CustomerDashboardComponent,
      },
      {
        path: 'u/invoices',
        component: MyinvoicesComponent,
      },
      {
        path: 'u/transactions',
        component: MytransactionsComponent,
      },
    ],
  },
  // {
  //   path: 'pay',
  //   component: PaymentDashboardComponent,
  //   canActivate: [AuthGuard],
  //   children: [
  //     {
  //       path: ':invoiceId/:meterNo',
  //       component: PaymentDetailsComponent,
  //     },
  //   ],
  // },
  {
    path: '**',
    redirectTo: '',
  },
];
