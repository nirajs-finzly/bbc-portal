import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterModule, HlmButtonDirective, HlmIconComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  title: String = 'Bharat Bijili Corporation';
  description: String =
    'Manage and pay all your electricity bills with ease. Our platform allows customers to view and settle invoices, while employees can efficiently oversee billing operations.';
}
