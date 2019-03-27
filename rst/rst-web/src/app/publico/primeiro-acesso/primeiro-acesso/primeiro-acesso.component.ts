import { ValidateEmail } from 'app/compartilhado/validators/email.validator';
import { Trabalhador } from './../../../modelo/trabalhador.model';
import { Usuario } from './../../../modelo/usuario.model';
import { PrimeiroAcesso } from './../../../modelo/primeiro-acesso.model';
import { ValidateData, ValidateDataFutura } from 'app/compartilhado/validators/data.validator';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { TrabalhadorService } from './../../../servico/trabalhador.service';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { Router } from '@angular/router';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { IMyDateModel } from 'mydatepicker';

@Component({
  selector: 'app-primeiro-acesso',
  templateUrl: './primeiro-acesso.component.html',
  styleUrls: ['./primeiro-acesso.component.scss'],
})
export class PrimeiroAcessoComponent extends BaseComponent implements OnInit {

  public confirmarSenha: string;
  public primeiroAcesso: PrimeiroAcesso;
  public formulario: FormGroup;
  public formularioSenha: FormGroup;

  constructor(
    private router: Router,
    private trabalhadorService: TrabalhadorService,
    private formBuilder: FormBuilder,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private location: Location
    ) {
    super(bloqueioService, dialogo);
    this.createForm();
    this.createFormSenha();
  }

  ngOnInit() {
    this.confirmarSenha = null;
    this.title = MensagemProperties.app_rst_primeiro_acesso_title_primeiro_acesso_trabalhador;
  }

  voltar() {
    this.location.back();
  }

  inicializarPrimeiroAcesso() {
    this.primeiroAcesso = new PrimeiroAcesso(new Trabalhador(), new Usuario());
  }

  createForm() {
    this.formulario = this.formBuilder.group({
      cpf: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
        ]),
      ],
      dataNascimento: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          ValidateData,
          ValidateDataFutura,
        ]),
      ],
    });
  }

  createFormSenha() {
    this.formularioSenha = this.formBuilder.group({
      senha: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.minLength(8),
        ]),
      ],
      confirmarSenha: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.minLength(8),
        ]),
      ],
      email: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(50),
          ValidateEmail,
        ]),
      ],
    });
  }

  removerMascara() {
    return MascaraUtil.removerMascara(this.formulario.controls['cpf'].value);
  }

  formatarData() {
    return this.convertDateToString(this.formulario.controls['dataNascimento'].value.date);
  }

  pesquisar(event: IMyDateModel) {
    if (this.verificarCamposPesquisa(event)) {
      this.inicializarPrimeiroAcesso();
      this.createFormSenha();
      const cpf = this.removerMascara();
      const data = this.formatarData();
      this.primeiroAcesso.trabalhador = new Trabalhador();
      this.trabalhadorService.buscarTrabalhadorCpfDataNascimento(cpf, data).subscribe((retorno: Trabalhador) => {
        this.primeiroAcesso.trabalhador = retorno;
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  public verificarCamposPesquisa(event: IMyDateModel): boolean {
    let verificador = true;

    if (this.formulario.controls['cpf'].invalid) {
      if (this.formulario.controls['cpf'].errors.required) {
        verificador = false;
      }
    }

    if (!this.isVazia(this.formulario.controls['cpf'].value)) {
      if (this.formulario.controls['cpf'].value.length < 14) {
        verificador = false;
      }
    }

    if (!this.isVazia(event) && !this.isVazia(event.jsdate)) {
      this.formulario.patchValue({
        dataNascimento: event,
      });
    } else {
      this.primeiroAcesso = undefined;
      verificador = false;
    }

    if (this.formulario.controls['dataNascimento'].invalid) {
      if (this.formulario.controls['dataNascimento'].errors.required) {
        verificador = false;
      } else if (this.formulario.controls['dataNascimento'].errors.validData) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['dataNascimento'],
          MensagemProperties.app_rst_labels_data_nascimento);
        verificador = false;
      } else if (this.formulario.controls['dataNascimento'].errors.validDataFutura) {
        this.mensagemErroComParametros('app_rst_labels_data_futura', this.formulario.controls['dataNascimento'],
          MensagemProperties.app_rst_labels_data_nascimento);
        verificador = false;
      }
    }
    return verificador;
  }

  existeTrabalhador(): boolean {
    return this.primeiroAcesso !== undefined && this.primeiroAcesso.trabalhador !== undefined
      && this.primeiroAcesso.trabalhador.id !== undefined;
  }

}