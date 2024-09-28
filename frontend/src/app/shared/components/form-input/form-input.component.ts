import { Component, Input, forwardRef } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
    selector: 'app-form-input',
    templateUrl: './form-input.component.html',
    styleUrls: ['./form-input.component.css'],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => FormInputComponent),
            multi: true,
        },
    ],
})
export class FormInputComponent implements ControlValueAccessor {
    @Input() label: string = '';
    @Input() placeholder: string = '';
    @Input() identifier: string = '';
    @Input() required: boolean = false;

    value: string = '';

    onChange = (value: string) => {};
    onTouched = () => {};

    // Method to be called when value changes
    writeValue(value: string): void {
        this.value = value;
    }

    // Registers a callback function to call when the input value changes
    registerOnChange(fn: any): void {
        this.onChange = fn;
    }

    // Registers a callback function to call when the input is touched
    registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    // Set the input as disabled
    setDisabledState?(isDisabled: boolean): void {
        // Handle the disabled state of your component
    }

    // Handle input change
    onInputChange(event: any): void {
        const value = event.target.value;
        this.onChange(value);
        this.onTouched();
    }
}
