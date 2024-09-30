import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formatId',
})
export class FormatIdPipe implements PipeTransform {

  transform(value: string, length: number = 12): string {
    if (!value) return '';
    return value.length > length ? value.substring(0, length) + '...' : value;
  }

}
