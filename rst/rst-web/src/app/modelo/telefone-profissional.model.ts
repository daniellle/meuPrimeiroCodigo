import { Telefone } from 'app/modelo/telefone.model';

export class TelefoneProfissional {
    id: number;
    telefone: Telefone;

    constructor() {
        this.telefone = new Telefone();
    }
}
