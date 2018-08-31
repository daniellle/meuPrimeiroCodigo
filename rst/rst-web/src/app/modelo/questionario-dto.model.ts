import { GrupoPerguntaDTO } from './grupo-pergunta-dto.model';
export class QuestionarioDTO {
    id: number;
    nome: string;
    descricao: string;
    listaGrupoPerguntaQuestionario: GrupoPerguntaDTO[];
}
