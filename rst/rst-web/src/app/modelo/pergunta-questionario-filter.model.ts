export class PerguntaQuestionarioFilter {
    id: number;

    constructor(init?: Partial<PerguntaQuestionarioFilter>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
