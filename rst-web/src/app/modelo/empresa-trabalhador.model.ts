import { Trabalhador } from 'app/modelo/trabalhador.model';
import { Empresa } from 'app/modelo/empresa.model';

export class EmpresaTrabalhador {
    id: number;
    empresa: Empresa;
    trabalhador: Trabalhador;
    dataAdmissao: any;
    dataDemissao: any;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;

    constructor(init?: Partial<EmpresaTrabalhador>) {
        this.empresa = new Empresa();
        this.trabalhador = new Trabalhador();

        if (init) {
            Object.assign(this, init);
        }
    }
}
