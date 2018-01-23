import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { environment } from './../../../../environments/environment';
import { DatePicker } from './../../../compartilhado/utilitario/date-picker';
import { Component, OnInit, ViewChild } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DialogService } from 'ng2-bootstrap-modal';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RedeCredenciadaService } from 'app/servico/rede-credenciada.service';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { ValidateData, ValidateDataFutura } from 'app/compartilhado/validators/data.validator';
import { RedeCredenciada } from 'app/modelo/rede-credenciada.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ValidateCNPJ } from 'app/compartilhado/validators/cnpj.validator';
import { TipoEmpresa } from 'app/modelo/tipo-empresa.model';
import { PorteEmpresa } from 'app/modelo/porte-empresa.model';
import { PorteEmpresaService } from 'app/servico/porte-empresa.service';
import { TipoEmpresaService } from 'app/servico/tipo-empresa.service';
import { ValidarNit } from 'app/compartilhado/validators/nit.validator';
import { ValidateEmail } from 'app/compartilhado/validators/email.validator';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { EnumValues } from 'enum-values';
import { TipoEmail } from 'app/modelo/enum/enum-tipo-email.model';
import { EmailRedeCredenciada } from 'app/modelo/email-rede-credenciada.model';
import { TelefoneRedeCredenciada } from 'app/modelo/telefone-rede-credenciada.model';
import { EnderecoRedeCredenciada } from 'app/modelo/endereco-rede-credenciada.model';
import { Segmento } from 'app/modelo/segmento.model';
import { Seguranca } from '../../../compartilhado/utilitario/seguranca.model';

@Component({
  selector: 'app-cadastro-rede-credenciada',
  templateUrl: './cadastro-rede-credenciada.component.html',
  styleUrls: ['./cadastro-rede-credenciada.component.scss'],
})
export class CadastroRedeCredenciadaComponent extends BaseComponent implements OnInit {

  public redeCredenciada: RedeCredenciada;
  public formulario: FormGroup;
  public listaTipoEmpresas: TipoEmpresa[];
  public listaPorteEmpresas: PorteEmpresa[];
  public segmento: Segmento;
  public showDataDesligamento: boolean;

  @ViewChild('modalEndereco') modalEndereco: NgbModal;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: RedeCredenciadaService,
    private formBuilder: FormBuilder,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private modalService: NgbModal,
    private dialogService: DialogService,
    private porteEmpresaService: PorteEmpresaService,
    private tipoEmpresaService: TipoEmpresaService,
  ) {
    super(bloqueioService, dialogo);
    this.redeCredenciada = new RedeCredenciada();
    this.tipoTela();
    this.createForm();
    this.carregarTela();
    this.carregarCombos();

    this.redeCredenciada.segmento = new Segmento();
  }

  ngOnInit() {
  }

  carregarCombos() {
    this.porteEmpresaService.pesquisarTodos().subscribe((retorno: PorteEmpresa[]) => {
      this.listaPorteEmpresas = retorno;
    }, (error) => {
      this.mensagemError(error);
    });
    this.tipoEmpresaService.pesquisarTodos().subscribe((retorno: TipoEmpresa[]) => {
      this.listaTipoEmpresas = retorno;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  tipoTela() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.REDE_CREDENCIADA,
        PermissoesEnum.REDE_CREDENCIADA_CADASTRAR,
        PermissoesEnum.REDE_CREDENCIADA_ALTERAR,
        PermissoesEnum.REDE_CREDENCIADA_DESATIVAR]);
  }

  carregarTela() {
    this.route.params.subscribe((params) => {
      const id = params['id'];
      if (id) {
        this.getRedeCredenciada(id);
        this.showDataDesligamento = true;
      } else {
        this.showDataDesligamento = false;
      }
      this.montarTitulo();
    }, (error) => {
      this.mensagemError(error);
    });
  }

  getRedeCredenciada(id: string): void {
    this.redeCredenciada = new RedeCredenciada();
    this.service.pesquisarPorId(id).subscribe((redeCredenciada: RedeCredenciada) => {
      if (redeCredenciada && redeCredenciada.id) {
        this.redeCredenciada = redeCredenciada;
        this.converterModelParaForm();
        this.inicilizaListasVazias();
        if (this.formulario.controls['numeroTelefoneResponsavel'].value) {
          if (this.formulario.controls['numeroTelefoneResponsavel'].value.length === 10) {
            this.mascaraTelFixo(this.formulario.controls['numeroTelefoneResponsavel'].value);
          } else {
            this.mascaraCelular(this.formulario.controls['numeroTelefoneResponsavel'].value);
          }
        }
      }
    },
      (error) => {
        this.mensagemError(error);
      });
  }

  inicilizaListasVazias() {
    if (!this.redeCredenciada.emailsRedeCredenciada) {
      this.redeCredenciada.emailsRedeCredenciada = new Array<EmailRedeCredenciada>();
    }
    if (!this.redeCredenciada.enderecosRedeCredenciada) {
      this.redeCredenciada.enderecosRedeCredenciada = new Array<EnderecoRedeCredenciada>();
    }
    if (!this.redeCredenciada.telefonesRedeCredenciada) {
      this.redeCredenciada.telefonesRedeCredenciada = new Array<TelefoneRedeCredenciada>();
    }
  }

  montarTitulo(): void {
    this.title = MensagemProperties.app_rst_rede_credenciada_cadastro;
  }

  voltar(): void {
    if (this.redeCredenciada.id) {
        this.router.navigate([`${environment.path_raiz_cadastro}/redecredenciada/${this.redeCredenciada.id}`]);
    } else {
        this.router.navigate([`${environment.path_raiz_cadastro}/redecredenciada`]);
    }
  }

  createForm() {
    this.formulario = this.formBuilder.group({
      numeroCnpj: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required, ValidateCNPJ,
        ]),
      ],
      razaoSocial: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      nomeFantasia: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(160),
        ]),
      ],
      inscricaoEstadual: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(20),
        ]),
      ],
      inscricaoMunicipal: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(20),
        ]),
      ],
      tipoEmpresa: [
        { value: undefined, disabled: this.modoConsulta },
        Validators.compose([]),
      ],
      porteEmpresa: [
        { value: undefined, disabled: this.modoConsulta },
        Validators.compose([]),
      ],
      email: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(100),
          ValidateEmail,
        ]),
      ],
      url: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(70),
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
          Validators.maxLength(60),
        ]),
      ],
      numeroNitResponsavel: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(14),
          ValidarNit,
        ]),
      ],
      numeroTelefoneResponsavel: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(15),
        ]),
      ],
      emailResponsavel: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(100),
          ValidateEmail,
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

  mascaraTelFixo(valor: any) {
    valor = MascaraUtil.removerMascara(valor);
    valor = valor.replace(/\W/g, '');
    valor = valor.replace(/^(\d{2})(\d)/, '($1) $2');
    valor = valor.replace(/(\d{4})(\d)/, '$1-$2');
    valor = valor.replace(/(\d{4})$/, '$1');
    this.formulario.patchValue({
      numeroTelefoneResponsavel: valor,
    });
  }

  mascaraCelular(valor: any) {
    valor = MascaraUtil.removerMascara(valor);
    valor = valor.replace(/\W/g, '');
    valor = valor.replace(/^(\d{2})(\d)/, '($1) $2');
    valor = valor.replace(/(\d{5})(\d)/, '$1-$2');
    valor = valor.replace(/(\d{4})$/, '$1');
    this.formulario.patchValue({
      numeroTelefoneResponsavel: valor,
    });
  }

  mudarMascaraTelResponsavel(event: any) {
    if (this.formulario.controls['numeroTelefoneResponsavel'].value) {
      const valor = this.formulario.controls['numeroTelefoneResponsavel'].value;
      if (valor.length <= 14) {
        this.mascaraTelFixo(valor);
      } else {
        this.mascaraCelular(valor);
      }
    }
  }

  converterModelParaForm() {
    this.formulario.patchValue({
      numeroCnpj: this.redeCredenciada.numeroCnpj,
      razaoSocial: this.redeCredenciada.razaoSocial,
      nomeFantasia: this.redeCredenciada.nomeFantasia,
      inscricaoEstadual: this.redeCredenciada.inscricaoEstadual,
      inscricaoMunicipal: this.redeCredenciada.inscricaoMunicipal,
      url: this.redeCredenciada.url,
      tipoEmpresa: this.redeCredenciada.tipoEmpresa ? this.redeCredenciada.tipoEmpresa.id : undefined,
      porteEmpresa: this.redeCredenciada.porteEmpresa ? this.redeCredenciada.porteEmpresa.id : undefined,
      nomeResponsavel: this.redeCredenciada.nomeResponsavel,
      cargoResponsavel: this.redeCredenciada.cargoResponsavel,
      numeroNitResponsavel: this.redeCredenciada.numeroNitResponsavel,
      numeroTelefoneResponsavel: this.redeCredenciada.numeroTelefoneResponsavel,
      emailResponsavel: this.redeCredenciada.emailResponsavel,
      dtdesligamento: this.redeCredenciada.dataDesligamento ?
        DatePicker.convertDateForMyDatePicker(this.redeCredenciada.dataDesligamento) : null,
    });

    if (this.redeCredenciada.emailsRedeCredenciada) {
      this.formulario.patchValue({
        email: this.redeCredenciada.emailsRedeCredenciada[0] ? this.redeCredenciada.emailsRedeCredenciada[0].email.descricao : '',
      });
    }

    if (!this.redeCredenciada.segmento) {
      this.redeCredenciada.segmento = new Segmento();
    }

  }

  salvar() {
    if (this.verificarCampos()) {
      this.prepareSave();
      this.removerMascaras();
      this.service.salvar(this.redeCredenciada).subscribe((redeCredenciada: RedeCredenciada) => {
        this.redeCredenciada = redeCredenciada;
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      }, (error) => {
        this.mensagemError(error);
      }, () => {
        this.voltar();
      });
    }
  }

  verificarCampos(): boolean {
    let retorno = true;

    // cnpj
    if (this.formulario.controls['numeroCnpj'].invalid) {
      if (this.formulario.controls['numeroCnpj'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.formulario.controls['numeroCnpj'],
          MensagemProperties.app_rst_labels_cnpj);
        retorno = false;
      }

      if (!this.formulario.controls['numeroCnpj'].errors.required && this.formulario.controls['numeroCnpj'].errors.validCNPJ) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['numeroCnpj'],
          MensagemProperties.app_rst_labels_cnpj);
        retorno = false;
      }
    }

    // razao social
    if (this.formulario.controls['razaoSocial'].invalid) {
      if (this.formulario.controls['razaoSocial'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.formulario.controls['razaoSocial'],
          MensagemProperties.app_rst_labels_razao_social);
        retorno = false;
      }
      if (this.formulario.controls['razaoSocial'].errors.maxLength) {
        this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
          this.formulario.controls['razaoSocial'], MensagemProperties.app_rst_labels_razao_social,
          this.formulario.controls['razaoSocial'].errors.maxLength.requiredLength);
        retorno = false;
      }
    }

    // email
    if (this.formulario.controls['email'].value
      && this.formulario.controls['email'].invalid
      && this.formulario.controls['email'].errors.validEmail) {
      this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['email'],
        MensagemProperties.app_rst_labels_email);
      retorno = false;
    }

    // numeroTelefoneResponsavel
    if (this.formulario.controls['numeroTelefoneResponsavel'].value) {
      const valor = this.formulario.controls['numeroTelefoneResponsavel'].value;
      if (MascaraUtil.removerMascara(valor).length > 11) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['numeroTelefoneResponsavel'],
          MensagemProperties.app_rst_labels_telefone);
        retorno = false;
      }
    }

    // numeroNitResponsavel
    if (this.formulario.controls['numeroNitResponsavel'].value
      && this.formulario.controls['numeroNitResponsavel'].invalid
      && this.formulario.controls['numeroNitResponsavel'].errors.validNIT) {
      this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['numeroNitResponsavel'],
        MensagemProperties.app_rst_labels_nit_responsavel);
      retorno = false;
    }

    // emailResponsavel
    if (this.formulario.controls['emailResponsavel'].value
      && this.formulario.controls['emailResponsavel'].invalid
      && this.formulario.controls['emailResponsavel'].errors.validEmail) {
      this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['emailResponsavel'],
        MensagemProperties.app_rst_labels_email_responsavel);
      retorno = false;
    }

    // data_desativaco
    if (this.formulario.controls['dtdesligamento'].value && this.formulario.controls['dtdesligamento'].invalid) {

      if (this.formulario.controls['dtdesligamento'].errors.validData) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['dtdesligamento'],
          MensagemProperties.app_rst_labels_data_desativacao);
        retorno = false;
      }

      if (this.formulario.controls['dtdesligamento'].errors.validDataFutura) {
        this.mensagemErroComParametros('app_rst_labels_data_futura', this.formulario.controls['dtdesligamento'],
          MensagemProperties.app_rst_labels_data_desativacao);
        retorno = false;
      }
    }

    return retorno;
  }

  removerMascaras() {
    if (this.redeCredenciada.numeroCnpj) {
      this.redeCredenciada.numeroCnpj = MascaraUtil.removerMascara(this.redeCredenciada.numeroCnpj);
    }
    if (this.redeCredenciada.numeroNitResponsavel) {
      this.redeCredenciada.numeroNitResponsavel = MascaraUtil.removerMascara(this.redeCredenciada.numeroNitResponsavel);
    }
    if (this.redeCredenciada.numeroTelefoneResponsavel) {
      this.redeCredenciada.numeroTelefoneResponsavel = MascaraUtil.removerMascara(this.redeCredenciada.numeroTelefoneResponsavel);
    }
  }

  prepareSave(): RedeCredenciada {
    const formModel = this.formulario.controls;
    this.redeCredenciada.numeroCnpj = formModel.numeroCnpj.value;
    this.redeCredenciada.razaoSocial = formModel.razaoSocial.value;
    this.redeCredenciada.nomeFantasia = formModel.nomeFantasia.value;
    this.redeCredenciada.inscricaoEstadual = formModel.inscricaoEstadual.value;
    this.redeCredenciada.inscricaoMunicipal = formModel.inscricaoMunicipal.value;
    this.redeCredenciada.url = formModel.url.value;
    this.redeCredenciada.nomeResponsavel = formModel.nomeResponsavel.value;
    this.redeCredenciada.cargoResponsavel = formModel.cargoResponsavel.value;
    this.redeCredenciada.numeroNitResponsavel = formModel.numeroNitResponsavel.value;
    this.redeCredenciada.numeroTelefoneResponsavel = formModel.numeroTelefoneResponsavel.value;
    this.redeCredenciada.emailResponsavel = formModel.emailResponsavel.value;
    this.redeCredenciada.dataDesligamento = formModel.dtdesligamento.value ?
      this.convertDateToString(formModel.dtdesligamento.value.date) : null;

    this.redeCredenciada.tipoEmpresa = null;
    if (!this.isUndefined(formModel.tipoEmpresa.value)) {
      this.redeCredenciada.tipoEmpresa = new TipoEmpresa();
      this.redeCredenciada.tipoEmpresa.id = formModel.tipoEmpresa.value;
    }

    this.redeCredenciada.porteEmpresa = null;
    if (!this.isUndefined(formModel.porteEmpresa.value)) {
      this.redeCredenciada.porteEmpresa = new PorteEmpresa();
      this.redeCredenciada.porteEmpresa.id = formModel.porteEmpresa.value;
    }

    // Email
    if (formModel.email.value) {
      if (this.redeCredenciada.emailsRedeCredenciada.length === 0) {
        this.redeCredenciada.emailsRedeCredenciada.push(new EmailRedeCredenciada());
      }
      this.redeCredenciada.emailsRedeCredenciada[0].email.descricao = formModel.email.value;
      this.redeCredenciada.emailsRedeCredenciada[0].email.tipo = EnumValues.getNameFromValue(TipoEmail, TipoEmail.P);
      this.redeCredenciada.emailsRedeCredenciada[0].email.notificacao = true;
    } else {
      this.redeCredenciada.emailsRedeCredenciada = new Array();
    }

    if (this.redeCredenciada.segmento && this.redeCredenciada.segmento.id == null) {
      this.redeCredenciada.segmento = null;
    }

    return this.redeCredenciada;
  }

  descricaoSegmento(): string {
    return this.redeCredenciada.segmento ? this.redeCredenciada.segmento.descricao : '';
  }

  existeTelefone() {
    return this.redeCredenciada.telefonesRedeCredenciada && this.redeCredenciada.telefonesRedeCredenciada.length > 0;
  }

  existeEndereco() {
    return this.redeCredenciada.enderecosRedeCredenciada && this.redeCredenciada.enderecosRedeCredenciada.length > 0;
  }

  existeSegmento() {
    return this.redeCredenciada.segmento && this.redeCredenciada.segmento.id !== undefined;
  }

  removeSegmento() {
    this.redeCredenciada.segmento = new Segmento();
  }

}
