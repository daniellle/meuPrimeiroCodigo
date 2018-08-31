import { RespostaDTO } from './resposta-dto.model';
export class PerguntaDTO {
    id: number;
    descricao: string;
    ordenacao: number;
    numeracao: string;
    tipoResposta: string;
    idPerguntaQuestionario: number;
    listaRespostaQuestionario: RespostaDTO[];
}
