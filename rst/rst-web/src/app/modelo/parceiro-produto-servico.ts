import { ProdutoServico } from './produto-servico.model';
import { Parceiro } from './parceiro.model';
export class ParceiroProdutoServico {
    public departamentoRegional: Parceiro;
    public produtoServico: ProdutoServico;
    public id: number;
    public dataCriacao: Date;
    public dataAlteracao: Date;
    public dataExclusao: Date;
}
