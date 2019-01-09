import { Uat } from 'app/modelo/uat.model';
import { Component, Input, Output, ViewChild } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UatService } from 'app/servico/uat.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { Paginacao } from 'app/modelo/paginacao.model';
import { BaseComponent } from 'app/componente/base.component';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { FiltroUat } from 'app/modelo/filtro-uat.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';

export interface IHash {
  [details: number]: boolean;
}

@Component({
  selector: 'app-uat-modal',
  templateUrl: './uat-modal.component.html',
  styleUrls: ['./uat-modal.component.scss'],
})
export class UatModalComponent extends BaseComponent {

  @Input() @Output()
  list: any[];

  @Input()
  adicionar: any;
  listaSelecionados: Uat[];
  public uats: Uat[];

  cnpj: string;
  razaoSocial: string;

  filtro: FiltroUat;
  paginacao = new Paginacao(1, 3, 5);
  checks: IHash = {};

  @Input()
  modoConsulta: boolean;

  @ViewChild('uatModalComponent') uatModalComponent;

  constructor(public activeModal: NgbActiveModal,
              private service: UatService,
              private modalService: NgbModal,
              protected bloqueioService: BloqueioService,
              protected dialogo: ToastyService,
              private dialogService: DialogService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.uats = Array<Uat>();
    this.filtro = new FiltroUat();
    this.listaSelecionados = new Array<Uat>();
  }

  existeUat() {
    return this.list && this.list.length > 0;
  }

  pesquisar() {
    if (this.validarCampos()) {
      this.removerMascara();
      this.paginacao.pagina = 1;
      this.service.pesquisarAtivos(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Uat>) => {
        this.uats = retorno.list;
        this.paginacao = this.getPaginacao(this.paginacao, retorno);
        if (retorno.quantidade === 0) {
          this.uats = new Array<Uat>();
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error: Response) => {
       //console.error(error.text);
        this.mensagemError(error.statusText);
      });
    }
  }

  validarCampos(): Boolean {
    let verificador: Boolean = true;

    if (this.isVazia(this.filtro.cnpj) && this.isVazia(this.filtro.razaoSocial)) {
      this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
      verificador = false;
    }

    if (!this.isVazia(this.filtro.cnpj)) {
      if (this.filtro.cnpj.length < 18) {
        this.mensagemError(MensagemProperties.app_rst_labels_cnpj_incompleto);
        verificador = false;
      }
    }
    return verificador;
  }

  removerMascara() {
    if (this.filtro.cnpj !== undefined) {
      this.filtro.cnpj = MascaraUtil.removerMascara(this.filtro.cnpj);
    }
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.service.pesquisar(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Uat>) => {
      this.uats = retorno.list;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  selecionar(selecionado: Uat, event: any, i: number) {
    if (selecionado && event.checked) {
      this.listaSelecionados.push(selecionado);
      this.checks[i] = true;
    } else {
      const index: number = this.listaSelecionados.indexOf(selecionado);
      if (index !== -1) {
        this.listaSelecionados.splice(index, 1);
        this.checks[i] = false;
      }
    }
  }

  adicionarUats() {
    this.limparModalUat();
    let uat = new Uat();
    const modalRef = this.modalService.open(this.uatModalComponent, { size: 'lg' });
    modalRef.result.then((result) => {
      this.listaSelecionados.forEach((item) => {
        let adiciona = true;
        this.list.forEach(( unid ) => {
          if (unid.unidadeAtendimentoTrabalhador.id === item.id) {
            adiciona = false;
          }
        });
        if (adiciona) {
          uat = item;
          this.list.push({ unidadeAtendimentoTrabalhador: uat });
        }
      });
      this.list.sort((left, right): number => {
        if (left.unidadeAtendimentoTrabalhador.razaoSocial > right.unidadeAtendimentoTrabalhador.razaoSocial) {
          return 1;
        }

        if (left.unidadeAtendimentoTrabalhador.razaoSocial < right.unidadeAtendimentoTrabalhador.razaoSocial) {
          return -1;
        }
        return 0;
      });

    }, (reason) => {
    });
  }

  limparModalUat() {
    this.filtro = new FiltroUat();
    this.cnpj = '';
    this.razaoSocial = '';
    this.uats = new Array<Uat>();
    this.listaSelecionados = new Array<Uat>();
    this.checks = {};
  }

}
