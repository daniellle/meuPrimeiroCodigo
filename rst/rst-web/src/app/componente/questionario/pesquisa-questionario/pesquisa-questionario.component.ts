import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { TipoQuestionarioService } from './../../../servico/tipo-questionario.service';
import { TipoQuestionario } from './../../../modelo/tipo-questionario.model';
import { StatusQuestionario } from './../../../modelo/enum/enum-status-questionario.model';
import { Component, OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { ListaPaginada } from '../../../modelo/lista-paginada.model';
import { QuestionarioFilter } from '../../../modelo/filtro-questionario';
import { Questionario } from '../../../modelo/questionario.model';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { QuestionarioService } from '../../../servico/questionario.service';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { environment } from './../../../../environments/environment';

@Component({
  selector: 'app-pesquisa-questionario',
  templateUrl: './pesquisa-questionario.component.html',
  styleUrls: ['./pesquisa-questionario.component.scss'],
})
export class PesquisaQuestionarioComponent extends BaseComponent implements OnInit {

  public filtro: QuestionarioFilter;
  public questionarios: Questionario[];
  public keysStatus: string[];
  public status = StatusQuestionario;
  public versoes: string[];
  listaTipoQuestionario = new Array<TipoQuestionario>();
  constructor(
    private router: Router, private fb: FormBuilder,
    protected bloqueioService: BloqueioService, private service: QuestionarioService,
    protected dialogo: ToastyService, private dialogService: DialogService,
    private activatedRoute: ActivatedRoute,
    private tipoQuestionarioService: TipoQuestionarioService) {
    super(bloqueioService, dialogo);
    this.keysStatus = Object.keys(this.status);
  }

  ngOnInit() {
    this.filtro = new QuestionarioFilter();
    this.questionarios = new Array<Questionario>();
    this.modoConsulta = !Seguranca.isPermitido([PermissoesEnum.QUESTIONARIO,
    PermissoesEnum.QUESTIONARIO_CADASTRAR,
    PermissoesEnum.QUESTIONARIO_ALTERAR,
    PermissoesEnum.QUESTIONARIO_DESATIVAR]);
    this.carregarVersoes();
    this.carregarTiposQuestionario();
  }
  private carregarVersoes() {
    this.service.pesquisarVersoes().subscribe((retorno) => { this.versoes = retorno; }, (error) => {
      this.mensagemError(error);
    });
  }
  pesquisar() {
    if (this.validarCampos()) {
      this.paginacao.pagina = 1;
      this.service.pesquisarPaginado(this.filtro, this.paginacao)
        .subscribe((retorno: ListaPaginada<Questionario>) => {
          this.questionarios = retorno.list;
          this.paginacao = this.getPaginacao(this.paginacao, retorno);
          if (retorno.quantidade === 0) {
            this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
          }
        }, (error) => {
          this.mensagemError(error);
        });
    }
  }

  validarCampos(): Boolean {
    let verificador: Boolean = true;

    if (this.isVazia(this.filtro.nome) && this.isVazia(this.filtro.versao)
      && this.isVazia(this.filtro.tipo) && this.isVazia(this.filtro.situacao)) {
      this.mensagemError(MensagemProperties.app_rst_msg_pesquisar_todos_vazios);
      verificador = false;

    }
    return verificador;
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.service.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Questionario>) => {
      this.questionarios = retorno.list;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  incluir() {
    this.router.navigate([`${environment.path_raiz_cadastro}/questionario/cadastrar`]);
  }

  voltar() {
    this.router.navigate([`${environment.path_raiz_cadastro}/questionario`]);
  }

  selecionar(model: any) {
    if (model && model.id) {
      this.router.navigate([`${environment.path_raiz_cadastro}/questionario/${model.id}`]);
    }
  }
  private carregarTiposQuestionario(): void {
    this.tipoQuestionarioService.buscarTodas().subscribe((response) => {
      this.listaTipoQuestionario = response;
    }, ((error) => {
      this.mensagemError(error);
    }));
  }

  remover(item) {
    const questionario: Questionario = item;
    this.service.remover(questionario).subscribe((retorno) => {
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.service.pesquisarPaginado(this.filtro, this.paginacao)
        .subscribe((response: ListaPaginada<Questionario>) => {
          this.questionarios = response.list;
          this.paginacao = this.getPaginacao(this.paginacao, response);
        }, (error) => {
          this.mensagemError(error);
        });
    }, (error) => {
      this.mensagemError(error);
    });
  }

  exibir(item: Questionario) {
    return item.status === 'E' && Seguranca.getUsuario().papeis.indexOf('GDNA') > -1;
  }

  hasPermissaoCadastro() {
    return Seguranca.isPermitido([PermissoesEnum.QUESTIONARIO,
    PermissoesEnum.QUESTIONARIO_CADASTRAR]);
  }
}
