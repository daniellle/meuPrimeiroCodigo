import { Telefone } from './../../../modelo/telefone.model';
import { TelefoneTrabalhador } from './../../../modelo/telefone-trabalhador.model';
import { SolicitarEmailService } from './../../../servico/solicitar-email.service';
import { ValidarEmail } from 'app/compartilhado/validators/email.validator';
import { SolicitacaoEmail } from './../../../modelo/solicitacao-email';
import { MascaraUtil } from './../../../compartilhado/utilitario/mascara.util';
import { TipoEmail } from 'app/modelo/enum/enum-tipo-email.model';
import { EnumValues } from 'enum-values';
import { Email } from './../../../modelo/email.model';
import { Router } from '@angular/router';
import { EmailTrabalhador } from 'app/modelo/email-trabalhador.model';
import { Parametro } from './../../../modelo/parametro.model';
import { ParametroService } from './../../../servico/parametro.service';
import { environment } from './../../../../environments/environment.prod';
import { TrabalhadorService } from 'app/servico/trabalhador.service';
import { PrimeiroAcesso } from './../../../modelo/primeiro-acesso.model';
import { Trabalhador } from './../../../modelo/trabalhador.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { FormGroup, FormBuilder } from '@angular/forms';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
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
  private indiceEmail: number;

  public solicitarEmail = false;
  public solicitacaoEmail: SolicitacaoEmail;
  private indiceTelefone: number;

  public telefoneTrabalhador: TelefoneTrabalhador;
  public existeTelefone = false;

  @ViewChild('modalTermoUso') modalTermoUso;

  constructor(
    public activeModal: NgbActiveModal,
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private trabalhadorService: TrabalhadorService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private dialogService: DialogService,
    private parametroService: ParametroService,
    private solicitarEmailService: SolicitarEmailService,
    private router: Router,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
  }

  verificarCamposSenha() {
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

    return verificador;
  }

  abrirTermo() {
    this.buscarTermoUso();
    if (this.verificarCamposSenha()) {
      const modalRef = this.modalService.open(this.modalTermoUso, { size: 'lg' });
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
      this.preencherEmail();
      this.formulario.controls['cpf'].disable();
      this.formulario.controls['dataNascimento'].disable();
    }, (error) => {
      this.mensagemError(error);
    });
  }

  preencherEmail() {
    this.existeEmail = false;
    // Verifica se trabalhador tem email para receber notificação. Se não tiver busca o primeiro na lista
    if (this.primeiroAcesso.trabalhador.listaEmailTrabalhador) {
      this.existeEmail = true;
      let notificacao = false;
      this.primeiroAcesso.trabalhador.listaEmailTrabalhador.forEach((email) => {
        if (email.email.notificacao) {
          notificacao = true;
          this.emailTrabalhador = email;
          this.formularioSenha.patchValue({
            email: email.email.descricao,
          });
        }
      });
      if (!notificacao) {
        this.emailTrabalhador = this.primeiroAcesso.trabalhador.listaEmailTrabalhador[0];
        this.formularioSenha.patchValue({
          email: this.primeiroAcesso.trabalhador.listaEmailTrabalhador[0].email.descricao,
        });
      }
    }
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
      this.prepareSave(false);
      this.trabalhadorService.salvarPrimeiroAcesso(this.primeiroAcesso).subscribe((response: PrimeiroAcesso) => {
        this.primeiroAcesso = response;
        window.location.href = environment.url_portal;
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  getPattern(): string {
    return '(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$';
  }

  validarPassPattern(senha: string): boolean {
    return new RegExp(this.getPattern()).test(senha);
  }

  solicitarEmailSesi() {
    const cpf = this.removerMascara();
    const data = this.formatarData();
    this.trabalhadorService.buscarTrabalhadorCpfDataNascimento(cpf, data).subscribe((retorno: Trabalhador) => {
      // this.trabalhadorService.buscarPorId(this.primeiroAcesso.trabalhador.id.toString()).subscribe((retorno: Trabalhador) => {
      this.existeEmail = false;
      this.existeTelefone = false;

      this.solicitacaoEmail = new SolicitacaoEmail();
      this.solicitacaoEmail.cpf = retorno.cpf;
      this.solicitacaoEmail.nome = retorno.nome;

      // Verifica se trabalhador tem email para receber notificação. Se não tiver busca o primeiro na lista
      if (retorno.listaEmailTrabalhador) {
        this.existeEmail = true;
        let notificacao = false;
        retorno.listaEmailTrabalhador.forEach((email) => {
          if (email.email.notificacao) {
            notificacao = true;
            this.solicitacaoEmail.email = email.email.descricao;
            this.emailTrabalhador = email;
          }
        });
        if (!notificacao) {
          this.solicitacaoEmail.email = retorno.listaEmailTrabalhador[0].email.descricao;
          this.emailTrabalhador = retorno.listaEmailTrabalhador[0];
        }
      }

      // Verifica se trabalhador tem telefone como contato. Se não tiver busca o primeiro na lista
      if (retorno.listaTelefoneTrabalhador) {
        this.existeTelefone = true;
        let contato = false;
        retorno.listaTelefoneTrabalhador.forEach((tel) => {
          if (tel.telefone.contato) {
            contato = true;
            this.solicitacaoEmail.telefone = tel.telefone.numero;
            this.telefoneTrabalhador = tel;
          }
        });
        if (!contato) {
          this.solicitacaoEmail.telefone = retorno.listaTelefoneTrabalhador[0].telefone.numero;
          this.telefoneTrabalhador = retorno.listaTelefoneTrabalhador[0];
        }
        this.mudarMascaraBanco();
      }
      this.solicitarEmail = true;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  mudarMascara(event: any) {
    if (this.solicitacaoEmail.telefone.length <= 14) {
      this.mascaraTelFixo();
    } else {
      this.mascaraCelular();
    }
  }

  mudarMascaraBanco() {
    if (this.solicitacaoEmail.telefone.length <= 10) {
      this.mascaraTelFixo();
    } else {
      this.mascaraCelular();
    }
  }

  mascaraTelFixo() {
    this.solicitacaoEmail.telefone = MascaraUtil.removerMascara(this.solicitacaoEmail.telefone);
    this.solicitacaoEmail.telefone = this.solicitacaoEmail.telefone.replace(/\W/g, '');
    this.solicitacaoEmail.telefone = this.solicitacaoEmail.telefone.replace(/^(\d{2})(\d)/, '($1) $2');
    this.solicitacaoEmail.telefone = this.solicitacaoEmail.telefone.replace(/(\d{4})(\d)/, '$1-$2');
    this.solicitacaoEmail.telefone = this.solicitacaoEmail.telefone.replace(/(\d{4})$/, '$1');
  }

  mascaraCelular() {
    this.solicitacaoEmail.telefone = MascaraUtil.removerMascara(this.solicitacaoEmail.telefone);
    this.solicitacaoEmail.telefone = this.solicitacaoEmail.telefone.replace(/\W/g, '');
    this.solicitacaoEmail.telefone = this.solicitacaoEmail.telefone.replace(/^(\d{2})(\d)/, '($1) $2');
    this.solicitacaoEmail.telefone = this.solicitacaoEmail.telefone.replace(/(\d{5})(\d)/, '$1-$2');
    this.solicitacaoEmail.telefone = this.solicitacaoEmail.telefone.replace(/(\d{4})$/, '$1');
  }

  isNomeValido() {
    if (this.solicitacaoEmail.nome) {
      return true;
    } else {
      this.mensagemError('O Campo nome é obrigatorio');
      return false;
    }
  }

  isCpfValido() {
    if (this.solicitacaoEmail.cpf) {
      return true;
    } else {
      this.mensagemError('O Campo CPF é obrigatorio');
      return false;
    }
  }

  isEmailValido(): Boolean {
    let isValido = true;
    if (!this.solicitacaoEmail.email) {
      this.mensagemError(MensagemProperties.app_rst_email_campo_obrigatorio_solicitar_email);
      isValido = false;
    }
    if (this.solicitacaoEmail.email) {
      if (!ValidarEmail(this.solicitacaoEmail.email)) {
        this.mensagemError(MensagemProperties.app_rst_email_campo_invalido_solicitar_email);
        isValido = false;
      }
    }
    return isValido;
  }

  isTelefoneValido(): Boolean {
    let isValido = true;
    if (this.solicitacaoEmail.telefone) {
      if (MascaraUtil.removerMascara(this.solicitacaoEmail.telefone).length > 11) {
        this.mensagemError(MensagemProperties.app_rst_msg_telefone_campo_invalido_solicitar_email);
        isValido = false;
      }
    }
    return isValido;
  }

  prepareSave(isSolicitarEmail: boolean) {
    // Adicionar Email
    if (this.existeEmail) {
      // Atualiza trabalhador com novo email
      if (isSolicitarEmail) {
        this.emailTrabalhador.email.descricao = this.solicitacaoEmail.email;
      } else {
        this.emailTrabalhador.email.descricao = this.formularioSenha.controls['email'].value;
      }
      this.primeiroAcesso.trabalhador.listaEmailTrabalhador.forEach((email, index) => {
        if (email.id === this.emailTrabalhador.id) {
          this.indiceEmail = index;
        }
      });
      if (this.indiceEmail >= 0) {
        this.primeiroAcesso.trabalhador.listaEmailTrabalhador[this.indiceEmail] = this.emailTrabalhador;
      }
    } else {
      // Adiciona novo email
      this.primeiroAcesso.trabalhador.listaEmailTrabalhador = new Array<EmailTrabalhador>();
      this.primeiroAcesso.trabalhador.listaEmailTrabalhador.push(new EmailTrabalhador());
      this.primeiroAcesso.trabalhador.listaEmailTrabalhador[0].email = new Email();
      this.primeiroAcesso.trabalhador.listaEmailTrabalhador[0].email.tipo = EnumValues.getNameFromValue(TipoEmail, TipoEmail.P);
      this.primeiroAcesso.trabalhador.listaEmailTrabalhador[0].email.notificacao = true;
      if (isSolicitarEmail) {
        this.primeiroAcesso.trabalhador.listaEmailTrabalhador[0].email.descricao = this.solicitacaoEmail.email;
      } else {
        this.primeiroAcesso.trabalhador.listaEmailTrabalhador[0].email.descricao = this.formularioSenha.controls['email'].value;
      }
    }

    // Adicionar Telefone
    if (isSolicitarEmail && this.solicitacaoEmail.telefone) {
      if (this.existeTelefone) {
        // Atualiza trabalhador com novo telefone
        this.telefoneTrabalhador.telefone.numero = MascaraUtil.removerMascara(this.solicitacaoEmail.telefone);
        this.primeiroAcesso.trabalhador.listaTelefoneTrabalhador.forEach((email, index) => {
          if (email.id === this.telefoneTrabalhador.id) {
            this.indiceTelefone = index;
          }
        });
        if (this.indiceTelefone >= 0) {
          this.primeiroAcesso.trabalhador.listaTelefoneTrabalhador[this.indiceTelefone]
            = this.telefoneTrabalhador;
        }
      } else {
        // Adiciona novo telefone
        this.primeiroAcesso.trabalhador.listaTelefoneTrabalhador = new Array<TelefoneTrabalhador>();
        this.primeiroAcesso.trabalhador.listaTelefoneTrabalhador.push(new TelefoneTrabalhador());
        this.primeiroAcesso.trabalhador.listaTelefoneTrabalhador[0].telefone = new Telefone();
        this.primeiroAcesso.trabalhador.listaTelefoneTrabalhador[0].telefone.numero
          = MascaraUtil.removerMascara(this.solicitacaoEmail.telefone);
      }
    }

    // Usuario
    this.primeiroAcesso.usuario.nome = this.primeiroAcesso.trabalhador.nome;
    this.primeiroAcesso.usuario.login = this.primeiroAcesso.trabalhador.cpf;
    this.primeiroAcesso.usuario.senha = this.formularioSenha.controls['senha'].value;
    this.primeiroAcesso.usuario.dados = undefined;
    if (isSolicitarEmail) {
      this.primeiroAcesso.usuario.email = this.solicitacaoEmail.email;
    } else {
      this.primeiroAcesso.usuario.email = this.formularioSenha.controls['email'].value;
    }

    if (isSolicitarEmail) {
      this.primeiroAcesso.solicitacaoEmail = this.solicitacaoEmail;
    } else {
      this.solicitacaoEmail = null;
    }
  }

  confirmarSolicitacaoEmailSesi() {
    if (this.isTelefoneValido() && this.isEmailValido()
      && this.isNomeValido() && this.isCpfValido()) {
      this.prepareSave(true);
      this.solicitarEmailService.mandarEmail(this.primeiroAcesso).subscribe((retorno) => {
        window.location.href = environment.url_portal;
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

}
