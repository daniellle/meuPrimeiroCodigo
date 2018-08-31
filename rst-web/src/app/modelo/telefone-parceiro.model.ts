import { Telefone } from 'app/modelo/telefone.model';

export class TelefoneParceiro {
    id: number;
    telefone: Telefone;

    constructor() {
        this.telefone = new Telefone();
    }
}
