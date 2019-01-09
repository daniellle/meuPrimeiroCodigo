import { Component, Input, OnInit, SimpleChanges, OnChanges } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';

import { ToastyService, ToastOptions } from 'ng2-toasty';

import { ValidateCPF } from 'app/compartilhado/validators/cpf.validator';
import { Usuario } from 'app/modelo/usuario.model';
import { MascaraUtil } from "../../../../compartilhado/utilitario/mascara.util";
import { MensagemProperties, RecursoPipe } from '../../../../compartilhado/utilitario/recurso.pipe';

@Component({
  selector: 'app-dados-gerais',
  templateUrl: './dados-gerais.component.html'
})
export class DadosGeraisComponent implements OnInit, OnChanges {

  usuarioForm: FormGroup;
  mascaraCpf = MascaraUtil.mascaraCpf;

  @Input() modoAlterar: boolean;
  @Input() modoConsulta: boolean;
  @Input() usuario: Usuario;

  constructor(
    private formBuilder: FormBuilder, 
    private dialogo: ToastyService, 
    private recursoPipe: RecursoPipe
  ) {}

  ngOnInit() {
    this.criarForm();
    if(this.usuario) {
      this.usuarioForm.patchValue(this.usuario)
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['usuario']) {
      this.criarForm();
      if(this.usuario){
        this.usuarioForm.patchValue(this.usuario);
      }
    }
  }

  private criarForm(): void {
      if(this.usuario.origemDados){
        this.usuarioForm = this.formBuilder.group({
            nome: [
                { value: '', disabled: this.modoAlterar || this.modoConsulta },
                [ Validators.required, Validators.maxLength(160)]
            ],
            login: [
                { value: '', disabled: this.modoAlterar || this.modoConsulta },
                [ Validators.required, ValidateCPF]
            ],
            email: [
                { value: '', disabled: this.modoAlterar || this.modoConsulta },
                [ Validators.required, Validators.maxLength(255), Validators.email ]
            ]
          });
      } else {
        this.usuarioForm = this.formBuilder.group({
            nome: [
                { value: '', disabled: this.modoConsulta },
                [ Validators.required, Validators.maxLength(160)]
            ],
            login: [
                { value: '', disabled: this.modoAlterar || this.modoConsulta },
                [ Validators.required, ValidateCPF]
            ],
            email: [
                { value: '', disabled: this.modoConsulta },
                [ Validators.required, Validators.maxLength(255), Validators.email ]
            ]
          });
      }
    
  }

  getFormValue() {
    return this.usuarioForm.getRawValue();
  }

  validarCampos(): boolean {
    let isValido: boolean = true;

    if (this.usuarioForm.controls['nome'].invalid) {
        if (this.usuarioForm.controls['nome'].errors.required) {
            this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.usuarioForm.controls['nome'],
                MensagemProperties.app_rst_labels_nome);
            isValido = false;
        }
    }

    if (this.usuarioForm.controls['login'].invalid) {
        if (this.usuarioForm.controls['login'].errors.required) {
            this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.usuarioForm.controls['login'],
                MensagemProperties.app_rst_labels_login_cpf);
            isValido = false;
        }

        if (!this.usuarioForm.controls['login'].errors.required
            && this.usuarioForm.controls['login'].errors.validCPF) {
            this.mensagemErroComParametros('app_rst_campo_invalido', this.usuarioForm.controls['login'],
                MensagemProperties.app_rst_labels_login_cpf);
            isValido = false;
        }
    }

    if (this.usuarioForm.controls['email'].invalid) {
        if (this.usuarioForm.controls['email'].errors.required) {
            this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.usuarioForm.controls['email'],
                MensagemProperties.app_rst_labels_email);
            isValido = false;
        }

        if (!this.usuarioForm.controls['email'].errors.required
            && this.usuarioForm.controls['email'].errors.validEmail) {
            this.mensagemErroComParametros('app_rst_campo_invalido', this.usuarioForm.controls['email'],
                MensagemProperties.app_rst_labels_email);
            isValido = false;
        }
    }

    return isValido;
  }

  protected mensagemErroComParametros(mensagem: string, controle?: AbstractControl, ...parametros: any[]) {
    this.mensagemError(this.recursoPipe.transform(mensagem, parametros), controle);
  }

  protected mensagemError(mensagem: string, controle?: AbstractControl) {
    if (this.dialogo) {
        this.dialogo.error(this.getMensagem(mensagem, controle));
    }
  }

  private getMensagem(mensagem: string, controle?: AbstractControl): ToastOptions {

    const configuracoes: ToastOptions = {
        title: '',
        timeout: 5000,
        msg: mensagem,
        showClose: true,
        theme: 'bootstrap',
    };

    return configuracoes;
  }

  usuarioehBarramento(): boolean{
      if(this.usuario.origemDados != null){
         //console.log("Origem de dados S4");
          return true;
      } else{
       //console.log("Origem de dados nossa");
          return false;
      }

  }

}
