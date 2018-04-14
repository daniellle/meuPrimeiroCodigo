import { Empresa } from 'app/modelo/empresa.model';
import { Jornada } from './jornada.model';
export class EmpresaJornada {

    id: number;
    jornada: Jornada;
    empresa: Empresa;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;

    constructor() {
        this.jornada = new Jornada();
        this.empresa = new Empresa();
     }

}
