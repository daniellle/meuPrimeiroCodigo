export class GrupoPerguntaFilter {
    descricao: string;

    constructor(init?: Partial<GrupoPerguntaFilter>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
