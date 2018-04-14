import { UnidadeObra } from 'app/modelo/unidade-obra.model';
import { EmpresaCbo } from './empresa-cbo.model';
import { EmpresaJornada } from './empresaJornada.model';
import { EmpresaFuncao } from './empresa-funcao.model';
import { EmpresaSetor } from 'app/modelo/empresa-setor.model';

export class EmpresaLotacao {
    id: number;
    empresaSetor: EmpresaSetor;
    empresaCbo: EmpresaCbo;
    empresaFuncao: EmpresaFuncao;
    empresaJornada: EmpresaJornada;
    unidadeObra: UnidadeObra;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;

    constructor() {
        this.empresaSetor = new EmpresaSetor();
        this.empresaCbo = new EmpresaCbo();
        this.empresaFuncao = new EmpresaFuncao();
        this.empresaJornada = new EmpresaJornada();
        this.unidadeObra = new UnidadeObra();
    }
}
