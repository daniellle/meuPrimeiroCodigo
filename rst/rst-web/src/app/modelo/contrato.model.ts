import {UnidadeObra} from "./unidade-obra.model";
import {UnidadeAtendimentoTrabalhador} from "./unid-atend-trabalhador.model";

export class  Contrato{
    id: number;
    dataInicio: Date;
    dataFim: Date;
    unidadeObra: UnidadeObra;
    anoVigencia: number;
    fl_inativo: string;
    programa: string;
    unidade_sesi: UnidadeAtendimentoTrabalhador;

}
