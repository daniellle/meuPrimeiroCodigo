import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { DatePicker } from './../../../compartilhado/utilitario/date-picker';
import { TipoProfissional } from 'app/modelo/enum/enum-tipo-profissional';
import { Profissional } from './../../../modelo/profissional.model';
import { UatProfissional } from './../../../modelo/unid-atend-trabalhador-profissional.model';
import { ProfissionalEspecialidade } from './../../../modelo/profissional-especialidade.model';
import { TipoEndereco } from 'app/modelo/enum/enum-tipo-endereco.model';
import { EnumValues } from 'enum-values';
import { EmailProfissional } from '../../../modelo/email-profissional.model';
import { TelefoneProfissional } from './../../../modelo/telefone-profissional.model';
import { IOption } from 'ng-select';
import { Especialidade } from './../../../modelo/especialidade.model';
import { EspecialidadeService } from './../../../servico/especialidade.service';
import { EstadoService } from './../../../servico/estado.service';
import { Component, OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';

import { ProfissionalService } from 'app/servico/profissional.service';

import { Estado } from 'app/modelo/estado.model';

import { UnidadeAtendimentoTrabalhador } from 'app/modelo/unid-atend-trabalhador.model';

import { EnderecoProfissional } from 'app/modelo/endereco-profissional.model';
import { TipoTelefone } from 'app/modelo/enum/enum-tipo-telefone.model';
import { Genero } from 'app/modelo/enum/enum-genero.model';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { ValidateCPF } from 'app/compartilhado/validators/cpf.validator';
import { ValidarNit } from 'app/compartilhado/validators/nit.validator';
import { ValidateData, ValidateDataFutura } from 'app/compartilhado/validators/data.validator';

@Component({
  selector: 'app-cadastro-profissionais',
  templateUrl: './cadastro-profissionais.component.html',
  styleUrls: ['./cadastro-profissionais.component.scss'],
})
export class CadastroProfissionaisComponent extends BaseComponent implements OnInit {
  optionsModel: number[];
  public especialdades = Array<IOption>();
  public profissionaisForm: FormGroup;
  public profissional: Profissional;
  public id: string;
  public genero = Genero;
  public mascaraData = MascaraUtil.mascaraNascimento;
  public especialidadesSelecionadas: Especialidade[];
  public uat: UnidadeAtendimentoTrabalhador;
  public uatsSelecionados = new Array<UnidadeAtendimentoTrabalhador>();
  public estados: any[];
  public municipios: any[];
  public dataSource: Observable<any>;
  public keysTipoProfissional: string[];
  public tipoProfissional = TipoProfissional;

  public inconsistencia = false;
  public mensagemInconsistencia: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: ProfissionalService,
    private formBuilder: FormBuilder,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private dialogService: DialogService,
    protected estadoService: EstadoService,
    protected especialidadeService: EspecialidadeService) {
    super(bloqueioService, dialogo);
    this.keysTipoProfissional = Object.keys(this.tipoProfissional);
    this.profissional = new Profissional();

    this.emModoConsulta();
    this.createForm();
    this.carregarTela();
    this.profissionaisForm.controls['municipio'].disable();
  }

  ngOnInit() {
    this.buscarEstados();
    this.buscarEspecialidades();
  }

  private emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.PROFISSIONAL,
      PermissoesEnum.PROFISSIONAL_CADASTRAR,
      PermissoesEnum.PROFISSIONAL_ALTERAR,
      PermissoesEnum.PROFISSIONAL_DESATIVAR]);
  }

  createForm() {
    if (!this.profissional.listaTelefoneProfissional) {
      this.profissional.listaTelefoneProfissional = new Array<TelefoneProfissional>();
    }

    if (!this.profissional.listaEmailProfissional) {
      this.profissional.listaEmailProfissional = new Array<EmailProfissional>();
    }
    this.profissionaisForm = this.formBuilder.group({
      nome: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(300),
        ]),
      ],

      dataNascimento: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(10),
          ValidateData,
          ValidateDataFutura,
        ]),
      ],
      genero: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(300),
        ]),
      ],
      rg: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(300),
        ]),
      ],
      cpf: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(14),
          ValidateCPF,
        ]),
      ],
      endereco: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(300),
        ]),
      ],
      bairro: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(300),
        ]),
      ],
      complemento: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(300),
        ]),
      ],
      numero: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(300),
        ]),
      ],
      estado: [
        { value: undefined, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(300),
        ]),
      ],
      municipio: [
        { value: null, disabled: true },
        Validators.compose([]),
      ],
      cep: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([]),
      ],
      uf: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(2),
        ]),
      ],
      regConsRegional: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(300),
        ]),
      ],
      nit: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(300),
          ValidarNit,
        ]),
      ],
      especialidades: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(300),
        ]),
      ],
      uats: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(300),
        ]),
      ],
      profissional: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(300),
        ]),
      ],
      tipoProfissional: [
        { value: undefined, disabled: this.modoConsulta },
        Validators.compose([
          Validators.maxLength(300),
        ]),
      ],
    });
  }

  carregarTela() {
    this.route.params.subscribe((params) => {
      const id = params['id'];
      if (id) {
        this.getProfissional(id);
      }
      this.montarTitulo();
    });
  }

  getProfissional(id: string): void {
    this.profissional = new Profissional();
    this.service.pesquisarPorId(id).subscribe((profissional: Profissional) => {
      if (profissional) {
        this.profissional = profissional;
        if (this.profissional.listaProfissionalEspecialidade) {
          this.especialidadesSelecionadas = this.profissional.listaProfissionalEspecialidade.map((a) => { return a.especialidade; });
        }

        this.converterModelParaForm();
        if (!this.profissional.listaProfissionalEspecialidade) {
          this.profissional.listaProfissionalEspecialidade = new Array<ProfissionalEspecialidade>();
        }

        if (!this.profissional.listaUnidadeAtendimentoTrabalhadorProfissional) {
          this.profissional.listaUnidadeAtendimentoTrabalhadorProfissional = new Array<UatProfissional>();
        }

        this.especialidadesSelecionadas = this.profissional.listaProfissionalEspecialidade.map((a) => { return a.especialidade; });

        this.verificarIncosistencia();
      }
    },
      (error: any) => {
        this.mensagemError(error);
      }, () => {
        if (!this.profissional.listaEmailProfissional) {
          this.profissional.listaEmailProfissional = new Array<EmailProfissional>();
        }

        if (!this.profissional.listaTelefoneProfissional) {
          this.profissional.listaTelefoneProfissional = new Array<TelefoneProfissional>();
        }
      });
  }

  verificarIncosistencia() {
    this.inconsistencia = false;
    if (!this.modoConsulta && this.profissional) {
      let dataNascimento = '';
      let cpf = '';
      let tipoProfissional = '';
      let especialidade = '';

      if (this.isVazia(this.profissional.dataNascimento)) {
        this.inconsistencia = true;
        dataNascimento = '\"' + MensagemProperties.app_rst_labels_data_nascimento + '\"';
      }

      if (this.isVazia(this.profissional.cpf)) {
        if (this.isVazia(this.profissional.dataNascimento)) {
          cpf = ', ';
        }
        this.inconsistencia = true;
        cpf = cpf + '\"' + MensagemProperties.app_rst_labels_cpf + '\"';
      }

      if (this.isVazia(this.profissional.tipoProfissional)) {
        if (this.isVazia(this.profissional.cpf) || this.isVazia(this.profissional.dataNascimento)) {
          tipoProfissional = ', ';
        }
        this.inconsistencia = true;
        tipoProfissional = tipoProfissional + '\"' + MensagemProperties.app_rst_labels_tipo_profissional + '\"';
      }

      if (this.listaUndefinedOuVazia(this.profissional.listaProfissionalEspecialidade)) {
        if (this.isVazia(this.profissional.cpf) || this.isVazia(this.profissional.tipoProfissional)
          || this.isVazia(this.profissional.dataNascimento)) {
          especialidade = ', ';
        }
        this.inconsistencia = true;
        especialidade = especialidade + '\"' + MensagemProperties.app_rst_labels_especialidades + '\"';
      }

      if (this.inconsistencia) {
        this.mensagemInconsistencia = this.recursoPipe.transform('app_rst_mensagem_inconsistencia'
          , dataNascimento, cpf, tipoProfissional, especialidade);
      }
    }
  }

  converterModelParaForm() {
    this.profissionaisForm.patchValue({
      nome: this.profissional.nome,
      dataNascimento: this.profissional.dataNascimento ? DatePicker.convertDateForMyDatePicker(this.profissional.dataNascimento) : null,
      genero: Genero[this.profissional.genero],
      rg: this.profissional.rg,
      cpf: this.profissional.cpf,
      regConsRegional: this.profissional.registro,
      nit: this.profissional.nit,
      tipoProfissional: this.profissional.tipoProfissional,
      especialidades: this.isVazia(this.especialidadesSelecionadas) ? null :
        this.especialidadesSelecionadas.map((a) => { return a.id.toString(); }),
    });
    if (this.profissional.estado) {
      this.profissionaisForm.patchValue({
        uf: this.profissional.estado.id,
      });
    }
    if (this.profissional.listaEnderecoProfissional) {
      this.profissionaisForm.patchValue({
        endereco: this.profissional.listaEnderecoProfissional[0].endereco.descricao,
        bairro: this.profissional.listaEnderecoProfissional[0].endereco.bairro,
        cep: this.profissional.listaEnderecoProfissional[0].endereco.cep,
        estado: this.profissional.listaEnderecoProfissional[0].endereco.municipio.estado.id.toString(),
        municipio: this.profissional.listaEnderecoProfissional[0].endereco.municipio.id.toString(),
      });
      this.buscarMunicipio();
    }

    if (this.profissional.listaTelefoneProfissional) {
      let telefoneCelular: string;
      let telefoneFixo: string;
      this.profissional.listaTelefoneProfissional.forEach((element) => {
        if (TipoTelefone.CE === element.telefone.tipo) {
          telefoneCelular = element.telefone.numero;
        } else {
          telefoneFixo = element.telefone.numero;
        }
      });

      this.profissionaisForm.patchValue({
        telefone: telefoneFixo,
        celular: telefoneCelular,
      });
    }

  }

  montarTitulo(): void {
    this.title = MensagemProperties.app_rst_profissionais_title_cadastrar;
  }

  buscarEstados() {
    this.estadoService.buscarEstados().subscribe((dados: any) => {
      this.estados = dados;
    });
  }

  buscarEspecialidades() {
    this.especialidadeService.pesquisarEspecialidades().subscribe((retorno: any[]) => {
      let listaOption: IOption[];
      listaOption = [];
      if (retorno) {
        retorno.forEach((element) => {
          const item = new Option();
          item.value = element.id;
          item.label = element.descricao;
          listaOption.push(item);
        });
      }
      this.especialdades = listaOption;
    });
  }

  estadoAlterado() {
    this.profissionaisForm.patchValue({
      municipio: null,
    });
    this.buscarMunicipio();
  }

  buscarMunicipio(id?: number) {
    if (this.profissionaisForm.controls['estado'].value && this.profissionaisForm.controls['estado'].value !== 0) {
      if (!this.modoConsulta) {
        this.profissionaisForm.controls['municipio'].enable();
      }

      if (id) {

        this.pesquisarMunicipiosPorEstado(id);
      } else {
        this.pesquisarMunicipiosPorEstado(this.profissionaisForm.controls['estado'].value);
      }
    } else {
      this.profissionaisForm.controls['municipio'].disable();
    }

  }

  estadoSelecionado(): void {
    return this.profissionaisForm && this.profissionaisForm.controls['estado'].value;
  }

  existeEmail() {
    return this.profissional.listaEmailProfissional && this.profissional.listaEmailProfissional.length > 0;
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/profissional`]);
  }

  existeTelefone() {
    return this.profissional.listaTelefoneProfissional && this.profissional.listaTelefoneProfissional.length > 0;
  }
  existeEndereco() {
    return this.profissional.listaEnderecoProfissional && this.profissional.listaEnderecoProfissional.length > 0;
  }

  salvar() {
    if (this.validarCampos()) {
      this.prepareSave();
      this.service.salvar(this.profissional).subscribe((response: Profissional) => {
        this.profissional = response;
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.verificarIncosistencia();
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  validarCampos(): Boolean {
    let isValido = true;
    if (this.profissionaisForm.controls['nome'].invalid) {
      if (this.profissionaisForm.controls['nome'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.profissionaisForm.controls['nome'],
          MensagemProperties.app_rst_labels_nome);
        isValido = false;
      }

    }

    if (this.profissionaisForm.controls['dataNascimento'].value && this.profissionaisForm.controls['dataNascimento'].invalid) {
      if (this.profissionaisForm.controls['dataNascimento'].errors.validData) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.profissionaisForm.controls['dataNascimento'],
          MensagemProperties.app_rst_labels_data_nascimento);
        isValido = false;
      }
      if (this.profissionaisForm.controls['dataNascimento'].errors.validDataFutura) {
        this.mensagemErroComParametros('app_rst_labels_data_futura', this.profissionaisForm.controls['dataNascimento'],
          MensagemProperties.app_rst_labels_data_nascimento);
        isValido = false;
      }
    }

    if (this.profissionaisForm.controls['genero'].invalid) {
      if (this.profissionaisForm.controls['genero'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.profissionaisForm.controls['genero'],
          MensagemProperties.app_rst_labels_genero);
        isValido = false;
      }

    }

    if (this.profissionaisForm.controls['cpf'].value && this.profissionaisForm.controls['cpf'].invalid) {
      if (this.profissionaisForm.controls['cpf'].errors.validCPF) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.profissionaisForm.controls['cpf'],
          MensagemProperties.app_rst_labels_cpf);
        isValido = false;
      }

    }

    if (this.profissionaisForm.controls['nit'].value && this.profissionaisForm.controls['nit'].invalid) {

      if (this.profissionaisForm.controls['nit'].errors.validNIT) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.profissionaisForm.controls['nit'],
          MensagemProperties.app_rst_labels_nit);
        isValido = false;
      }
    }

    if (this.profissionaisForm.controls['regConsRegional'].invalid) {
      if (this.profissionaisForm.controls['regConsRegional'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.profissionaisForm.controls['regConsRegional'],
          MensagemProperties.app_rst_labels_rg_conselho_regional);
        isValido = false;
      }

    }
    return isValido;
  }

  verificarSeEnderecoEstaPreenchido() {
    if ((this.profissionaisForm.controls['endereco'].value && this.profissionaisForm.controls['endereco'].value !== '')
      || (this.profissionaisForm.controls['complemento'].value && this.profissionaisForm.controls['complemento'].value !== '')
      || (this.profissionaisForm.controls['numero'].value && this.profissionaisForm.controls['numero'].value !== '')
      || (this.profissionaisForm.controls['bairro'].value && this.profissionaisForm.controls['bairro'].value !== '')
      || (this.profissionaisForm.controls['estado'].value && this.profissionaisForm.controls['estado'].value === undefined)
      || (this.profissionaisForm.controls['municipio'].value && this.profissionaisForm.controls['municipio'].value !== 0)
      || (this.profissionaisForm.controls['cep'].value && this.profissionaisForm.controls['cep'].value !== '')) {
      return true;
    } else {
      return false;

    }

  }

  validarEndereco() {
    let isValido = true;
    if (!this.profissionaisForm.controls['endereco'].value) {
      this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.profissionaisForm.controls['endereco'],
        MensagemProperties.app_rst_labels_endereco);
      isValido = false;
    }

    if (!this.profissionaisForm.controls['cep'].value) {
      this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.profissionaisForm.controls['cep'],
        MensagemProperties.app_rst_labels_CEP);
      isValido = false;
    }

    if (!this.profissionaisForm.controls['estado'].value) {
      this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.profissionaisForm.controls['estado'],
        MensagemProperties.app_rst_labels_estado);
      isValido = false;
    }

    if (this.profissionaisForm.controls['estado'].value && !this.profissionaisForm.controls['municipio'].value) {
      this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.profissionaisForm.controls['municipio'],
        MensagemProperties.app_rst_labels_municipio);
      isValido = false;
    }

    return isValido;
  }

  prepareSave(): Profissional {
    const formModel = this.profissionaisForm.controls;
    this.profissional.nome = formModel.nome.value;
    this.profissional.dataNascimento = formModel.dataNascimento.value ?
      this.convertDateToString(formModel.dataNascimento.value.date) : null;
    this.profissional.genero = EnumValues.getNameFromValue(Genero, formModel.genero.value);
    this.profissional.nit = MascaraUtil.removerMascara(formModel.nit.value);
    this.profissional.cpf = MascaraUtil.removerMascara(formModel.cpf.value);
    this.profissional.rg = formModel.rg.value;
    this.profissional.registro = formModel.regConsRegional.value;
    if (!formModel.uf.value) {
      this.profissional.estado = null;
    } else {
      this.profissional.estado = new Estado();
      this.profissional.estado.id = formModel.uf.value;
    }
    this.profissional.tipoProfissional = formModel.tipoProfissional.value;

    this.gerenciarEspecialidades(formModel);

    // endereco
    if (this.verificarSeEnderecoEstaPreenchido()) {
      const endereco: EnderecoProfissional = new EnderecoProfissional();
      if (!this.profissional.listaEnderecoProfissional) {
        this.profissional.listaEnderecoProfissional = new Array<EnderecoProfissional>();
      }
      endereco.endereco.descricao = formModel.endereco.value;
      endereco.endereco.complemento = formModel.complemento.value;
      endereco.endereco.numero = formModel.numero.value;
      endereco.endereco.bairro = formModel.bairro.value;

      if (formModel.municipio.value) {
        endereco.endereco.municipio.id = formModel.municipio.value;
        if (endereco.endereco.municipio.estado) {
          endereco.endereco.municipio.estado = new Estado();
        }
        endereco.endereco.municipio.estado.id = formModel.estado.value;
      } else {
        endereco.endereco.municipio = null;
      }
      endereco.endereco.tipoEndereco = EnumValues.getNameFromValue(TipoEndereco, TipoEndereco.P);

      if (formModel.cep.value) {
        endereco.endereco.cep = MascaraUtil.removerMascara(formModel.cep.value);
      }
      if (this.profissional.listaEnderecoProfissional && this.profissional.listaEnderecoProfissional.length > 0) {
        this.profissional.listaEnderecoProfissional[0].endereco = endereco.endereco;
      } else {
        this.profissional.listaEnderecoProfissional.push(endereco);
      }
    } else {
      this.profissional.listaEnderecoProfissional = new Array<EnderecoProfissional>();
    }
    return this.profissional;
  }
  existeUat() {
    return this.profissional.listaUnidadeAtendimentoTrabalhadorProfissional &&
      this.profissional.listaUnidadeAtendimentoTrabalhadorProfissional.length > 0;
  }

  private gerenciarEspecialidades(formModel) {
    if (formModel.especialidades.value) {
      this.setNewsEspecialidades(formModel);
    }
    this.parseEspecialidadesFormToModel(formModel);
  }

  private setNewsEspecialidades(formModel) {
    formModel.especialidades.value.forEach((idEspecialidade) => {
      let jaExiste: boolean;
      this.profissional.listaProfissionalEspecialidade.forEach((profissionalEspecialidade) => {
        if (profissionalEspecialidade.especialidade.id === Number(idEspecialidade)) {
          jaExiste = true;
        }
      });
      if (!jaExiste) {
        const profissionalEspecialidade = new ProfissionalEspecialidade();
        profissionalEspecialidade.especialidade.id = Number(idEspecialidade);
        this.profissional.listaProfissionalEspecialidade.push(profissionalEspecialidade);
      }
    });
  }

  private parseEspecialidadesFormToModel(formModel) {
    const profissionalEspecialidades = [];
    this.profissional.listaProfissionalEspecialidade.forEach((profissionalEspecialidade) => {
      let existe = false;
      if (formModel.especialidades.value) {
        formModel.especialidades.value.forEach((idEspecialidade) => {
          if (profissionalEspecialidade.especialidade.id === Number(idEspecialidade)) {
            existe = true;
          }
        });
      }
      if (!existe) {
        profissionalEspecialidades.push(profissionalEspecialidade);
      }
    });

    profissionalEspecialidades.forEach((element) => {
      this.profissional.listaProfissionalEspecialidade.splice(this.profissional.listaProfissionalEspecialidade.indexOf(element), 1);
    });
  }
}
