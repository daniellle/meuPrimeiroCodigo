import { EmpresaSetorService } from './../../../servico/empresa-setor.service';
import { FiltroEmpresa } from 'app/modelo/filtro-empresa.model';
import { FiltroSetor } from 'app/modelo/filtro-setor.model';
import { Component, OnInit, Input, Output, ViewChild } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { Paginacao } from 'app/modelo/paginacao.model';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { EmpresaSetor } from 'app/modelo/empresa-setor.model';

@Component({
  selector: 'app-setor-modal',
  templateUrl: './setor-modal.component.html',
  styleUrls: ['./setor-modal.component.scss'],
})
export class SetorModalComponent extends BaseComponent implements OnInit {

  sigla: string;
  descricao: string;

  filtro: FiltroSetor;

  @Input() @Output()
  emFiltro: FiltroEmpresa;

  model: EmpresaSetor;

  public paginacao: Paginacao;

  public setores: EmpresaSetor[];

  public modalRef;

  @Input() @Output()
  setor: EmpresaSetor;

  @Input()
  adicionar: any;

  @ViewChild('setorModalComponent') setorModalComponent;

  constructor(
    public activeModal: NgbActiveModal,
    private empresaSetorService: EmpresaSetorService,
    protected bloqueioService: BloqueioService,
    private modalService: NgbModal,
    protected dialogo: ToastyService) {
    super(bloqueioService, dialogo);
    this.model = new EmpresaSetor();
    this.setores = Array<EmpresaSetor>();
    this.filtro = new FiltroSetor();
    this.paginacao = new Paginacao(1, 5);
  }

  ngOnInit() {
    this.setores = Array<EmpresaSetor>();
    this.filtro = new FiltroSetor();
  }

  pesquisar() {
    if (this.isVazia(this.sigla) && this.isVazia(this.descricao)) {
      this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
      return;
    }
    this.filtro.sigla = this.sigla;
    this.filtro.descricao = this.descricao;
    this.filtro.idEmpresa = this.emFiltro.id;
    this.paginacao.pagina = 1;
    this.empresaSetorService.pesquisarEmpresasSetores(this.filtro, this.paginacao)
    .subscribe((retorno: ListaPaginada<EmpresaSetor>) => {
      this.paginacao = this.getPaginacao(this.paginacao, retorno);
      this.setLista(retorno);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  selecionar(item: EmpresaSetor) {
    this.model = item;
  }
  setLista(retorno: ListaPaginada<EmpresaSetor>) {
    if (retorno && retorno.list) {
      this.setores = retorno.list;
    } else {
      this.setores = new Array<EmpresaSetor>();
      this.model = new EmpresaSetor();
      this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
    }
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.empresaSetorService.pesquisarEmpresasSetores(this.filtro, this.paginacao)
    .subscribe((retorno: ListaPaginada<EmpresaSetor>) => {
      this.paginacao = this.getPaginacao(this.paginacao, retorno);
      this.setLista(retorno);
    });
  }

  adicionarSetor() {
    this.modalRef = this.modalService.open(this.setorModalComponent, { size: 'lg' });
    this.salvar();
  }

  existeSetor() {
    return this.setor && this.setor.id !== undefined;
  }

  salvar() {
    this.modalRef.result.then((result) => {
      this.setor.id = this.model.id;
      this.setor.dataCriacao = this.model.dataCriacao;
      this.setor.dataAlteracao = this.model.dataAlteracao;
      this.setor.dataExclusao = this.model.dataExclusao;
      this.setor.setor = this.model.setor;
    }, (reason) => {

    });
  }

}
