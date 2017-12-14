import { Setor } from './setor.model';
import { Empresa } from 'app/modelo/empresa.model';

export class EmpresaSetor {

    id: number;
    setor: Setor;
    empresa: Empresa;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;
    constructor() {

    }
}
