import { UsuarioPerfilSistema } from ".";
import { DepartamentoRegional } from "./departamento-regional.model";
import { UnidadeAtendimentoTrabalhador } from "./unid-atend-trabalhador.model";
import { Empresa } from "./empresa.model";

export class UsuarioRelatorio {
    nome: string;
    login: string;
    perfisSistema: UsuarioPerfilSistema[];
    departamentoRegional: DepartamentoRegional;
    unidadeSesi: UnidadeAtendimentoTrabalhador;
    empresa: Empresa;
}