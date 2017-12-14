import { Municipio } from 'app/modelo/municipio.model';
import { EstadoService } from './../../../servico/estado.service';
import { IOption } from 'ng-select';
import { Estado } from 'app/modelo/estado.model';
import { Endereco } from 'app/modelo/endereco.model';
import { BaseComponent } from 'app/componente/base.component';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Component, OnInit, Input, Output, ViewChild } from '@angular/core';
import { MensagemProperties } from '../../utilitario/recurso.pipe';
import { MascaraUtil } from '../../utilitario/mascara.util';
import { TipoEndereco } from 'app/modelo/enum/enum-tipo-endereco.model';
import { EnumValues } from 'enum-values';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';

@Component({
    selector: 'app-endereco-modal',
    templateUrl: './endereco-modal.component.html',
    styleUrls: ['./endereco-modal.component.scss'],
})
export class EnderecoModalComponent extends BaseComponent implements OnInit {

    model: Endereco;
    estados: Estado[];
    idEstadoSelecionado: number;
    idMunicipioSelecionado: string;

    public tiposEndereco = TipoEndereco;
    public keysTipoEndereco: string[];

    @Input() @Output()
    list: any[];

    @Input()
    adicionar: any;

    @Input()
    modoConsulta: boolean;

    @ViewChild('modalEndereco') modalEndereco;

    public modal;

    public estadoSelecionado: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private modalService: NgbModal,
        protected bloqueioService: BloqueioService,
        protected estadoService: EstadoService,
        protected dialogo: ToastyService,
        private dialogService: DialogService,
    ) {
        super(bloqueioService, dialogo);
        this.model = new Endereco();
        this.model.municipio = new Municipio();
        this.estados = new Array<Estado>();
        this.buscarEstados();
        this.keysTipoEndereco = Object.keys(this.tiposEndereco);
        this.model.tipoEndereco = undefined;
    }

    existeList() {
        return this.list && this.list.length > 0;
      }

    buscarEstados() {
        this.estadoService.buscarEstados().subscribe((dados: any) => {
            this.estados = dados;
        });
    }

    pesquisarMunicipiosPorEstado(idEstado: number) {
        this.estadoSelecionado = false;
        this.municipios = [];
        this.estadoService.pesquisarMunicipiosPorEstado(idEstado).subscribe((dados: any) => {
            let listaOption: IOption[];
            listaOption = [];
            dados.forEach((elemento) => {
                const item = new Option();
                item.value = elemento.id;
                item.label = elemento.descricao;
                listaOption.push(item);
            });
            this.estadoSelecionado = true;
            this.idEstadoSelecionado = idEstado;
            this.municipios = listaOption;
        });
    }

    adicionarEndereco(index?: any) {
        const endereco = this.model;
        const modalRef = this.modalService.open(this.modalEndereco, { size: 'lg' });

        if (endereco.municipio && endereco.municipio.id) {
            if (endereco.municipio.estado && endereco.municipio.estado.id) {
                this.idEstadoSelecionado = endereco.municipio.estado.id;
                this.buscarMunicipio(this.idEstadoSelecionado);
                this.idMunicipioSelecionado = endereco.municipio.id.toString();
            }
        }

        modalRef.result.then((result) => {
            if (this.isEnderecoValido(endereco)) {
                endereco.cep =  MascaraUtil.removerMascara(endereco.cep);
                this.municipios.forEach((m) => {
                    if (this.idMunicipioSelecionado.toString() === m.value) {
                        endereco.municipio.id = parseInt(this.idMunicipioSelecionado, 10);
                        endereco.municipio.descricao = m.label;
                        this.estados.forEach((estado) => {
                            if (estado.id === Number(this.idEstadoSelecionado)) {
                                endereco.municipio.estado = estado;
                            }
                        });
                    }
                });
                if (index !== undefined) {
                    this.list[index].endereco = endereco;
                } else {
                    endereco.cep = MascaraUtil.removerMascara(endereco.cep);
                    endereco.tipoEndereco = EnumValues.getNameFromValue(TipoEndereco, TipoEndereco.P);
                    this.list.push({ endereco });
                }
                this.limparModalEndereco();
            }
        }, (reason) => {
            this.limparModalEndereco();
        });

    }

    isEnderecoValido(model: Endereco): Boolean {
        let isValido = true;
        if (!model.descricao) {
             this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_endereco);
             isValido = false;
        }
        if (!model.cep) {
            this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_CEP);
            isValido = false;
        }
        if (!this.idMunicipioSelecionado) {
            this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_municipio);
            isValido = false;
        }
        if (!model.tipoEndereco) {
             this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_tipo);
             isValido = false;
        }

        return isValido;
    }

    limparModalEndereco() {
        this.model = new Endereco();
        this.idEstadoSelecionado = undefined;
        this.idMunicipioSelecionado = undefined;
        this.model.municipio = new Municipio();
    }

    selecionarEndereco(item: any, index: any) {
        if (!this.modoConsulta) {
            if (!item.endereco) {
                this.model = new Endereco();
            }
            this.model = new Endereco(item.endereco);
            this.adicionarEndereco(index);
        }
    }

    buscarMunicipio(idEstadoSelecionado: number) {
        if (idEstadoSelecionado > 0) {
            this.pesquisarMunicipiosPorEstado(idEstadoSelecionado);
        } else {
            this.municipios = [];
        }
    }

    ngOnInit() {
    }
}
