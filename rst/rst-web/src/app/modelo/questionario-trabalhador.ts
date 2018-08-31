import { Trabalhador } from './trabalhador.model';
import { RespostaQuestionarioTabalhador } from './resposta-questionario-trabalhador.model';
import { Questionario } from 'app/modelo/questionario.model';
import { ClassificacaoPontuacao } from 'app/modelo/classificacao-pontuacao.model';

export class QuestionarioTrabalhador {
    id: number;
    dataQuestionarioTrabalhador: string;
    questionario = new Questionario();
    trabalhador = new Trabalhador();
    classificacaoPontuacao = new ClassificacaoPontuacao();
    listaRespostaQuestionarioTrabalhador = new Array<RespostaQuestionarioTabalhador>();
    quantidadePonto: number;
}
