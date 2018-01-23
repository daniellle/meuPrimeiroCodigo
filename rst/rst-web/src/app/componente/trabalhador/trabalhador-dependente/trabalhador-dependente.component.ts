import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { DatePicker } from './../../../compartilhado/utilitario/date-picker';
import { TrabalhadorDependente } from 'app/modelo/trabalhador-dependente.model';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { EnumValues } from 'enum-values';
import { ValidateData, ValidateDataFutura } from 'app/compartilhado/validators/data.validator';
import { TrabalhadorDependenteService } from './../../../servico/trabalhador-dependente.service';
import { Component, OnInit } from '@angular/core';
import { TipoDependente } from 'app/modelo/enum/enum-tipo-dependente.model';
import { ActivatedRoute, Router } from '@angular/router';
import { TrabalhadorService } from 'app/servico/trabalhador.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DialogService } from 'ng2-bootstrap-modal';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BaseComponent } from 'app/componente/base.component';
import { Genero } from 'app/modelo/enum/enum-genero.model';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ValidateCPF } from 'app/compartilhado/validators/cpf.validator';

@Component({
  selector: 'app-trabalhador-dependente',
  templateUrl: './trabalhador-dependente.component.html',
  styleUrls: ['./trabalhador-dependente.component.scss'],
})
export class TrabalhadorDependenteComponent extends BaseComponent implements OnInit {

  public tipoDependente = TipoDependente;
  public keysTiposDependente: string[];
  public trabalhadorDependente: TrabalhadorDependente;
  public trabalhadorDependenteBusca: TrabalhadorDependente;
  public listaTrabalhadorDependente: TrabalhadorDependente[];
  public genero = Genero;
  public idTrabalhador: number;
  public indexItemSelecionado = -1;
  public trabalhadorDependenteForm: FormGroup;
  public selecionado = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: TrabalhadorDependenteService,
    private trabalhadorService: TrabalhadorService,
    private formBuilder: FormBuilder,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private modalService: NgbModal,
    private dialogService: DialogService) {
    super(bloqueioService, dialogo);
    this.tipoTela();
    this.carregarCombosEnum();
    this.createForm();
    this.carregarTela();
    this.bloqueiarCampos();
  }

  ngOnInit() {
    this.trabalhadorDependente = new TrabalhadorDependente();
  }

  tipoTela() {
    this.modoConsulta = !Seguranca.isPermitido([PermissoesEnum.TRABALHADOR_DEPENDENTE,
    PermissoesEnum.TRABALHADOR_DEPENDENTE_CADASTRAR,
    PermissoesEnum.TRABALHADOR_DEPENDENTE_ALTERAR,
    PermissoesEnum.TRABALHADOR_DEPENDENTE_DESATIVAR]);
  }

  carregarCombosEnum() {
    this.keysTiposDependente = Object.keys(this.tipoDependente);
  }

  carregarTela() {
    this.route.params.subscribe((params) => {
      this.idTrabalhador = params['id'];
      if (this.idTrabalhador) {
        this.pesquisarTrabalhadorDependentePaginadoService();
      }
    });
  }

  pageChangedTrabalhadorDependente(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarTrabalhadorDependentePaginadoService();
  }

  private pesquisarTrabalhadorDependentePaginadoService(): void {
    this.service.pesquisarTrabalhadorDependente(this.idTrabalhador, this.paginacao).
      subscribe((retorno: ListaPaginada<TrabalhadorDependente>) => {
        if (retorno.quantidade !== 0) {
          this.listaTrabalhadorDependente = retorno.list;
          this.paginacao = this.getPaginacao(this.paginacao, retorno);
        }
      });
  }

  buscarTrabalhadorDependentePorCpf(cpf: string) {
    this.paginacao.pagina = 1;
    this.service.buscarPorCpf(cpf, this.idTrabalhador).subscribe((trabalhadorDependente: TrabalhadorDependente) => {
      if (!trabalhadorDependente) {
        this.trabalhadorDependenteBusca = new TrabalhadorDependente();
        this.limparForm();
        this.indexItemSelecionado = -1;
      } else {
        this.trabalhadorDependenteBusca = trabalhadorDependente;
        this.converterModelDependneteParaForm();
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  buscarDependente(event) {
    this.desbloqueiarCampos();

    if (event.target.value) {
      this.selecionado = true;
      this.trabalhadorDependenteForm.patchValue({
        cpf: event.target.value,
      });
      this.buscarTrabalhadorDependentePorCpf(MascaraUtil.removerMascara(this.trabalhadorDependenteForm.controls['cpf'].value));
    }
  }

  buscarTrabalhador() {
    this.service.buscarPorId(this.idTrabalhador).subscribe((listaTrabalhadorDependente: TrabalhadorDependente[]) => {
      if (!listaTrabalhadorDependente) {
        this.listaTrabalhadorDependente = new Array<TrabalhadorDependente>();
      } else {
        this.listaTrabalhadorDependente = listaTrabalhadorDependente;
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  bloqueiarCampos() {
    this.trabalhadorDependenteForm.controls['nome'].disable();
    this.trabalhadorDependenteForm.controls['dataNascimento'].disable();
    this.trabalhadorDependenteForm.controls['tipoDependente'].disable();
    this.trabalhadorDependenteForm.controls['genero'].disable();
    this.trabalhadorDependenteForm.controls['inativo'].disable();
  }

  desbloqueiarCampos() {
    this.trabalhadorDependenteForm.controls['nome'].enable();
    this.trabalhadorDependenteForm.controls['dataNascimento'].enable();
    this.trabalhadorDependenteForm.controls['tipoDependente'].enable();
    this.trabalhadorDependenteForm.controls['genero'].enable();
    this.trabalhadorDependenteForm.controls['inativo'].enable();
  }

  salvar(): void {
    if (this.validarCampos()) {
      this.prepareSave();
      this.service.salvar(this.trabalhadorDependente, this.idTrabalhador).subscribe((response: TrabalhadorDependente) => {
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.carregarTela();
      }, (error) => {
        this.mensagemError(error);
      });

      this.trabalhadorDependenteForm.controls['cpf'].enable();
      this.createForm();
    }
  }

  voltar(): void {
    if (this.route.snapshot.url[0].path === 'meusdados') {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}`]);
    }
  }

  validarCampos(): Boolean {
    let isValido = true;

    if (this.trabalhadorDependenteForm.controls['nome'].invalid) {
      if (this.trabalhadorDependenteForm.controls['nome'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.trabalhadorDependenteForm.controls['nome'],
          MensagemProperties.app_rst_labels_nome);
        isValido = false;
      }
    }

    if (this.trabalhadorDependenteForm.controls['cpf'].invalid) {

      if (this.trabalhadorDependenteForm.controls['cpf'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.trabalhadorDependenteForm.controls['cpf'],
          MensagemProperties.app_rst_labels_cpf);
        isValido = false;
      }

      if (!this.trabalhadorDependenteForm.controls['cpf'].errors.required &&
        this.trabalhadorDependenteForm.controls['cpf'].errors.validCPF) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.trabalhadorDependenteForm.controls['cpf'],
          MensagemProperties.app_rst_labels_cpf);
        isValido = false;
      }
    }

    if (this.trabalhadorDependenteForm.controls['dataNascimento'].invalid) {

      if (this.trabalhadorDependenteForm.controls['dataNascimento'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.trabalhadorDependenteForm.controls['dataNascimento'],
          MensagemProperties.app_rst_labels_data_nascimento);
        isValido = false;

      } else if (!this.trabalhadorDependenteForm.controls['dataNascimento'].errors.valiData) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.trabalhadorDependenteForm.controls['dataNascimento'],
          MensagemProperties.app_rst_labels_data_nascimento);
        isValido = false;
      }

      if (this.trabalhadorDependenteForm.controls['dataNascimento'].errors.validDataFutura) {
        this.mensagemErroComParametros('app_rst_labels_data_futura', this.trabalhadorDependenteForm.controls['dataNascimento'],
          MensagemProperties.app_rst_labels_data_nascimento);
        isValido = false;
      }
    }

    if (this.trabalhadorDependenteForm.controls['tipoDependente'].invalid) {
      if (this.trabalhadorDependenteForm.controls['tipoDependente'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.trabalhadorDependenteForm.controls['tipoDependente'],
          MensagemProperties.app_rst_labels_tipo);
        isValido = false;
      }
    }

    if (this.trabalhadorDependenteForm.controls['genero'].invalid) {
      if (this.trabalhadorDependenteForm.controls['genero'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.trabalhadorDependenteForm.controls['genero'],
          MensagemProperties.app_rst_labels_genero);
        isValido = false;
      }
    }
    return isValido;
  }

  selecionarDependente(trabalhadorDependente: TrabalhadorDependente, index: any) {
    if (!this.modoConsulta) {
      this.trabalhadorDependente = new TrabalhadorDependente(trabalhadorDependente);
      this.indexItemSelecionado = index;
      this.converterModelParaForm();
      this.trabalhadorDependenteForm.controls['cpf'].disable();
      this.desbloqueiarCampos();
      this.selecionado = true;
    }
  }

  prepareSave() {
    const formModel = this.trabalhadorDependenteForm.controls;
    this.trabalhadorDependente.dependente.nome = formModel.nome.value;
    this.trabalhadorDependente.dependente.dataNascimento = formModel.dataNascimento.value ?
      this.convertDateToString(formModel.dataNascimento.value.date) : null;
    this.trabalhadorDependente.dependente.cpf = MascaraUtil.removerMascara(formModel.cpf.value);
    this.trabalhadorDependente.tipoDependente = formModel.tipoDependente.value;
    this.trabalhadorDependente.dependente.genero = EnumValues.getNameFromValue(Genero, formModel.genero.value);
    this.trabalhadorDependente.inativo = formModel.inativo.value;
  }

  converterModelDependneteParaForm() {

    this.trabalhadorDependenteForm.patchValue({
      nome: this.trabalhadorDependenteBusca.dependente.nome,
      dataNascimento: this.trabalhadorDependenteBusca.dependente.dataNascimento ?
        DatePicker.convertDateForMyDatePicker(this.trabalhadorDependenteBusca.dependente.dataNascimento) : null,
      cpf: this.trabalhadorDependenteBusca.dependente.cpf,
      tipoDependente: this.trabalhadorDependenteBusca.tipoDependente,
      genero: Genero[this.trabalhadorDependenteBusca.dependente.genero],
    });
  }

  limparFormComCpf() {
    this.limparCPF();
    this.limparForm();
    this.selecionado = false;
    this.trabalhadorDependente = new TrabalhadorDependente();
  }

  limparCPF() {
    this.trabalhadorDependenteForm.patchValue({
      cpf: '',
    });
    this.trabalhadorDependenteForm.controls['cpf'].enable();
  }

  limparForm() {
    this.trabalhadorDependenteForm.patchValue({
      nome: '',
      dataNascimento: null,
      tipoDependente: '',
      genero: '',
      inativo: '',
    });
  }

  converterModelParaForm() {
    this.trabalhadorDependenteForm.patchValue({
      nome: this.trabalhadorDependente.dependente.nome,
      dataNascimento: this.trabalhadorDependente.dependente.dataNascimento
        ? DatePicker.convertDateForMyDatePicker(this.trabalhadorDependente.dependente.dataNascimento) : null,
      cpf: this.trabalhadorDependente.dependente.cpf,
      tipoDependente: this.trabalhadorDependente.tipoDependente,
      genero: Genero[this.trabalhadorDependente.dependente.genero],
      inativo: this.trabalhadorDependente.inativo,
    });
  }

  remover(trabalhadorDependente: TrabalhadorDependente): void {
    this.service.desativarTrabalhadorDependente(trabalhadorDependente).subscribe((response: TrabalhadorDependente) => {
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.limparFormComCpf();
      const index: number = this.listaTrabalhadorDependente.indexOf(trabalhadorDependente);
      if (index !== -1) {
        this.listaTrabalhadorDependente.splice(index, 1);
      }
    }, (error) => {
      this.mensagemError(error);
    }, () => {
      this.pesquisarTrabalhadorDependentePaginadoService();
    });
  }

  hasPermissaoInclusao() {
    return Seguranca.isPermitido([PermissoesEnum.TRABALHADOR_DEPENDENTE, PermissoesEnum.TRABALHADOR_DEPENDENTE_CADASTRAR]);
  }

  createForm() {
    this.trabalhadorDependenteForm = this.formBuilder.group({
      nome: [
        { value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador() },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      dataNascimento: [
        { value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador() },
        Validators.compose([
          Validators.required,
          ValidateData,
          ValidateDataFutura,
        ]),
      ],
      cpf: [
        { value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador() },
        Validators.compose([
          Validators.required, ValidateCPF,
        ]),
      ],
      tipoDependente: [
        { value: undefined, disabled: this.modoConsulta || this.isSomenteTrabalhador() },
        Validators.compose([
          Validators.required,
        ]),
      ],
      genero: [
        { value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador() },
        Validators.compose([
          Validators.required,
        ]),
      ],
      inativo: [
        { value: null, disabled: this.modoConsulta || this.isSomenteTrabalhador() },
        Validators.compose([
          Validators.required,
        ]),
      ],
    });
  }

}
