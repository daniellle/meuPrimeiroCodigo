import { DepartamentoRegional } from './departamento-regional.model';
import { Empresa } from './empresa.model';
import {Usuario} from "./usuario.model";
export class FiltroUsuario {
    login: string;
    nome: string;
    listaIdPerfis: number[];
    codigoPerfil: '';
    empresa = new Empresa();
    departamentoRegional = new DepartamentoRegional();
}
