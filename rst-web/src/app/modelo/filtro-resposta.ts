export class RespostaFilter {
    descricao: string;

    constructor(init?: Partial<RespostaFilter>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
