export class PerguntaFilter {
    descricao: string;
    id = '';

    constructor(init?: Partial<PerguntaFilter>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
