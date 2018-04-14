import { ValidarEmail } from 'app/compartilhado/validators/email.validator';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { SolicitarEmailService } from './../../servico/solicitar-email.service';
import { TrabalhadorService } from 'app/servico/trabalhador.service';
import { SolicitacaoEmail } from './../../modelo/solicitacao-email';
import { Trabalhador } from './../../modelo/trabalhador.model';
import { MensagemProperties } from './../../compartilhado/utilitario/recurso.pipe';
import { DialogService } from 'ng2-bootstrap-modal';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../servico/bloqueio.service';
import { AutenticacaoService } from './../../servico/autenticacao.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-solicitar-email',
  templateUrl: './solicitar-email.component.html',
  styleUrls: ['./solicitar-email.component.scss']
})
export class SolicitarEmailComponent extends BaseComponent implements OnInit {
  public id: number;
  public solicitacaoEmail: SolicitacaoEmail;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private autenticacaoService: AutenticacaoService,
    private route: ActivatedRoute,
    protected bloqueioService: BloqueioService,
    protected formBuilder: FormBuilder,
    protected dialogo: ToastyService,
    private dialogService: DialogService,
    private trabalhadorService: TrabalhadorService,
    private solicitarEmailService: SolicitarEmailService,
  ) {
    super(bloqueioService, dialogo);
    this.solicitacaoEmail = new SolicitacaoEmail();
    this.title = MensagemProperties.app_rst_solicitar_email_sesi;
    this.activatedRoute.params.subscribe((params) => {
      this.id = params['id'];
      if (this.id) {
        this.buscarTrabalhadorPorId();
      }
    });
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
  ngOnInit() {

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
    if (this.solicitacaoEmail.nome) {
      return true;
    } else {
      this.mensagemError('O Campo CPF é obrigatorio');
      return false;
    }
  }
  isEmailValido(): Boolean {
    let isValido = true;
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
      if (this.solicitacaoEmail.telefone && MascaraUtil.removerMascara(this.solicitacaoEmail.telefone).length < 10) {
        this.mensagemError(MensagemProperties.app_rst_msg_telefone_campo_invalido_solicitar_email);
        isValido = false;
      }
    }
    return isValido;
  }

  buscarTrabalhadorPorId() {
    // this.trabalhadorService.buscarPorId(this.id.toString()).subscribe((retorno: Trabalhador) => {
    //   this.solicitacaoEmail.cpf = retorno.cpf;
    //   this.solicitacaoEmail.nome = retorno.nome;
    //   if (retorno.listaEmailTrabalhador) {
    //     this.solicitacaoEmail.email = retorno.listaEmailTrabalhador[0].email.descricao;
    //   }
    //   if (retorno.listaTelefoneTrabalhador) {
    //     this.solicitacaoEmail.telefone = retorno.listaTelefoneTrabalhador[0].telefone.numero;
    //     this.mudarMascaraBanco();
    //   }
    // });
  }

  mandarEmail() {
    // if (this.isTelefoneValido() && this.isEmailValido() && this.isNomeValido() && this.isCpfValido()) {
    //   this.solicitarEmailService.mandarEmail(this.solicitacaoEmail).subscribe((retorno) => {
    //     this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
    //   }, (error) => {
    //     this.mensagemError(error);
    //   });
    // }
  }
}
