import { Empresa } from "./empresa.model";
import { DepartamentoRegional } from "./departamento-regional.model";
import {Usuario} from "./usuario.model";

export class PerfilUsuarioFilter {
    login: string;
    nome: string;
    listaIdPerfis: number[];
    codigoPerfil: '';
    empresa = new Empresa();
    departamentoRegional = new DepartamentoRegional();
    idUnidadeSesi: string;
    usuarioLogadoHierarquia: number;
}
