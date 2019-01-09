import {PerfilEnum} from './../../../modelo/enum/enum-perfil';
import {FiltroTrabalhador} from './../../../modelo/filtro-trabalhador.model';
import {PermissoesEnum} from './../../../modelo/enum/enum-permissoes';
import {Seguranca} from './../../../compartilhado/utilitario/seguranca.model';
import {environment} from './../../../../environments/environment';
import {DatePicker} from './../../../compartilhado/utilitario/date-picker';
import {PaisService} from './../../../servico/pais.service';
import {Nacionalidade} from 'app/modelo/enum/enum-nacionalidade.model';
import {SituacaoTrabalhador} from 'app/modelo/enum/enum-situacao-trabalhador.model';
import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastyService} from 'ng2-toasty';
import {DialogService} from 'ng2-bootstrap-modal';
import {BaseComponent} from 'app/componente/base.component';
import {MensagemProperties} from 'app/compartilhado/utilitario/recurso.pipe';
import {BloqueioService} from 'app/servico/bloqueio.service';

import {Trabalhador} from './../../../modelo/trabalhador.model';
import {TrabalhadorService} from './../../../servico/trabalhador.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Municipio} from 'app/modelo/municipio.model';
import {EstadoService} from 'app/servico/estado.service';
import {EnderecoTrabalhador} from 'app/modelo/endereco-trabalhador.model';
import {Dependente} from 'app/modelo/dependente.model';
import {TelefoneTrabalhador} from 'app/modelo/telefone-trabalhador.model';
import {EmailTrabalhador} from 'app/modelo/email-trabalhador.model';
import {MascaraUtil} from 'app/compartilhado/utilitario/mascara.util';

import {TipoEndereco} from 'app/modelo/enum/enum-tipo-endereco.model';
import {ValidateCPF} from 'app/compartilhado/validators/cpf.validator';
import {ValidarNit} from 'app/compartilhado/validators/nit.validator';
import {Raca} from 'app/modelo/enum/enum-raca.model';
import {BrPdh} from 'app/modelo/enum/enum-br-pdh.model';
import {Escolaridade} from 'app/modelo/enum/enum-escolaridade.model';
import {FaixaSalarial} from 'app/modelo/enum/enum-faixa-salarial.model';
import {Genero} from 'app/modelo/enum/enum-genero.model';
import {EstadoCivil} from 'app/modelo/enum/enum-estado-civil.model';
import {EnumValues} from 'enum-values';
import {TipoDependente} from 'app/modelo/enum/enum-tipo-dependente.model';
import {ValidateData, ValidateDataFutura} from 'app/compartilhado/validators/data.validator';
import {Profissao} from 'app/modelo/profissao.model';
import {ProfissaoService} from 'app/servico/profissao.service';
import {TipoSanguineo} from 'app/modelo/enum/enum-tipo-sanguineo.model';
import {Pais} from 'app/modelo/pais.model';
import {IOption} from 'ng-select';
import {CropperSettings, ImageCropperComponent} from 'ng2-img-cropper';

@Component({
    selector: 'app-cadastro-trabalhador',
    templateUrl: './cadastro-trabalhador.component.html',
    styleUrls: ['./cadastro-trabalhador.component.scss'],
})
export class CadastroTrabalhadorComponent extends BaseComponent implements OnInit {

    public trabalhador: Trabalhador;
    public trabalhadorForm: FormGroup;
    public id: string;

    public email: EmailTrabalhador;
    public dependente: Dependente;
    public listImagem: ByteString[];
    public estados: any[];
    public municipiosNacionalidade: IOption[] = [];
    public paises: Pais[];
    public profissoes: Profissao[];

    public brpdh = BrPdh;
    public keysBrpdh: string[];
    public escolaridade = Escolaridade;
    public keysEscolaridade: string[];
    public faixaSalarial = FaixaSalarial;
    public keysFaixaSalarial: string[];
    public genero = Genero;
    public raca = Raca;
    public keysRaca: string[];
    public estadoCivil = EstadoCivil;
    public keysEstadoCivil: string[];
    public tipoDependente = TipoDependente;
    public keysTiposDependente: string[];
    public tipoSanguineo = TipoSanguineo;
    public keystipoSanguineo: string[];
    public situacaoTrabalhador = SituacaoTrabalhador;
    public keysSituacaoTrabalhador: string[];
    public nacionalidade = Nacionalidade;
    public keysNacionalidade: string[];
    public nacional: String = '';
    public vidaAtiva: string;
    public temvidaAtiva: boolean;

    foto: any;
    cropperSettings: CropperSettings;
    @ViewChild('cropper')
    cropper: ImageCropperComponent;

    temImagem = false;

    @ViewChild('upload') upload;

    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private service: TrabalhadorService,
        private formBuilder: FormBuilder,
        protected estadoService: EstadoService,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        protected profissaoService: ProfissaoService,
        protected paisService: PaisService,
        private modalService: NgbModal,
        private dialogService: DialogService) {
        super(bloqueioService, dialogo, estadoService);
    }

    ngOnInit() {
        this.trabalhador = new Trabalhador();
        this.email = new EmailTrabalhador();
        this.dependente = new Dependente();
        this.municipiosNacionalidade = [];
        this.listImagem = new Array<ByteString>();
        this.emModoConsulta();
        this.createForm();
        this.carregarCombos();
        this.carregarTela();
        this.title = MensagemProperties.app_rst_trabalhador_title_cadastrar;
        this.inicializarImagem();

    }

    getVidaAtiva(id: string) {
        this.service.buscarVidaAtivaTrabalhador(id).subscribe((dados: string) => {
            if (dados) {
                this.vidaAtiva = dados + " Vida Ativa";
                this.temvidaAtiva = true;
            } else {
                this.vidaAtiva = "Sem Vida Ativa";
                this.temvidaAtiva = false;
            }
        }, (error) => {
            this.mensagemError(error);
            this.vidaAtiva = "Sem Vida Ativa";
            this.temvidaAtiva = false;
        });
    }

    private temEmpPerfil(): boolean {
        const isPermitido = this.usuarioLogado.papeis.find((element) =>
            element === PerfilEnum.ADM
            || element === PerfilEnum.DIDN
            || element === PerfilEnum.DIDR) != null;
        return isPermitido;

        // const isPerfil =  this.usuarioLogado.perfisSistema.find((element) =>
        // ((element.perfil.codigo === PerfilEnum.ADM
        //     || element.perfil.codigo === PerfilEnum.DIDN
        //     || element.perfil.codigo === PerfilEnum.DIDR)
        //     && isPermitido)) != null;
        // return isPerfil && isPermitido;
    }

    inicializarImagem() {
        this.cropperSettings = new CropperSettings();
        this.cropperSettings.width = 50;
        this.cropperSettings.height = 50;
        this.cropperSettings.croppedWidth = 200;
        this.cropperSettings.croppedHeight = 200;
        this.cropperSettings.canvasWidth = 400;
        this.cropperSettings.canvasHeight = 300;
        this.cropperSettings.dynamicSizing = true;
        this.foto = {};
    }

    desabilitarSituacao() {
        if (!this.isSomenteTrabalhador()) {
            if (this.trabalhador.dataFalecimento || this.modoConsulta) {
                this.trabalhadorForm.controls['situacaoTrabalhador'].disable();
            } else {
                this.trabalhadorForm.controls['situacaoTrabalhador'].enable();
            }
        }
    }

    private carregarCombos() {
        this.buscarEstados();
        this.buscarPaises();
        this.buscarProfissoes();
        this.keysEscolaridade = Object.keys(this.escolaridade);
        this.keysFaixaSalarial = Object.keys(this.faixaSalarial);
        this.keysRaca = Object.keys(this.raca);
        this.keysEstadoCivil = Object.keys(this.estadoCivil);
        this.keysTiposDependente = Object.keys(this.tipoDependente);
        this.keystipoSanguineo = Object.keys(this.tipoSanguineo);
        this.keysSituacaoTrabalhador = Object.keys(this.situacaoTrabalhador);
        this.keysNacionalidade = Object.keys(this.nacionalidade);
    }

    carregarTela() {
        this.id = this.activatedRoute.snapshot.params['id'];
        if (this.id) {
            this.modoAlterar = true;
            this.buscarTrabalhador();
        } else if (this.isProducao()) {
            this.router.navigate(['/acessonegado']);
        }
    }

    buscarProfissoes() {
        this.profissaoService.pesquisarProfissoes().subscribe((dados: any) => {
            this.profissoes = dados;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    buscarEstados() {
        this.estadoService.buscarEstados().subscribe((dados: any) => {
            this.estados = dados;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    buscarPaises() {
        this.paisService.buscarPaises().subscribe((dados: any) => {
            this.paises = dados;
            this.paises.forEach((item) => {
                if (item.descricao.toUpperCase() === MensagemProperties.app_rst_labels_brasil_up) {
                    const index: number = this.paises.indexOf(item);
                    if (index !== -1) {
                        this.paises.splice(index, 1);
                    }
                }
            });
        }, (error) => {
            this.mensagemError(error);
        });
    }

    buscarMunicipio() {

        if (this.trabalhadorForm.controls['estado'].value && this.trabalhadorForm.controls['estado'].value !== 0 && !this.modoConsulta) {
            this.trabalhadorForm.controls['municipio'].enable();
            this.pesquisarMunicipiosPorEstado(this.trabalhadorForm.controls['estado'].value);
        } else {
            this.trabalhadorForm.controls['municipio'].disable();
        }

        if (this.modoConsulta || this.isSomenteTrabalhador()) {
            this.trabalhadorForm.controls['municipio'].disable();
        }
    }

    buscarMunicipioNacionalidade() {
        if (this.trabalhadorForm.controls['estadoNacionalidade'].value
            && !this.isUndefined(this.trabalhadorForm.controls['estadoNacionalidade'].value)) {
            this.trabalhadorForm.controls['municipioNacionalidade'].enable();
            this.pesquisarMunicipiosNacionalidadePorEstado(this.trabalhadorForm.controls['estadoNacionalidade'].value);
        } else {
            this.trabalhadorForm.controls['municipioNacionalidade'].disable();
        }

        if (this.modoConsulta || this.isSomenteTrabalhador()) {
            this.trabalhadorForm.controls['municipioNacionalidade'].disable();
        }
    }

    pesquisarMunicipiosNacionalidadePorEstado(idEstado: number) {
        this.estadoService.pesquisarMunicipiosPorEstado(idEstado).subscribe((dados: any) => {
            let lista: IOption[];
            lista = [];
            dados.forEach((element) => {
                const item = new Option();
                item.value = element.id;
                item.label = element.descricao;
                lista.push(item);
            });
            this.municipiosNacionalidade = lista;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    estadoAlterado() {
        this.buscarMunicipio();
    }

    estadoNacionalidadelterado() {
        this.trabalhadorForm.patchValue({
            municipioNacionalidade: null,
        });
        this.buscarMunicipioNacionalidade();
    }

    converterModelParaForm() {
        this.trabalhadorForm.patchValue({
            nome: this.trabalhador.nome,
            dataNascimento: this.trabalhador.dataNascimento ?
                DatePicker.convertDateForMyDatePicker(this.trabalhador.dataNascimento) : null,
            dataFalecimento: this.trabalhador.dataFalecimento ?
                DatePicker.convertDateForMyDatePicker(this.trabalhador.dataFalecimento) : null,
            cpf: this.trabalhador.cpf,
            rg: this.trabalhador.rg,
            orgaoRg: this.trabalhador.orgaoRg,
            nit: this.getNitFormatado(this.trabalhador.nit),
            ctps: this.trabalhador.ctps,
            serieCtps: this.trabalhador.serieCtps,
            ufCtps: this.trabalhador.ufCtps,
            escolaridade: this.trabalhador.escolaridade,
            faixaSalarial: this.trabalhador.faixaSalarial,
            raca: this.trabalhador.raca,
            genero: Genero[this.trabalhador.genero],
            estadoCivil: this.trabalhador.estadoCivil,
            nomeMae: this.trabalhador.nomeMae,
            nomePai: this.trabalhador.nomePai,
            tipoSanguineo: this.trabalhador.tipoSanguineo,
            situacaoTrabalhador: this.trabalhador.situacaoTrabalhador,
            planoSaude: this.trabalhador.planoSaude,
            atividadeFisica: this.trabalhador.atividadeFisica,
            exameRegular: this.trabalhador.exameRegular,
            possuiAutomovel: this.trabalhador.automovel,
            notificacao: this.trabalhador.notificacao,
            nacionalidade: this.trabalhador.nacionalidade,
            descricaoAlergias: this.trabalhador.descricaoAlergias,
            descricaoVacinas: this.trabalhador.descricaoVacinas,
            descricaoMedicamentos: this.trabalhador.descricaoMedicamentos,
            nomeSocial: this.trabalhador.nomeSocial,
        });

        this.trabalhadorForm.patchValue({
            nacionalidade: this.trabalhador.nacionalidade,
            dataNaturalizacao: this.trabalhador.dataNaturalizacao ?
                DatePicker.convertDateForMyDatePicker(this.trabalhador.dataNaturalizacao) : null,
            dataEntradaPais: this.trabalhador.dataEntradaPais ?
                DatePicker.convertDateForMyDatePicker(this.trabalhador.dataEntradaPais) : null,
        });
        if (this.trabalhador.pais) {
            this.trabalhadorForm.patchValue({
                pais: this.trabalhador.pais.id,
            });
        }
        if (this.trabalhador.municipio) {
            this.trabalhadorForm.patchValue({
                estadoNacionalidade: this.trabalhador.municipio.estado.id,
                municipioNacionalidade: this.trabalhador.municipio.id.toString(),
            });
        }

        if (this.trabalhador.profissao) {
            this.trabalhadorForm.patchValue({
                profissao: this.trabalhador.profissao.id,
            });
        }

        if (this.trabalhador.listaEnderecoTrabalhador) {
            this.trabalhadorForm.patchValue({
                endereco: this.trabalhador.listaEnderecoTrabalhador[0].endereco.descricao,
                complemento: this.trabalhador.listaEnderecoTrabalhador[0].endereco.complemento,
                numero: this.trabalhador.listaEnderecoTrabalhador[0].endereco.numero,
                bairro: this.trabalhador.listaEnderecoTrabalhador[0].endereco.bairro,
                cep: this.trabalhador.listaEnderecoTrabalhador[0].endereco.cep,
                municipio: this.trabalhador.listaEnderecoTrabalhador[0].endereco.municipio.id.toString(),
                estado: this.trabalhador.listaEnderecoTrabalhador[0].endereco.municipio.estado.id,
            });
        }

        if (!this.trabalhador.listaTelefoneTrabalhador) {
            this.trabalhador.listaTelefoneTrabalhador = new Array<TelefoneTrabalhador>();
        }

        if (!this.trabalhador.listaEmailTrabalhador) {
            this.trabalhador.listaEmailTrabalhador = new Array<EmailTrabalhador>();
        }
        this.buscarMunicipio();
        this.buscarMunicipioNacionalidade();
        this.alterarNacionalidade();
        this.desabilitarSituacao();
        // imagem
        if (this.trabalhador.imagem) {
            this.temImagem = true;
            this.foto.image = this.trabalhador.tipoImagem ?
                this.trabalhador.tipoImagem.concat(this.trabalhador.imagem) : this.trabalhador.imagem;
        }
    }

    estadoSelecionado(): void {
        return this.trabalhadorForm && this.trabalhadorForm.controls['estado'].value;
    }

    estadoNacionalidadeSelecionado(): void {
        return this.trabalhadorForm && this.trabalhadorForm.controls['estadoNacionalidade'].value;
    }

    validarCampos(): Boolean {
        let isValido = true;
        if (this.trabalhadorForm.controls['nome'].invalid) {
            if (this.trabalhadorForm.controls['nome'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.trabalhadorForm.controls['nome'],
                    MensagemProperties.app_rst_labels_nome);
                isValido = false;
            }

        }

        if (this.trabalhadorForm.controls['estadoCivil'].invalid) {

            if (this.trabalhadorForm.controls['estadoCivil'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.trabalhadorForm.controls['estadoCivil'],
                    MensagemProperties.app_rst_labels_estadoCivil);
                isValido = false;
            }

        }

        if (this.trabalhadorForm.controls['cpf'].invalid) {

            if (this.trabalhadorForm.controls['cpf'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.trabalhadorForm.controls['cpf'],
                    MensagemProperties.app_rst_labels_cpf);
                isValido = false;
            }

            if (!this.trabalhadorForm.controls['cpf'].errors.required && this.trabalhadorForm.controls['cpf'].errors.validCPF) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.trabalhadorForm.controls['cpf'],
                    MensagemProperties.app_rst_labels_cpf);
                isValido = false;
            }

        }

        if (this.trabalhadorForm.controls['dataNascimento'].invalid) {
            if (this.trabalhadorForm.controls['dataNascimento'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.trabalhadorForm.controls['dataNascimento'],
                    MensagemProperties.app_rst_labels_data_nascimento);
                isValido = false;

            } else if (this.trabalhadorForm.controls['dataNascimento'].errors.validData) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.trabalhadorForm.controls['dataNascimento'],
                    MensagemProperties.app_rst_labels_data_nascimento);
                isValido = false;
            } else if (this.trabalhadorForm.controls['dataNascimento'].errors.validDataFutura) {
                this.mensagemErroComParametros('app_rst_labels_data_futura', this.trabalhadorForm.controls['dataNascimento'],
                    MensagemProperties.app_rst_labels_data_nascimento);
                isValido = false;
            }
        }

        if (this.trabalhadorForm.controls['dataFalecimento'].value) {
            if (this.trabalhadorForm.controls['dataFalecimento'].invalid) {
                if (this.trabalhadorForm.controls['dataFalecimento'].errors.validData) {
                    this.mensagemErroComParametros('app_rst_campo_invalido', this.trabalhadorForm.controls['dataFalecimento'],
                        MensagemProperties.app_rst_labels_data_falecimento);
                    isValido = false;
                } else if (this.trabalhadorForm.controls['dataFalecimento'].errors.validDataFutura) {
                    this.mensagemErroComParametros('app_rst_labels_data_futura', this.trabalhadorForm.controls['dataFalecimento'],
                        MensagemProperties.app_rst_labels_data_falecimento);
                    isValido = false;
                }
            } else {
                if (this.trabalhadorForm.controls['dataFalecimento'].value) {
                    if (!this.validarDataFalecimento(this.trabalhadorForm.controls['dataNascimento'].value,
                        this.trabalhadorForm.controls['dataFalecimento'].value)) {
                        this.mensagemErroComParametrosModel('app_rst_trabalhador_dataFalecimento_maior_que_dataNascimento');
                        isValido = false;
                    }
                }
            }
        }

        if (this.trabalhadorForm.controls['dataNaturalizacao'].value
            && this.trabalhadorForm.controls['dataNaturalizacao'].invalid) {
            if (this.trabalhadorForm.controls['dataNaturalizacao'].errors.validData) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.trabalhadorForm.controls['dataNaturalizacao'],
                    MensagemProperties.app_rst_labels_data_naturalizacao);
                isValido = false;
            }

            if (this.trabalhadorForm.controls['dataNaturalizacao'].errors.validDataFutura) {
                this.mensagemErroComParametros('app_rst_labels_data_futura', this.trabalhadorForm.controls['dataNaturalizacao'],
                    MensagemProperties.app_rst_labels_data_naturalizacao);
                isValido = false;
            }
        }

        if (this.trabalhadorForm.controls['dataEntradaPais'].value
            && this.trabalhadorForm.controls['dataEntradaPais'].invalid) {
            if (this.trabalhadorForm.controls['dataEntradaPais'].errors.validData) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.trabalhadorForm.controls['dataEntradaPais'],
                    MensagemProperties.app_rst_labels_data_entrada_pais);
                isValido = false;
            }

            if (this.trabalhadorForm.controls['dataEntradaPais'].errors.validDataFutura) {
                this.mensagemErroComParametros('app_rst_labels_data_futura', this.trabalhadorForm.controls['dataEntradaPais'],
                    MensagemProperties.app_rst_labels_data_entrada_pais);
                isValido = false;
            }
        }

        if (this.trabalhadorForm.controls['nit'].invalid) {

            if (this.trabalhadorForm.controls['nit'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.trabalhadorForm.controls['nit'],
                    MensagemProperties.app_rst_labels_nit);
                isValido = false;
            }

            if (!this.trabalhadorForm.controls['nit'].errors.required && this.trabalhadorForm.controls['nit'].errors.validNIT) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.trabalhadorForm.controls['nit'],
                    MensagemProperties.app_rst_labels_nit);
                isValido = false;
            }

        }

        if (this.trabalhadorForm.controls['genero'].invalid) {

            if (this.trabalhadorForm.controls['genero'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.trabalhadorForm.controls['genero'],
                    MensagemProperties.app_rst_labels_sexo);
                isValido = false;
            }

        }

        if (this.verificarSeEnderecoEstaPreenchido()) {
            if (!this.trabalhadorForm.controls['endereco'].value) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio',
                    this.trabalhadorForm.controls['endereco'],
                    MensagemProperties.app_rst_labels_endereco);
                isValido = false;
            }

            if (!this.trabalhadorForm.controls['cep'].value) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.trabalhadorForm.controls['cep'],
                    MensagemProperties.app_rst_labels_CEP);
                isValido = false;
            }

            if (!this.trabalhadorForm.controls['estado'].value) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.trabalhadorForm.controls['estado'],
                    MensagemProperties.app_rst_labels_estado);
                isValido = false;
            }

            if (this.trabalhadorForm.controls['estado'].value && !this.trabalhadorForm.controls['municipio'].value) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.trabalhadorForm.controls['municipio'],
                    MensagemProperties.app_rst_labels_municipio);
                isValido = false;
            }
        }
        return isValido;
    }

    verificarSeEnderecoEstaPreenchido() {
        return this.trabalhadorForm.controls['endereco'].value ||
            this.trabalhadorForm.controls['numero'].value ||
            this.trabalhadorForm.controls['cep'].value ||
            this.trabalhadorForm.controls['bairro'].value ||
            this.trabalhadorForm.controls['estado'].value ||
            this.trabalhadorForm.controls['municipio'].value ||
            this.trabalhadorForm.controls['complemento'].value;
    }

    private prepareSave(): Trabalhador {
        const formModel = this.trabalhadorForm.controls;
        this.trabalhador.nome = formModel.nome.value;
        this.trabalhador.dataNascimento = formModel.dataNascimento.value ?
            this.convertDateToString(formModel.dataNascimento.value.date) : null;
        this.trabalhador.dataFalecimento = formModel.dataFalecimento.value ?
            this.convertDateToString(formModel.dataFalecimento.value.date) : null;
        this.trabalhador.nit = MascaraUtil.removerMascara(formModel.nit.value);
        this.trabalhador.cpf = MascaraUtil.removerMascara(formModel.cpf.value);
        this.trabalhador.rg = formModel.rg.value;
        this.trabalhador.orgaoRg = formModel.orgaoRg.value;
        this.trabalhador.ctps = formModel.ctps.value;
        this.trabalhador.serieCtps = formModel.serieCtps.value;
        this.trabalhador.ufCtps = formModel.ufCtps.value;
        this.trabalhador.escolaridade = formModel.escolaridade.value;
        this.trabalhador.faixaSalarial = formModel.faixaSalarial.value;
        this.trabalhador.genero = EnumValues.getNameFromValue(Genero, formModel.genero.value);
        this.trabalhador.raca = formModel.raca.value;
        this.trabalhador.estadoCivil = formModel.estadoCivil.value;
        this.trabalhador.descricaoAlergias = formModel.descricaoAlergias.value;
        this.trabalhador.descricaoVacinas = formModel.descricaoVacinas.value;
        this.trabalhador.descricaoMedicamentos = formModel.descricaoMedicamentos.value;
        this.trabalhador.nomeMae = formModel.nomeMae.value;
        this.trabalhador.nomePai = formModel.nomePai.value;
        this.trabalhador.tipoSanguineo = formModel.tipoSanguineo.value;
        this.trabalhador.profissao = null;
        if (!this.isUndefined(formModel.profissao.value)) {
            this.trabalhador.profissao = new Profissao();
            this.trabalhador.profissao.id = formModel.profissao.value;
        }
        this.trabalhador.situacaoTrabalhador = formModel.situacaoTrabalhador.value;
        this.trabalhador.planoSaude = formModel.planoSaude.value;
        this.trabalhador.automovel = formModel.possuiAutomovel.value;
        this.trabalhador.atividadeFisica = formModel.atividadeFisica.value;
        this.trabalhador.exameRegular = formModel.exameRegular.value;
        this.trabalhador.notificacao = formModel.notificacao.value;
        this.trabalhador.nomeSocial= formModel.nomeSocial.value;


        // nacionalidade
        this.trabalhador.nacionalidade = formModel.nacionalidade.value;
        this.trabalhador.pais = null;
        this.trabalhador.municipio = null;
        this.trabalhador.dataNaturalizacao = null;
        this.trabalhador.dataEntradaPais = null;
        if (formModel.pais.value && !this.isUndefined(formModel.pais.value)) {
            this.trabalhador.pais = new Pais();
            this.trabalhador.pais.id = formModel.pais.value;
        }
        if (formModel.municipioNacionalidade.value) {
            this.trabalhador.municipio = new Municipio();
            this.trabalhador.municipio.id = formModel.municipioNacionalidade.value;
        }
        if (!this.isUndefined(formModel.dataNaturalizacao.value)) {
            this.trabalhador.dataNaturalizacao = formModel.dataNaturalizacao.value ?
                this.convertDateToString(formModel.dataNaturalizacao.value.date) : null;
        }
        if (!this.isUndefined(this.trabalhador.dataEntradaPais)) {
            this.trabalhador.dataEntradaPais = formModel.dataEntradaPais.value ?
                this.convertDateToString(formModel.dataEntradaPais.value.date) : null;
        }

        // endereco
        if (this.verificarSeEnderecoEstaPreenchido()) {
            const endereco: EnderecoTrabalhador = new EnderecoTrabalhador();
            if (!this.trabalhador.listaEnderecoTrabalhador) {
                this.trabalhador.listaEnderecoTrabalhador = new Array<EnderecoTrabalhador>();
            }
            endereco.endereco.descricao = formModel.endereco.value;
            endereco.endereco.complemento = formModel.complemento.value;
            endereco.endereco.numero = formModel.numero.value;
            endereco.endereco.bairro = formModel.bairro.value;

            if (formModel.municipio.value) {
                endereco.endereco.municipio.id = formModel.municipio.value;
            } else {
                endereco.endereco.municipio = null;
            }
            if (formModel.cep.value) {
                endereco.endereco.cep = MascaraUtil.removerMascara(formModel.cep.value);
            }
            endereco.endereco.tipoEndereco = EnumValues.getNameFromValue(TipoEndereco, TipoEndereco.P);
            if (this.trabalhador.listaEnderecoTrabalhador && this.trabalhador.listaEnderecoTrabalhador.length > 0) {
                this.trabalhador.listaEnderecoTrabalhador[0].endereco = endereco.endereco;
            } else {
                this.trabalhador.listaEnderecoTrabalhador.push(endereco);
            }
        }
        return this.trabalhador;
    }

    getFiltro(): FiltroTrabalhador {
        const filtro = new FiltroTrabalhador();
        filtro.id = this.id;
        if (this.activatedRoute.snapshot.url[0].path === 'meusdados') {
            filtro.aplicarDadosFilter = false;
        }
        return filtro;
    }

    buscarTrabalhador() {
        this.trabalhador = new Trabalhador();
        this.service.buscarPorId(this.getFiltro()).subscribe((trabalhador: Trabalhador) => {
            if (trabalhador && trabalhador.id) {
                this.trabalhador = trabalhador;
                this.converterModelParaForm();
                if (this.temEmpPerfil()) {
                    this.getVidaAtiva(this.id);
                }

            }
        }, (error) => {
            return this.router.navigate(['/acessonegado']);
        });
    }

    isMeusDados() {
        return this.activatedRoute.snapshot.url[0].path === 'meusdados';
    }

    voltar(): void {
        if (this.trabalhador.id) {
            if (this.activatedRoute.snapshot.url[0].path === 'meusdados') {
                this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados`]);
            } else {
                this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.trabalhador.id}`]);
            }

        } else {
            this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador`]);
        }
    }

    salvar(): void {
        if (this.validarCampos()) {
            this.prepareSave();
            this.service.salvar(this.trabalhador).subscribe((response: Trabalhador) => {
                let user = Seguranca.getUsuario();
                if (user.papeis.find((element) =>
                    element === PerfilEnum.ADM
                    || element === PerfilEnum.GDRA
                    || element === PerfilEnum.SUDR
                    || element === PerfilEnum.GDRM
                    || element === PerfilEnum.GEEM
                    || element === PerfilEnum.TRA
                    || element === PerfilEnum.MTSDR
                    || element === PerfilEnum.GCDR)  && !this.trabalhador.id) {
                    this.trabalhador = response;
                    this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso_trabalhador);
                } else {
                    this.trabalhador = response;
                    this.voltar();
                }
            }, (error) => {
                this.mensagemError(error);
            });
        }
    }

    existeTelefone() {
        return this.trabalhador.listaTelefoneTrabalhador && this.trabalhador.listaTelefoneTrabalhador.length > 0;
    }

    existeEmail() {
        return this.trabalhador.listaEmailTrabalhador && this.trabalhador.listaEmailTrabalhador.length > 0;
    }

    limparCamposNacionalidade() {
        this.nacional = '';
        this.trabalhadorForm.patchValue({
            pais: undefined,
            estadoNacionalidade: undefined,
            municipioNacionalidade: undefined,
            dataNaturalizacao: null,
            dataEntradaPais: null,
        });
        // this.trabalhadorForm.controls['dataNaturalizacao'].enable();
        // this.trabalhadorForm.controls['dataEntradaPais'].enable();
        // this.trabalhadorForm.controls['pais'].enable();
        // this.trabalhadorForm.controls['estadoNacionalidade'].disable();
        // this.trabalhadorForm.controls['municipioNacionalidade'].disable();
    }

    selectNacionalidade() {
        // this.trabalhadorForm.controls['pais'].enable();
        this.limparCamposNacionalidade();
        this.alterarNacionalidade();
    }

    alterarNacionalidade() {
        if (!this.isUndefined(this.trabalhadorForm.controls['nacionalidade'].value)) {
            const nacionalidade = this.trabalhadorForm.controls['nacionalidade'].value;
            if (nacionalidade === EnumValues.getNameFromValue(Nacionalidade, Nacionalidade.BR)) {
                this.nacional = EnumValues.getNameFromValue(Nacionalidade, Nacionalidade.BR);
                this.paisService.buscarPaises().subscribe((dados: any) => {
                    this.paises = dados;
                    this.paises.forEach((item) => {
                        if (item.descricao.toUpperCase() === MensagemProperties.app_rst_labels_brasil_up) {
                            this.trabalhadorForm.patchValue({
                                pais: item.id,
                            });
                            // this.trabalhadorForm.controls['estadoNacionalidade'].enable();
                        }
                    });
                }, (error) => {
                    this.mensagemError(error);
                });
            }

            if (nacionalidade === EnumValues.getNameFromValue(Nacionalidade, Nacionalidade.NA)) {
                // this.trabalhadorForm.controls['dataNaturalizacao'].enable();
                // this.trabalhadorForm.controls['dataEntradaPais'].enable();
                // this.trabalhadorForm.controls['pais'].enable();

                this.nacional = EnumValues.getNameFromValue(Nacionalidade, Nacionalidade.NA);
            }
            if (nacionalidade === EnumValues.getNameFromValue(Nacionalidade, Nacionalidade.ES)) {
                this.nacional = EnumValues.getNameFromValue(Nacionalidade, Nacionalidade.ES);
                // this.trabalhadorForm.controls['dataEntradaPais'].enable();
                this.paisService.buscarPaises().subscribe((dados: any) => {
                    this.paises = dados;
                    this.paises.forEach((item) => {
                        if (item.descricao.toUpperCase() === MensagemProperties.app_rst_labels_brasil_up) {
                            const index: number = this.paises.indexOf(item);
                            if (index !== -1) {
                                this.paises.splice(index, 1);
                            }
                        }
                    });
                    // this.trabalhadorForm.controls['pais'].enable();
                }, (error) => {
                    this.mensagemError(error);
                });
            }
        }
        this.checkEstrangeiro();
        this.checkNaturalizado();
    }

    private validarDataFalecimento(dataNascimento: any, dataFalecimento: any): Boolean {
        const data1 = new Date(this.convertDateToStringUS(dataNascimento));
        const data2 = new Date(this.convertDateToStringUS(dataFalecimento));
        return data2 > data1;
    }

    createForm() {
        this.trabalhadorForm = this.formBuilder.group({
            nome: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(160),
                ]),
            ],
            nomeSocial: [
                {value: undefined, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([]),
            ],
            dataNascimento: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.required,
                    ValidateData,
                    ValidateDataFutura,
                ]),
            ],
            dataFalecimento: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    ValidateData,
                    ValidateDataFutura,
                ]),
            ],
            cpf: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.required, ValidateCPF,
                ]),
            ],
            rg: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ],
            orgaoRg: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ],
            nit: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.required,
                    ValidarNit,
                ]),
            ],
            ctps: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ],
            brPdh: [
                {value: undefined, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([]),
            ],
            escolaridade: [
                {value: undefined, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([]),
            ],
            estadoCivil: [
                {value: undefined, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.required,
                ]),
            ],
            faixaSalarial: [
                {value: undefined, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([]),
            ],
            genero: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.required,
                ]),
            ],
            raca: [
                {value: undefined, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([]),
            ],
            serieCtps: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ]
            ,
            ufCtps: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ],
            endereco: [
                {value: null, disabled: this.modoConsulta},
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ],
            bairro: [
                {value: null, disabled: this.modoConsulta},
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ],
            complemento: [
                {value: null, disabled: this.modoConsulta},
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ],
            numero: [
                {value: null, disabled: this.modoConsulta},
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ],
            estado: [
                {value: undefined, disabled: this.modoConsulta},
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ],
            municipio: [
                {value: undefined, disabled: true},
                Validators.compose([]),
            ],
            cep: [
                {value: null, disabled: this.modoConsulta},
                Validators.compose([]),
            ],
            email: [
                {value: null, disabled: this.modoConsulta},
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ],
            telefone: [
                {value: null, disabled: this.modoConsulta},
                Validators.compose([]),
            ],
            planoSaude: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.maxLength(1),
                ]),
            ],
            possuiAutomovel: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.maxLength(1),
                ]),
            ],
            atividadeFisica: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.maxLength(1),
                ]),
            ],
            exameRegular: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.maxLength(1),
                ]),
            ],
            notificacao: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.maxLength(1),
                ]),
            ],
            nomeMae: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.maxLength(160),
                ]),
            ],
            nomePai: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    Validators.maxLength(160),
                ]),
            ],
            nacionalidade: [
                {value: undefined, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([]),
            ],
            situacaoTrabalhador: [
                {value: undefined, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([]),
            ],
            profissao: [
                {value: undefined, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([]),
            ],
            tipoSanguineo: [
                {value: undefined, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([]),
            ],
            pais: [
                {value: undefined, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([]),
            ],
            dataNaturalizacao: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    ValidateData,
                    ValidateDataFutura,
                ]),
            ],
            dataEntradaPais: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([
                    ValidateData,
                    ValidateDataFutura,
                ]),
            ],
            estadoNacionalidade: [
                {value: undefined, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([]),
            ],
            municipioNacionalidade: [
                {value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador()},
                Validators.compose([]),
            ],
            descricaoMedicamentos: [
                {value: null, disabled: this.modoConsulta},
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ],
            descricaoAlergias: [
                {value: null, disabled: this.modoConsulta},
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ],
            descricaoVacinas: [
                {value: null, disabled: this.modoConsulta},
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ],
        });

        this.verificarTermo();
    }

    private verificarTermo() {
        if (this.trabalhador.termo) {
            this.trabalhadorForm.controls['cpf'].disable();
            this.trabalhadorForm.controls['dataNascimento'].disable();
        }
    }

    checkEstrangeiro() {
        return this.nacional === 'ES';
    }

    checkNaturalizado() {
        return this.nacional === 'NA';
    }

    checkBrasileiro() {
        return this.nacional === 'BR';
    }

    municipioPreenchido(): void {
        if (this.modoConsulta) {
            this.trabalhadorForm.controls['municipio'].disable();
        } else {
            this.trabalhadorForm.controls['municipio'].enable();
        }
    }

    private emModoConsulta() {
        this.modoConsulta = !Seguranca.isPermitido([
            PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_CADASTRAR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR]);
    }

    adicionarImagem() {
        this.temImagem = true;
        let dados;
        const modalRef = this.modalService.open(this.upload);
        modalRef.result.then((result) => {
            dados = this.foto.image.split('base64,');
            this.trabalhador.tipoImagem = dados[0] + 'base64,';
            this.trabalhador.imagem = this.foto.image.replace(/^data:image\/\w+;base64,/, '');

        }, (reason) => {
            if (!this.trabalhador.imagem) {
                this.temImagem = false;
            }
            this.foto.image = this.trabalhador.tipoImagem ?
                this.trabalhador.tipoImagem.concat(this.trabalhador.imagem) : this.trabalhador.imagem;
        });

    }

    excluirImagem() {
        this.trabalhador.imagem = undefined;
        this.trabalhador.tipoImagem = undefined;
        this.foto = {};
        this.temImagem = false;
    }

    isProducao(): Boolean {
        return environment.isProduction;
    }
}
