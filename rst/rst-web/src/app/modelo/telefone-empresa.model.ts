import { Telefone } from 'app/modelo/telefone.model';

export class TelefoneEmpresa {
    id: number;
    telefone: Telefone;

    constructor() {
        this.telefone = new Telefone();
    }
}
