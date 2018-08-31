import { FiltroLinha } from './../../../modelo/filtro-linha.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { LinhaService } from './../../../servico/linha.service';
import { Linha } from './../../../modelo/linha.model';
import { ProdutoServicoService } from './../../../servico/produto-servico.service';
import { ProdutoServico } from './../../../modelo/produto-servico.model';
import { ProdutoServicoFilter } from './../../../modelo/filtro-produto-servico';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastyService } from 'ng2-toasty';
import { BaseComponent } from 'app/componente/base.component';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { Paginacao } from 'app/modelo/paginacao.model';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';

@Component({
  selector: 'app-pesquisa-produto-servico',
  templateUrl: './pesquisa-produto-servico.component.html',
  styleUrls: ['./pesquisa-produto-servico.component.scss'],
})
export class PesquisaProdutoServicoComponent extends BaseComponent implements OnInit {

  filtro: ProdutoServicoFilter;
  filtroSelecionado: ProdutoServicoFilter;
  listaProdutosServicos: ProdutoServico[];
  listaLinha: Linha[];
  produtoSelecionado: ProdutoServico;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private produtoServicoService: ProdutoServicoService,
    private linhaService: LinhaService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.filtro = new ProdutoServicoFilter();
    this.filtroSelecionado = new ProdutoServicoFilter();
    this.listaProdutosServicos = new Array<ProdutoServico>();
    this.title = this.activatedRoute.snapshot.data.title;
    this.produtoSelecionado = new ProdutoServico();
    this.listaLinha = new Array<Linha>();
    this.carregarListaLinha();
  }

  hasPermissaoCadastro() {
    return Seguranca.isPermitido(
      [PermissoesEnum.PRODUTO_SERVICO,
      PermissoesEnum.PRODUTO_SERVICO_CADASTRAR]);
  }

  selecionar(model: any) {
    if (model && model.id) {
      this.router.navigate([model.id], { relativeTo: this.activatedRoute },
      );
    }
  }

  incluir() {
    this.router.navigate(['cadastrar'], { relativeTo: this.activatedRoute });
  }

  pesquisar(): void {
    this.listaProdutosServicos = new Array<ProdutoServico>();
    this.filtroSelecionado = new ProdutoServicoFilter(this.filtro);
    this.paginacao.pagina = 1;
    this.pesquisarProdutoServicoPaginadoService(this.filtroSelecionado, this.paginacao);
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarProdutoServicoPaginadoService(this.filtroSelecionado, this.paginacao);
  }

  private pesquisarProdutoServicoPaginadoService(filtro: ProdutoServicoFilter, paginacao: Paginacao): void {
    this.filtro.aplicarDadosFilter = true;
    this.produtoServicoService.pesquisarPaginado(filtro, paginacao).
      subscribe((retorno: ListaPaginada<ProdutoServico>) => {
        if (retorno.quantidade !== 0) {
          this.listaProdutosServicos = retorno.list;
          this.paginacao = this.getPaginacao(paginacao, retorno);
        } else {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  private carregarListaLinha(): void {
    this.listaLinha = new Array<Linha>();
    this.linhaService.buscarTodas(new FiltroLinha()).subscribe((listaLinha: Linha[]) => {
      this.listaLinha = listaLinha;
    }, (error) => {
      this.mensagemError(error);
    });
  }
}
