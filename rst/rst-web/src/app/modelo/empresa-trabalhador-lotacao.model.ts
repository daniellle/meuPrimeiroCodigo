import { EmpresaLotacao } from './empresa-lotacao.model';
import { EmpresaTrabalhador } from 'app/modelo/empresa-trabalhador.model';
export class  EmpresaTrabalhadorLotacao {

    id: number;
    empresaTrabalhador: EmpresaTrabalhador;
    empresaLotacao: EmpresaLotacao;
    dataAssociacao: string;
    dataDesligamento: string;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;

    constructor(init?: Partial<EmpresaTrabalhadorLotacao>) {
        this.empresaTrabalhador = new EmpresaTrabalhador();
        this.empresaLotacao = new EmpresaLotacao();

        if (init) {
            Object.assign(this, init);
        }
    }

}
