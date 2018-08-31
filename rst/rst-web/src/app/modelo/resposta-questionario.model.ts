import { PerguntaQuestionario } from './pergunta-questionario.model';
import { Resposta } from './resposta.model';
export class RespostaQuestionario {
    id: number;
    perguntaQuestionario = new PerguntaQuestionario();
    resposta = new Resposta();
    ordemResposta: number;
    pontuacao: number;
}
