import { Especialidade } from './especialidade.model';
export class ProfissionalEspecialidade {

    id: Number;
    especialidade: Especialidade;
    dataExclusao: Date;

    constructor() {
            this.especialidade = new Especialidade();
        }

}
