import { Empresa } from 'app/modelo/empresa.model';

export class EmpresaContrato {

    id: number;
    empresa: Empresa;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;
    constructor() {

    }
}
