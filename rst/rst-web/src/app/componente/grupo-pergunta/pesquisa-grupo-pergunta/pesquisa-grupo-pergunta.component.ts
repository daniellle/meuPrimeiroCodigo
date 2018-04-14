import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { GrupoPerguntaService } from './../../../servico/grupo-pergunta.service';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { Router, ActivatedRoute } from '@angular/router';
import { GrupoPergunta } from './../../../modelo/grupo-pergunta.model';
import { GrupoPerguntaFilter } from './../../../modelo/filtro-grupo-pergunta';
import { Component, OnInit } from '@angular/core';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
  selector: 'app-pesquisa-grupo-pergunta',
  templateUrl: './pesquisa-grupo-pergunta.component.html',
  styleUrls: ['./pesquisa-grupo-pergunta.component.scss'],
})
export class PesquisaGrupoPerguntaComponent extends BaseComponent implements OnInit {

  filtro: GrupoPerguntaFilter;
  filtroSelecionado: GrupoPerguntaFilter;
  listaGrupoPergunta: GrupoPergunta[];
  grupoPerguntaSelecionado: GrupoPergunta;
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private grupoPerguntaService: GrupoPerguntaService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,

  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.filtro = new GrupoPerguntaFilter();
    this.filtroSelecionado = new GrupoPerguntaFilter();
    this.listaGrupoPergunta = new Array<GrupoPergunta>();
    this.title = this.activatedRoute.snapshot.data.title;
    this.grupoPerguntaSelecionado = new GrupoPergunta();
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

  pesquisar() {
    this.listaGrupoPergunta = new Array<GrupoPergunta>();
    this.filtroSelecionado = new GrupoPerguntaFilter(this.filtro);
    this.paginacao.pagina = 1;
    this.pesquisarProdutoServicoPaginadoService(this.filtroSelecionado, this.paginacao);
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarProdutoServicoPaginadoService(this.filtroSelecionado, this.paginacao);
  }

  private pesquisarProdutoServicoPaginadoService(filtro: GrupoPerguntaFilter, paginacao: Paginacao): void {
    this.grupoPerguntaService.pesquisarPaginado(filtro, paginacao).
      subscribe((retorno: ListaPaginada<GrupoPergunta>) => {
        if (retorno.quantidade !== 0) {
          this.listaGrupoPergunta = retorno.list;
          this.paginacao = this.getPaginacao(paginacao, retorno);
        } else {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  remover(item) {
    const grupoPergunta: GrupoPergunta = item;
    this.grupoPerguntaService.desativar(grupoPergunta).subscribe((response: GrupoPergunta) => {
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
