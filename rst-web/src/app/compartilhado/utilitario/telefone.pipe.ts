import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'telefonePipe',
})
export class TelefonePipe implements PipeTransform {
    transform(value: string) {
        if (value) {
            try {
                if (value.length === 8) {
                    value = value.toString();
                    return value.substring(0, 4).concat('-')
                        .concat(value.substring(4, value.length));
                } else if (value.length === 9) {
                    value = value.toString();
                    return value.substring(0, 5)
                        .concat('-')
                        .concat(value.substring(5, value.length));
                } else if (value.length === 10) {
                    value = value.toString();
                    return '(' + value.substring(0, 2).concat(') ')
                        .concat(value.substring(2, 6))
                        .concat('-')
                        .concat(value.substring(6, value.length));
                } else if (value.length === 11) {
                    value = value.toString();
                    if (value[0] === '0') {
                        return '(' + value.substring(0, 3).concat(') ')
                        .concat(value.substring(3, 7))
                        .concat('-')
                        .concat(value.substring(7, value.length));
                    }
                    return '(' + value.substring(0, 2).concat(') ')
                    .concat(value.substring(2, 6))
                    .concat('-')
                    .concat(value.substring(6, value.length));
                } else if (value.length === 12) {
                    value = value.toString();
                    return '(' + value.substring(0, 3).concat(') ')
                    .concat(value.substring(3, 8))
                    .concat('-')
                    .concat(value.substring(8, value.length));
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
