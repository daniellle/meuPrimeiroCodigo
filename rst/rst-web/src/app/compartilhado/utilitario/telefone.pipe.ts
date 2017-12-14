import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'telefonePipe',
})
export class TelefonePipe implements PipeTransform {
    transform(value: string) {
        if (value) {
            try {
                if (value.length === 10) {
                    value = value.toString();
                    return '(' + value.substring(0, 2).concat(') ')
                        .concat(value.substring(2, 6))
                        .concat('-')
                        .concat(value.substring(6, value.length));
                } else {
                    value = value.toString();
                    return '(' + value.substring(0, 2).concat(') ')
                        .concat(value.substring(2, 7))
                        .concat('-')
                        .concat(value.substring(7, value.length));
                }
            } catch (error) {
                return value;
            }
        }
        return value;
    }
}
