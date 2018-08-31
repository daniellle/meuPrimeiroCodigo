import { Telefone } from './telefone.model';
export class TelefoneSindicato {

    id: Number;
    telefone: Telefone;

    constructor() {
        this.telefone = new Telefone();
    }

}
