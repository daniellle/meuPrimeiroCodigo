import {UnidadeObra} from "./unidade-obra.model";
import {UnidadeAtendimentoTrabalhador} from "./unid-atend-trabalhador.model";
import {TipoPrograma} from "./tipo-programa.model";

export class  Contrato{
    id: number;
    dataContratoInicio: Date;
    dataContratoFim: Date;
    unidadeObra: UnidadeObra;
    anoVigencia: number;
    fl_inativo: string;
    tipoPrograma: TipoPrograma;
    unidadeAtendimentoTrabalhador: UnidadeAtendimentoTrabalhador;

}
