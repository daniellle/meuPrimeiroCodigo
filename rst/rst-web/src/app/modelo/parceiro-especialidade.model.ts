import { Especialidade } from './especialidade.model';
export class ParceiroEspecialidade {

    id: Number;
    especialidade: Especialidade;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;

    constructor() {
       this.especialidade = new Especialidade();
    }

}
