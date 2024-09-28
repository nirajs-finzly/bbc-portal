import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { EmployeeDashboardComponent } from './components/employee-dashboard/employee-dashboard.component';

@NgModule({
    declarations: [EmployeeDashboardComponent],
    imports: [CommonModule, SharedModule],
    exports: [EmployeeDashboardComponent],
})
export class CoreModule {}
