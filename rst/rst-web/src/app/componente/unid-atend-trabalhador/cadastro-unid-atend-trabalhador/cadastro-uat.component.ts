import { FiltroDepartRegional } from 'app/modelo/filtro-depart-regional.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { DatePicker } from './../../../compartilhado/utilitario/date-picker';
import { DepartRegionalService } from './../../../servico/depart-regional.service';
import { ValidarDataFutura, ValidateData } from 'app/compartilhado/validators/data.validator';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { Component, OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import { Uat } from 'app/modelo/uat.model';
import { UatService } from 'app/servico/uat.service';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { DepartamentoRegional } from 'app/modelo/departamento-regional.model';
import { TelefoneUnidAtendTrabalhador } from 'app/modelo/telefone-unid-atend-trabalhador.model';
import { Email } from 'app/modelo/email.model';
import { EmailUnidAtendTrabalhador } from 'app/modelo/email-unid-atend-trabalhador.model';
import { EnderecoUnidAtendTrabalhador } from 'app/modelo/endereco-unid-atend-trabalhador.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EstadoService } from 'app/servico/estado.service';
import { TipoEmail } from 'app/modelo/enum/enum-tipo-email.model';
import { TipoEndereco } from 'app/modelo/enum/enum-tipo-endereco.model';
import { EnumValues } from 'enum-values';
import { ValidateCNPJ } from 'app/compartilhado/validators/cnpj.validator';
import { ValidateEmail } from 'app/compartilhado/validators/email.validator';

@Component({
  selector: 'app-cadastro-uat',
  templateUrl: './cadastro-uat.component.html',
  styleUrls: ['./cadastro-uat.component.scss'],
})
export class CadastroUatComponent extends BaseComponent implements OnInit {
  public uat: Uat;
  public formulario: FormGroup;
  public id: string;
  public departamentos: DepartamentoRegional[];

  public telefone: TelefoneUnidAtendTrabalhador;

  public estados: any[];

  public inconsistencia = false;
  public mensagemInconsistencia: string;
  public showDataDesligamento = false;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: UatService,
    private departamentoService: DepartRegionalService,
    private formBuilder: FormBuilder,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private modalService: NgbModal,
    private dialogService: DialogService,
    protected estadoService: EstadoService) {
    super(bloqueioService, dialogo, estadoService);
    this.emModoConsulta();
    this.createForm();
    this.buscarEstados();
    this.carregarTela();
    this.title = MensagemProperties.app_rst_uat_title_cadastrar;
  }

  ngOnInit() {
    this.uat = new Uat();
    this.telefone = new TelefoneUnidAtendTrabalhador();
  }

  private emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.CAT,
      PermissoesEnum.CAT_CADASTRAR,
      PermissoesEnum.CAT_ALTERAR,
      PermissoesEnum.CAT_DESATIVAR]);
  }

  createForm() {
    this.formulario = this.formBuilder.group({
      cnpj: [
        { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.maxLength(18),
          ValidateCNPJ,
        ]),
      ],
      razaoSocial: [
        { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      nomeFantasia: [
        { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      endereco: [
        { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.required,
          Validators.maxLength(100),
        ]),
      ],
      complemento: [
        { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.maxLength(100),
        ]),
      ],
      numeroEnd: [
        { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.maxLength(5),
        ]),
      ],
      numero: [
        { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.required,
          Validators.maxLength(14),
        ]),
      ],
      tipo: [
        { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.required,
        ]),
      ],
      contato: [
        { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.required,
        ]),
      ],
      bairro: [
        { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.maxLength(50),
        ]),
      ],
      cep: [
        { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.required,
          Validators.maxLength(10),
        ]),
      ],
      municipio: [
        { value: null, disabled: true || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.required,
          Validators.maxLength(50),
        ]),
      ],
      estado: [
        { value: undefined, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.required,
          Validators.maxLength(100),
        ]),
      ],
      telefone: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(50),
        ]),
      ],
      email: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(50),
          ValidateEmail,
        ]),
      ],
      nomeResponsavel: [
        { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.maxLength(160),
        ]),
      ],
      dtdesligamento: [
        { value: null, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          ValidateData,
        ]),
      ],
      depRegional: [
        { value: undefined, disabled: this.modoConsulta || !this.isPermitidoEditar() },
        Validators.compose([
          Validators.required,
        ]),
      ],
    });
  }

  carregarTela() {
    this.route.params.subscribe((params) => {
      this.id = params['id'];
      if (this.id) {
        this.showDataDesligamento = true;
        this.getUat(this.id);
      } else {
        this.buscarDepartamentos();
      }

    });
  }

  getUat(id: string): void {
    this.uat = new Uat();
    this.service.pesquisarPorId(id).subscribe((uat: Uat) => {
      if (uat && uat.id) {
        this.uat = uat;
        if (!uat.departamentoRegional) {
          uat.departamentoRegional = new DepartamentoRegional();
        }
        this.buscarDepartamentos();
        this.setMunicipio();
        this.converterModelParaForm();
        this.verificarIncosistencia();
      }
    },
      (error) => {
        this.mensagemError(error);
      });
  }

  setDepartamento() {
    if (this.uat && this.uat.id && this.departamentos) {
      let contem = false;
      this.departamentos.forEach((item) => {
        if (item.id === this.uat.departamentoRegional.id) {
          contem = true;
        }
      });
      if (!contem) {
        this.departamentos.push(this.uat.departamentoRegional);
      }
    }
  }

  orderByRazaoSocial(list: DepartamentoRegional[]) {
    if (!this.listaUndefinedOuVazia(list)) {
      this.departamentos.sort((left, right): number => {
        if (left.razaoSocial > right.razaoSocial) {
          return 1;
        }
        if (left.razaoSocial < right.razaoSocial) {
          return -1;
        }
        return 0;
      });
    }
  }

  verificarIncosistencia() {
    this.inconsistencia = false;
    if (!this.modoConsulta && this.uat) {
      let nomeResponsavel = '';
      let email = '';
      let telefone = '';

      if (this.isVazia(this.uat.nomeResponsavel)) {
        this.inconsistencia = true;
        nomeResponsavel = '\"' + MensagemProperties.app_rst_labels_responsavel + '\"';
      }

      if (this.listaUndefinedOuVazia(this.uat.telefone)) {
        if (this.isVazia(this.uat.nomeResponsavel)) {
          telefone = ', ';
        }
        this.inconsistencia = true;
        telefone = telefone + '\"' + MensagemProperties.app_rst_labels_telefone + '\"';
      }

      if (this.listaUndefinedOuVazia(this.uat.email)) {
        if (this.listaUndefinedOuVazia(this.uat.telefone) || this.isVazia(this.uat.nomeResponsavel)) {
          email = ', ';
        }
        this.inconsistencia = true;
        email = email + '\"' + MensagemProperties.app_rst_labels_email + '\"';
      }

      if (this.inconsistencia) {
        this.mensagemInconsistencia = this.recursoPipe.transform('app_rst_mensagem_inconsistencia',
          nomeResponsavel, telefone, email, '');
      }
    }
  }

  setMunicipio() {
    if (this.uat.endereco) {
      this.pesquisarMunicipiosPorEstado(this.uat.endereco[0].endereco.municipio.estado.id);
      if (!this.modoConsulta && this.isPermitidoEditar()) {
        this.formulario.controls['municipio'].enable();
      }
    }
  }

  existeTelefone() {
    return this.uat.telefone && this.uat.telefone.length > 0;
  }

  converterModelParaForm() {

    this.formulario.patchValue({
      cnpj: this.uat.cnpj,
      razaoSocial: this.uat.razaoSocial,
      nomeFantasia: this.uat.nomeFantasia,
      depRegional: this.uat.departamentoRegional.id,
      nomeResponsavel: this.uat.nomeResponsavel,
      dtdesligamento: this.uat.dataDesativacao ? DatePicker.convertDateForMyDatePicker(this.uat.dataDesativacao) : null,
    });

    if (this.uat.endereco) {
      this.formulario.patchValue({
        endereco: this.uat.endereco[0].endereco.descricao,
        complemento: this.uat.endereco[0].endereco.complemento,
        numeroEnd: this.uat.endereco[0].endereco.numero,
        bairro: this.uat.endereco[0].endereco.bairro,
        cep: this.uat.endereco[0].endereco.cep,
        municipio: this.uat.endereco[0].endereco.municipio.id.toString(),
        estado: this.uat.endereco[0].endereco.municipio.estado.id,
      });
      this.municipioPreenchido();
    }

    if (this.uat.email) {
      this.formulario.patchValue({
        email: this.uat.email[0] ? this.uat.email[0].email.descricao : '',
      });
    }

    if (!this.uat.telefone) {
      this.uat.telefone = new Array<TelefoneUnidAtendTrabalhador>();
    }

  }

  salvar() {
    if (this.verificarCampos()) {
      this.prepareSaveUat();
      this.service.salvar(this.uat).subscribe((response: Uat) => {
        this.uat = response;
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.verificarIncosistencia();
      }, (error) => {
        this.mensagemError(error);
      }, () => {
        this.voltar();
      });
    }
  }

  prepareSaveUat(): Uat {
    const formModel = this.formulario.controls;
    this.uat.cnpj = MascaraUtil.removerMascara(formModel.cnpj.value);
    this.uat.razaoSocial = formModel.razaoSocial.value;
    this.uat.nomeFantasia = formModel.nomeFantasia.value;
    this.uat.nomeResponsavel = formModel.nomeResponsavel.value;
    const departamento = new DepartamentoRegional();
    departamento.id = formModel.depRegional.value;
    this.uat.departamentoRegional = departamento;
    this.uat.dataDesativacao = formModel.dtdesligamento.value ? this.convertDateToString(formModel.dtdesligamento.value.date) : null;
    // endereco
    if (this.uat.endereco) {
      if (this.uat.endereco.length === 0) {
        this.uat.endereco.push(new EnderecoUnidAtendTrabalhador());
      }
      this.uat.endereco[0].endereco.descricao = formModel.endereco.value;
      this.uat.endereco[0].endereco.complemento = formModel.complemento.value;
      this.uat.endereco[0].endereco.numero = formModel.numeroEnd.value;
      this.uat.endereco[0].endereco.bairro = formModel.bairro.value;
      this.uat.endereco[0].endereco.cep = MascaraUtil.removerMascara(formModel.cep.value);
      this.uat.endereco[0].endereco.municipio.id = formModel.municipio.value;
      this.uat.endereco[0].endereco.tipoEndereco = EnumValues.getNameFromValue(TipoEndereco, TipoEndereco.P);
    } else {
      this.uat.endereco = new Array<EnderecoUnidAtendTrabalhador>();
    }

    // Email
    if (formModel.email.value) {
      if (!this.uat.email || this.uat.email.length === 0) {
        this.uat.email = new Array<EmailUnidAtendTrabalhador>();
        this.uat.email.push(new EmailUnidAtendTrabalhador());
        this.uat.email[0].email = new Email();
      }
      this.uat.email[0].email.descricao = formModel.email.value;
      this.uat.email[0].email.tipo = EnumValues.getNameFromValue(TipoEmail, TipoEmail.P);
      this.uat.email[0].email.notificacao = true;
    } else {
      this.uat.email = new Array<EmailUnidAtendTrabalhador>();
    }

    if (!this.uat.telefone) {
      this.uat.telefone = new Array<TelefoneUnidAtendTrabalhador>();
    }

    return this.uat;
  }

  buscarDepartamentos() {
    this.departamentoService.listarTodosAtivos(new FiltroDepartRegional()).subscribe((dados: any) => {
      this.departamentos = dados;
      this.setDepartamento();
      this.orderByRazaoSocial(this.departamentos);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  verificarCampos(): boolean {
    let retorno = true;

    // email
    if (this.formulario.controls['email'].value && this.formulario.controls['email'].invalid) {
      this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['email'], MensagemProperties.app_rst_labels_email);
      retorno = false;
    }

    // cep
    if (this.formulario.controls['cep'].invalid) {
      if (this.formulario.controls['cep'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.formulario.controls['cep'], MensagemProperties.app_rst_labels_CEP);
        retorno = false;
      }
      if (this.formulario.controls['cep'].errors.maxLength) {
        this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
          this.formulario.controls['cep'], MensagemProperties.app_rst_labels_CEP,
          this.formulario.controls['cep'].errors.maxLength.requiredLength);
        retorno = false;
      }
    }

    if (this.formulario.controls['municipio'].invalid) {

      if (this.formulario.controls['municipio'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio',
          this.formulario.controls['municipio'],
          MensagemProperties.app_rst_labels_municipio);
        retorno = false;
      }

      if (this.formulario.controls['municipio'].errors.maxLength) {
        this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
          this.formulario.controls['municipio'], MensagemProperties.app_rst_labels_municipio,
          this.formulario.controls['municipio'].errors.maxLength.requiredLength);
        retorno = false;
      }
    }

    // estado
    if (this.formulario.controls['estado'].invalid) {
      if (this.formulario.controls['estado'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio',
          this.formulario.controls['estado'],
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
    // Data desativação
    if (!this.isVazia(this.formulario.controls['dtdesligamento'].value)) {
      if (this.formulario.controls['dtdesligamento'].errors && this.formulario.controls['dtdesligamento'].errors.validData) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['dtdesligamento'],
          MensagemProperties.app_rst_labels_data_desativacao);
        retorno = false;
      } else if (ValidarDataFutura(this.formulario.controls['dtdesligamento'].value.jsdate)) {
        this.mensagemErroComParametrosModel('app_rst_labels_data_futura', MensagemProperties.app_rst_labels_data_desativacao);
        retorno = false;
      }
    }
    // endereco
    if (this.formulario.controls['endereco'].invalid) {
      if (this.formulario.controls['endereco'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio',
          this.formulario.controls['endereco'],
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

    // depRegional

    if (this.formulario.controls['depRegional'].invalid) {
      if (this.formulario.controls['depRegional'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio',
          this.formulario.controls['depRegional'], MensagemProperties.app_rst_labels_departamento_regional);
        retorno = false;
      }
    }

    // nome fantasia
    if (this.formulario.controls['nomeFantasia'].invalid) {
      if (this.formulario.controls['nomeFantasia'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio',
          this.formulario.controls['nomeFantasia'], MensagemProperties.app_rst_labels_nome_fantasia);
        retorno = false;
      }
      if (this.formulario.controls['nomeFantasia'].errors.maxLength) {
        this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
          this.formulario.controls['nomeFantasia'], MensagemProperties.app_rst_labels_nome_fantasia,
          this.formulario.controls['nomeFantasia'].errors.maxLength.requiredLength);
        retorno = false;
      }
    }

    // razao social
    if (this.formulario.controls['razaoSocial'].invalid) {
      if (this.formulario.controls['razaoSocial'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio',
          this.formulario.controls['razaoSocial'], MensagemProperties.app_rst_labels_razao_social);
        retorno = false;
      }
      if (this.formulario.controls['razaoSocial'].errors.maxLength) {
        this.mensagemErroComParametros('app_rst_quantidade_caracteres_maximos_invalido',
          this.formulario.controls['razaoSocial'], MensagemProperties.app_rst_labels_razao_social,
          this.formulario.controls['razaoSocial'].errors.maxLength.requiredLength);
        retorno = false;
      }
    }

    // cnpj
    if (this.formulario.controls['cnpj'].invalid) {
      if (this.formulario.controls['cnpj'].value && this.formulario.controls['cnpj'].errors.validCNPJ) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.formulario.controls['cnpj'], MensagemProperties.app_rst_labels_cnpj);
        retorno = false;
      }

    }
    return retorno;
  }

  public filtrarMunicipios(token: string): Observable<any> {
    const query = new RegExp(token, 'ig');
    return Observable.of(
      this.municipios.filter((municipio: any) => {
        if (token.length > 3) {
          return query.test(municipio.descricao);
        }
      }),
    );
  }

  buscarMunicipio() {
    this.formulario.patchValue({
      municipio: null,
    });
    if (this.formulario.controls['estado'].value && this.formulario.controls['estado'].value !== 0 && !this.modoConsulta && this.isPermitidoEditar()) {
      this.formulario.controls['municipio'].enable();
      this.pesquisarMunicipiosPorEstado(this.formulario.controls['estado'].value);
    } else {
      this.formulario.controls['municipio'].disable();
    }
  }

  buscarEstados() {
    this.service.buscarEstados().subscribe((dados: any) => {
      this.estados = dados;
      if (this.estados) {
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  municipioPreenchido(): void {
    if (this.modoAlterar && !this.formulario.controls['estado'].value) {
      this.formulario.controls['municipio'].disable();
    }
    if (this.modoAlterar && this.formulario.controls['estado'].value && !this.modoConsulta && this.isPermitidoEditar()) {
      this.formulario.controls['municipio'].enable();
    }
  }

  voltar(): void {
    if (this.uat.id) {
      this.router.navigate([`${environment.path_raiz_cadastro}/uat/${this.uat.id}`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/uat`]);
    }
  }

  isPermitidoEditar(): boolean {
    return this.usuarioLogado.papeis.length >= 1 &&
      this.usuarioLogado.papeis.indexOf('GDNA') === -1
      && this.usuarioLogado.papeis.indexOf('GDNP') === -1
      && this.usuarioLogado.papeis.indexOf('GDRA') === -1
      && this.usuarioLogado.papeis.indexOf('GDRP') === -1;
  }

}
