import { Endereco } from 'app/modelo/endereco.model';

export class EnderecoUnidAtendTrabalhador {
    id: number;
    endereco: Endereco;

    constructor () {
        this.endereco = new Endereco();
    }
}
