import { Paginacao } from './../../modelo/paginacao.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ListaPaginada } from './../../modelo/lista-paginada.model';
import { MensagemProperties } from './../utilitario/recurso.pipe';
import { IndicadorQuestionarioService } from './../../servico/indicador-questionario.service';
import { IndicadorQuestionarioFilter } from './../../modelo/filtro-indicador-questionario';
import { IndicadorQuestionario } from './../../modelo/indicador-questionario.model';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { Component, Input, Output, EventEmitter, ViewChild } from '@angular/core';

@Component({
  selector: 'app-modal-selecionar-indicador',
  templateUrl: './modal-selecionar-indicador.component.html',
  styleUrls: ['./modal-selecionar-indicador.component.scss'],
})
export class ModalSelecionarIndicadorQuestionarioComponent extends BaseComponent {

  paginacao = new Paginacao(1, 3, 5);

  @Input()
  model: IndicadorQuestionario;

  @Output()
  onAdicionar = new EventEmitter<IndicadorQuestionario>();

  @Output()
  onLimpar = new EventEmitter();

  @Input()
  modoConsulta: boolean;

  @Input()
  isIndicadorSelecionado: boolean;

  filtro = new IndicadorQuestionarioFilter();

  listaIndicadorQuestionario = new Array<IndicadorQuestionario>();

  @ViewChild('indicadorModalComponent') indicadorModalComponent;

  constructor(
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private indicadorService: IndicadorQuestionarioService,
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
    this.indicadorService.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<IndicadorQuestionario>) => {
      this.listaIndicadorQuestionario = retorno.list;
      this.paginacao = this.getPaginacao(this.paginacao, retorno);
      if (retorno.quantidade === 0) {
        this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  selecionar(item: IndicadorQuestionario) {
    this.model = item;
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.indicadorService.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<IndicadorQuestionario>) => {
      this.listaIndicadorQuestionario = retorno.list;
    });
  }

  openModal() {
    const modalRef = this.modalService.open(this.indicadorModalComponent, { size: 'lg' });
    modalRef.result.then((result) => {
      this.onAdicionar.emit(this.model);
      this.limparModalIndicador();
    }, (reason) => {
    });
  }

  limparModalIndicador() {
    this.listaIndicadorQuestionario = null;
    this.model = new IndicadorQuestionario();
  }
}
