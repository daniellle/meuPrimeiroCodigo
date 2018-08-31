import { Sindicato } from './sindicato.model';
import { Empresa } from './empresa.model';

export class EmpresaSindicato {
    id: number;
    empresa: Empresa;
    sindicato: Sindicato;
    dataAssociacao: string;
    dataDesligamento: string;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;

    constructor(init?: Partial<EmpresaSindicato>) {
        this.empresa = new Empresa();
        this.sindicato = new Sindicato();

        if (init) {
            Object.assign(this, init);
        }
    }
}
