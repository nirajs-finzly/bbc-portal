import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { EmployeeDashboardComponent } from './employee-dashboard/components/employee-dashboard/employee-dashboard.component';
import { CustomerDashboardComponent } from './customer-dashboard/components/customer-dashboard/customer-dashboard.component';
import { RouterModule } from '@angular/router';
import { InvoicesComponent } from './customer-dashboard/components/invoices/invoices.component';
import { InvoicesTableComponent } from './customer-dashboard/components/invoices-table/invoices-table.component';
import { PayCardComponent } from './customer-dashboard/components/pay-card/pay-card.component';

@NgModule({
    declarations: [
        EmployeeDashboardComponent,
        CustomerDashboardComponent,
        InvoicesComponent,
        InvoicesTableComponent,
        PayCardComponent,
    ],
    imports: [CommonModule, SharedModule, RouterModule],
    exports: [
        EmployeeDashboardComponent,
        CustomerDashboardComponent,
        InvoicesComponent,
        InvoicesTableComponent,
        PayCardComponent
    ],
})
export class FeaturesModule {}
