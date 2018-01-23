import { RespostaQuestionario } from './../../modelo/resposta-questionario.model';
import { Paginacao } from './../../modelo/paginacao.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ListaPaginada } from './../../modelo/lista-paginada.model';
import { MensagemProperties } from './../utilitario/recurso.pipe';
import { RespostaService } from './../../servico/resposta.service';
import { RespostaFilter } from './../../modelo/filtro-resposta';
import { Resposta } from './../../modelo/resposta.model';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { Component, Input, Output, EventEmitter, ViewChild } from '@angular/core';

@Component({
  selector: 'app-modal-selecionar-resposta',
  templateUrl: './modal-selecionar-resposta.component.html',
})
export class ModalSelecionarRespostaComponent extends BaseComponent {

  paginacao = new Paginacao(1, 3, 5);

  @Input()
  model = new RespostaQuestionario();

  @Output()
  onAdicionar = new EventEmitter<RespostaQuestionario>();
  
  @Input()
  modoConsulta: boolean;

  @Input()
  isRespostasQuestionario: boolean;

  filtro = new RespostaFilter();

  listaResposta = new Array<Resposta>();

  @ViewChild('respostaModalComponent') respostaModalComponent;

  public modalRef;

  constructor(
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private respostaService: RespostaService,
    private modalService: NgbModal,
  ) {
    super(bloqueioService, dialogo);
  }

  adicionarResposta() {
    this.modalRef = this.modalService.open(this.respostaModalComponent, { size: 'lg' });
    this.modalRef.result.then((result) => {
      if (this.validarCampos()) {
        this.onAdicionar.emit(this.model);
      }
      this.limparModalResposta();
    }, (reason) => {
    });
  }

  private validarCampos(): Boolean {
    let isValido = true;
    if (!this.model.pontuacao) {
      this.mensagemErroComParametrosModel('app_rst_resposta_questionario_campo_obrigatorio', MensagemProperties.app_rst_labels_pontuacao);
      isValido = false;
    }
    if (!this.model.ordemResposta) {
      this.mensagemErroComParametrosModel('app_rst_resposta_questionario_campo_obrigatorio',
        MensagemProperties.app_rst_labels_ordem_resposta);
      isValido = false;
    }

    if (this.model.ordemResposta && !Number(this.model.ordemResposta)) {
      this.mensagemErroComParametrosModel('app_rst_campo_inteiro',
        MensagemProperties.app_rst_labels_ordem_resposta);
      isValido = false;
    }

    if (this.model.pontuacao && !(Number(this.model.pontuacao) === 0 || Number(this.model.pontuacao) === 1)) {
      this.mensagemError(MensagemProperties.app_rst_resposta_questionario_pontuacao_invalida);
      isValido = false;
    }

    return isValido;
  }

  limparModalResposta() {
    this.listaResposta = null;
    this.model = new RespostaQuestionario();
  }

  pesquisar() {
    if (!this.filtro.descricao) {
      this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
      return;
    }
    this.paginacao.pagina = 1;
    this.respostaService.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Resposta>) => {
      this.listaResposta = retorno.list;
      this.paginacao = this.getPaginacao(this.paginacao, retorno);
      if (retorno.quantidade === 0) {
        this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  selecionar(item: Resposta) {
    this.model.resposta = item;
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.respostaService.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Resposta>) => {
      this.listaResposta = retorno.list;
    }, (error) => {
      this.mensagemError(error);
    });
  }
}
