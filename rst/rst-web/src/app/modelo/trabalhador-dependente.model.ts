import { SimNao } from 'app/modelo/enum/enum-simnao.model';
import { Dependente } from 'app/modelo/dependente.model';
import { Trabalhador } from 'app/modelo/trabalhador.model';

export class TrabalhadorDependente {
    id: number;
    dependente: Dependente;
    trabalhador: Trabalhador;
    tipoDependente: string;
    inativo: SimNao;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;

    constructor(init?: Partial<TrabalhadorDependente>) {
        this.trabalhador = new Trabalhador();
        if (init) {
            Object.assign(this, init);
            this.dependente = new Dependente(init.dependente);
        } else {
            this.dependente = new Dependente();
        }
    }
}
