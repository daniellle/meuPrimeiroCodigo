import { environment } from './../../../environments/environment';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { DialogService } from 'ng2-bootstrap-modal';
import { ToastyService } from 'ng2-toasty';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BloqueioService } from './../../servico/bloqueio.service';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { Router, ActivatedRoute } from '@angular/router';
import { BaseComponent } from './../../componente/base.component';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-alterar-senha',
  templateUrl: './alterar-senha.component.html',
  styleUrls: ['./alterar-senha.component.scss'],
})
export class AlterarSenhaComponent extends BaseComponent implements OnInit {

  public alterarSenhaForm: FormGroup;
  hash: string;
  isHashValido: boolean;

  constructor(
    private router: Router,
    private autenticacaoService: AutenticacaoService,
    private route: ActivatedRoute,
    protected bloqueioService: BloqueioService,
    protected formBuilder: FormBuilder,
    protected dialogo: ToastyService,
    private dialogService: DialogService,
  ) {
    super(bloqueioService, dialogo);
    this.title = MensagemProperties.app_rst_alterar_senha_titulo;
    this.criarForm();
  }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.hash = params['hash'];

      if (!this.hash) {
        window.location.href = environment.url_portal;
      }else{
          this.validarHash();
      }
    });
  }

  criarForm(): void {
    this.alterarSenhaForm = this.formBuilder.group({
      senha: [
        { value: null, disabled: false },
        Validators.compose([
          Validators.required,
        ]),
      ],
      confirmaSenha: [
        { value: null, disabled: false },
        Validators.compose([
          Validators.required,
        ]),
      ],
    });
  }

  verificarSenha(senha: string, confirmaSenha: string) {
    return senha === confirmaSenha;
  }

  validarPassPattern(senha: string): boolean {
    return new RegExp(this.getPattern()).test(senha);
  }

  validarForm(): boolean {
    let isValido = true;

    if (this.alterarSenhaForm.controls['senha'].invalid) {
      if (this.alterarSenhaForm.controls['senha'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.alterarSenhaForm.controls['senha'],
          MensagemProperties.app_rst_labels_nova_senha);
        isValido = false;
      } else if (this.alterarSenhaForm.controls['senha'].errors.minlength) {
        isValido = false;
      }
    } else if (!this.validarPassPattern(this.alterarSenhaForm.controls['senha'].value)) {
      isValido = false;
    }
    return isValido;
  }

  cadastrarNovaSenha(): void {
    if (!this.validarForm()) {
      this.mensagemError(MensagemProperties.app_rst_alterar_senha_pattern_invalido);
    } else if (!this.verificarSenha(this.alterarSenhaForm.controls.senha.value,
      this.alterarSenhaForm.controls.confirmaSenha.value)) {
      this.mensagemError(MensagemProperties.app_rst_alterar_senha_confirmar_invalido);
    } else {
      this.autenticacaoService.alterarSenha(this.hash, this.alterarSenhaForm.controls.senha.value)
      .subscribe((retorno: any) => {
        this.mensagemSucesso(MensagemProperties.app_rst_alterar_senha_sucesso);
        window.location.href = environment.url_portal;
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  getPattern(): string {
    return '(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$';
  }

    validarHash(): void {
        this.autenticacaoService.validarHash(this.hash)
            .subscribe((retorno: any) => {
                this.isHashValido = true;
            }, (error) => {
                this.mensagemError(error);
                this.isHashValido = false;
            });
    }

  enviarEmailHash(): void{
      this.autenticacaoService.enivarEmailHash(this.hash)
          .subscribe((retorno: any) => {
              this.mensagemSucesso(retorno);
              window.location.href = environment.url_portal;
          }, (error) => {
              this.mensagemError(error);
              window.location.href = environment.url_portal;
          });
  }
}
