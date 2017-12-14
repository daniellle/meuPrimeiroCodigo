import { Empresa } from './empresa.model';
import { Cbo } from './cbo.model';
export class EmpresaCbo {

    id: number;
    cbo: Cbo;
    empresa: Empresa;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;

    constructor() {
        this.cbo = new Cbo();
        this.empresa = new Empresa();
    }

}
