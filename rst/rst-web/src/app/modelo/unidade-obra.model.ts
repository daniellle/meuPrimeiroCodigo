import { Empresa } from 'app/modelo/empresa.model';
export class UnidadeObra {

    id: number;
    descricao: String;
    cei: String;
    empresa: Empresa;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;

    constructor(init?: Partial<UnidadeObra>) {
        if (init) {
            Object.assign(this, init);
        }
    }

}
