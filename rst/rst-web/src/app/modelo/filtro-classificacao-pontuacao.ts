export class ClassificacaoPontuacaoFilter {
    descricao: string;

    constructor(init?: Partial<ClassificacaoPontuacaoFilter>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
