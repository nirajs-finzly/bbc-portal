import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-form-input',
  templateUrl: './form-input.component.html',
  styleUrl: './form-input.component.css'
})
export class FormInputComponent {
  @Input() label: string = '';
  @Input() placeholder: string = '';
  @Input() identifier: string = '';
  @Input() required: boolean = false;
}
