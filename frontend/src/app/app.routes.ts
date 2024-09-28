import { Routes } from '@angular/router';
import { HomeComponent } from './core/components/home/home.component';
import { LoginComponent } from './core/components/login/login.component';
import { DashboardLayoutComponent } from './shared/dashboard-layout/dashboard-layout.component';
import { EmployeeDashboardComponent } from './features/components/employee-dashboard/employee-dashboard.component';

export const routes: Routes = [
    {
        path: '',
        component: HomeComponent,
    },
    {
        path: 'login',
        component: LoginComponent,
    },
    {
        path: 'dashboard',
        component: DashboardLayoutComponent,
        children: [
            {
                path: '',
                component: EmployeeDashboardComponent,
            }
        ]
    },
];
