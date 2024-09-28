import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './components/home/home.component';
import { RouterLink, RouterModule } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { SharedModule } from "../shared/shared.module";

@NgModule({
    declarations: [HomeComponent, LoginComponent],
    imports: [
    CommonModule,
    RouterModule,
    RouterLink,
    SharedModule
],
    exports: [HomeComponent, LoginComponent]
})
export class CoreModule {}
