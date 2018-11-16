import {UnidadeObra} from "./unidade-obra.model";
import {UnidadeAtendimentoTrabalhador} from "./unid-atend-trabalhador.model";
import {TipoPrograma} from "./tipo-programa.model";

export class  Contrato{
    id: number;
    dataContratoInicio: Date | string;
    dataContratoFim: Date | string;
    unidadeObra: UnidadeObra;
    anoVigencia: number;
    flagInativo: string;
    tipoPrograma: TipoPrograma;
    unidadeAtendimentoTrabalhador: UnidadeAtendimentoTrabalhador;

}
