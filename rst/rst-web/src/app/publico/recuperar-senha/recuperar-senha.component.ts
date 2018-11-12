import { AutenticacaoService } from './../../servico/autenticacao.service';
import { MensagemProperties } from './../../compartilhado/utilitario/recurso.pipe';
import { ValidateEmail } from './../../compartilhado/validators/email.validator';
import { DialogService } from 'ng2-bootstrap-modal';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../servico/bloqueio.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { BaseComponent } from './../../componente/base.component';
import { Component, OnInit } from '@angular/core';
import { ParametroService } from 'app/servico/parametro.service';
import { Parametro } from 'app/modelo/parametro.model';

@Component({
  selector: 'app-recuperar-senha',
  templateUrl: './recuperar-senha.component.html',
  styleUrls: ['./recuperar-senha.component.scss'],
})
export class RecuperarSenhaComponent extends BaseComponent implements OnInit {

  public form: FormGroup;
  public email: String;
  public telefone = '';
  public central = false;

  constructor(
    private router: Router,
    private autenticacaoService: AutenticacaoService,
    private route: ActivatedRoute,
    protected bloqueioService: BloqueioService,
    protected formBuilder: FormBuilder,
    protected dialogo: ToastyService,
    private dialogService: DialogService,
    private parametroService: ParametroService,
  ) {
    super(bloqueioService, dialogo);
    this.title = MensagemProperties.app_rst_recupera_senha_titulo;
    this.criarForm();
    this.buscarTelefone();
  }

  ngOnInit() {
    this.buscarTelefone();
  }

  enviar(): void {
    if (this.validarEmail()) {
      this.autenticacaoService.recuperarSenha(this.form.controls.email.value).subscribe((retorno: any) => {
        this.mensagemSucesso(MensagemProperties.app_rst_recupera_senha_sucesso);
        this.central = false;
      }, (error) => {
        this.mensagemError(error);
        this.central = true;
      });
    }
  }

  criarForm(): void {
    this.form = this.formBuilder.group({
      email: [
        { value: null, disabled: false },
        Validators.compose([
          Validators.required,
          Validators.maxLength(255),
          ValidateEmail,
        ]),
      ],
    });
  }

  validarEmail(): boolean {
    let isValido = true;

    if (this.form.controls['email'].invalid) {
      if (this.form.controls['email'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.form.controls['email'],
          MensagemProperties.app_rst_labels_email);
        isValido = false;
      } else if (this.form.controls['email'].errors.validEmail) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.form.controls['email'],
            MensagemProperties.app_rst_labels_email);
        isValido = false;
      }
    }

    return isValido;
  }

  buscarTelefone() {
    this.parametroService.buscarTelefoneCentralRelacionamento().subscribe((response: Parametro) => {
      this.telefone = response.valor;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  showCardCentral():boolean{
    return this.central;
  }

}
