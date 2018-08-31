export class ProdutoServicoFilter {
    codigo: string;
    nome: string;
    idLinha = '';
    descricao: string;
    aplicarDadosFilter = true;

    constructor(init?: Partial<ProdutoServicoFilter>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
