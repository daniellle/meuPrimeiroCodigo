import { TipoQuestionario } from './tipo-questionario.model';
import { Periodicidade } from './enum/periodicidade.model';

export class Questionario {
    id: number;
    nome: string;
    descricao: string;
    periodicidade = new Periodicidade();
    versao: number;
    status: string;
    tipoQuestionario = new TipoQuestionario();
}
