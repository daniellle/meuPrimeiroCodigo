import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'inativo',
})
export class InativoPipe implements PipeTransform {
    transform(value) {
        if (value) {
            return 'Inativo';
        }
        return 'Ativo';
    }
}
