import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { DatePicker } from './../../../compartilhado/utilitario/date-picker';
import { ValidarDataFutura, ValidateData, ValidateDataFutura } from 'app/compartilhado/validators/data.validator';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { EstadoService } from './../../../servico/estado.service';
import { Sindicato } from './../../../modelo/sindicato.model';
import { EmailSindicato } from './../../../modelo/email-sindicato.model';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { Observable } from 'rxjs/Observable';
import { TelefoneSindicato } from './../../../modelo/telefoneSindicato.model';
import { Telefone } from 'app/modelo/telefone.model';
import { Endereco } from 'app/modelo/endereco.model';
import { Email } from './../../../modelo/email.model';
import { ToastyService } from 'ng2-toasty';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { ActivatedRoute, Router } from '@angular/router';
import { BloqueioService } from '../../../servico/bloqueio.service';
import { DialogService } from 'ng2-bootstrap-modal';
import { SindicatoService } from '../../../servico/sindicato.service';
import { EnderecoSindicato } from '../../../modelo/endereco-sindicato.model';
import { DepartRegionalService } from '../../../servico/depart-regional.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TipoEndereco } from 'app/modelo/enum/enum-tipo-endereco.model';
import { TipoEmail } from 'app/modelo/enum/enum-tipo-email.model';
import { ValidateCNPJ } from 'app/compartilhado/validators/cnpj.validator';
import { EnumValues } from 'enum-values';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import * as moment from 'moment';

@Component({
    selector: 'app-sindicatos-cadastrar',
    templateUrl: './sindicatos-cadastrar.component.html',
    styleUrls: ['./sindicatos-cadastrar.component.scss'],
})
export class SindicatosCadastrarComponent extends BaseComponent implements OnInit {

    public formulario: FormGroup;
    public id: string;

    public sindicato: Sindicato;
    public email: Email;
    public endreco: Endereco;

    public estados: any[];
    public municipios: any[];
    public municipioSelecionado;
    public typeaheadLoading: boolean;
    public typeaheadNoResults: boolean;

    private variable: boolean;

    public dataSource: Observable<any>;

    private cipa: boolean;
    public umCnaeSelecionado = false;
    private sesmt: boolean;
    public status: any = {
        isOpen: false,
    };

    public telefone: Telefone;

    public telefones = new Array<Telefone>();
    public tipo: string;
    public showDataDesligamento: boolean;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private dpService: DepartRegionalService,
        private formBuilder: FormBuilder,
        protected bloqueioService: BloqueioService,
        private modalService: NgbModal,
        protected dialogo: ToastyService,
        private service: SindicatoService,
        protected estadoService: EstadoService,
        private dialogService: DialogService) {
        super(bloqueioService, dialogo, estadoService);
        this.showDataDesligamento = false;
        this.tipoTela();
        this.createForm();
        this.buscarEstados();
        this.carregarTela();
        this.title = MensagemProperties.app_rst_sindicatot_title_cadastrar;
        this.variable = false;

        this.status.isOpen = this.modoConsulta;

        this.dataSource = Observable
            .create((observer: any) => {
                observer.next(this.formulario.controls.municipio.value);
            })
            .mergeMap((token: string) => this.filtrarMunicipios(token));
    }

    carregarTela() {
        this.route.params.subscribe((params) => {
            this.id = params['id'];
        }, (error) => {
            this.mensagemError(error);
        });

        this.sindicato = new Sindicato();
        if (this.id) {
            this.showDataDesligamento = true;
            this.service.pesquisarPorId(this.id).subscribe((sindicato: Sindicato) => {
                if (sindicato && sindicato.id) {
                    this.sindicato = sindicato;
                    this.setMunicipio();
                    this.converterModelParaForm();
                }
            },
                (error) => {
                    this.mensagemError(error);
                }, () => {
                    if (!this.sindicato.telefone) {
                        this.sindicato.telefone = new Array<TelefoneSindicato>();
                    }
                });
        }
    }

    buscarEstados() {
        this.dpService.buscarEstados().subscribe((dados: any) => {
            this.estados = dados;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    ngOnInit() {
        this.sindicato = new Sindicato();
        this.email = new Email();
        this.endreco = new Endereco();
        this.telefone = new Telefone();
    }

    onverterTipoTelefone(item: string): string {
        // converter
        return item[0].toUpperCase() + item.toLowerCase().substring(1);
    }

    buscarMunicipio() {
        this.formulario.patchValue({
            municipio: null,
        });
        if (this.formulario.controls['estado'].value && this.formulario.controls['estado'].value !== 0 && !this.modoConsulta) {
            this.formulario.controls['municipio'].enable();
            this.pesquisarMunicipiosPorEstado(this.formulario.controls['estado'].value);
        } else {
            this.formulario.controls['municipio'].disable();
        }
    }

    public changeTypeaheadLoading(e: boolean): void {
        this.typeaheadLoading = e;
        this.municipioSelecionado = false;
    }

    public changeTypeaheadNoResults(e: boolean): void {
        this.typeaheadNoResults = e;
    }

    prepareSaveSindicato(): Sindicato {

        const formModel = this.formulario.controls;
        this.sindicato.nome_presidente = formModel.nome_presidente.value;
        this.sindicato.cd_siga = formModel.cd_siga.value;
        this.sindicato.federacao = formModel.federacao.value;
        this.sindicato.sigla = formModel.sigla.value;

        if (formModel.filiado.value == true){
            this.sindicato.filiado = 'S';
        } else {
            this.sindicato.filiado = 'N';
        }
        if (formModel.sede_sindicato_nacional.value == true){
            this.sindicato.sede_sindicato_nacional = 'S';
        }
        else {
            this.sindicato.sede_sindicato_nacional = 'N';
        }
        this.sindicato.tipo_abrangencia = formModel.tipo_abrangencia.value;
        this.sindicato.sexo = formModel.sexo.value;
        this.sindicato.home_page = formModel.home_page.value;
        this.sindicato.nm_contato = formModel.nm_contato.value;
        this.sindicato.area_contato = formModel.area_contato.value;
        this.sindicato.dt_fundacao = formModel.dt_fundacao.value;
        
        this.sindicato.cnpj = formModel.cnpj.value;
        if (!this.isVazia(this.sindicato.cnpj)) {
            this.sindicato.cnpj = MascaraUtil.removerMascara(this.sindicato.cnpj);
        }
        this.sindicato.razaoSocial = formModel.razao.value;
        this.sindicato.nomeFantasia = formModel.nome.value;
        this.sindicato.inscricaoEstadual = formModel.inscEstatual.value;
        this.sindicato.inscricaoMunicipal = formModel.inscMunincipal.value;
        this.sindicato.sesmt = formModel.SESMT.value;
        this.sindicato.cipa = formModel.CIPA.value;
        // this.sindicato.dataDesativacao = formModel.dtDesativacao.value ?
        //     this.convertDateToString(formModel.dtDesativacao.value.date) : null;

        // endereco
        if (formModel.endereco.value) {
            if (!this.isNotEmpty(this.sindicato.endereco)) {
                this.sindicato.endereco = new Array<EnderecoSindicato>();
                this.sindicato.endereco.push(new EnderecoSindicato());
                this.sindicato.endereco[0].endereco = new Endereco();
            }
            this.sindicato.endereco[0].endereco.descricao = formModel.endereco.value;
            this.sindicato.endereco[0].endereco.complemento = formModel.complemento.value;
            this.sindicato.endereco[0].endereco.numero = formModel.numero.value;
            this.sindicato.endereco[0].endereco.bairro = formModel.bairro.value;
            if (!this.isVazia(formModel.cep.value)) {
                this.sindicato.endereco[0].endereco.cep = MascaraUtil.removerMascara(formModel.cep.value);
            }
            this.sindicato.endereco[0].endereco.municipio.id = formModel.municipio.value;
            this.sindicato.endereco[0].endereco.tipoEndereco = EnumValues.getNameFromValue(TipoEndereco, TipoEndereco.P);
        } else {
            this.sindicato.endereco = new Array<EnderecoSindicato>();
        }

        if (formModel.email.value) {
            if (!this.sindicato || !this.isNotEmpty(this.sindicato.email)) {
                this.sindicato.email = new Array<EmailSindicato>();
                this.sindicato.email.push(new EmailSindicato());
                this.sindicato.email[0].email = new Email();
            }
            this.sindicato.email[0].email.descricao = formModel.email.value;
            this.sindicato.email[0].email.tipo = EnumValues.getNameFromValue(TipoEmail, TipoEmail.P);
            this.sindicato.email[0].email.notificacao = true;
        } else {
            this.sindicato.email = new Array();
        }

        if (!this.sindicato.endereco) {
            this.sindicato.endereco = new Array<EnderecoSindicato>();
        }

        if (!this.sindicato.email) {
            this.sindicato.email = new Array<EmailSindicato>();
        }

        return this.sindicato;
    }

    verificarEndereco(): boolean {
        let v = true;
        const formModel = this.formulario.controls;
        if (!this.isVazia(formModel.endereco.value) ||
            !this.isVazia(formModel.cep.value) ||
            (formModel.estado.value) ||
            formModel.municipio.value ||
            !this.isVazia(formModel.numero.value) ||
            !this.isVazia(formModel.bairro.value)) {
            if (this.isVazia(formModel.municipio.value) ||
                this.isVazia(formModel.estado.value)) {
                this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_municipio);
                v = false;
            }

            if (this.isVazia(formModel.cep.value)) {
                this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_CEP);
                v = false;
            }

            if (this.isVazia(formModel.endereco.value)) {
                this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_endereco);
                v = false;
            }

        }

        return v;

    }

    createForm() {
        this.formulario = this.formBuilder.group({
            sede_sindicato_nacional: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(5),
                ]),
            ], 
            dt_fundacao: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    ValidateData, ValidateDataFutura,
                ]),
            ],            
            area_contato: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(40),
                ]),
            ],                                                 
            nm_contato: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(40),
                ]),
            ], 
            home_page: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(60),
                ]),
            ], 
            sexo: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(30),
                ]),
            ], 
            nome_presidente: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(60),
                ]),
            ], 
            tipo_abrangencia: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(40),
                ]),
            ], 
            filiado: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(40),
                ]),
            ],                                                                         
            sigla: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(30),
                ]),
            ],  
            federacao: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(30),
                ]),
            ],            
            cd_siga: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(30),
                ]),
            ],            
            cnpj: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(18),
                    Validators.minLength(18),
                    ValidateCNPJ,
                ]),
            ],
            razao: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(160),
                ]),
            ],
            nome: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(160),
                ]),
            ],
            inscEstatual: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(20),
                ]),
            ],
            inscMunincipal: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(20),
                ]),
            ],
            SESMT: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([

                ]),
            ],
            CIPA: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([

                ]),
            ],
            endereco: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(100),
                ]),
            ],
            complemento: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(100),
                ]),
            ],
            municipio: [
                { value: null, disabled: true },
                Validators.compose([
                    Validators.maxLength(100),
                ]),
            ],
            numero: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(5),
                ]),
            ],
            estado: [
                { value: undefined, disabled: this.modoConsulta },
                Validators.compose([

                ]),
            ],
            bairro: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(50),
                ]),
            ],
            cep: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(100),
                ]),
            ],
            email: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(8),
                ]),
            ],
            numeroTelefone: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([

                ]),
            ],
            contato: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([

                ]),
            ],
            tipo: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([

                ]),
            ],
            // dtDesativacao: [
            //     { value: null, disabled: this.modoConsulta },
            //     Validators.compose([
            //         ValidateData, ValidateDataFutura,
            //     ]),
            // ],
        });
    }

    salvar() {
        if (this.verificarCampos()) {
            this.prepareSaveSindicato();
            this.service.salvar(this.sindicato).subscribe((response: Sindicato) => {
                this.sindicato = response;
                this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
            }, (error) => {
                this.mensagemError(error);
            });
        }
    }

    verificarCampos(): boolean {
        let retorno = true;
        // cnpj
        if (this.formulario.controls['cnpj'].invalid) {
            if (this.formulario.controls['cnpj'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.formulario.controls['cnpj'],
                    MensagemProperties.app_rst_labels_cnpj);
                retorno = false;
            }

            if (!this.formulario.controls['cnpj'].errors.required && this.formulario.controls['cnpj'].errors.validCNPJ) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['cnpj'],
                    MensagemProperties.app_rst_labels_cnpj);
                retorno = false;
            }
        }

        if (!this.isVazia(this.formulario.controls['email'].value)
            && !this.validarEmail(this.formulario.controls['email'].value)) {
            this.mensagemErroComParametros('app_rst_campo_obrigatorio',
                this.formulario.controls['email'], MensagemProperties.app_rst_labels_email);
            retorno = false;
        }

        if (this.isVazia(this.formulario.controls['razao'].value)) {
            this.mensagemErroComParametros('app_rst_campo_obrigatorio',
                this.formulario.controls['razao'], MensagemProperties.app_rst_labels_razao_social);
            retorno = false;
        }

        if (!this.verificarEndereco()) {
            retorno = false;
        }

        // validar data desativacao
        // if (!this.isVazia(this.formulario.controls['dtDesativacao'].value)) {
        //     if (this.formulario.controls['dtDesativacao'].errors && this.formulario.controls['dtDesativacao'].errors.validData) {
        //         this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['dtDesativacao'],
        //           MensagemProperties.app_rst_labels_data_desativacao);
        //         retorno = false;
        //       } else if (ValidarDataFutura(this.formulario.controls['dtDesativacao'].value.jsdate)) {
        //         this.mensagemErroComParametrosModel('app_rst_labels_data_futura', MensagemProperties.app_rst_labels_data_desativacao);
        //         retorno = false;
        //     }
        // }

        return retorno;
    }

    validarEmail(valor: string): boolean {
        return ((/^[a-zA-Z0-9\._-]+@/.test(valor)) &&
            ((/[a-zA-Z0-9\._-]+./.test(valor)) &&
                (/([a-zA-Z]{2,4})$/.test(valor))));
    }

    public isVazia(valor: any): boolean {
        return valor === undefined || valor === null || valor === '';
    }

    public isObjetoVazio(valor: any): boolean {
        return valor === undefined || valor === null;
    }

    voltar() {
        this.router.navigate([`${environment.path_raiz_cadastro}/sindicato`]);
    }

    converterModelParaForm() {

        if (this.sindicato.sesmt) {
            this.sesmt = true;
        } else {
            this.sesmt = false;
        }

        if (this.sindicato.cipa) {
            this.cipa = true;
        } else {
            this.cipa = false;
        }
        
        this.formulario.patchValue({
            dt_fundacao: this.sindicato.dt_fundacao ? DatePicker.convertDateForMyDatePicker(
                moment(this.sindicato.dt_fundacao).format("DD/MM/YYYY")) : null, 
            area_contato: this.sindicato.area_contato,
            nm_contato: this.sindicato.nm_contato,
            home_page: this.sindicato.home_page,
            sexo: this.sindicato.sexo,
            nome_presidente: this.sindicato.nome_presidente,
            tipo_abrangencia: this.sindicato.tipo_abrangencia,
            filiado: this.sindicato.filiado,
            federacao: this.sindicato.federacao,
            sigla: this.sindicato.sigla,
            cd_siga: this.sindicato.cd_siga,
            sede_sindicato_nacional: this.sindicato.sede_sindicato_nacional,
            cnpj: this.sindicato.cnpj,
            razao: this.sindicato.razaoSocial,
            nome: this.sindicato.nomeFantasia,
            inscEstatual: this.sindicato.inscricaoEstadual,
            inscMunincipal: this.sindicato.inscricaoMunicipal,
            SESMT: this.sesmt,
            CIPA: this.cipa,
            //dtDesativacao: this.sindicato.dataDesativacao ? DatePicker.convertDateForMyDatePicker(this.sindicato.dataDesativacao) : null,
        });

        if (this.isNotEmpty(this.sindicato.endereco)) {
            this.formulario.patchValue({
                endereco: this.sindicato.endereco[0].endereco.descricao,
                complemento: this.sindicato.endereco[0].endereco.complemento,
                municipio: this.sindicato.endereco[0].endereco.municipio.id.toString(),
                numero: this.sindicato.endereco[0].endereco.numero,
                bairro: this.sindicato.endereco[0].endereco.bairro,
                estado: this.sindicato.endereco[0].endereco.municipio.estado.id,
                cep: this.sindicato.endereco[0].endereco.cep,
            });
        }
        if (this.isNotEmpty(this.sindicato.email)) {
            this.formulario.patchValue({
                email: this.sindicato.email[0].email.descricao,
            });
        }

    }

    setMunicipio() {
        if (this.isNotEmpty(this.sindicato.endereco)) {
            if (!this.modoConsulta) {
                this.formulario.controls['municipio'].enable();
            }
            this.pesquisarMunicipiosPorEstado(this.sindicato.endereco[0].endereco.municipio.estado.id);
        }
    }

    public filtrarMunicipios(token: string): Observable<any> {
        const query = new RegExp(token, 'ig');
        return Observable.of(
            this.municipios.filter((municipio: any) => {
                if (token.length > 3) {
                    return query.test(municipio.descricao);
                }
            }),
        );
    }

    existeTelefone() {
        return this.sindicato.telefone && this.sindicato.telefone.length > 0;
    }

    tipoTela() {
        this.modoConsulta = !Seguranca.isPermitido([PermissoesEnum.SINDICATO,
        PermissoesEnum.SINDICATO_CADASTRAR,
        PermissoesEnum.SINDICATO_ALTERAR,
        PermissoesEnum.SINDICATO_DESATIVAR]);
    }

}
