import { Cnae } from './cnae.model';
import { Empresa } from 'app/modelo/empresa.model';
export class EmpresaCnae {
    public principal: boolean;
    public empresa: Empresa;
    public cnae: Cnae;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;
}
