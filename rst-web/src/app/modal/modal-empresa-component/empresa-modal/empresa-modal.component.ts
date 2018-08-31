import { MascaraUtil } from './../../../compartilhado/utilitario/mascara.util';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { FiltroEmpresa } from './../../../modelo/filtro-empresa.model';
import { BaseComponent } from 'app/componente/base.component';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { EmpresaService } from 'app/servico/empresa.service';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Paginacao } from 'app/modelo/paginacao.model';
import { Empresa } from './../../../modelo/empresa.model';
import { Component, OnInit, Input, Output, ViewChild, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-empresa-modal',
  templateUrl: './empresa-modal.component.html',
  styleUrls: ['./empresa-modal.component.scss'],
})
export class EmpresaModalComponent extends BaseComponent implements OnInit {

  filtro: FiltroEmpresa;
  empresas: Empresa[];
  modalRef;
  paginacao = new Paginacao(1, 5, 5);
  model: Empresa;

  @Output()
  onAdicionar = new EventEmitter<Empresa>();

  @Output()
  onRemover = new EventEmitter();

  @Input()
  isSelecionado: boolean;

  @Input()
  modoConsulta: boolean;

  @ViewChild('empresaModalComponent') empresaModalComponent;
  constructor(
    public activeModal: NgbActiveModal,
    private service: EmpresaService,
    protected bloqueioService: BloqueioService,
    private modalService: NgbModal,
    protected dialogo: ToastyService) {
    super(bloqueioService, dialogo);
    this.model = new Empresa();
    this.empresas = Array<Empresa>();
    this.filtro = new FiltroEmpresa();
  }

  ngOnInit() {
    this.empresas = Array<Empresa>();
    this.filtro = new FiltroEmpresa();
  }

  selecionar(item: Empresa) {
    this.model = item;
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.service.pesquisar(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Empresa>) => {
      this.empresas = retorno.list;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  adicionarEmpresa() {
    this.modalRef = this.modalService.open(this.empresaModalComponent, { size: 'lg' });
    this.modalRef.result.then((result) => {
      this.empresas = new Array<Empresa>();
      this.onAdicionar.emit(this.model);
     }, (reason) => {
     });
  }

  removerEmpresa() {
    this.onRemover.emit();
  }

  pesquisar() {
    if (this.validarCampos()) {
      this.paginacao.pagina = 1;
      this.service.pesquisar(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Empresa>) => {
        this.empresas = retorno.list;
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

    if (this.isVazia(this.filtro.cnpj) && this.isVazia(this.filtro.nomeFantasia)
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
