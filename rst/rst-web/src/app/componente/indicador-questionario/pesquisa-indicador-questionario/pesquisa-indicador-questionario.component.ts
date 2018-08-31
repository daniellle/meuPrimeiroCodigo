import { environment } from './../../../../environments/environment';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { IndicadorQuestionarioService } from './../../../servico/indicador-questionario.service';
import { Paginacao } from 'app/modelo/paginacao.model';
import { IndicadorQuestionarioFilter } from './../../../modelo/filtro-indicador-questionario';
import { IndicadorQuestionario } from './../../../modelo/indicador-questionario.model';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { Router, ActivatedRoute } from '@angular/router';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit } from '@angular/core';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
  selector: 'app-pesquisa-indicador-questionario',
  templateUrl: './pesquisa-indicador-questionario.component.html',
  styleUrls: ['./pesquisa-indicador-questionario.component.scss'],
})
export class PesquisaIndicadorQuestionarioComponent extends BaseComponent implements OnInit {

  filtro: IndicadorQuestionarioFilter;
  filtroSelecionado: IndicadorQuestionarioFilter;
  listaIndicadorQuestionario: IndicadorQuestionario[];
  indicadorQuestionarioSelecionado: IndicadorQuestionario;
  model: any;
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private indicadorQuestionarioService: IndicadorQuestionarioService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.filtro = new IndicadorQuestionarioFilter();
    this.filtroSelecionado = new IndicadorQuestionarioFilter();
    this.listaIndicadorQuestionario = new Array<IndicadorQuestionario>();
    this.title = this.activatedRoute.snapshot.data.title;
    this.indicadorQuestionarioSelecionado = new IndicadorQuestionario();
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
    this.listaIndicadorQuestionario = new Array<IndicadorQuestionario>();
    this.filtroSelecionado = new IndicadorQuestionarioFilter(this.filtro);
    this.paginacao.pagina = 1;
    this.pesquisarProdutoServicoPaginadoService(this.filtroSelecionado, this.paginacao);
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarProdutoServicoPaginadoService(this.filtroSelecionado, this.paginacao);
  }

  excluir(item: IndicadorQuestionario): void {
    this.indicadorQuestionarioService.desativar(item).subscribe((response: IndicadorQuestionario) => {
      this.model = response;
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.pesquisar();
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private pesquisarProdutoServicoPaginadoService(filtro: IndicadorQuestionarioFilter, paginacao: Paginacao): void {
    this.indicadorQuestionarioService.pesquisarPaginado(filtro, paginacao).
      subscribe((retorno: ListaPaginada<IndicadorQuestionario>) => {
        if (retorno.quantidade !== 0) {
          this.listaIndicadorQuestionario = retorno.list;
          this.paginacao = this.getPaginacao(paginacao, retorno);
        } else {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  hasPermissaoCadastro() {
    return Seguranca.isPermitido([PermissoesEnum.INDICADOR_QUESTIONARIO,
      PermissoesEnum.INDICADOR_QUESTIONARIO_CADASTRAR]);
  }

}
