import { Endereco } from 'app/modelo/endereco.model';

export class EnderecoProfissional {
    id: number;
    endereco: Endereco;

    constructor() {
        this.endereco = new Endereco();
    }
}
