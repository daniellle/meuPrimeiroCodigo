import { Endereco } from 'app/modelo/endereco.model';

export class EnderecoTrabalhador {
    id: number;
    endereco: Endereco;

    constructor() {
        this.endereco = new Endereco();
    }
}
