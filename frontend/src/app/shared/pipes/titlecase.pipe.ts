import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'titlecase', standalone: true })
export class TitleCasePipe implements PipeTransform {
  transform(value: string | undefined | null): string {
    if (value === undefined || value === null) return ''; // Change to 'N/A' if you prefer that

    return value
      .toLowerCase()
      .split(' ')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  }
}
