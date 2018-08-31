import { IOption } from 'ng-select';
import { Estado } from 'app/modelo/estado.model';

export class Option implements IOption {
    value: string;
    label: string;
    estado: Estado;
}
