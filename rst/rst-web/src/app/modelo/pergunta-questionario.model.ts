import { GrupoPergunta } from './grupo-pergunta.model';
import { Pergunta } from './pergunta.model';
import { IndicadorQuestionario } from './indicador-questionario.model';
import { Questionario } from './questionario.model';
import { RespostaQuestionario } from './resposta-questionario.model';

export class PerguntaQuestionario {
    id: number;
    questionario = new Questionario();
    pergunta = new Pergunta();
    tipoResposta: string;
    grupoPergunta = new GrupoPergunta();
    ordemPergunta: number;
    indicadorQuestionario = new IndicadorQuestionario();
    respostaQuestionarios = new Array<RespostaQuestionario>();
    ordemGrupo: number;

    constructor(init?: Partial<PerguntaQuestionario>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
