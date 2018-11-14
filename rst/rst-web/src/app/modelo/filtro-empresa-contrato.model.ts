import {UnidadeObra} from "./unidade-obra.model";
import {UnidadeAtendimentoTrabalhador} from "./unid-atend-trabalhador.model";

export class FiltroEmpresaContrato{
    id: number;
    idEmpresa: number;
    und_obra: UnidadeObra;
    dtInicio: Date;
    dtFim: Date;
    unidadeSESI: UnidadeAtendimentoTrabalhador;
    anoVigencia: number;

}
