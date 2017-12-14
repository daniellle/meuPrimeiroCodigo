import { EmpresaFuncaoService } from './../../../servico/empresa-funcao.service';
import { ToastyService } from 'ng2-toasty';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FiltroFuncao } from './../../../modelo/filtro-funcao.model';
import { FiltroEmpresa } from './../../../modelo/filtro-empresa.model';
import { BaseComponent } from 'app/componente/base.component';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { Paginacao } from 'app/modelo/paginacao.model';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { EmpresaFuncao } from './../../../modelo/empresa-funcao.model';
import { Component, OnInit, Output, Input, ViewChild } from '@angular/core';

@Component({
  selector: 'app-funcao-modal',
  templateUrl: './funcao-modal.component.html',
  styleUrls: ['./funcao-modal.component.scss'],
})
export class FuncaoModalComponent extends BaseComponent implements OnInit {

  codigo: string;
  descricao: string;

  filtro: FiltroFuncao;

  @Input() @Output()
  emFiltro: FiltroEmpresa;

  model: EmpresaFuncao;

  public paginacao: Paginacao;

  public funcoes: EmpresaFuncao[];

  public modalRef;

  @Input() @Output()
  funcao: EmpresaFuncao;

  @Input()
  adicionar: any;

  @ViewChild('funcaoModalComponent') funcaoModalComponent;

  constructor(
    public activeModal: NgbActiveModal,
    private empresaFuncaoService: EmpresaFuncaoService,
    protected bloqueioService: BloqueioService,
    private modalService: NgbModal,
    protected dialogo: ToastyService) {
    super(bloqueioService, dialogo);
    this.model = new EmpresaFuncao();
    this.funcoes = Array<EmpresaFuncao>();
    this.filtro = new FiltroFuncao();
    this.paginacao = new Paginacao(1, 5);
  }

  ngOnInit() {
    this.funcoes = Array<EmpresaFuncao>();
    this.filtro = new FiltroFuncao();
    this.codigo = '';
    this.descricao = '';
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
    this.empresaFuncaoService.pesquisarEmpresasFuncoes(this.filtro,  this.paginacao)
    .subscribe((retorno: ListaPaginada<EmpresaFuncao>) => {
      this.paginacao = this.getPaginacao(this.paginacao, retorno);
      this.setLista(retorno);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  selecionar(item: EmpresaFuncao) {
    this.model = item;
  }

  setLista(retorno: ListaPaginada<EmpresaFuncao>) {
    if (retorno && retorno.list) {
      this.model = new EmpresaFuncao();
      this.funcoes = retorno.list;
    } else {
      this.funcoes = new Array<EmpresaFuncao>();
      this.model = new EmpresaFuncao();
      this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
    }
  }

  public pageChanged(event: any): void {
    this.empresaFuncaoService.pesquisarEmpresasFuncoes(this.filtro, new Paginacao(event.page, 10))
    .subscribe((retorno: ListaPaginada<EmpresaFuncao>) => {
      this.paginacao = this.getPaginacao(this.paginacao, retorno);
      this.setLista(retorno);
    });
  }

  adicionarFuncao() {
    this.modalRef = this.modalService.open(this.funcaoModalComponent, { size: 'lg' });
    this.salvar();
  }

  existeFuncao() {
    return this.funcao && this.funcao.id !== undefined;
  }

  salvar() {
    this.modalRef.result.then((result) => {
      this.funcao.id = this.model.id;
      this.funcao.dataAlteracao = this.model.dataAlteracao;
      this.funcao.dataExclusao = this.model.dataExclusao;
      this.funcao.funcao = this.model.funcao;
    }, (reason) => {

    });
  }

}
