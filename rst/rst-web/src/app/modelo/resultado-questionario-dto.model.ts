import { ClassificacaoDTO } from './classificacao-dto.model';
import { IndicadorDTO } from './indicador-dto.model';
export class ResultadoQuestionarioDTO {

    tituloQuestionario: string;
    descricaoQuestionario: string;
    classificacao = new ClassificacaoDTO();
    listaIndicadores = new  Array<IndicadorDTO>();
}
