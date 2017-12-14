import { Paginacao } from './../../modelo/paginacao.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ListaPaginada } from './../../modelo/lista-paginada.model';
import { MensagemProperties } from './../utilitario/recurso.pipe';
import { PerguntaService } from './../../servico/pergunta.service';
import { PerguntaFilter } from './../../modelo/filtro-pergunta';
import { Pergunta } from './../../modelo/pergunta.model';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { Component, Input, Output, EventEmitter, ViewChild } from '@angular/core';

@Component({
  selector: 'app-modal-selecionar-pergunta',
  templateUrl: './modal-selecionar-pergunta.component.html',
  styleUrls: ['./modal-selecionar-pergunta.component.scss'],
})
export class ModalSelecionarPerguntaComponent extends BaseComponent {

  paginacao = new Paginacao(1, 3, 5);

  @Input()
  model: Pergunta;

  @Output()
  onAdicionar = new EventEmitter<Pergunta>();

  @Output()
  onLimpar = new EventEmitter();

  @Input()
  modoConsulta: boolean;

  @Input()
  isPerguntaSelecionada: boolean;

  filtro = new PerguntaFilter();

  listaPergunta = new Array<Pergunta>();

  @ViewChild('perguntaModalComponent') perguntaModalComponent;

  constructor(
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private perguntaService: PerguntaService,
    private modalService: NgbModal,
  ) {
    super(bloqueioService, dialogo);
  }

  limpar() {
    this.onLimpar.emit();
  }

  pesquisar() {
    if (!this.filtro.descricao) {
      this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
      return;
    }
    this.paginacao.pagina = 1;
    this.perguntaService.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Pergunta>) => {
      this.listaPergunta = retorno.list;
      this.paginacao = this.getPaginacao(this.paginacao, retorno);
      if (retorno.quantidade === 0) {
        this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  selecionar(item: Pergunta) {
    this.model = item;
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.perguntaService.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Pergunta>) => {
      this.listaPergunta = retorno.list;
    });
  }

  openModal() {
    const modalRef = this.modalService.open(this.perguntaModalComponent, { size: 'lg' });
    modalRef.result.then((result) => {
      this.onAdicionar.emit(this.model);
      this.limparModalPergunta();
    }, (reason) => {
    });
  }

  limparModalPergunta() {
    this.listaPergunta = null;
    this.model = new Pergunta();
  }

}
