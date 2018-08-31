import { Linha } from './linha.model';

export class ProdutoServico {

        id: number;
        nome: string;
        descricao: string;
        dataCriacao: Date;
        dataAlteracao: Date;
        dataExclusao: Date;
        linha: Linha;

        constructor() {
            this.linha = new Linha();
        }
}
