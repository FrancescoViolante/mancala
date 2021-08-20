import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'removeP'
})
export class RemovePPipe implements PipeTransform {

  transform(value: any, ...args: any[]): any {

      return value.replace('P', '');
  }

}
