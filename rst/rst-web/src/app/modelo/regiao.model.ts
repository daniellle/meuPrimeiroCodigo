import { Pais } from 'app/modelo/pais.model';

export class Regiao {
    id: number;
    descricao: string;
    pais: Pais;

    constructor() {
        this.pais = new Pais();
    }
}
