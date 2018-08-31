import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { Component, Input, ViewChild, Output } from '@angular/core';
import { Telefone } from 'app/modelo/telefone.model';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { BaseComponent } from 'app/componente/base.component';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { TipoTelefone } from 'app/modelo/enum/enum-tipo-telefone.model';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';

@Component({
  selector: 'telefone-modal',
  templateUrl: 'telefone-modal.component.html',
})

export class TelefoneModalComponent extends BaseComponent {
  model: Telefone;

  @Input() @Output()
  list: any[];

  @Input()
  adicionar: any;

  @Input()
  modoConsulta: boolean;

  @ViewChild('modalTelefone') modalTelefone;

  public modal;
  public tiposTelefone = TipoTelefone;
  public keysTiposTelefone: string[];
  public modalRef;

  constructor(
    public activeModal: NgbActiveModal,
    private modalService: NgbModal,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private dialogService: DialogService,
  ) {
    super(bloqueioService, dialogo);
    this.model = new Telefone();
    this.model.tipo = undefined;
    this.keysTiposTelefone = Object.keys(this.tiposTelefone);

  }

  existeTelefone() {
    return this.list && this.list.length > 0;
  }

  adicionarTel(index?: any) {
    const telefone = this.model;
    this.modalRef = this.modalService.open(this.modalTelefone);
    this.modalRef.result.then((result) => {
      this.model.numero = MascaraUtil.removerMascara(this.model.numero);
      if (this.isTelefoneValido(telefone, index)) {
        if (index !== undefined) {
          this.list[index].telefone = telefone;
        } else {

          this.list.push({ telefone });
        }
      }
      this.limparModalTelefone();
    }, (reason) => {
      this.limparModalTelefone();
    });

  }

  mascaraTelFixo() {
    this.model.numero = MascaraUtil.removerMascara(this.model.numero);
    this.model.numero = this.model.numero.replace(/\W/g, '');
    this.model.numero = this.model.numero.replace(/^(\d{2})(\d)/, '($1) $2');
    this.model.numero = this.model.numero.replace(/(\d{4})(\d)/, '$1-$2');
    this.model.numero = this.model.numero.replace(/(\d{4})$/, '$1');
  }

  mascaraCelular() {
    this.model.numero = MascaraUtil.removerMascara(this.model.numero);
    this.model.numero = this.model.numero.replace(/\W/g, '');
    this.model.numero = this.model.numero.replace(/^(\d{2})(\d)/, '($1) $2');
    this.model.numero = this.model.numero.replace(/(\d{5})(\d)/, '$1-$2');
    this.model.numero = this.model.numero.replace(/(\d{4})$/, '$1');
  }

  mudarMascara(event: any) {
    if (this.model.numero.length <= 14) {
      this.mascaraTelFixo();
    } else {
      this.mascaraCelular();
    }
  }

  isTelefoneValido(model: any, index?: any): Boolean {
    let isValido = true;
    if (!model.numero) {
      this.mensagemErroComParametrosModel('app_rst_msg_telefone_campo_obrigatorio', MensagemProperties.app_rst_labels_numero);
      isValido = false;
    }
    if (model.numero && MascaraUtil.removerMascara(model.numero).length > 11) {
      this.mensagemErroComParametrosModel('app_rst_msg_telefone_campo_invalido', MensagemProperties.app_rst_labels_numero);
      isValido = false;
    }
    if (!model.tipo || model.tipo === 'undefined') {
      this.mensagemErroComParametrosModel('app_rst_msg_telefone_campo_obrigatorio', MensagemProperties.app_rst_labels_tipo);
      isValido = false;
    }
    if (!this.verificaContato(index)) {
      isValido = false;
    }
    return isValido;
  }

  limparModalTelefone() {
    this.model = new Telefone();
  }

  selecionarTel(item: any, index: any, modoConsulta: boolean) {
    this.model = new Telefone(item.telefone);
    this.modoConsulta = modoConsulta;
    if (this.model.numero.length === 10) {
      this.mascaraTelFixo();
    } else {
      this.mascaraCelular();
    }
    this.adicionarTel(index);
  }

  verificaContato(index?: any): boolean {
    let isValido = true;
    if (this.model.contato && this.list) {
      for (let i = 0; i < this.list.length; i++) {
        if (index == null && this.list[i].telefone.contato === true) {
          this.mensagemErroComParametrosModel('app_rst_msg_telefone_existente', MensagemProperties.app_rst_msg_telefone_existente);
          isValido = false;
        }
        if (index !== i && this.list[i].telefone.contato === true && isValido) {
          this.mensagemErroComParametrosModel('app_rst_msg_telefone_existente', MensagemProperties.app_rst_msg_telefone_existente);
          isValido = false;
        }
      }
    }
    return isValido;
  }
}
