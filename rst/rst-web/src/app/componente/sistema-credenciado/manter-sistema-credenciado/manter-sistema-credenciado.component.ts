import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { ToastyService } from 'ng2-toasty';
import { BaseComponent } from 'app/componente/base.component';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { SistemaCredenciadoService } from 'app/servico/sistema-credenciado.service';
import { SistemaCredenciado } from 'app/modelo/sistema-credenciado.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ValidateCNPJ } from 'app/compartilhado/validators/cnpj.validator';
import { ValidateEmail } from 'app/compartilhado/validators/email.validator';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { environment } from 'environments/environment.homolog';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
    selector: 'app-manter-sistema-credenciado',
    templateUrl: './manter-sistema-credenciado.component.html',
    styleUrls: ['./manter-sistema-credenciado.component.scss'],
})
export class ManterSistemaCredenciadoComponent extends BaseComponent implements OnInit {

    id: number;
    sistemaCredenciado: SistemaCredenciado;
    public sistemaCredenciadoForm: FormGroup;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private sistemaCredenciadoService: SistemaCredenciadoService,
        protected dialogo: ToastyService,
        protected bloqueioService: BloqueioService,
        protected formBuilder: FormBuilder,
    ) {
        super(bloqueioService, dialogo);
    }

    private emModoConsulta() {
        this.modoConsulta = !Seguranca.isPermitido(
            [PermissoesEnum.SISTEMA_CREDENCIADO_CADASTRAR, PermissoesEnum.SISTEMA_CREDENCIADO_ALTERAR]);
    }

    ngOnInit() {
        this.route.params.subscribe((params) => {
            this.id = params['id'];
            this.sistemaCredenciado = new SistemaCredenciado();
            if (this.id) {
                this.modoAlterar = true;
                this.buscarPorId();
            }
        });

        this.title = MensagemProperties.app_rst_sistema_credenciado_cadastrar;
        this.criarForm();
    }

    buscarPorId(): void {
        this.sistemaCredenciadoService.findById(this.id).subscribe((retorno: SistemaCredenciado) => {
            this.sistemaCredenciado = retorno;
            if (this.sistemaCredenciado) {
                this.converterModelParaForm();
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    criarForm(): void {
        this.sistemaCredenciadoForm = this.formBuilder.group({

            cnpj: [
                { value: null, disabled: this.modoAlterar || this.emModoConsulta() },
                Validators.compose([
                    ValidateCNPJ,
                    Validators.required,
                ]),
            ],

            nomeResponsavel: [
                { value: null, disabled: this.emModoConsulta() },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(160),
                ]),
            ],

            emailResponsavel: [
                { value: null, disabled: this.emModoConsulta() },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(100),
                    ValidateEmail,
                ]),
            ],

            telefoneResponsavel: [
                { value: null, disabled: this.emModoConsulta() },
                Validators.compose([
                    Validators.maxLength(11),
                ]),
            ],

            sistema: [
                { value: null, disabled: this.modoAlterar || this.emModoConsulta() },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(160),
                ]),
            ],

            entidade: [
                { value: null, disabled: this.modoAlterar || this.emModoConsulta() },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(8),
                ]),
            ],

        });
    }

    converterModelParaForm(): void {
        this.sistemaCredenciadoForm.patchValue({
            cnpj: this.sistemaCredenciado.cnpj,
            nomeResponsavel: this.sistemaCredenciado.nomeResponsavel,
            emailResponsavel: this.sistemaCredenciado.emailResponsavel,
            telefoneResponsavel: this.sistemaCredenciado.telefoneResponsavel,
            sistema: this.sistemaCredenciado.sistema,
            entidade: this.sistemaCredenciado.entidade,
        });
    }

    validarCampos(): Boolean {
        let isValido: Boolean = true;

        if (this.sistemaCredenciadoForm.controls['cnpj'].invalid) {
            if (this.sistemaCredenciadoForm.controls['cnpj'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.sistemaCredenciadoForm.controls['cnpj'],
                    MensagemProperties.app_rst_labels_cnpj);
                isValido = false;
            }

            if (!this.sistemaCredenciadoForm.controls['cnpj'].errors.required &&
                this.sistemaCredenciadoForm.controls['cnpj'].errors.validCNPJ) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.sistemaCredenciadoForm.controls['cnpj'],
                    MensagemProperties.app_rst_labels_cnpj);
                isValido = false;
            }
        }

        if (this.sistemaCredenciadoForm.controls['nomeResponsavel'].invalid) {

            if (this.sistemaCredenciadoForm.controls['nomeResponsavel'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.sistemaCredenciadoForm.controls['nomeResponsavel'],
                    MensagemProperties.app_rst_labels_nome_responsavel);
                isValido = false;
            }

            if (this.sistemaCredenciadoForm.controls['nomeResponsavel'].errors.maxLength) {
                this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
                    this.sistemaCredenciadoForm.controls['nomeResponsavel'], MensagemProperties.app_rst_labels_nome_responsavel,
                    this.sistemaCredenciadoForm.controls['nomeResponsavel'].errors.maxLength.requiredLength);
                isValido = false;
            }
        }

        if (this.sistemaCredenciadoForm.controls['emailResponsavel'].invalid) {

            if (this.sistemaCredenciadoForm.controls['emailResponsavel'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.sistemaCredenciadoForm.controls['emailResponsavel'],
                    MensagemProperties.app_rst_labels_email_responsavel);
                isValido = false;
            }

            if (this.sistemaCredenciadoForm.controls['emailResponsavel'].errors.maxLength) {
                this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
                    this.sistemaCredenciadoForm.controls['emailResponsavel'], MensagemProperties.app_rst_labels_email_responsavel,
                    this.sistemaCredenciadoForm.controls['emailResponsavel'].errors.maxLength.requiredLength);
                isValido = false;
            }

            if (!this.sistemaCredenciadoForm.controls['emailResponsavel'].errors.required
                && this.sistemaCredenciadoForm.controls['emailResponsavel'].errors.validEmail) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.sistemaCredenciadoForm.controls['emailResponsavel'],
                    MensagemProperties.app_rst_labels_email_responsavel);
                isValido = false;
            }
        }

        if (this.sistemaCredenciadoForm.controls['sistema'].invalid) {
            if (this.sistemaCredenciadoForm.controls['sistema'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.sistemaCredenciadoForm.controls['sistema'],
                    MensagemProperties.app_rst_label_sistema);
                isValido = false;
            }

            if (this.sistemaCredenciadoForm.controls['sistema'].errors.maxLength) {
                this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
                    this.sistemaCredenciadoForm.controls['sistema'], MensagemProperties.app_rst_label_sistema,
                    this.sistemaCredenciadoForm.controls['sistema'].errors.maxLength.requiredLength);
                isValido = false;
            }
        }

        if (this.sistemaCredenciadoForm.controls['entidade'].invalid) {
            if (this.sistemaCredenciadoForm.controls['entidade'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.sistemaCredenciadoForm.controls['entidade'],
                    MensagemProperties.app_rst_label_entidade);
                isValido = false;
            }

            if (this.sistemaCredenciadoForm.controls['entidade'].errors.maxLength) {
                this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
                    this.sistemaCredenciadoForm.controls['entidade'], MensagemProperties.app_rst_label_entidade,
                    this.sistemaCredenciadoForm.controls['entidade'].errors.maxLength.requiredLength);
                isValido = false;
            }
        }

        if (this.sistemaCredenciadoForm.controls['telefoneResponsavel'].invalid) {

            if (this.sistemaCredenciadoForm.controls['telefoneResponsavel'].errors.maxLength) {
                this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
                    this.sistemaCredenciadoForm.controls['telefoneResponsavel'], MensagemProperties.app_rst_telefone_responsavel,
                    this.sistemaCredenciadoForm.controls['telefoneResponsavel'].errors.maxLength.requiredLength);
                isValido = false;
            }
        }

        return isValido;
    }

    converterFormParaModel(): void {
        const formModel = this.sistemaCredenciadoForm.controls;
        this.sistemaCredenciado.cnpj = MascaraUtil.removerMascara(formModel.cnpj.value);
        this.sistemaCredenciado.nomeResponsavel = formModel.nomeResponsavel.value;
        this.sistemaCredenciado.emailResponsavel = formModel.emailResponsavel.value;
        this.sistemaCredenciado.telefoneResponsavel = MascaraUtil.removerMascara(formModel.telefoneResponsavel.value);
        this.sistemaCredenciado.sistema = formModel.sistema.value;
        this.sistemaCredenciado.entidade = formModel.entidade.value;
        this.sistemaCredenciado.dataCriacao = null;
        this.sistemaCredenciado.dataAtualizacao = null;
        this.sistemaCredenciado.dataDesativacao = null;
    }

    voltar(): void {
        this.router.navigate([`${environment.path_raiz_cadastro}/sistema-credenciado`]);
    }

    salvar(): void {
        if (this.validarCampos()) {
            this.converterFormParaModel();
            this.sistemaCredenciadoService.salvar(this.sistemaCredenciado).subscribe((retorno: any) => {
                this.mensagemSucesso(retorno['content']);
                this.voltar();
            }, (error) => {
                this.mensagemError(error['content'] || error);
            }
                , () => {
                    this.voltar();
                });
        }
    }

}
