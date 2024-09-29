import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { FormInputComponent } from './components/form-input/form-input.component';
import { DashboardLayoutComponent } from './layouts/dashboard-layout/dashboard-layout.component';
import { TitleCasePipe } from './pipes/titlecase.pipe';

@NgModule({
    declarations: [
        FormInputComponent,
        DashboardLayoutComponent,
        TitleCasePipe 
    ],
    imports: [CommonModule, RouterModule, FormsModule],
    exports: [
        FormInputComponent,
        DashboardLayoutComponent,
        TitleCasePipe
    ],
})
export class SharedModule {}
