import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formatId', standalone: true
})
export class FormatIdPipe implements PipeTransform {

  transform(value: string | null | undefined, length: number = 12): string {
    if (value == null || value === undefined) return ''; // Handle null and undefined
    return value.length > length ? value.substring(0, length) + '...' : value;
  }
  
}
