import { GrupoPergunta } from './grupo-pergunta.model';
import { Questionario } from './questionario.model';

export class GrupoPerguntaQuestionario {
    id: number;
    questionario = new Questionario();
    grupoPergunta = new GrupoPergunta();
    ordenacao: number;
}
