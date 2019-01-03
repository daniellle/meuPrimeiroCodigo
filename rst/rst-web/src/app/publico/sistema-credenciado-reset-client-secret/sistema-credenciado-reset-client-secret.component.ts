import { Component, OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SistemaCredenciadoService } from 'app/servico/sistema-credenciado.service';
import { DialogService } from 'ng2-bootstrap-modal';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { SistemaCredenciado } from 'app/modelo/sistema-credenciado.model';

@Component({
    selector: 'app-sistema-credenciado-reset-client-secret',
    templateUrl: './sistema-credenciado-reset-client-secret.component.html',
    styleUrls: ['./sistema-credenciado-reset-client-secret.component.scss']
})
export class SistemaCredenciadoResetClientSecretComponent extends BaseComponent implements OnInit {

    public resetCredencialForm: FormGroup;
    public sistemaCredenciado: SistemaCredenciado;

    constructor(
        private router: Router,
        private sistemaCredenciadoService: SistemaCredenciadoService,
        private route: ActivatedRoute,
        protected bloqueioService: BloqueioService,
        protected formBuilder: FormBuilder,
        protected dialogo: ToastyService,
        private dialogService: DialogService) {
        super(bloqueioService, dialogo);
        this.title = MensagemProperties.app_rst_sistema_credenciado_reset_client_secret;
        this.criarForm();
    }

    ngOnInit() {
    }

    criarForm(): void {
        this.resetCredencialForm = this.formBuilder.group({
            clientId: [
                { value: null, disabled: false },
                Validators.compose([
                    Validators.required, Validators.maxLength(32), Validators.minLength(32),
                ]),
            ],
        });
    }

    validarForm(): boolean {
        let isValido = true;

        if (this.resetCredencialForm.controls['clientId'].invalid) {
            if (this.resetCredencialForm.controls['clientId'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.resetCredencialForm.controls['clientId'],
                    MensagemProperties.app_rst_label_client_id);
                isValido = false;
            }

            if (this.resetCredencialForm.controls['clientId'].errors.maxLength) {
                this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
                    this.resetCredencialForm.controls['clientId'], MensagemProperties.app_rst_label_client_id,
                    this.resetCredencialForm.controls['clientId'].errors.maxLength.requiredLength);
                isValido = false;
            }

            if (this.resetCredencialForm.controls['clientId'].errors.minLength) {
                this.mensagemErroComParametros('app_rst_quantidade_caracteres_minimos_invalido',
                    this.resetCredencialForm.controls['clientId'], MensagemProperties.app_rst_label_client_id,
                    this.resetCredencialForm.controls['clientId'].errors.minLength.requiredLength);
                isValido = false;
            }
        }
        return isValido;
    }

    reset(): void {
        if (this.validarForm()) {
            const sistemaCredenciado: SistemaCredenciado = new SistemaCredenciado();
            sistemaCredenciado.clientId = this.resetCredencialForm.get('clientId').value;
            this.sistemaCredenciadoService.resetarClientSecret(sistemaCredenciado)
                .subscribe((response) => {
                    this.mensagemSucesso(response['content']);
                }, (error) => {
                    this.mensagemError(error['content'] || error);
                });
        }
    }
}
