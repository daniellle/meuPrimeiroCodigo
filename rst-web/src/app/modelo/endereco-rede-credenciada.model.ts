import { Endereco } from './endereco.model';

export class EnderecoRedeCredenciada {

    id: number;
    endereco: Endereco;

    constructor() {
        this.endereco = new Endereco();
    }

}
