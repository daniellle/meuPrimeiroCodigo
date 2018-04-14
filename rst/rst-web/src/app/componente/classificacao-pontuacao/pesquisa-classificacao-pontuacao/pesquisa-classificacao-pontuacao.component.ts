import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { ClassificacaoPontuacaoService } from './../../../servico/classificacao-pontuacao.service';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { Router, ActivatedRoute } from '@angular/router';
import { ClassificacaoPontuacao } from './../../../modelo/classificacao-pontuacao.model';
import { ClassificacaoPontuacaoFilter } from './../../../modelo/filtro-classificacao-pontuacao';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit } from '@angular/core';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
  selector: 'app-pesquisa-classificacao-pontuacao',
  templateUrl: './pesquisa-classificacao-pontuacao.component.html',
  styleUrls: ['./pesquisa-classificacao-pontuacao.component.scss']
})
export class PesquisaClassificacaoPontuacaoComponent extends BaseComponent implements OnInit {

  filtro: ClassificacaoPontuacaoFilter;
  filtroSelecionado: ClassificacaoPontuacaoFilter;
  listaClassificacaoPontuacao: ClassificacaoPontuacao[];
  classificacaoPontuacaoSelecionado: ClassificacaoPontuacao;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private classificacaoPontuacaoService: ClassificacaoPontuacaoService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.filtro = new ClassificacaoPontuacaoFilter();
    this.filtroSelecionado = new ClassificacaoPontuacaoFilter();
    this.listaClassificacaoPontuacao = new Array<ClassificacaoPontuacao>();
    this.title = this.activatedRoute.snapshot.data.title;
    this.classificacaoPontuacaoSelecionado = new ClassificacaoPontuacao();
    this.pesquisar();
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

  voltar() {
    this.router.navigate([`${environment.path_raiz_cadastro}/questionario`]);
  }

  pesquisar(): void {
    this.listaClassificacaoPontuacao = new Array<ClassificacaoPontuacao>();
    this.filtroSelecionado = new ClassificacaoPontuacaoFilter(this.filtro);
    this.paginacao.pagina = 1;
    this.pesquisarProdutoServicoPaginadoService(this.filtroSelecionado, this.paginacao);
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarProdutoServicoPaginadoService(this.filtroSelecionado, this.paginacao);
  }

  private pesquisarProdutoServicoPaginadoService(filtro: ClassificacaoPontuacaoFilter, paginacao: Paginacao): void {
    this.classificacaoPontuacaoService.pesquisarPaginado(filtro, paginacao).
      subscribe((retorno: ListaPaginada<ClassificacaoPontuacao>) => {
        if (retorno.quantidade !== 0) {
          this.listaClassificacaoPontuacao = retorno.list;
          this.paginacao = this.getPaginacao(paginacao, retorno);
        } else {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  remover(item) {
    const classificacao: ClassificacaoPontuacao = item;
    this.classificacaoPontuacaoService.desativar(classificacao).subscribe((response: ClassificacaoPontuacao) => {
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.paginacao.pagina = 1;
      this.pesquisarProdutoServicoPaginadoService(this.filtro, this.paginacao);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  hasPermissaoCadastro() {
    return Seguranca.isPermitido([PermissoesEnum.GRUPO_PERGUNTA,
    PermissoesEnum.GRUPO_PERGUNTA_CADASTRAR]);
  }
}
