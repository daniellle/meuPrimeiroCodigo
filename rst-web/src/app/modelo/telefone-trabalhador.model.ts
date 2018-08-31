import { Telefone } from 'app/modelo/telefone.model';

export class TelefoneTrabalhador {
    id: number;
    telefone: Telefone;

    constructor() {
        this.telefone = new Telefone();
    }
}
