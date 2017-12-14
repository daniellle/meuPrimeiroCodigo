import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { DatePicker } from './../../../compartilhado/utilitario/date-picker';
import { TipoEmpresaService } from './../../../servico/tipo-empresa.service';
import { PorteEmpresaService } from 'app/servico/porte-empresa.service';
import { EspecialidadeService } from './../../../servico/especialidade.service';
import { EnderecoParceiro } from 'app/modelo/endereco-parceiro.model';
import { EmailParceiro } from 'app/modelo/email-parceiro.model';
import { Parceiro } from './../../../modelo/parceiro.model';
import { Component, OnInit } from '@angular/core';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { BaseComponent } from 'app/componente/base.component';
import { ParceiroService } from 'app/servico/parceiro.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PorteEmpresa } from 'app/modelo/porte-empresa.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ValidateCNPJ } from 'app/compartilhado/validators/cnpj.validator';
import { Genero } from 'app/modelo/enum/enum-genero.model';
import { TipoEmpresa } from 'app/modelo/tipo-empresa.model';
import { ValidateCPF } from 'app/compartilhado/validators/cpf.validator';
import { ValidateEmail } from 'app/compartilhado/validators/email.validator';
import { TelefoneParceiro } from 'app/modelo/telefone-parceiro.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { EnumValues } from 'enum-values';
import { TipoEmail } from 'app/modelo/enum/enum-tipo-email.model';
import { ValidateData, ValidateDataFutura } from 'app/compartilhado/validators/data.validator';
import { Segmento } from 'app/modelo/segmento.model';
import { ParceiroEspecialidade } from 'app/modelo/parceiro-especialidade.model';
import { IOption } from 'ng-select';
import { Especialidade } from 'app/modelo/especialidade.model';

@Component({
    selector: 'app-cadastro-parceiro-credenciado',
    templateUrl: './cadastro-parceiro-credenciado.component.html',
    styleUrls: ['./cadastro-parceiro-credenciado.component.scss'],
})
export class CadastroParceiroCredenciadoComponent extends BaseComponent implements OnInit {

    listaEspecialidades = Array<IOption>();
    parceiro: Parceiro;
    id: number;
    parceiroForm: FormGroup;
    tipoEmpresa: TipoEmpresa;
    porteEmpresa: PorteEmpresa;
    genero = Genero;
    listaPorteParceiros: PorteEmpresa[];
    listaTipoParceiros: TipoEmpresa[];
    isPessoaJuridica: boolean;
    especialidadesSelecionadas: Especialidade[];

    constructor(
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private service: ParceiroService,
        private formBuilder: FormBuilder,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private especialidadeService: EspecialidadeService,
        protected porteEmpresaService: PorteEmpresaService,
        protected tipoEmpresaService: TipoEmpresaService,
    ) {
        super(bloqueioService, dialogo);
        this.title = this.activatedRoute.snapshot.data.title;
        this.emModoConsulta();
    }

    ngOnInit() {
        this.isPessoaJuridica = true;
        this.parceiro = new Parceiro();
        this.carregarCombos();
        this.createForm();
        this.carregarTela();
    }

    existeTelefone() {
        return this.parceiro.telefonesParceiro && this.parceiro.telefonesParceiro.length > 0;
    }
    existeEndereco() {
        return this.parceiro.enderecosParceiro && this.parceiro.enderecosParceiro.length > 0;
    }
    existeSegmento() {
        return this.parceiro.segmento && this.parceiro.segmento.descricao != null;
    }

    emModoConsulta() {
        this.modoConsulta = !Seguranca.isPermitido(
            [PermissoesEnum.PARCEIRO_CREDENCIADA,
            PermissoesEnum.PARCEIRO_CREDENCIADA_CADASTRAR,
            PermissoesEnum.PARCEIRO_CREDENCIADA_ALTERAR,
            PermissoesEnum.PARCEIRO_CREDENCIADA_DESATIVAR]);
    }

    mudarMascaraTelResponsavel(event: any) {
        if (this.parceiroForm.controls['telefoneResponsavel'].value) {
            const valor = this.parceiroForm.controls['telefoneResponsavel'].value;
            if (valor.length <= 14) {
                this.mascaraTelFixo(valor);
            } else {
                this.mascaraCelular(valor);
            }
        }
    }

    salvar() {
        if (this.verificarCampos()) {
            this.prepareSave();
            this.service.salvar(this.parceiro).subscribe((response: Parceiro) => {
                this.parceiro = response;
                this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
            }, (error) => {
                this.mensagemError(error);
            }, () => {
                this.voltar();
            });
        }
    }

    removeSegmento() {
        this.parceiro.segmento = new Segmento();
    }

    isPessoaJuridic(isPessoaJuridica: boolean) {
        if (!this.modoConsulta) {
            this.isPessoaJuridica = isPessoaJuridica;
            this.limparCampos();
        }
    }

    limparCampos() {
        this.parceiro.nomeFantasia = null;
        this.parceiro.inscricaoEstadual = null;
        this.parceiro.inscricaoMunicipal = null;
        this.parceiro.porteEmpresa = new PorteEmpresa();
        this.parceiro.tipoEmpresa = new TipoEmpresa();
        this.parceiro.cargoResponsavel = null;
        this.parceiro.emailResponsavel = null;
        this.parceiro.nomeResponsavel = null;
        this.parceiro.numeroNitResponsavel = null;
        this.parceiro.numeroTelefoneResponsavel = null;
        this.parceiro.dataNascimento = null;
        this.parceiro.genero = null;
        this.parceiro.numeroCnpjCpf = null;
        this.parceiro.nome = null;
        this.parceiro.url = null;
        this.parceiro.dataDesligamento = null;
        this.parceiro.parceiroEspecialidades = new Array<ParceiroEspecialidade>();
        this.parceiro.telefonesParceiro = new Array<TelefoneParceiro>();
        this.parceiro.enderecosParceiro = new Array<EnderecoParceiro>();
        this.parceiro.segmento = new Segmento();
        this.parceiro.parceiroEspecialidades = new Array<ParceiroEspecialidade>();

        this.parceiroForm.patchValue({
            nome: this.parceiro.nome,
            razaoSocial: this.parceiro.nome,
            numeroCnpjCpf: this.parceiro.numeroCnpjCpf,
            nomeFantasia: this.parceiro.nomeFantasia,
            cargoResponsavel: this.parceiro.cargoResponsavel,
            nomeResponsavel: this.parceiro.nomeResponsavel,
            numeroNitResponsavel: this.parceiro.numeroNitResponsavel,
            telefoneResponsavel: this.parceiro.numeroTelefoneResponsavel,
            emailResponsavel: this.parceiro.emailResponsavel,
            dataNascimento: null,
            inscricaoEstadual: this.parceiro.inscricaoEstadual,
            inscricaoMunicipal: this.parceiro.inscricaoMunicipal,
            genero: this.parceiro.genero,
            url: this.parceiro.url,
            porteEmpresa: undefined,
            tipoEmpresa: undefined,
            especialidades: this.parceiro.parceiroEspecialidades,
            dtdesligamento: this.parceiro.dataDesligamento ?
                DatePicker.convertDateForMyDatePicker(this.parceiro.dataDesligamento) : null,
        });
    }

    voltar(): void {
        if (this.parceiro.id) {
            this.router.navigate([`${environment.path_raiz_cadastro}/parceirocredenciado/${this.parceiro.id}`]);
        } else {
            this.router.navigate([`${environment.path_raiz_cadastro}/parceirocredenciado`]);
        }
    }

    getDescricaoSegmento(): string {
        return this.parceiro.segmento ? this.parceiro.segmento.descricao : '';
    }

    private mascaraCelular(valor: any) {
        valor = MascaraUtil.removerMascara(valor);
        valor = valor.replace(/\W/g, '');
        valor = valor.replace(/^(\d{2})(\d)/, '($1) $2');
        valor = valor.replace(/(\d{5})(\d)/, '$1-$2');
        valor = valor.replace(/(\d{4})$/, '$1');
        this.parceiroForm.patchValue({
            telefoneResponsavel: valor,
        });
    }

    private converterModelParaForm() {
        this.parceiroForm.patchValue({
            numeroCnpjCpf: this.parceiro.numeroCnpjCpf,
            nome: this.parceiro.nome,
            nomeFantasia: this.parceiro.nomeFantasia,
            cargoResponsavel: this.parceiro.cargoResponsavel,
            nomeResponsavel: this.parceiro.nomeResponsavel,
            numeroNitResponsavel: this.parceiro.numeroNitResponsavel,
            telefoneResponsavel: this.parceiro.numeroTelefoneResponsavel,
            emailResponsavel: this.parceiro.emailResponsavel,
            url: this.parceiro.url,
            genero: Genero[this.parceiro.genero],
            dataNascimento: this.parceiro.dataNascimento ? DatePicker.convertDateForMyDatePicker(this.parceiro.dataNascimento) : null,
            dtdesligamento: this.parceiro.dataDesligamento ? DatePicker.convertDateForMyDatePicker(this.parceiro.dataDesligamento) : null,
            tipoPessoa: this.parceiro.numeroCnpjCpf.length < 14 ? 'PF' : 'PJ',
            inscricaoEstadual: this.parceiro.inscricaoEstadual,
            inscricaoMunicipal: this.parceiro.inscricaoMunicipal,
            porteEmpresa: this.parceiro.porteEmpresa ? this.parceiro.porteEmpresa.id : undefined,
            tipoEmpresa: this.parceiro.tipoEmpresa ? this.parceiro.tipoEmpresa.id : undefined,
            especialidades: !this.especialidadesSelecionadas ? null :
                this.especialidadesSelecionadas.map((a) => a.id.toString()),
        });
        this.isPessoaJuridica = this.parceiro.numeroCnpjCpf.length < 14 ? false : true;
        if (this.parceiro.emailsParceiro) {
            this.parceiroForm.patchValue({
                email: this.parceiro.emailsParceiro[0] ? this.parceiro.emailsParceiro[0].email.descricao : '',
            });
        }

        if (!this.parceiro.telefonesParceiro) {
            this.parceiro.telefonesParceiro = new Array<TelefoneParceiro>();
        }

        if (!this.parceiro.enderecosParceiro) {
            this.parceiro.enderecosParceiro = new Array<EnderecoParceiro>();
        }

    }

    private verificarCampos(): boolean {
        let retorno = true;
        // cnpj
        if (this.parceiroForm.controls['numeroCnpjCpf'].invalid && this.isPessoaJuridica) {

            if (this.parceiroForm.controls['numeroCnpjCpf'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.parceiroForm.controls['numeroCnpjCpf'],
                    MensagemProperties.app_rst_labels_cnpj);
                retorno = false;
            }

            if (!this.parceiroForm.controls['numeroCnpjCpf'].errors.required
                && this.parceiroForm.controls['numeroCnpjCpf'].errors.validCNPJ) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.parceiroForm.controls['numeroCnpjCpf'],
                    MensagemProperties.app_rst_labels_cnpj);
                retorno = false;
            }
        }
        // cpf
        if (this.parceiroForm.controls['numeroCnpjCpf'].invalid && !this.isPessoaJuridica) {

            if (this.parceiroForm.controls['numeroCnpjCpf'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.parceiroForm.controls['numeroCnpjCpf'],
                    MensagemProperties.app_rst_labels_cpf);
                retorno = false;
            }

            if (!this.parceiroForm.controls['numeroCnpjCpf'].errors.required
                && this.parceiroForm.controls['numeroCnpjCpf'].errors.validCPF) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.parceiroForm.controls['numeroCnpjCpf'],
                    MensagemProperties.app_rst_labels_cpf);
                retorno = false;
            }
        }
        // Razao Social / nome
        if (this.parceiroForm.controls['nome'].invalid) {
            if (this.parceiroForm.controls['nome'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.parceiroForm.controls['nome'],
                    MensagemProperties.app_rst_labels_razao_social);
                retorno = false;
            }
            if (this.parceiroForm.controls['nome'].errors.maxLength) {
                this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
                    this.parceiroForm.controls['nome'], MensagemProperties.app_rst_labels_razao_social,
                    this.parceiroForm.controls['nome'].errors.maxLength.requiredLength);
                retorno = false;
            }
        }

        // nome fantasia
        if (this.parceiroForm.controls['nomeFantasia'].invalid && this.isPessoaJuridica) {
            if (this.parceiroForm.controls['nomeFantasia'].errors.maxLength) {
                this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
                    this.parceiroForm.controls['nomeFantasia'], MensagemProperties.app_rst_labels_nome_fantasia,
                    this.parceiroForm.controls['nomeFantasia'].errors.maxLength.requiredLength);
                retorno = false;
            }
        }
        // email
        if (this.parceiroForm.controls['email'].value
            && this.parceiroForm.controls['email'].invalid
            && this.parceiroForm.controls['email'].errors.validEmail) {
            this.mensagemErroComParametros('app_rst_campo_invalido', this.parceiroForm.controls['email'],
                MensagemProperties.app_rst_labels_email_contato);
            retorno = false;
        }

        // numeroNitResponsavel
        if (this.parceiroForm.controls['numeroNitResponsavel'].value
            && this.parceiroForm.controls['numeroNitResponsavel'].invalid
            && this.parceiroForm.controls['numeroNitResponsavel'].errors.validNIT) {
            this.mensagemErroComParametros('app_rst_campo_invalido', this.parceiroForm.controls['numeroNitResponsavel'],
                MensagemProperties.app_rst_labels_nit);
            retorno = false;
        }

        // telefoneResponsavel
        if (this.parceiroForm.controls['telefoneResponsavel'].value) {
            const valor = this.parceiroForm.controls['telefoneResponsavel'].value;
            if (MascaraUtil.removerMascara(valor).length > 11) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.parceiroForm.controls['telefoneResponsavel'],
                    MensagemProperties.app_rst_labels_telefone);
                retorno = false;
            }
        }

        // dataNascimento
        if (this.parceiroForm.controls['dataNascimento'].value
            && this.parceiroForm.controls['dataNascimento'].invalid) {
            if (this.parceiroForm.controls['dataNascimento'].errors.validData) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.parceiroForm.controls['dataNascimento'],
                    MensagemProperties.app_rst_labels_data_nascimento);
                retorno = false;
            }

            if (this.parceiroForm.controls['dataNascimento'].errors.validDataFutura) {
                this.mensagemErroComParametros('app_rst_labels_data_futura', this.parceiroForm.controls['dataNascimento'],
                    MensagemProperties.app_rst_labels_data_nascimento);
                retorno = false;
            }
        }

        // dataDesativaco
        if (this.parceiroForm.controls['dtdesligamento'].value
            && this.parceiroForm.controls['dtdesligamento'].invalid) {
            if (this.parceiroForm.controls['dtdesligamento'].errors.validData) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.parceiroForm.controls['dtdesligamento'],
                    MensagemProperties.app_rst_labels_data_desativacao);
                retorno = false;
            }

            if (this.parceiroForm.controls['dtdesligamento'].errors.validDataFutura) {
                this.mensagemErroComParametros('app_rst_labels_data_futura', this.parceiroForm.controls['dtdesligamento'],
                    MensagemProperties.app_rst_labels_data_desativacao);
                retorno = false;
            }
        }

        return retorno;
    }

    private mascaraTelFixo(valor: any) {
        valor = MascaraUtil.removerMascara(valor);
        valor = valor.replace(/\W/g, '');
        valor = valor.replace(/^(\d{2})(\d)/, '($1) $2');
        valor = valor.replace(/(\d{4})(\d)/, '$1-$2');
        valor = valor.replace(/(\d{4})$/, '$1');
        this.parceiroForm.patchValue({
            telefoneResponsavel: valor,
        });
    }

    private prepareSave() {
        const formModel = this.parceiroForm.controls;
        this.parceiro.numeroCnpjCpf = MascaraUtil.removerMascara(formModel.numeroCnpjCpf.value);
        this.parceiro.nome = formModel.nome.value;
        this.parceiro.nomeFantasia = formModel.nomeFantasia.value;
        this.parceiro.genero = EnumValues.getNameFromValue(Genero, formModel.genero.value);
        this.parceiro.inscricaoEstadual = formModel.inscricaoEstadual.value;
        this.parceiro.inscricaoMunicipal = formModel.inscricaoMunicipal.value;
        this.parceiro.cargoResponsavel = formModel.cargoResponsavel.value;
        this.parceiro.nomeResponsavel = formModel.nomeResponsavel.value;
        this.parceiro.numeroNitResponsavel = MascaraUtil.removerMascara(formModel.numeroNitResponsavel.value);
        this.parceiro.numeroTelefoneResponsavel = MascaraUtil.removerMascara(formModel.telefoneResponsavel.value);
        this.parceiro.emailResponsavel = formModel.emailResponsavel.value;
        this.parceiro.dataNascimento = formModel.dataNascimento.value ?
            this.convertDateToString(formModel.dataNascimento.value.date) : null;
        this.parceiro.dataDesligamento = formModel.dtdesligamento.value ?
            this.convertDateToString(formModel.dtdesligamento.value.date) : null;
        this.parceiro.url = formModel.url.value;
        if (formModel.porteEmpresa.value && !this.isUndefined(formModel.porteEmpresa.value)) {
            const porteEmpresa = new PorteEmpresa();
            porteEmpresa.id = formModel.porteEmpresa.value;
            this.parceiro.porteEmpresa = porteEmpresa;
        } else {
            this.parceiro.porteEmpresa = null;
        }
        if (formModel.tipoEmpresa.value && !this.isUndefined(formModel.tipoEmpresa.value)) {
            const tipoEmpresa = new TipoEmpresa();
            tipoEmpresa.id = formModel.tipoEmpresa.value;
            this.parceiro.tipoEmpresa = tipoEmpresa;
        } else {
            this.parceiro.tipoEmpresa = null;
        }
        // Especialidades
        this.gerenciarEspecialidades(formModel);
        // email
        if (formModel.email.value) {
            this.gerenciarEmails(formModel);
        }

        if (this.parceiro.segmento && this.parceiro.segmento.id == null) {
            this.parceiro.segmento = null;
        }
    }

    private gerenciarEmails(formModel) {
        if (this.isEmpty(this.parceiro.emailsParceiro)) {
            this.parceiro.emailsParceiro = new Array<EmailParceiro>();
            this.parceiro.emailsParceiro.push(new EmailParceiro());
        }

        this.parceiro.emailsParceiro[0].email.descricao = formModel.email.value;
        this.parceiro.emailsParceiro[0].email.tipo = EnumValues.getNameFromValue(TipoEmail, TipoEmail.T);
        this.parceiro.emailsParceiro[0].email.notificacao = true;
    }

    private gerenciarEspecialidades(formModel) {
        if (formModel.especialidades.value) {
            this.setNewsEspecialidades(formModel);
        }
        this.parseEspecialidadesFormToModel(formModel);
    }

    private setNewsEspecialidades(formModel) {
        formModel.especialidades.value.forEach((idEspecialidade) => {
            let jaExiste: boolean;
            this.parceiro.parceiroEspecialidades.forEach((parceiroEspecialidade) => {
                if (parceiroEspecialidade.especialidade.id === Number(idEspecialidade)) {
                    jaExiste = true;
                }
            });
            if (!jaExiste) {
                const parceiroEspecilidade: ParceiroEspecialidade = new ParceiroEspecialidade();
                parceiroEspecilidade.especialidade.id = Number(idEspecialidade);
                this.parceiro.parceiroEspecialidades.push(parceiroEspecilidade);
            }
        });
    }

    private parseEspecialidadesFormToModel(formModel) {
        const parceiroEspecialidades: ParceiroEspecialidade[] = [];
        this.parceiro.parceiroEspecialidades.forEach((parceiroEspecialidade) => {
            let existe = false;
            if (formModel.especialidades.value) {
                formModel.especialidades.value.forEach((idEspecialidade) => {
                    if (parceiroEspecialidade.especialidade.id === Number(idEspecialidade)) {
                        existe = true;
                    }
                });
            }
            if (!existe) {
                parceiroEspecialidades.push(parceiroEspecialidade);
            }
        });

        parceiroEspecialidades.forEach((element) => {
            this.parceiro.parceiroEspecialidades.splice(this.parceiro.parceiroEspecialidades.indexOf(element), 1);
        });
    }

    private carregarEspecialidades() {
        this.especialidadeService.pesquisarEspecialidades().subscribe((retorno: any[]) => {
            let listaOption: IOption[];
            listaOption = [];
            if (retorno) {
                retorno.forEach((element) => {
                    const item = new Option();
                    item.value = element.id;
                    item.label = element.descricao;
                    listaOption.push(item);
                });
            }
            this.listaEspecialidades = listaOption;
        });
    }

    private carregarCombos() {
        this.porteEmpresaService.pesquisarTodos().subscribe((retorno: PorteEmpresa[]) => {
            this.listaPorteParceiros = retorno;
        });
        this.tipoEmpresaService.pesquisarTodos().subscribe((retorno: TipoEmpresa[]) => {
            this.listaTipoParceiros = retorno;
        });
        this.carregarEspecialidades();
    }

    private carregarTela() {
        this.id = this.activatedRoute.snapshot.params['id'];
        if (this.id) {
            this.service.buscarPorId(this.id).subscribe((parceiro: Parceiro) => {
                this.parceiro = parceiro;
                this.setEspecialidadesParceiro(this.parceiro);
                this.converterModelParaForm();
                this.carregarMascaraTelefoneResp();
            }, (error) => {
                this.mensagemError(error);
            });
        }
    }

    private setEspecialidadesParceiro(parceiro: Parceiro) {
        if (this.parceiro.parceiroEspecialidades) {
            this.especialidadesSelecionadas = this.parceiro.parceiroEspecialidades.map((a) => {
                return a.especialidade;
            });
        }
    }

    private carregarMascaraTelefoneResp() {
        if (this.parceiroForm.controls['telefoneResponsavel'].value) {
            if (this.parceiroForm.controls['telefoneResponsavel'].value.length === 10) {
                this.mascaraTelFixo(this.parceiroForm.controls['telefoneResponsavel'].value);
            } else {
                this.mascaraCelular(this.parceiroForm.controls['telefoneResponsavel'].value);
            }
        }
    }

    private createForm() {
        this.parceiroForm = this.formBuilder.group({
            tipoPessoa: [
                { value: 'PJ', disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                ]),
            ],
            nomeFantasia: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                ]),
            ],
            url: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                ]),
            ],
            numeroNitResponsavel: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                ]),
            ],
            numeroCnpjCpf: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    ValidateCNPJ,
                    ValidateCPF,
                ]),
            ],
            razaoSocial: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(160),
                ]),
            ],
            nome: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(300),
                ]),
            ],
            dataNascimento: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    ValidateData,
                    ValidateDataFutura,
                    Validators.maxLength(10),
                ]),
            ],
            tipoEmpresa: [
                { value: undefined, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                ]),
            ],
            inscricaoEstadual: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(20),
                ]),
            ],
            porteEmpresa: [
                { value: undefined, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                ]),
            ],
            inscricaoMunicipal: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(20),
                ]),
            ],
            genero: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                ]),
            ],
            nomeResponsavel: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(160),
                ]),
            ],
            cargoResponsavel: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(60),
                ]),
            ],
            telefoneResponsavel: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(50),
                ]),
            ],
            emailResponsavel: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(100),
                    ValidateEmail,
                ]),
            ],
            email: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(100),
                    ValidateEmail,
                ]),
            ],
            segmento: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(100),
                ]),
            ],
            especialidades: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.maxLength(300),
                ]),
            ],
            dtdesligamento: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    ValidateData,
                    ValidateDataFutura,
                    Validators.maxLength(10),
                ]),
            ],

        });
    }
}
