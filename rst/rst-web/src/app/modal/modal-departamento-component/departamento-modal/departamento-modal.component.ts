import { DepartRegionalService } from 'app/servico/depart-regional.service';
import { DepartamentoRegional } from 'app/modelo/departamento-regional.model';
import { FiltroDepartRegional } from './../../../modelo/filtro-depart-regional.model';
import { MascaraUtil } from './../../../compartilhado/utilitario/mascara.util';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { BaseComponent } from 'app/componente/base.component';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Paginacao } from 'app/modelo/paginacao.model';
import { Component, OnInit, Input, Output, ViewChild, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-departamento-modal',
  templateUrl: './departamento-modal.component.html',
  styleUrls: ['./departamento-modal.component.scss']
})
export class DepartamentoModalComponent extends BaseComponent implements OnInit {

  filtro: FiltroDepartRegional;
  departamentos: DepartamentoRegional[];
  modalRef;
  paginacao = new Paginacao(1, 5, 5);
  estados: any[];
  estadoSelecionado = undefined;
  model: DepartamentoRegional;

  @Output()
  onAdicionar = new EventEmitter<DepartamentoRegional>();

  @Output()
  onRemover = new EventEmitter();

  @Input()
  isSelecionado: boolean;

  @Input()
  modoConsulta: boolean;

  @ViewChild('departamentoModalComponent') departamentoModalComponent;
  constructor(
    public activeModal: NgbActiveModal,
    private service: DepartRegionalService,
    protected bloqueioService: BloqueioService,
    private modalService: NgbModal,
    protected dialogo: ToastyService) {
    super(bloqueioService, dialogo);
    this.model = new DepartamentoRegional();
    this.departamentos = Array<DepartamentoRegional>();
    this.filtro = new FiltroDepartRegional();
    this.buscarEstados();
  }

  ngOnInit() {
    this.departamentos = Array<DepartamentoRegional>();
    this.filtro = new FiltroDepartRegional();
  }

  buscarEstados() {
    if (!this.modoConsulta) {
      this.service.buscarEstados().subscribe((dados: any) => {
        this.estados = dados;
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  selecionar(item: DepartamentoRegional) {
    this.model = item;
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.filtro.cnpj = MascaraUtil.removerMascara(this.filtro.cnpj);
    if (this.isUndefined(this.estadoSelecionado)) {
      this.filtro.idEstado = '0';
    } else {
      this.filtro.idEstado = this.estadoSelecionado;
    }
    this.service.pesquisar(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<DepartamentoRegional>) => {
      this.departamentos = retorno.list;
      if (this.filtro.idEstado === '0') {
        this.filtro.idEstado = undefined;
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  adicionarDepartamento() {
    this.modalRef = this.modalService.open(this.departamentoModalComponent, { size: 'lg' });
    this.modalRef.result.then((result) => {
      this.departamentos = new Array<DepartamentoRegional>();
      this.onAdicionar.emit(this.model);
    }, (reason) => {
    });
  }

  removerDepartamento() {
    this.onRemover.emit();
  }

  pesquisar() {
    if (this.validarCampos()) {
      this.paginacao.pagina = 1;
      this.filtro.cnpj = MascaraUtil.removerMascara(this.filtro.cnpj);
      if (this.isUndefined(this.estadoSelecionado)) {
        this.filtro.idEstado = '0';
      } else {
        this.filtro.idEstado = this.estadoSelecionado;
      }
      this.service.pesquisar(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<DepartamentoRegional>) => {
        this.departamentos = retorno.list;
        this.paginacao = this.getPaginacao(this.paginacao, retorno);
        if (retorno.quantidade === 0) {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
        if (this.filtro.idEstado === '0') {
          this.filtro.idEstado = undefined;
        }
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  validarCampos(): Boolean {
    let verificador: Boolean = true;

    if (this.isVazia(this.filtro.cnpj)
      && this.isVazia(this.filtro.razaoSocial)) {
      this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
      verificador = false;
    }
    if (!this.isVazia(this.filtro.cnpj)) {
      if (MascaraUtil.removerMascara(this.filtro.cnpj).length < 14) {
        this.mensagemError(MensagemProperties.app_rst_labels_cnpj_incompleto);
        verificador = false;
      }
      this.filtro.cnpj = MascaraUtil.removerMascara(this.filtro.cnpj);
    }
    return verificador;
  }
}
