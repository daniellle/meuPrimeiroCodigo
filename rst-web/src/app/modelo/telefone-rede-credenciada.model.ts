import { Telefone } from 'app/modelo/telefone.model';

export class TelefoneRedeCredenciada {
    id: number;
    telefone: Telefone;

    constructor() {
        this.telefone = new Telefone();
    }
}
