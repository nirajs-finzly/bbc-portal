import { Routes } from '@angular/router';
import { HomeComponent } from './core/components/home/home.component';
import { LoginComponent } from './core/components/login/login.component';
import { CustomerDashboardComponent } from './features/customer/components/customer-dashboard/customer-dashboard.component';
import { MyinvoicesComponent } from './features/customer/components/myinvoices/myinvoices.component';
import { MytransactionsComponent } from './features/customer/components/mytransactions/mytransactions.component';
import { CustomersComponent } from './features/employee/components/customers/customers.component';
import { EmployeeDashboardComponent } from './features/employee/components/employee-dashboard/employee-dashboard.component';
import { InvoicesComponent } from './features/employee/components/invoices/invoices.component';
import { GatewayLayoutComponent } from './features/payment-gateway/components/gateway-layout/gateway-layout.component';
import { AuthGuard } from './guards/auth.guard';
import { RedirectGuard } from './guards/redirect.guard';
import { DashboardLayoutComponent } from './shared/layouts/dashboard-layout/dashboard-layout.component';
import { CheckoutComponent } from './features/payment-gateway/components/checkout/checkout.component';
import { PaymentSuccessComponent } from './features/payment-gateway/components/payment-success/payment-success.component';
import { PaymentFailureComponent } from './features/payment-gateway/components/payment-failure/payment-failure.component';
import { PaymentGuard } from './guards/payment.guard';

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
  {
    path: 'pay/:invoiceId/:meterNo',
    component: GatewayLayoutComponent,
    canActivate: [AuthGuard, PaymentGuard],
  },
  {
    path: 'payment-success',
    component: PaymentSuccessComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'payment-failure/:message',
    component: PaymentFailureComponent,
    canActivate: [AuthGuard],
  },
  {
    path: '**',
    redirectTo: '/',
  },
];