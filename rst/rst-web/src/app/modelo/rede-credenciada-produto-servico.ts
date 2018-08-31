import { ProdutoServico } from './produto-servico.model';
import { RedeCredenciada } from './rede-credenciada.model';
export class RedeCredenciadaProdutoServico {
    public departamentoRegional: RedeCredenciada;
    public produtoServico: ProdutoServico;
    public id: number;
    public dataCriacao: Date;
    public dataAlteracao: Date;
    public dataExclusao: Date;
}
