import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormInputComponent } from './components/form-input/form-input.component';
import { DashboardLayoutComponent } from './dashboard-layout/dashboard-layout.component';

@NgModule({
    declarations: [FormInputComponent, DashboardLayoutComponent],
    imports: [CommonModule, RouterModule],
    exports: [FormInputComponent],
})
export class SharedModule {}
