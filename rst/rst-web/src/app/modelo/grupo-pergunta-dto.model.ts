import { PerguntaDTO } from './pergunta-dto.model';
export class GrupoPerguntaDTO {
    id: number;
    descricao: string;
    ordenacao: number;
    listaPerguntaQuestionarioDTO: PerguntaDTO[];
}
