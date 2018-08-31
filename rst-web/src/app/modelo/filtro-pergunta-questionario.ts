export class PerguntaQuestionarioFilter {

    idQuestionario: number;

    constructor(init?: Partial<PerguntaQuestionarioFilter>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
