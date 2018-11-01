import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { DatePicker } from './../../../compartilhado/utilitario/date-picker';
import { EmpresaCnae } from './../../../modelo/empresa-cnae';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { Paginacao } from './../../../modelo/paginacao.model';
import { FiltroCnae } from './../../../modelo/filtro-cnae';
import { EmpresaUnidadeAtendimentoTrabalhador } from 'app/modelo/empresa-unidade-atendimento-trabalhador.model';
import { PorteEmpresaService } from './../../../servico/porte-empresa.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ToastyService } from 'ng2-toasty';
import { BaseComponent } from 'app/componente/base.component';
import { ActivatedRoute, Router } from '@angular/router';
import { DialogService } from 'ng2-bootstrap-modal';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { TipoEmpresa } from 'app/modelo/tipo-empresa.model';
import { PorteEmpresa } from 'app/modelo/porte-empresa.model';
import { Empresa } from 'app/modelo/empresa.model';
import { EmpresaService } from 'app/servico/empresa.service';
import { UnidadeObra } from 'app/modelo/unidade-obra.model';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { Email } from 'app/modelo/email.model';
import { EmailEmpresa } from 'app/modelo/email-empresa.model';
import { EnderecoEmpresa } from 'app/modelo/endereco-empresa.model';
import { TelefoneEmpresa } from 'app/modelo/telefone-empresa.model';
import { EstadoService } from 'app/servico/estado.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ValidateCNPJ } from 'app/compartilhado/validators/cnpj.validator';
import { EnumValues } from 'enum-values';
import { ValidateEmail } from 'app/compartilhado/validators/email.validator';
import { ValidarNit } from 'app/compartilhado/validators/nit.validator';
import { Segmento } from 'app/modelo/segmento.model';
import { TipoEmail } from 'app/modelo/enum/enum-tipo-email.model';
import { RamoEmpresa } from 'app/modelo/ramo-empresa.model';
import { RamoEmpresaService } from 'app/servico/ramo-empresa.service';
import { UnidadeObraService } from 'app/servico/unidade-obra.service';
import { ValidarDataFutura, ValidateData } from 'app/compartilhado/validators/data.validator';
import { Cnae } from '../../../modelo/cnae.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import {PerfilEnum} from "../../../modelo/enum/enum-perfil";

@Component({
    selector: 'app-cadastro-empresa',
    templateUrl: './cadastro-empresa.component.html',
    styleUrls: ['./cadastro-empresa.component.scss'],
})

export class CadastroEmpresaComponent extends BaseComponent implements OnInit {

    @ViewChild('modalCnae') modalCnae: NgbModal;
    public empresa: Empresa;
    public id: number;
    public empresaForm: FormGroup;
    public listaTipoEmpresas: TipoEmpresa[];
    public tipoClienteSelecionado: TipoEmpresa;
    public listaPorteEmpresas: PorteEmpresa[];
    public unidadesObras = Array<UnidadeObra>();
    public ramosEmpresa = Array<RamoEmpresa>();
    public segmento: Segmento;
    public filtroCnae = new FiltroCnae();
    public versoes: string[];
    public cnaes = new Array<Cnae>();
    public Selecionadoscnaes = new Array<Cnae>();
    public checkButton = new Array<any>();
    public umCnaeSelecionado = false;
    public cnaePorPagina: Cnae[];
    public itensCarregados: number;
    paginacaoCnae: Paginacao = new Paginacao(1, 3, 5);

    public inconsistencia = false;
    public mensagemInconsistencia: string;

    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private service: EmpresaService,
        private formBuilder: FormBuilder,
        protected bloqueioService: BloqueioService,
        protected estadoService: EstadoService,
        private modalService: NgbModal,
        protected dialogo: ToastyService,
        private dialogService: DialogService,
        private porteEmpresaService: PorteEmpresaService,
        private ramoEmpresaService: RamoEmpresaService,
        private unidadeObraService: UnidadeObraService) {
        super(bloqueioService, dialogo, estadoService);
    }

    ngOnInit() {
        this.empresa = new Empresa();
        this.empresa.segmento = new Segmento();
        this.unidadesObras = new Array<UnidadeObra>();
        this.emModoConsulta();
        this.createForm();
        this.carregarCombos();
        this.carregarTela();
        this.pesquisarTipoCliente();
        this.title = MensagemProperties.app_rst_empresa_title_cadastrar;
    }

    pageChangedCnae(event: any): void {
        this.paginacaoCnae.pagina = event.page;
        this.pesquisar();
    }

    existeCnaes() {
        return this.empresa.empresaCnaes && this.empresa.empresaCnaes.length > 0;
    }

    selecionarCnae(item) {
        let cnaeCadastrado = false;
        this.Selecionadoscnaes.map((a) => {
            if (a === item) {
                cnaeCadastrado = true;
            }
        });
        if (cnaeCadastrado) {
            const index = this.Selecionadoscnaes.indexOf(item);
            this.Selecionadoscnaes.splice(index, 1);
        } else {
            this.Selecionadoscnaes.push(item);
        }
    }

    pesquisarCnaes() {
        this.paginacaoCnae.pagina = 1;
        this.pesquisar();
    }

    private carregarCombos() {
        this.carregarPorteEmpresa();
        this.carregarRamoEmpresa();
        this.carregarVersoes();
    }
    private carregarRamoEmpresa() {
        this.ramoEmpresaService.pesquisarTodos().subscribe((dados: any) => {
            this.ramosEmpresa = dados;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private carregarPorteEmpresa() {
        this.porteEmpresaService.pesquisarTodos().subscribe((retorno: PorteEmpresa[]) => {
            this.listaPorteEmpresas = retorno;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private emModoConsulta() {
        this.modoConsulta = !Seguranca.isPermitido(
            [PermissoesEnum.EMPRESA,
            PermissoesEnum.EMPRESA_CADASTRAR,
            PermissoesEnum.EMPRESA_ALTERAR,
            PermissoesEnum.EMPRESA_DESATIVAR]);
    }

    private carregarVersoes() {
        this.service.pesquisarVersoes().subscribe((retorno) => {
            this.versoes = retorno;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private pesquisar() {
        if (this.filtroCnae.codigo === '' && this.filtroCnae.versao === '' && this.filtroCnae.descricao === '') {
            this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
        } else {
            if (this.empresa.empresaCnaes) {
                this.filtroCnae.ids = this.empresa.empresaCnaes.map((a: EmpresaCnae) => {
                    return a.cnae.id.toString();
                });
            }
            this.service.pesquisarCnaes(this.filtroCnae, this.paginacaoCnae).subscribe((retorno: ListaPaginada<Cnae>) => {
                if (retorno.list != null) {
                    this.cnaes = retorno.list;
                    this.paginacaoCnae = this.getPaginacao(this.paginacaoCnae, retorno);
                } else {
                    this.cnaes = new Array<Cnae>();
                    retorno.list = new Array<Cnae>();
                    retorno.quantidade = retorno.list.length;
                    this.paginacaoCnae = this.getPaginacao(this.paginacaoCnae, retorno);
                    this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
                }
            }, (error) => {
                this.mensagemError(error);
            });
        }
    }

    openModalCnae(index?: any) {
        this.limparCnae();
        this.modalService.open(this.modalCnae, { size: 'lg' }).result
            .then((result) => {
                this.adicionar();
            }, (reason) => {
            });
    }

    adicionar() {
        this.Selecionadoscnaes.map((a) => {
            const empresaCnae = new EmpresaCnae();
            let podeAdicionar = true;
            empresaCnae.empresa = new Empresa();
            empresaCnae.empresa.id = this.empresa.id;
            empresaCnae.cnae = a;
            empresaCnae.principal = false;
            this.empresa.empresaCnaes.forEach((element) => {
                if (element.cnae.id === a.id) {
                    podeAdicionar = false;
                }
            });
            if (podeAdicionar) {
                this.empresa.empresaCnaes.push(empresaCnae);
            }
        });
        this.Selecionadoscnaes = [];

        this.cnaeOrderByDescricao(this.empresa.empresaCnaes);
        this.chekcUmCnaeSelecionado();
    }

    removeCnae(item) {
        const index = this.empresa.empresaCnaes.indexOf(item);
        this.empresa.empresaCnaes.splice(index, 1);
        this.chekcUmCnaeSelecionado();
    }

    chekcUmCnaeSelecionado() {
        if (this.empresa.empresaCnaes) {
            if (this.empresa.empresaCnaes.length === 1) {
                this.umCnaeSelecionado = true;
                this.empresa.empresaCnaes[0].principal = true;
            }
        }
    }

    principal(item) {
        this.empresa.empresaCnaes.map((a) => {
            if (a === item) {
                a.principal = true;
            } else {
                a.principal = false;
            }
        });
    }

    limparCnae() {
        this.filtroCnae = new FiltroCnae();
        this.cnaePorPagina = [];
        this.cnaes = [];
    }

    private carregarTela() {
        this.id = this.activatedRoute.snapshot.params['id'];
        if (this.id) {
            this.modoAlterar = true;
            this.buscar(this.id);
        }
    }

    pesquisarTipoCliente() {
        this.service.pesquisarTipoCliente().subscribe((retorno: TipoEmpresa[]) => {
            this.listaTipoEmpresas = retorno;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    buscar(id: number) {
        this.service.pesquisarPorId(id).subscribe((retorno: Empresa) => {
            this.empresa = retorno;
            this.orderByDescricao(this.empresa.unidadeObra);
            this.cnaeOrderByDescricao(this.empresa.empresaCnaes);
            this.converterModelParaForm();
            this.inicializarListas();
            this.verificarIncosistencia();
            this.inserirMascaraTelefones();
        }, (error) => {
            this.mensagemError(error);
        }, () => {
        });
    }

    inserirMascaraTelefones() {
        if (this.empresaForm.controls['telefoneResp'].value) {
            if (this.empresaForm.controls['telefoneResp'].value.length === 10) {
                this.mascaraTelFixoResponsavel(this.empresaForm.controls['telefoneResp'].value);
            } else {
                this.mascaraCelularResponsavel(this.empresaForm.controls['telefoneResp'].value);
            }
        }

        if (this.empresaForm.controls['numeroTelefoneContato'].value) {
            if (this.empresaForm.controls['numeroTelefoneContato'].value.length === 10) {
                this.mascaraTelFixoContato(this.empresaForm.controls['numeroTelefoneContato'].value);
            } else {
                this.mascaraCelularContato(this.empresaForm.controls['numeroTelefoneContato'].value);
            }
        }
    }

    verificarIncosistencia() {
        this.inconsistencia = false;
        if (!this.modoConsulta && this.empresa) {
            let unidadeObra = '';
            let cnae = '';
            let ramoEmpresa = '';
            let segmento = '';
            if (this.listaUndefinedOuVazia(this.empresa.empresaCnaes)) {
                this.inconsistencia = true;
                cnae = '\"' + MensagemProperties.app_rst_labels_cnae_ + '\"';
            }

            if (!this.empresa.ramoEmpresa) {
                if (this.listaUndefinedOuVazia(this.empresa.empresaCnaes)) {
                    ramoEmpresa = ', ';
                }
                this.inconsistencia = true;
                ramoEmpresa = ramoEmpresa + '\"' + MensagemProperties.app_rst_labels_ramo + '\"';
            }
            if (!this.existeSegmento()) {
                if (!this.empresa.ramoEmpresa || this.listaUndefinedOuVazia(this.empresa.empresaCnaes)) {
                    segmento = ', ';
                }

                this.inconsistencia = true;
                segmento = segmento + '\"' + MensagemProperties.app_rst_labels_segmento + '\"';
            }

            if (this.empresa.unidadeObra.length === 0) {
                if (!this.existeSegmento() || !this.empresa.ramoEmpresa || this.listaUndefinedOuVazia(this.empresa.empresaCnaes)) {
                    unidadeObra = ', ';
                }
                this.inconsistencia = true;
                unidadeObra = unidadeObra + '\"' + MensagemProperties.app_rst_labels_unidade_nome_obra + '\"';
            }

            if (this.inconsistencia) {
                this.mensagemInconsistencia = this.recursoPipe.transform('app_rst_mensagem_inconsistencia',
                    cnae, ramoEmpresa, segmento, unidadeObra);
            }
        }
    }

    inicializarListas() {

        if (!this.empresa.telefoneEmpresa) {
            this.empresa.telefoneEmpresa = new Array<TelefoneEmpresa>();
        }
        if (!this.empresa.enderecosEmpresa) {
            this.empresa.enderecosEmpresa = new Array<EnderecoEmpresa>();
        }
        if (!this.empresa.empresaUats) {
            this.empresa.empresaUats = new Array<EmpresaUnidadeAtendimentoTrabalhador>();
        }
        if (!this.empresa.empresaCnaes) {
            this.empresa.empresaCnaes = new Array<EmpresaCnae>();
        }

        if (!this.empresa.unidadeObra) {
            this.empresa.unidadeObra = new Array<UnidadeObra>();
        }

    }

    createForm() {
        this.empresaForm = this.formBuilder.group({
            cnpj: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.required, ValidateCNPJ,
                ]),
            ],
            razaoSocial: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(160),
                ]),
            ],
            nomeFantasia: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(160),
                ]),
            ],
            endereco: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.maxLength(100),
                ]),
            ],
            unidadeNomeObra: [
                { value: undefined, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.maxLength(100),
                ]),
            ],
            ramoEmpresa: [
                { value: undefined, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.maxLength(100),
                ]),
            ],
            municipio: [
                { value: null, disabled: true || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(300),
                ]),
            ],
            bairro: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.maxLength(50),
                ]),
            ],
            cep: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.maxLength(8),
                ]),
            ],
            telefone: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(50),
                ]),
            ],
            nomeResponsavel: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(160),
                ]),
            ],
            depRegional: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.required,
                ]),
            ],
            estado: [
                { value: 0, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(300),
                ]),
            ],
            emailEmpresa: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(100),
                    ValidateEmail,
                ]),
            ],
            cargoResponsavel: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(60),
                ]),
            ],
            telefoneResp: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(50),
                ]),
            ],
            numeroNitResponsavel: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(25),
                    ValidarNit,
                ]),
            ],
            tipoEmpresa: [
                { value: undefined, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.required,
                ]),
            ],
            inscricaoEstadual: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(20),
                ]),
            ],
            sesmt: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.maxLength(1),
                ]),
            ],
            cipa: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.maxLength(1),
                ]),
            ],
            matriz: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.maxLength(1),
                ]),
            ],
            qtdMembrosCipa: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.required,
                ]),
            ],
            porteEmpresa: [
                { value: undefined, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.required,
                ]),
            ],
            emailResp: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(100),
                    ValidateEmail,
                ]),
            ],
            inscricaoMunicipal: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.maxLength(20),
                ]),
            ],
            designCipa: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.maxLength(1),
                ]),
            ],
            url: [
                { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
                Validators.compose([
                    Validators.maxLength(70),
                ]),
            ],
            dtdesligamento: [
                { value: null, disabled: this.modoConsulta || !this.hasPermissaoDesativar() || !this.isPermitidoEditar() },
                Validators.compose([
                    ValidateData,
                ]),
            ],
            nomeContato: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(60),
                ]),
            ],
            numeroTelefoneContato: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(50),
                ]),
            ],
            numeroNitContato: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(25),
                    ValidarNit,
                ]),
            ],
            descricaCargoContato: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(60),
                ]),
            ],
            emailContato: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(100),
                    ValidateEmail,
                ]),
            ],
        });
    }

    voltar(): void {
        if (this.empresa.id) {
            if (this.activatedRoute.snapshot.url[0].path === 'minhaempresa') {
                this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa`]);
            } else {
                this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.empresa.id}`]);
            }
        } else {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa`]);
        }
    }

    salvar() {
        if (this.verificarCampos()) {
            this.prepareSave();
            this.removerMascaras();
            this.service.salvar(this.empresa).subscribe((response: Empresa) => {
                this.empresa = response;
                this.orderByDescricao(this.empresa.unidadeObra);
                this.cnaeOrderByDescricao(this.empresa.empresaCnaes);
                this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
                this.verificarIncosistencia();
            }, (error) => {
                this.mensagemError(error);
            }, () => {
                this.inicializarListas();
                this.voltar();
            });
        }
    }

    mudarMascaraTelContato(event: any) {
        if (this.empresaForm.controls['numeroTelefoneContato'].value) {
            const valor = this.empresaForm.controls['numeroTelefoneContato'].value;
            if (valor.length <= 14) {
                this.mascaraTelFixoContato(valor);
            } else {
                this.mascaraCelularContato(valor);
            }
        }
    }

    mascaraTelFixoContato(valor: any) {
        valor = MascaraUtil.removerMascara(valor);
        valor = valor.replace(/\W/g, '');
        valor = valor.replace(/^(\d{2})(\d)/, '($1) $2');
        valor = valor.replace(/(\d{4})(\d)/, '$1-$2');
        valor = valor.replace(/(\d{4})$/, '$1');
        this.empresaForm.patchValue({
            numeroTelefoneContato: valor,
        });
    }

    mascaraCelularContato(valor: any) {
        valor = MascaraUtil.removerMascara(valor);
        valor = valor.replace(/\W/g, '');
        valor = valor.replace(/^(\d{2})(\d)/, '($1) $2');
        valor = valor.replace(/(\d{5})(\d)/, '$1-$2');
        valor = valor.replace(/(\d{4})$/, '$1');
        this.empresaForm.patchValue({
            numeroTelefoneContato: valor,
        });
    }

    mudarMascaraTelResponsavel(event: any) {
        if (this.empresaForm.controls['telefoneResp'].value) {
            const valor = this.empresaForm.controls['telefoneResp'].value;
            if (valor.length <= 14) {
                this.mascaraTelFixoResponsavel(valor);
            } else {
                this.mascaraCelularResponsavel(valor);
            }
        }
    }

    mascaraTelFixoResponsavel(valor: any) {
        valor = MascaraUtil.removerMascara(valor);
        valor = valor.replace(/\W/g, '');
        valor = valor.replace(/^(\d{2})(\d)/, '($1) $2');
        valor = valor.replace(/(\d{4})(\d)/, '$1-$2');
        valor = valor.replace(/(\d{4})$/, '$1');
        this.empresaForm.patchValue({
            telefoneResp: valor,
        });
    }

    mascaraCelularResponsavel(valor: any) {
        valor = MascaraUtil.removerMascara(valor);
        valor = valor.replace(/\W/g, '');
        valor = valor.replace(/^(\d{2})(\d)/, '($1) $2');
        valor = valor.replace(/(\d{5})(\d)/, '$1-$2');
        valor = valor.replace(/(\d{4})$/, '$1');
        this.empresaForm.patchValue({
            telefoneResp: valor,
        });
    }

    prepareSave(): Empresa {
        const formModel = this.empresaForm.controls;
        this.empresa.cnpj = formModel.cnpj.value;
        this.empresa.razaoSocial = formModel.razaoSocial.value;
        this.empresa.nomeFantasia = formModel.nomeFantasia.value;
        this.empresa.numeroInscricaoEstadual = formModel.inscricaoEstadual.value;
        this.empresa.numeroInscricaoMunicipal = formModel.inscricaoMunicipal.value;
        this.empresa.sesmt = formModel.sesmt.value;
        this.empresa.cipa = formModel.cipa.value;
        this.empresa.matriz = formModel.matriz.value;
        const porteEmpresa = new PorteEmpresa();
        porteEmpresa.id = formModel.porteEmpresa.value;
        this.empresa.porteEmpresa = porteEmpresa;
        const tipoEmpresa = new TipoEmpresa();
        if (formModel.tipoEmpresa.value !== 0) {
            tipoEmpresa.id = formModel.tipoEmpresa.value;
        }
        this.empresa.tipoEmpresa = tipoEmpresa;
        this.empresa.url = formModel.url.value;
        this.empresa.dataDesativacao = formModel.dtdesligamento.value ?
            this.convertDateToString(formModel.dtdesligamento.value.date) : null;
        this.empresa.nomeResponsavel = formModel.nomeResponsavel.value;
        this.empresa.numeroNitResponsavel = formModel.numeroNitResponsavel.value;
        if (formModel.telefoneResp) {
            this.empresa.numeroTelefone = MascaraUtil.removerMascara(formModel.telefoneResp.value);
        }
        // contato
        this.empresa.emailResponsavel = this.isVazia(formModel.emailResp.value) ? null : formModel.emailResp.value;
        this.empresa.nomeContato = formModel.nomeContato.value;
        this.empresa.descricaCargoContato = formModel.descricaCargoContato.value;
        this.empresa.numeroTelefoneContato = MascaraUtil.removerMascara(formModel.numeroTelefoneContato.value);
        this.empresa.numeroNitContato = formModel.numeroNitContato.value;
        this.empresa.emailContato = this.isVazia(formModel.emailContato.value) ? null : formModel.emailContato.value;

        if (this.empresa.segmento && this.empresa.segmento.id == null) {
            this.empresa.segmento = null;
        }

        this.empresa.ramoEmpresa = null;
        if (!this.isUndefined(formModel.ramoEmpresa.value)) {
            this.empresa.ramoEmpresa = new RamoEmpresa();
            this.empresa.ramoEmpresa.id = formModel.ramoEmpresa.value;
        }

        // Email
        if (formModel.emailEmpresa.value) {
            if (!this.empresa.emailsEmpresa || this.empresa.emailsEmpresa.length === 0) {
                this.empresa.emailsEmpresa = new Array<EmailEmpresa>();
                this.empresa.emailsEmpresa.push(new EmailEmpresa());
                this.empresa.emailsEmpresa[0].email = new Email();
            }
            this.empresa.emailsEmpresa[0].email.descricao = formModel.emailEmpresa.value;
            this.empresa.emailsEmpresa[0].email.tipo = EnumValues.getNameFromValue(TipoEmail, TipoEmail.P);
            this.empresa.emailsEmpresa[0].email.notificacao = true;
        } else {
            this.empresa.emailsEmpresa = new Array<EmailEmpresa>();
        }

        return this.empresa;
    }

    descricaoSegmento(): string {
        return this.empresa.segmento ? this.empresa.segmento.descricao : '';
    }

    existeSegmento() {
        return this.empresa.segmento && this.empresa.segmento.id !== undefined;
    }

    removeSegmento() {
        this.empresa.segmento = new Segmento();
    }

    verificarCampos(): boolean {
        let retorno = true;
        // cnpj

        if (this.empresaForm.controls['cnpj'].invalid) {

            if (this.empresaForm.controls['cnpj'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.empresaForm.controls['cnpj'],
                    MensagemProperties.app_rst_labels_cnpj);
                retorno = false;
            }

            if (!this.empresaForm.controls['cnpj'].errors.required && this.empresaForm.controls['cnpj'].errors.validCNPJ) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.empresaForm.controls['cnpj'],
                    MensagemProperties.app_rst_labels_cnpj);
                retorno = false;
            }
        }

        // razao social
        if (this.empresaForm.controls['razaoSocial'].invalid) {
            if (this.empresaForm.controls['razaoSocial'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.empresaForm.controls['razaoSocial'],
                    MensagemProperties.app_rst_labels_razao_social);
                retorno = false;
            }
            if (this.empresaForm.controls['razaoSocial'].errors.maxLength) {
                this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
                    this.empresaForm.controls['razaoSocial'], MensagemProperties.app_rst_labels_razao_social,
                    this.empresaForm.controls['razaoSocial'].errors.maxLength.requiredLength);
                retorno = false;
            }
        }

        // nome fantasia
        if (this.empresaForm.controls['nomeFantasia'].invalid) {
            if (this.empresaForm.controls['nomeFantasia'].errors.maxLength) {
                this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
                    this.empresaForm.controls['nomeFantasia'], MensagemProperties.app_rst_labels_nome_fantasia,
                    this.empresaForm.controls['nomeFantasia'].errors.maxLength.requiredLength);
                retorno = false;
            }
        }

        // Cnae
        if (this.empresa.empresaCnaes && this.empresa.empresaCnaes.length !== 0) {
            let TemCnaePricnipal = false;
            this.empresa.empresaCnaes.map((a) => {
                if (a.principal) {
                    TemCnaePricnipal = true;
                }
            });

            if (TemCnaePricnipal) {
                retorno = true;
            } else {
                retorno = false;
                this.mensagemError(MensagemProperties.app_rst_empresa_cnae);
            }

        }

        // porte
        if (this.empresaForm.controls['porteEmpresa'].invalid) {
            this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.empresaForm.controls['porteEmpresa'],
                MensagemProperties.app_rst_labels_porte);
            retorno = false;
        }

        // tipo
        if (this.empresaForm.controls['tipoEmpresa'].invalid) {
            this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.empresaForm.controls['tipoEmpresa'],
                MensagemProperties.app_rst_labels_tipo);
            retorno = false;
        }
        // emailEmpresa
        if (this.empresaForm.controls['emailEmpresa'].value
            && this.empresaForm.controls['emailEmpresa'].invalid) {
            this.mensagemErroComParametros('app_rst_campo_invalido', this.empresaForm.controls['emailEmpresa'],
                MensagemProperties.app_rst_labels_email);
            retorno = false;
        }
        // numeroNitContato
        if (this.empresaForm.controls['numeroNitContato'].value && this.empresaForm.controls['numeroNitContato'].invalid
            && this.empresaForm.controls['numeroNitContato'].errors.validNIT) {
            this.mensagemErroComParametros('app_rst_campo_invalido', this.empresaForm.controls['numeroNitContato'],
                MensagemProperties.app_rst_labels_nit_contato);
            retorno = false;
        }
        // emailContato
        if (this.empresaForm.controls['emailContato'].value
            && this.empresaForm.controls['emailContato'].invalid && this.empresaForm.controls['emailContato'].errors.validEmail) {
            this.mensagemErroComParametros('app_rst_campo_invalido', this.empresaForm.controls['emailContato'],
                MensagemProperties.app_rst_labels_email_contato);
            retorno = false;
        }
        // numeroTelefoneContato
        if (this.empresaForm.controls['numeroTelefoneContato'].value) {
            const valor = this.empresaForm.controls['numeroTelefoneContato'].value;
            if (MascaraUtil.removerMascara(valor).length > 11) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.empresaForm.controls['numeroTelefoneContato'],
                    MensagemProperties.app_rst_labels_telefone);
                retorno = false;
            }
        }
        // telefoneResp
        if (this.empresaForm.controls['telefoneResp'].value) {
            const valor = this.empresaForm.controls['telefoneResp'].value;
            if (MascaraUtil.removerMascara(valor).length > 11) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.empresaForm.controls['telefoneResp'],
                    MensagemProperties.app_rst_labels_telefone);
                retorno = false;
            }
        }
        // emailResponsavel
        if (this.empresaForm.controls['emailResp'].value && this.empresaForm.controls['emailResp'].invalid
            && this.empresaForm.controls['emailResp'].errors.validEmail) {
            this.mensagemErroComParametros('app_rst_campo_invalido', this.empresaForm.controls['emailResp'],
                MensagemProperties.app_rst_labels_email);
            retorno = false;
        }
        // numeroNitResponsavel
        if (this.empresaForm.controls['numeroNitResponsavel'].value && this.empresaForm.controls['numeroNitResponsavel'].invalid
            && this.empresaForm.controls['numeroNitResponsavel'].errors.validNIT) {
            this.mensagemErroComParametros('app_rst_campo_invalido', this.empresaForm.controls['numeroNitResponsavel'],
                MensagemProperties.app_rst_labels_nit);
            retorno = false;
        }

        // validar data desativacao
        if (!this.isVazia(this.empresaForm.controls['dtdesligamento'].value)) {
            if (this.empresaForm.controls['dtdesligamento'].errors && this.empresaForm.controls['dtdesligamento'].errors.validData) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.empresaForm.controls['dtdesligamento'],
                    MensagemProperties.app_rst_labels_data_desativacao);
                retorno = false;
            } else if (ValidarDataFutura(this.empresaForm.controls['dtdesligamento'].value.jsdate)) {
                this.mensagemErroComParametrosModel('app_rst_labels_data_futura', MensagemProperties.app_rst_labels_data_desativacao);
                retorno = false;
            }
        }

        return retorno;
    }

    removerMascaras() {
        if (this.empresa.cnpj) {
            this.empresa.cnpj = MascaraUtil.removerMascara(this.empresa.cnpj);
        }
        if (this.empresa.numeroNitResponsavel) {
            this.empresa.numeroNitResponsavel = MascaraUtil.removerMascara(this.empresa.numeroNitResponsavel);
        }
        if (this.empresa.numeroNitContato) {
            this.empresa.numeroNitContato = MascaraUtil.removerMascara(this.empresa.numeroNitContato);
        }
        if (this.empresa.telefoneEmpresa) {
            this.empresa.telefoneEmpresa.forEach((tel) => {
                tel.telefone.numero = MascaraUtil.removerMascara(tel.telefone.numero);
            });
        }
    }

    converterModelParaForm() {
        this.empresaForm.patchValue({
            cnpj: this.empresa.cnpj,
            razaoSocial: this.empresa.razaoSocial,
            nomeFantasia: this.empresa.nomeFantasia,
            cargoResponsavel: this.empresa.cargoResp,
            nomeResponsavel: this.empresa.nomeResponsavel,
            dataExclusao: this.empresa.dataExclusao,
            cargoResp: this.empresa.cargoResp,
            telefoneResp: this.empresa.numeroTelefone,
            numeroNitResponsavel: this.empresa.numeroNitResponsavel,
            nomeContato: this.empresa.nomeContato,
            descricaCargoContato: this.empresa.descricaCargoContato,
            numeroTelefoneContato: this.empresa.numeroTelefoneContato,
            numeroNitContato: this.empresa.numeroNitContato,
            emailContato: this.empresa.emailContato,
            dtdesligamento: this.empresa.dataDesativacao ? DatePicker.convertDateForMyDatePicker(this.empresa.dataDesativacao) : null,
            sesmt: this.empresa.sesmt,
            cipa: this.empresa.cipa,
            matriz: this.empresa.matriz,
            qtdMembrosCipa: this.empresa.qtdMembrosCipa,
            emailResp: this.empresa.emailResponsavel,
            inscricaoEstadual: this.empresa.numeroInscricaoEstadual,
            inscricaoMunicipal: this.empresa.numeroInscricaoMunicipal,
            designCipa: this.empresa.designCipa,
            tipoEmpresa: this.empresa.tipoEmpresa ? this.empresa.tipoEmpresa.id : undefined,
            porteEmpresa: this.empresa.porteEmpresa ? this.empresa.porteEmpresa.id : undefined,
            ramoEmpresa: this.empresa.ramoEmpresa ? this.empresa.ramoEmpresa.id : undefined,
            url: this.empresa.url,

        });
        if (!this.empresa.segmento) {
            this.empresa.segmento = new Segmento();
        }

        if (this.empresa.emailsEmpresa) {
            this.empresaForm.patchValue({
                emailEmpresa: this.empresa.emailsEmpresa[0] ? this.empresa.emailsEmpresa[0].email.descricao : '',
            });
        }
    }

    existeTelefone() {
        return this.empresa.telefoneEmpresa && this.empresa.telefoneEmpresa.length > 0;
    }
    existeEndereco() {
        return this.empresa.enderecosEmpresa && this.empresa.enderecosEmpresa.length > 0;
    }
    existeUat() {
        return this.empresa.empresaUats && this.empresa.empresaUats.length > 0;
    }

    cnaeOrderByDescricao(list: any) {
        if (!this.listaUndefinedOuVazia(list)) {
            list.sort((left, right): number => {
                if (left.cnae.descricao > right.cnae.descricao) {
                    return 1;
                }
                if (left.cnae.descricao < right.cnae.descricao) {
                    return -1;
                }
                return 0;
            });
        }
    }

    hasPermissaoDesativar() {
        return this.hasPermissao(PermissoesEnum.EMPRESA_DESATIVAR)
            || this.hasPermissao(PermissoesEnum.EMPRESA);
    }

    isPermitidoEditar(): boolean {
        return this.naoTemPapel(PerfilEnum.GEEM,PerfilEnum.GEEMM) || this.temPapel(PerfilEnum.ADM);
    }

    showAppSegmentoModal(): boolean {
        return !this.existeSegmento() && this.isPermitidoEditar();
    }

    showButtonSegmento(): boolean {
        return this.existeSegmento() && !this.modoConsulta && this.isPermitidoEditar();
    }
}
