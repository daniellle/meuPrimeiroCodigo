import {UnidadeObra} from "./unidade-obra.model";
import {UnidadeAtendimentoTrabalhador} from "./unid-atend-trabalhador.model";
import {TipoPrograma} from "./tipo-programa.model";
import {DepartamentoRegional} from "./departamento-regional.model";
import {Uat} from "./uat.model";

export class  Contrato{
    id: number;
    dataContratoInicio: Date | string | any;
    dataContratoFim: Date | string | any;
    unidadeObra: UnidadeObra;
    anoVigencia: number;
    flagInativo: string;
    dataInativo: string;
    tipoPrograma: TipoPrograma;
    unidadeAtendimentoTrabalhador: UnidadeAtendimentoTrabalhador | Uat;
    dr: DepartamentoRegional;

}
