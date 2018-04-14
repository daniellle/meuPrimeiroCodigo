import { Email } from './../../../modelo/email.model';
import { BaseComponent } from 'app/componente/base.component';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ViewChild } from '@angular/core';
import { Component, OnInit, Input, Output } from '@angular/core';
import { BloqueioService } from '../../../servico/bloqueio.service';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { ValidarEmail } from 'app/compartilhado/validators/email.validator';

@Component({
    selector: 'app-email-modal',
    templateUrl: './email-modal.component.html',
    styleUrls: ['./email-modal.component.scss'],
})
export class EmailModalComponent extends BaseComponent implements OnInit {

    model: Email;

    @Input() @Output()
    list: any[];

    @Input()
    adicionar: any;

    @Input()
    modoConsulta: boolean;

    @ViewChild('modalEmail') modalEmail;

    public modal;

    // tslint:disable-next-line:max-line-length
    public regex_email = '[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@[A-Za-zàáâãéêíóôõúüç0-9]+[A-Za-zàáâãéêíóôõúüç0-9-]*([A-Za-zàáâãéêíóôõúüç0-9])(\.[A-Za-z]+)*(\.[A-Za-z]{2,})$';

    constructor(
        public activeModal: NgbActiveModal,
        private modalService: NgbModal,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private dialogService: DialogService,
    ) {
        super(bloqueioService, dialogo);
        this.model = new Email();
    }


  existeEmail() {
    return this.list && this.list.length > 0;
  }

    adicionarEmail(index?: any) {
        const email = this.model;
        const modalRef = this.modalService.open(this.modalEmail);
        modalRef.result.then((result) => {

            if (!email.notificacao) {
                email.notificacao = false;
            } else {
                this.list.forEach((mail) => {
                    mail.email.notificacao = false;
                });
            }

            if (this.isEmailValido(email)) {
                if (index !== undefined) {
                    this.list[index].email = email;
                } else {
                    this.list.push({ email });
                }
                this.limparModalEmail();
            }
        }, (reason) => {
            this.limparModalEmail();
        });

    }

    isEmailValido(model: Email): Boolean {
        let isValido = true;
        if (!model.descricao) {
            this.mensagemErroComParametrosModel('app_rst_email_campo_obrigatorio', MensagemProperties.app_rst_labels_email);
            isValido = false;
        } else {
            if (!ValidarEmail(model.descricao)) {
              this.mensagemErroComParametrosModel('app_rst_email_campo_invalido', MensagemProperties.app_rst_labels_email);
              isValido = false;
            }
        }
        return isValido;
    }

    limparModalEmail() {
        this.model = new Email();
    }

    selecionarEmail(item: any, index: any) {
        if (!this.modoConsulta) {
            this.model = new Email(item.email);
            this.adicionarEmail(index);
        }
    }

    ngOnInit() {
    }

}
