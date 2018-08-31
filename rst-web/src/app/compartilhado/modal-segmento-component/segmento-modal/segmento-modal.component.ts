import { Paginacao } from 'app/modelo/paginacao.model';
import { Segmento } from 'app/modelo/segmento.model';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { BaseComponent } from 'app/componente/base.component';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { FiltroSegmento } from './../../../modelo/filtro-segmento';
import { SegmentoService } from './../../../servico/segmento.service';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Component, OnInit, Input, Output, ViewChild } from '@angular/core';

@Component({
  selector: 'app-segmento-modal',
  templateUrl: './segmento-modal.component.html',
  styleUrls: ['./segmento-modal.component.scss'],
})
export class SegmentoModalComponent extends BaseComponent implements OnInit {

  codigo: string;
  descricao: string;
  filtro: FiltroSegmento;
  segmentos: Segmento[];
  modalRef;
  paginacao = new Paginacao(1, 3, 5);

  @Input() @Output()
  model: Segmento;

  @Input() @Output()
  segmento: Segmento;

  @Input()
  adicionar: any;

  @Input()
  modoConsulta: boolean;

  @ViewChild('segmentoModalComponent') segmentoModalComponent;

  constructor(public activeModal: NgbActiveModal,
    private service: SegmentoService,
    protected bloqueioService: BloqueioService,
    private modalService: NgbModal,
    protected dialogo: ToastyService) {
    super(bloqueioService, dialogo);
    this.model = new Segmento();
    this.segmentos = Array<Segmento>();
    this.filtro = new FiltroSegmento();
  }

  ngOnInit() {
    this.segmentos = Array<Segmento>();
    this.filtro = new FiltroSegmento();
  }

  pesquisar() {
    if (this.isVazia(this.filtro.codigo) && this.isVazia(this.filtro.descricao)) {
      this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
      return;
    }
    this.paginacao.pagina = 1;
    this.service.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Segmento>) => {
      this.segmentos = retorno.list;
      this.paginacao = this.getPaginacao(this.paginacao, retorno);
      if (retorno.quantidade === 0) {
        this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  selecionar(item: Segmento) {
    this.model = item;
  }

  editar(item: Segmento) {
    this.model = item;
    this.modalRef = this.modalService.open(this.segmentoModalComponent, { size: 'lg' });
    this.salvar();
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.service.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Segmento>) => {
      this.segmentos = retorno.list;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  adicionarSegmento() {
    this.modalRef = this.modalService.open(this.segmentoModalComponent, { size: 'lg' });
    this.salvar();
  }

  salvar() {
    this.modalRef.result.then((result) => {
      this.segmento.id = this.model.id;
      this.segmento.codigo = this.model.codigo;
      this.segmento.descricao = this.model.descricao;
      this.segmento.dataCriacao = this.model.dataCriacao;
      this.segmento.dataAlteracao = this.model.dataAlteracao;
      this.segmento.dataExclusao = this.model.dataExclusao;
    }, (reason) => {

    });
  }

  existeSegmento() {
    return this.segmento && this.segmento.id !== undefined;
  }
}
