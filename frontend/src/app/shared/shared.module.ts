import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormInputComponent } from './components/form-input/form-input.component';
import { DashboardLayoutComponent } from './dashboard-layout/dashboard-layout.component';
import { FormsModule } from '@angular/forms';

@NgModule({
    declarations: [FormInputComponent, DashboardLayoutComponent],
    imports: [CommonModule, RouterModule, FormsModule],
    exports: [FormInputComponent],
})
export class SharedModule {}
