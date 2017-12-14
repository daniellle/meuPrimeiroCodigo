import { Estado } from 'app/modelo/estado.model';
import { Regiao } from 'app/modelo/regiao.model';

export class Municipio {
id: number;
descricao: string;
estado: Estado;
regiao: Regiao;

constructor() {
    this.estado = new Estado();
    this.regiao = new Regiao();
}

}
