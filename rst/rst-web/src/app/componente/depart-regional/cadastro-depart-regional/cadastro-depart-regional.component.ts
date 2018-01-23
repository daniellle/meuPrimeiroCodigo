import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { DatePicker } from './../../../compartilhado/utilitario/date-picker';
import { ValidateData, ValidateDataFutura, ValidarDataFutura } from 'app/compartilhado/validators/data.validator';

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BaseComponent } from 'app/componente/base.component';
import { DepartRegionalService } from 'app/servico/depart-regional.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { DepartamentoRegional } from 'app/modelo/departamento-regional.model';
import { Municipio } from 'app/modelo/municipio.model';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { TelefoneDepartamentoRegional } from 'app/modelo/telefone-departamento-regional.model';
import { EnderecoDepartamentoRegional } from 'app/modelo/endereco-departamento-regional.model';
import { EmailDepartamentoRegional } from 'app/modelo/email-departamento-regional.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EstadoService } from 'app/servico/estado.service';
import { TipoEndereco } from 'app/modelo/enum/enum-tipo-endereco.model';
import { TipoEmail } from 'app/modelo/enum/enum-tipo-email.model';
import { ValidateCNPJ } from 'app/compartilhado/validators/cnpj.validator';
import { EnumValues } from 'enum-values';
import { ValidateEmail } from 'app/compartilhado/validators/email.validator';

@Component({
  selector: 'app-cadastro-depart-regional',
  templateUrl: './cadastro-depart-regional.component.html',
  styleUrls: ['./cadastro-depart-regional.component.scss'],
})
export class CadastroDepartRegionalComponent extends BaseComponent implements OnInit {

  public departamentoRegional: DepartamentoRegional;
  public formulario: FormGroup;
  public id: string;
  public estados: any[];

  public municipioSelecionado;

  public telefone: TelefoneDepartamentoRegional;
  public tipo: string;
  public showDataDesligamento: boolean;

  public inconsistencia = false;
  public mensagemInconsistencia: string;

  constructor(private route: ActivatedRoute,
    private router: Router,
    private service: DepartRegionalService,
    private formBuilder: FormBuilder,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private modalService: NgbModal,
    private dialogService: DialogService,
    protected estadoService: EstadoService) {
    super(bloqueioService, dialogo);
    this.showDataDesligamento = false;
    this.emModoConsulta();
    this.emModoIncluir();
    this.createForm();
    this.carregarTela();
    this.buscarEstados();
    this.telefone = new TelefoneDepartamentoRegional();

    if (this.modoIncluir) {
      this.departamentoRegional = new DepartamentoRegional();
    }

    if (this.modoAlterar) {
      this.municipioSelecionado = true;
    }
  }

  ngOnInit() {
    this.title = MensagemProperties.app_rst_depart_regional_cadastro;
  }

  verificarIncosistencia() {
    this.inconsistencia = false;
    if (!this.modoConsulta && this.departamentoRegional) {
      let nomeFantasia = '';
      let nomeResponsavel = '';
      let telefone = '';
      let email = '';

      if (this.isVazia(this.departamentoRegional.nomeFantasia)) {
        this.inconsistencia = true;
        nomeFantasia = '\"' + MensagemProperties.app_rst_labels_nome_fantasia + '\"';
      }

      if (this.isVazia(this.departamentoRegional.nomeResponsavel)) {
        if (this.isVazia(this.departamentoRegional.nomeFantasia)) {
          nomeResponsavel = ', ';
        }
        this.inconsistencia = true;
        nomeResponsavel = nomeResponsavel + '\"' + MensagemProperties.app_rst_labels_nome_responsavel + '\"';
      }

      if (this.listaUndefinedOuVazia(this.departamentoRegional.listaTelDepRegional)) {
        if (this.isVazia(this.departamentoRegional.nomeResponsavel) || this.isVazia(this.departamentoRegional.nomeFantasia)) {
          telefone = ', ';
        }
        this.inconsistencia = true;
        telefone = telefone + '\"' + MensagemProperties.app_rst_labels_telefone + '\"';
      }

      if (this.listaUndefinedOuVazia(this.departamentoRegional.listaEmailDepRegional)) {
        if (this.listaUndefinedOuVazia(this.departamentoRegional.listaTelDepRegional) ||
          this.isVazia(this.departamentoRegional.nomeResponsavel) || this.isVazia(this.departamentoRegional.nomeFantasia)) {
          email = ', ';
        }
        this.inconsistencia = true;
        email = email + '\"' + MensagemProperties.app_rst_labels_email + '\"';
      }

      if (this.inconsistencia) {
        this.mensagemInconsistencia =
          this.recursoPipe.transform('app_rst_mensagem_inconsistencia', nomeFantasia, nomeResponsavel, telefone, email);
      }
    }
  }

  openModalTelefone(content) {
    this.modalService.open(content).result.then((result) => {
      if (this.validarTelefone()) {
        this.telefone.telefone.contato = this.telefone.telefone.contato;
        this.telefone.telefone.numero = MascaraUtil.removerMascara(this.telefone.telefone.numero);
        this.departamentoRegional.listaTelDepRegional.push(this.telefone);
        this.telefone = new TelefoneDepartamentoRegional();
      }
    }, (reason) => {
      this.telefone = new TelefoneDepartamentoRegional();
    });
  }

  removeTel(item: TelefoneDepartamentoRegional) {
    const index: number = this.departamentoRegional.listaTelDepRegional.indexOf(item);
    if (index !== -1) {
      this.departamentoRegional.listaTelDepRegional.splice(index, 1);
    }
  }

  validarTelefone(): Boolean {
    let isValido = true;
    if (!this.telefone.telefone.numero) {
      this.mensagemErroComParametrosModel('app_rst_msg_telefone_campo_obrigatorio', MensagemProperties.app_rst_labels_numero);
      isValido = false;
    }

    if (!this.telefone.telefone.tipo) {
      this.mensagemErroComParametrosModel('app_rst_msg_telefone_campo_obrigatorio', MensagemProperties.app_rst_labels_tipo);
      isValido = false;
    }
    return isValido;
  }

  createForm() {
    this.formulario = this.formBuilder.group({
      cnpj: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required, ValidateCNPJ,
        ]),
      ],
      siglaDr: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(2),
        ]),
      ],
      nomeFantasia: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(80),
        ]),
      ],
      razaoSocial: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(150),
        ]),
      ],
      nomeResponsavel: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(50),
        ]),
      ],
      endereco: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(100),
        ]),
      ],
      complemento: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(100),
        ]),
      ],
      numeroEnd: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(5),
        ]),
      ],
      bairro: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(50),
        ]),
      ],
      estado: [
        { value: undefined, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(100),
        ]),
      ],
      municipio: [
        { value: null, disabled: true },
        Validators.compose([
          Validators.required,
          Validators.maxLength(50),
        ]),
      ],
      cep: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(10),
        ]),
      ],
      tipo: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
        ]),
      ],
      numero: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(14),
        ]),
      ],
      contato: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
        ]),
      ],
      dtDesativacao: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          ValidateData, ValidateDataFutura,
        ]),
      ],
      email: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(100),
          ValidateEmail,
        ]),
      ],
    });
  }

  // estadoIsVazio(idEstado) {
  //     if (idEstado === 'undefined') {
  //       this.formulario.controls['municipio'].disable();
  //     } else {
  //       this.formulario.controls['municipio'].enable(); 
  //     }
  // }

  emModoIncluir() {
    this.modoIncluir = Boolean(Seguranca.isPermitido([PermissoesEnum.DEPARTAMENTO_REGIONAL,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_CADASTRAR]));
  }

  emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.DEPARTAMENTO_REGIONAL,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_CADASTRAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_ALTERAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_DESATIVAR]);
  }

  carregarTela() {
    this.route.params.subscribe((params) => {
      this.id = params['id'];
      if (this.id) {
        this.showDataDesligamento = true;
        this.getDepartamento(this.id);
      }
      this.montarTitulo();
    }, (error) => {
      this.mensagemError(error);
    });
  }

  getDepartamento(id: string): void {
    this.departamentoRegional = new DepartamentoRegional();
    this.service.pesquisarPorId(id).subscribe((departamentoRegional: DepartamentoRegional) => {
      if (departamentoRegional) {
        this.departamentoRegional = departamentoRegional;
        this.setMunicipio();
        this.converterModelParaForm();
        this.verificarIncosistencia();
      }
    },
      (error) => {
        this.mensagemError(error.error.mensagem);
      });
  }

  salvar() {
    if (this.verificarCampos()) {
      this.prepareSaveDepartamento();
      this.removerMascaras();
      this.service.salvar(this.departamentoRegional).subscribe((response: DepartamentoRegional) => {
        this.departamentoRegional = response;
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.verificarIncosistencia();
      }, (error) => {
        this.mensagemError(error);
      }, () => {
        this.voltar();
      });
    }
  }

  prepareSaveDepartamento(): DepartamentoRegional {
    const formModel = this.formulario.controls;
    this.departamentoRegional.cnpj = formModel.cnpj.value;
    this.departamentoRegional.siglaDR = formModel.siglaDr.value;
    this.departamentoRegional.nomeFantasia = formModel.nomeFantasia.value;
    this.departamentoRegional.razaoSocial = formModel.razaoSocial.value;
    this.departamentoRegional.nomeResponsavel = formModel.nomeResponsavel.value;
    this.departamentoRegional.dataDesativacao = formModel.dtDesativacao.value ?
      this.convertDateToString(formModel.dtDesativacao.value.date) : null;
    // endereco
    if (this.verificaEnderecoPreenchido()) {
      if (this.listaUndefinedOuVazia(this.departamentoRegional.listaEndDepRegional)) {
        this.departamentoRegional.listaEndDepRegional = new Array<EnderecoDepartamentoRegional>();
        this.departamentoRegional.listaEndDepRegional.push(new EnderecoDepartamentoRegional());
      }

      this.departamentoRegional.listaEndDepRegional[0].endereco.descricao = formModel.endereco.value;
      this.departamentoRegional.listaEndDepRegional[0].endereco.complemento = formModel.complemento.value;
      this.departamentoRegional.listaEndDepRegional[0].endereco.numero = formModel.numeroEnd.value;
      this.departamentoRegional.listaEndDepRegional[0].endereco.bairro = formModel.bairro.value;
      this.departamentoRegional.listaEndDepRegional[0].endereco.cep = MascaraUtil.removerMascara(formModel.cep.value);
      if (!this.departamentoRegional.listaEndDepRegional[0].endereco.municipio) {
        this.departamentoRegional.listaEndDepRegional[0].endereco.municipio = new Municipio();
      }
      this.departamentoRegional.listaEndDepRegional[0].endereco.municipio.id = formModel.municipio.value;
      this.departamentoRegional.listaEndDepRegional[0].endereco.tipoEndereco = EnumValues.getNameFromValue(TipoEndereco, TipoEndereco.P);
    } else {
      this.departamentoRegional.listaEndDepRegional = new Array<EnderecoDepartamentoRegional>();
    }

    // email
    if (formModel.email.value) {

      if (this.listaUndefinedOuVazia(this.departamentoRegional.listaEmailDepRegional)) {
        this.departamentoRegional.listaEmailDepRegional = new Array<EmailDepartamentoRegional>();
        this.departamentoRegional.listaEmailDepRegional.push(new EmailDepartamentoRegional());
      }

      this.departamentoRegional.listaEmailDepRegional[0].email.descricao = formModel.email.value;
      this.departamentoRegional.listaEmailDepRegional[0].email.tipo = EnumValues.getNameFromValue(TipoEmail, TipoEmail.T);
      this.departamentoRegional.listaEmailDepRegional[0].email.notificacao = true;
    } else {
      this.departamentoRegional.listaEmailDepRegional = new Array();
    }

    if (!this.departamentoRegional.listaTelDepRegional) {
      this.departamentoRegional.listaTelDepRegional = new Array<TelefoneDepartamentoRegional>();
    }

    return this.departamentoRegional;
  }

  verificarCampos(): boolean {
    let retorno = true;
    // cnpj
    if (this.formulario.controls['cnpj'].invalid) {

      if (this.formulario.controls['cnpj'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.formulario.controls['cnpj'],
          MensagemProperties.app_rst_labels_cnpj);
        retorno = false;
      }

      if (!this.formulario.controls['cnpj'].errors.required && this.formulario.controls['cnpj'].errors.validCNPJ) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['cnpj'],
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

    if (this.verificaEnderecoPreenchido()) {
      // endereco
      if (this.formulario.controls['endereco'].invalid) {
        if (this.formulario.controls['endereco'].errors.required) {
          this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.formulario.controls['endereco'],
            MensagemProperties.app_rst_labels_endereco);
          retorno = false;
        }
        if (this.formulario.controls['endereco'].errors.maxLength) {
          this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
            this.formulario.controls['endereco'], MensagemProperties.app_rst_labels_endereco,
            this.formulario.controls['endereco'].errors.maxLength.requiredLength);
          retorno = false;
        }
      }
      // estado
      if (this.formulario.controls['estado'].invalid) {
        if (this.formulario.controls['estado'].errors.required) {
          this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.formulario.controls['estado'],
            MensagemProperties.app_rst_labels_estado);
          retorno = false;
        }
        if (this.formulario.controls['estado'].errors.maxLength) {
          this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
            this.formulario.controls['estado'], MensagemProperties.app_rst_labels_estado,
            this.formulario.controls['estado'].errors.maxLength.requiredLength);
          retorno = false;
        }
      }

      // municipio
      if (this.formulario.controls['municipio'].value === null || this.formulario.controls['municipio'].value === undefined) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.formulario.controls['municipio'],
          MensagemProperties.app_rst_labels_municipio);
        retorno = false;
      }
      if (this.formulario.controls['municipio'].invalid) {
        if (this.formulario.controls['municipio'].errors.maxLength) {
          this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
            this.formulario.controls['municipio'], 'app_rst_labels_municipio',
            this.formulario.controls['municipio'].errors.maxLength.requiredLength);
          retorno = false;
        }
      }
      // cep
      if (this.formulario.controls['cep'].invalid) {
        if (this.formulario.controls['cep'].errors.required) {
          this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.formulario.controls['cep'],
            MensagemProperties.app_rst_labels_CEP);
          retorno = false;
        }
        if (this.formulario.controls['cep'].errors.maxLength) {
          this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
            this.formulario.controls['cep'], MensagemProperties.app_rst_labels_CEP,
            this.formulario.controls['cep'].errors.maxLength.requiredLength);
          retorno = false;
        }
      }
    }

    if (this.formulario.controls['email'].value && this.formulario.controls['email'].invalid) {
      this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['email'], MensagemProperties.app_rst_labels_email);
      retorno = false;
    }

    // validar data desativacao
    if (!this.isVazia(this.formulario.controls['dtDesativacao'].value)) {
      if (this.formulario.controls['dtDesativacao'].errors && this.formulario.controls['dtDesativacao'].errors.validData) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['dtDesativacao'],
          MensagemProperties.app_rst_labels_data_desativacao);
        retorno = false;
      } else if (ValidarDataFutura(this.formulario.controls['dtDesativacao'].value.jsdate)) {
        this.mensagemErroComParametrosModel('app_rst_labels_data_futura', MensagemProperties.app_rst_labels_data_desativacao);
        retorno = false;
      }
    }

    return retorno;
  }

  existeTelefone() {
    return this.departamentoRegional.listaTelDepRegional && this.departamentoRegional.listaTelDepRegional.length > 0;
  }

  verificaEnderecoPreenchido(): boolean {
    if ((this.formulario.controls['endereco'].value && this.formulario.controls['endereco'].value !== '')
      || (this.formulario.controls['complemento'].value && this.formulario.controls['complemento'].value !== '')
      || (this.formulario.controls['numeroEnd'].value && this.formulario.controls['numeroEnd'].value !== '')
      || (this.formulario.controls['bairro'].value && this.formulario.controls['bairro'].value !== '')
      || (this.formulario.controls['estado'].value && this.formulario.controls['estado'].value !== undefined)
      || (this.formulario.controls['municipio'].value && this.formulario.controls['municipio'].value !== 0)
      || (this.formulario.controls['cep'].value && this.formulario.controls['cep'].value !== '')) {
      return true;
    }
    return false;
  }

  converterModelParaForm() {
    this.formulario.patchValue({
      cnpj: this.departamentoRegional.cnpj,
      siglaDr: this.departamentoRegional.siglaDR,
      nomeFantasia: this.departamentoRegional.nomeFantasia,
      razaoSocial: this.departamentoRegional.razaoSocial,
      nomeResponsavel: this.departamentoRegional.nomeResponsavel,
      dtDesativacao: this.departamentoRegional.dataDesativacao ?
        DatePicker.convertDateForMyDatePicker(this.departamentoRegional.dataDesativacao) : null,
    });
    if (this.departamentoRegional.listaEndDepRegional) {
      this.formulario.patchValue({
        endereco: this.departamentoRegional.listaEndDepRegional[0].endereco.descricao.toString(),
        complemento: this.departamentoRegional.listaEndDepRegional[0].endereco.complemento,
        numeroEnd: this.departamentoRegional.listaEndDepRegional[0].endereco.numero,
        bairro: this.departamentoRegional.listaEndDepRegional[0].endereco.bairro,
        cep: this.departamentoRegional.listaEndDepRegional[0].endereco.cep,
        municipio: this.departamentoRegional.listaEndDepRegional[0].endereco.municipio.id.toString(),
        estado: this.departamentoRegional.listaEndDepRegional[0].endereco.municipio.estado.id,
      });
      this.municipioPreenchido();
    }
    if (this.departamentoRegional.listaEmailDepRegional) {
      this.formulario.patchValue({
        email: this.departamentoRegional.listaEmailDepRegional[0].email.descricao,
      });
    }
    if (!this.departamentoRegional.listaTelDepRegional) {
      this.departamentoRegional.listaTelDepRegional = new Array<TelefoneDepartamentoRegional>();
    }
  }

  montarTitulo(): void {
    this.title = '';
    if (this.modoIncluir) {
      this.title = MensagemProperties.app_rst_depart_regional_cadastro;
    }

    if (this.modoAlterar) {
      this.title = MensagemProperties.app_rst_depart_regional_alterar;
    }

    if (this.modoConsulta) {
      this.title = MensagemProperties.app_rst_depart_regional_consultar;
    }
  }

  voltar(): void {
    if (this.departamentoRegional.id) {
      this.router.navigate([`${environment.path_raiz_cadastro}/departamentoregional/${this.departamentoRegional.id}`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/departamentoregional`]);
    }
  }

  buscarEstados() {
    this.service.buscarEstados().subscribe((dados: any) => {
      this.estados = dados;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  buscarMunicipio() {
    this.formulario.patchValue({
      municipio: null,
    });
    if (this.formulario.controls['estado'].value && this.formulario.controls['estado'].value !== 0 && !this.modoConsulta) {
      this.formulario.controls['municipio'].enable();
      this.pesquisarMunicipiosPorEstado(this.formulario.controls['estado'].value);
    } else {
      this.formulario.controls['municipio'].disable();
    }
  }

  removerMascaras() {
    if (this.departamentoRegional.cnpj) {
      this.departamentoRegional.cnpj = MascaraUtil.removerMascara(this.departamentoRegional.cnpj);
    }
  }

  municipioPreenchido(): void {
    if (!this.formulario.controls['estado'].value) {
      this.formulario.controls['municipio'].disable();
    }
    if (this.formulario.controls['estado'].value && !this.modoConsulta) {
      this.formulario.controls['municipio'].enable();
    }
  }

  setMunicipio() {
    if (this.departamentoRegional.listaEndDepRegional) {
      this.pesquisarMunicipiosPorEstado(this.departamentoRegional.listaEndDepRegional[0].endereco.municipio.estado.id);
    }
  }
}
