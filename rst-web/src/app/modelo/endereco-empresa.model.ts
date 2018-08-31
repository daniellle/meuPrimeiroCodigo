import { Endereco } from 'app/modelo/endereco.model';

export class EnderecoEmpresa {
    id: number;
    endereco: Endereco;
    constructor() {
        this.endereco = new Endereco();
    }
}
