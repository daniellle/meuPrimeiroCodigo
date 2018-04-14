
import { Component, Input, Output, EventEmitter, ViewChild } from '@angular/core';
import { Paginacao } from './../../modelo/paginacao.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ListaPaginada } from './../../modelo/lista-paginada.model';
import { MensagemProperties } from './../utilitario/recurso.pipe';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { GrupoPergunta } from '../../modelo/grupo-pergunta.model';
import { GrupoPerguntaFilter } from '../../modelo/filtro-grupo-pergunta';
import { GrupoPerguntaService } from '../../servico/grupo-pergunta.service';

@Component({
  selector: 'app-modal-selecionar-grupo',
  templateUrl: './modal-selecionar-grupo.component.html',
  styleUrls: ['./modal-selecionar-grupo.component.scss'],
})
export class ModalSelecionarGrupoPerguntaComponent extends BaseComponent {

  paginacao = new Paginacao(1, 3, 5);

  @Input()
  model: GrupoPergunta;

  @Output()
  onAdicionar = new EventEmitter<GrupoPergunta>();

  @Output()
  onLimpar = new EventEmitter();

  @Input()
  modoConsulta: boolean;

  @Input()
  isGrupoSelecionado: boolean;

  filtro = new GrupoPerguntaFilter();

  listaGrupo = new Array<GrupoPergunta>();

  @ViewChild('grupoModalComponent') grupoModalComponent;

  constructor(
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private grupoService: GrupoPerguntaService,
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
    this.grupoService.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<GrupoPergunta>) => {
      this.listaGrupo = retorno.list;
      this.paginacao = this.getPaginacao(this.paginacao, retorno);
      if (retorno.quantidade === 0) {
        this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  selecionar(item: GrupoPergunta) {
    this.model = item;
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.grupoService.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<GrupoPergunta>) => {
      this.listaGrupo = retorno.list;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  openModal() {
    const modalRef = this.modalService.open(this.grupoModalComponent, { size: 'lg' });
    modalRef.result.then((result) => {
      this.onAdicionar.emit(this.model);
      this.limparModalGrupo();
    }, (reason) => {
    });
  }

  limparModalGrupo() {
    this.listaGrupo = null;
    this.model = new GrupoPergunta();
  }

}
