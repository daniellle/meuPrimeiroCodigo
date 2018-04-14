import { Funcao } from './funcao.model';
import { Empresa } from 'app/modelo/empresa.model';
export class EmpresaFuncao {

    id: number;
    funcao: Funcao;
    empresa: Empresa;
    dataAlteracao: Date;
    dataExclusao: Date;
    constructor() {

    }
}
