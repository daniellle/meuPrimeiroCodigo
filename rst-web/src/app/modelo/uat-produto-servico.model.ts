import { Uat } from './uat.model';
import { ProdutoServico } from './produto-servico.model';

export class UatProdutoServico {
    public uat: Uat;
    public produtoServico: ProdutoServico;
    public id: number;
    public dataCriacao: Date;
    public dataAlteracao: Date;
    public dataExclusao: Date;
}
