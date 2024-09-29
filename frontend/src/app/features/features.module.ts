import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { EmployeeDashboardComponent } from './employee-dashboard/components/employee-dashboard/employee-dashboard.component';
import { CustomerDashboardComponent } from './customer-dashboard/components/customer-dashboard/customer-dashboard.component';
import { RouterModule } from '@angular/router';

@NgModule({
    declarations: [EmployeeDashboardComponent, CustomerDashboardComponent],
    imports: [CommonModule, SharedModule, RouterModule],
    exports: [EmployeeDashboardComponent, CustomerDashboardComponent],
})
export class FeaturesModule {}
