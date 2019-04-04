import { TelefoneTrabalhador } from './../../../modelo/telefone-trabalhador.model';
import { ValidarEmail } from 'app/compartilhado/validators/email.validator';
import { SolicitacaoEmail } from './../../../modelo/solicitacao-email';
import { MascaraUtil } from './../../../compartilhado/utilitario/mascara.util';
import { TipoEmail } from 'app/modelo/enum/enum-tipo-email.model';
import { EnumValues } from 'enum-values';
import { Email } from './../../../modelo/email.model';
import { EmailTrabalhador } from 'app/modelo/email-trabalhador.model';
import { Parametro } from './../../../modelo/parametro.model';
import { ParametroService } from './../../../servico/parametro.service';
import { TrabalhadorService } from 'app/servico/trabalhador.service';
import { PrimeiroAcesso } from './../../../modelo/primeiro-acesso.model';
import { Trabalhador } from './../../../modelo/trabalhador.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { FormGroup } from '@angular/forms';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit, ViewChild, Input, Output } from '@angular/core';

@Component({
  selector: 'app-termo-uso-modal',
  templateUrl: './termo-uso-modal.component.html',
  styleUrls: ['./termo-uso-modal.component.scss'],
})
export class TermoUsoModalComponent extends BaseComponent implements OnInit {

  public termoUso = '';
  public sucesso = false;

  @Input()
  adicionar: any;

  @Input() @Output()
  public formulario: FormGroup;

  @Input() @Output()
  public formularioSenha: FormGroup;

  @Input() @Output()
  public primeiroAcesso: PrimeiroAcesso;

  @Input() @Output()
  public aceitouTermo = false;

  public emailTrabalhador: EmailTrabalhador;
  public existeEmail = false;

  public solicitarEmail = false;
  public solicitacaoEmail: SolicitacaoEmail;

  public telefoneTrabalhador: TelefoneTrabalhador;
  public existeTelefone = false;

  @ViewChild('modalTermoUso') modalTermoUso;
  public show  = false;
  constructor(
    public activeModal: NgbActiveModal,
    private modalService: NgbModal,
    private trabalhadorService: TrabalhadorService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private parametroService: ParametroService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
  }

  abrirTermo() {
    this.buscarTermoUso();
    if (this.verificarCamposSenha()) {
      const modalRef = this.modalService.open(this.modalTermoUso);
      modalRef.result.then((result) => {
        this.aceitouTermo = true;
        this.cadastrarPrimeiroAcesso();
      }, (reason) => {
      });
    }
  }

  buscarTermoUso() {
    this.parametroService.buscarTermoUso().subscribe((response: Parametro) => {
      this.termoUso = response.valor;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  removerMascara() {
    return MascaraUtil.removerMascara(this.formulario.controls['cpf'].value);
  }

  formatarData() {
    return this.convertDateToString(this.formulario.controls['dataNascimento'].value.date);
  }

  cadastrarPrimeiroAcesso() {
    const cpf = this.removerMascara();
    const data = this.formatarData();
    this.trabalhadorService.buscarTrabalhadorCpfDataNascimento(cpf, data).subscribe((retorno: Trabalhador) => {
      this.primeiroAcesso.trabalhador = retorno;
      this.formulario.controls['cpf'].disable();
      this.formulario.controls['dataNascimento'].disable();
    }, (error) => {
      this.mensagemError(error);
    });
  }

 verificarCampoEmail(): boolean {
    let retorno = true;

    if (!this.formularioSenha.controls['email'].value) {
      this.mensagemErroComParametros('app_rst_campo_obrigatorio',
        this.formulario.controls['email'], MensagemProperties.app_rst_labels_email);
      retorno = false;
    }

    if (this.formularioSenha.controls['email'].value
      && this.formularioSenha.controls['email'].invalid) {
      this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['email'], MensagemProperties.app_rst_labels_email);
      retorno = false;
    }

    return retorno;
  }

  confirmarPrimeiroAcesso() {
    if (this.verificarCampoEmail()) {
      this.prepareSave();
      this.trabalhadorService.salvarPrimeiroAcesso(this.primeiroAcesso).subscribe((response: PrimeiroAcesso) => {
        this.primeiroAcesso = response;
        this.formularioSenha.controls['email'].disable();
        this.mensagemSucesso("Trabalhador fez o primeiro acesso com sucesso.");
        this.sucesso = true;
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  getPattern(): string {
    return '(?=^.{8,50}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$';
  }

  validarPassPattern(senha: string): boolean {
    return new RegExp(this.getPattern()).test(senha);
  }

  prepareSave() {
    this.primeiroAcesso.trabalhador.listaEmailTrabalhador = new Array<EmailTrabalhador>();
    this.primeiroAcesso.trabalhador.listaEmailTrabalhador.push(new EmailTrabalhador());
    this.primeiroAcesso.trabalhador.listaEmailTrabalhador[0].email = new Email();
    this.primeiroAcesso.trabalhador.listaEmailTrabalhador[0].email.tipo = EnumValues.getNameFromValue(TipoEmail, TipoEmail.P);
    this.primeiroAcesso.trabalhador.listaEmailTrabalhador[0].email.notificacao = true;
    this.primeiroAcesso.trabalhador.listaEmailTrabalhador[0].email.descricao = this.formularioSenha.controls['email'].value;
     
     // Usuario
    this.primeiroAcesso.usuario.nome = this.primeiroAcesso.trabalhador.nome;
    this.primeiroAcesso.usuario.login = this.primeiroAcesso.trabalhador.cpf;
    this.primeiroAcesso.usuario.senha = this.formularioSenha.controls['senha'].value;
    this.primeiroAcesso.usuario.email = this.formularioSenha.controls['email'].value;
    this.primeiroAcesso.usuario.dados = undefined;
   
  }

  showModal() {
   if (this.show) {
    this.show = false;
   } else {
    this.show = true;
   }
 }

 private verificarCamposSenha() {
    let verificador = true;
    if (this.formularioSenha.controls['senha'].invalid) {
      if (this.formularioSenha.controls['senha'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.formularioSenha.controls['senha'],
          MensagemProperties.app_rst_labels_senha);
        verificador = false;
      }

      if (this.formularioSenha.controls['senha'].errors.minlength) {
        this.mensagemError(this.recursoPipe.transform('app_rst_quantidade_caracteres_minimos_invalido',
          MensagemProperties.app_rst_labels_senha,
          this.formularioSenha.controls['senha'].errors.minlength.requiredLength));
        verificador = false;
      }
    }

    if (this.formularioSenha.controls['confirmarSenha'].invalid) {
      if (this.formularioSenha.controls['confirmarSenha'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.formularioSenha.controls['confirmarSenha'],
          MensagemProperties.app_rst_labels_confirmar_senha);
        verificador = false;
      }

      if (this.formularioSenha.controls['confirmarSenha'].errors.minlength) {
        this.mensagemError(this.recursoPipe.transform('app_rst_quantidade_caracteres_minimos_invalido',
          MensagemProperties.app_rst_labels_confirmar_senha,
          this.formularioSenha.controls['confirmarSenha'].errors.minlength.requiredLength));
        verificador = false;
      }
    }

    if (!this.formularioSenha.controls['senha'].invalid && !this.formularioSenha.controls['confirmarSenha'].invalid) {
      if (this.formularioSenha.controls['senha'].value !== this.formularioSenha.controls['confirmarSenha'].value) {
        this.mensagemError(MensagemProperties.app_rst_primeiro_acesso_campos_senha_confirmar_senha_diferentes);
        verificador = false;
      }
    }

    if (verificador && !this.validarPassPattern(this.formularioSenha.controls['senha'].value)) {
      this.mensagemError(MensagemProperties.app_rst_alterar_senha_pattern_invalido);
      verificador = false;
    }

    if (verificador && !this.validarPassPattern(this.formularioSenha.controls['confirmarSenha'].value)) {
      this.mensagemError(MensagemProperties.app_rst_alterar_senha_confirmar_invalido);
      verificador = false;
    }

    return verificador;
  }
}
