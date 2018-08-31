import { Endereco } from './endereco.model';
export class EnderecoSindicato {

    id: number;
    endereco: Endereco;

    constructor() {
            this.endereco = new Endereco();
        }

}
