import { EmpresaCboService } from './../../../servico/empresa-cbo.service';
import { EmpresaCbo } from './../../../modelo/empresa-cbo.model';
import { CboFilter } from './../../../modelo/filtro-cbo.model';
import { Component, OnInit, Input, Output, ViewChild } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { FiltroEmpresa } from 'app/modelo/filtro-empresa.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';

@Component({
  selector: 'app-cargo-modal',
  templateUrl: './cargo-modal.component.html',
  styleUrls: ['./cargo-modal.component.scss'],
})
export class CargoModalComponent extends BaseComponent implements OnInit {

  codigo: string;
  descricao: string;

  filtro: CboFilter;

  @Input() @Output()
  emFiltro: FiltroEmpresa;

  model: EmpresaCbo;

  public cbos: EmpresaCbo[];

  public modalRef;

  public paginacao = new Paginacao(1, Paginacao.qtdRegistos5);

  @Input() @Output()
  cbo: EmpresaCbo;

  @Input()
  adicionar: any;

  @ViewChild('cboModalComponent') cboModalComponent;

  constructor(
    public activeModal: NgbActiveModal,
    private empresaCboService: EmpresaCboService,
    protected bloqueioService: BloqueioService,
    private modalService: NgbModal,
    protected dialogo: ToastyService) {
    super(bloqueioService, dialogo);
    this.model = new EmpresaCbo();
    this.cbos = Array<EmpresaCbo>();
    this.filtro = new CboFilter();
  }

  ngOnInit() {
    this.cbos = Array<EmpresaCbo>();
    this.filtro = new CboFilter();
  }

  pesquisar() {
    if (this.isVazia(this.codigo) && this.isVazia(this.descricao)) {
      this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
      return;
    }
    this.filtro.codigo = this.codigo;
    this.filtro.descricao = this.descricao;
    this.filtro.idEmpresa = this.emFiltro.id;
    this.paginacao.pagina = 1;
    this.empresaCboService.pesquisar(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<EmpresaCbo>) => {
      this.paginacao = this.getPaginacao(this.paginacao, retorno);
      this.verificarRetornoEmpresaCargo(retorno);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  selecionar(item: EmpresaCbo) {
    this.model = item;
  }

  verificarRetornoEmpresaCargo(retorno: ListaPaginada<EmpresaCbo>) {
    if (retorno && retorno.list) {
      this.cbos = retorno.list;
      this.model = new EmpresaCbo();
    } else {
      this.cbos = new Array<EmpresaCbo>();
      this.model = new EmpresaCbo();
      this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
    }
  }
  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.empresaCboService.pesquisar(this.filtro,  this.paginacao).subscribe((retorno: ListaPaginada<EmpresaCbo>) => {
      this.paginacao = this.getPaginacao(this.paginacao, retorno);
      this.verificarRetornoEmpresaCargo(retorno);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  adicionarCbo() {
    this.modalRef = this.modalService.open(this.cboModalComponent, { size: 'lg' });
    this.salvar();
  }

  existeCbo() {
    return this.cbo && this.cbo.id !== undefined;
  }

  salvar() {
    this.modalRef.result.then((result) => {
      this.cbo.id = this.model.id;
      this.cbo.dataCriacao = this.model.dataCriacao;
      this.cbo.dataAlteracao = this.model.dataAlteracao;
      this.cbo.dataExclusao = this.model.dataExclusao;
      this.cbo.cbo = this.model.cbo;
    }, (reason) => {

    });
  }

}
