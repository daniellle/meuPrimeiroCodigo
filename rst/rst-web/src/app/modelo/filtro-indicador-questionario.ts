export class IndicadorQuestionarioFilter {
    descricao: string;

    constructor(init?: Partial<IndicadorQuestionarioFilter>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
