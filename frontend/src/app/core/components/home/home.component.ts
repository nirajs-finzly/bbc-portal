import { Component } from '@angular/core';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrl: './home.component.css',
})
export class HomeComponent {
    title: String = "Bharat Bijili Corporation"
    description: String = "Manage and pay all your electricity bills with ease. Our platform allows customers to view and settle invoices, while employees can efficiently oversee billing operations."
}
