import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

@Pipe({
    name: 'datePipe',
})
export class DatePipe implements PipeTransform {
    transform(item: any): any {
        if (!item) {
            return item;
        }
        return moment(item).utc().format('DD/MM/YYYY');
    }
}
