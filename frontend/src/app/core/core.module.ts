import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterLink, RouterModule } from '@angular/router';
import { LoginComponent } from '../features/auth/components/login/login.component';
import { SharedModule } from "../shared/shared.module";
import { HomeComponent } from './components/home/home.component';

@NgModule({
    declarations: [HomeComponent, LoginComponent],
    imports: [
    CommonModule,
    RouterModule,
    RouterLink,
    SharedModule,
    ReactiveFormsModule,
],
    exports: [HomeComponent, LoginComponent]
})
export class CoreModule {}
