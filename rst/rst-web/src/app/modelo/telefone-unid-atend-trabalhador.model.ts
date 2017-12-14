import { Telefone } from 'app/modelo/telefone.model';

export class TelefoneUnidAtendTrabalhador {
    id: number;
    telefone: Telefone;

    constructor() {
        this.telefone = new Telefone();
    }
}
