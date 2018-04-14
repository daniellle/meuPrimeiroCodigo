import { element } from 'protractor';
import { UnidadeObraService } from './../../../servico/unidade-obra.service';
import { ViewChild } from '@angular/core';
import { Output } from '@angular/core';
import { UnidadeObra } from 'app/modelo/unidade-obra.model';
import { DialogService } from 'ng2-bootstrap-modal';
import { ToastyService } from 'ng2-toasty';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit, Input } from '@angular/core';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';

@Component({
    selector: 'app-unidade-modal',
    templateUrl: './unidade-modal.component.html',
})
export class UnidadeModalComponent extends BaseComponent implements OnInit {
    model: UnidadeObra;

    @Input() @Output()
    list: any[];

    @Input()
    adicionar: any;

    @Input()
    modoConsulta: boolean;

    @ViewChild('modalUnidade') modalUnidade;

    public modal;
    public modalRef;

    constructor(
        public activeModal: NgbActiveModal, private modalService: NgbModal,
        protected bloqueioService: BloqueioService, protected dialogo: ToastyService,
        private dialogService: DialogService, private service: UnidadeObraService) {
        super(bloqueioService, dialogo);
        this.model = new UnidadeObra();
    }

    ngOnInit() {
    }

    existeList() {
        return this.list && this.list.length > 0;
      }

    adicionarUnidade(index?: any) {
        const unidadeObra = this.model;
        this.modalRef = this.modalService.open(this.modalUnidade);
        this.modalRef.result.then((result) => {
            if (this.isUnidadeObraValido(unidadeObra, index)) {
                if (index !== undefined) {
                    this.list[index] = unidadeObra;
                } else {
                    this.list.push(unidadeObra);
                }
                this.limparModalUnidade();
            } else {
                this.limparModalUnidade();
            }
            this.orderByDescricao(this.list);
        }, (reason) => {
            this.limparModalUnidade();
        });

    }

    isUnidadeObraValido(model: any, index?: any): Boolean {
        let isValido = true;
        if (!model.descricao) {
            this.mensagemErroComParametrosModel('app_rst_msg_unidade_descricao_campo_obrigatorio',
                MensagemProperties.app_rst_labels_descricao);
            isValido = false;

        } else {
            model.descricao = model.descricao.trim();
            if (this.list) {
                let posicao: number;
                this.list.forEach((element) => {
                    if (index) {
                        posicao = this.list.indexOf(element);
                        if (element.descricao.toUpperCase() === model.descricao.toUpperCase() && posicao !== index) {
                            this.mensagemErroComParametrosModel('app_rst_msg_unidade_existente');
                            isValido = false;
                        }

                        if (element.cei === model.cei && posicao !== index) {
                            this.mensagemErroComParametrosModel('app_rst_msg_unidade_mesmo_codigo_cei');
                            isValido = false;
                        }
                    } else {
                        if ((element.descricao.toUpperCase() === model.descricao.toUpperCase() && model.id !== element.id)
                            || (element.descricao.toUpperCase() === model.descricao.toUpperCase() && !model.id && !element.id)) {
                            this.mensagemErroComParametrosModel('app_rst_msg_unidade_existente');
                            isValido = false;
                        }
                        if ((element.cei === model.cei && model.id !== element.id)
                            || (element.cei === model.cei && !model.id && !element.id)) {
                            this.mensagemErroComParametrosModel('app_rst_msg_unidade_mesmo_codigo_cei');
                            isValido = false;
                        }
                    }
                });
            }

        }
        return isValido;
    }

    limparModalUnidade() {
        this.model = new UnidadeObra();
    }

    selecionarTel(item: any, index: any) {
        if (!this.modoConsulta) {
            this.model = new UnidadeObra(item);
            this.adicionarUnidade(index);
        }
    }

}
