import { ProdutoServico } from './produto-servico.model';
import { DepartamentoRegional } from './departamento-regional.model';

export class DepartamentoRegionalProdutoServico {
    public departamentoRegional: DepartamentoRegional;
    public produtoServico: ProdutoServico;
    public id: number;
    public dataCriacao: Date;
    public dataAlteracao: Date;
    public dataExclusao: Date;
}
