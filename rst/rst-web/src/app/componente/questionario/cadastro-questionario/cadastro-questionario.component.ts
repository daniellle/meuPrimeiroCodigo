import { EnumValues } from 'enum-values';
import { PeriodicidadeService } from './../../../servico/periodicidade.service';
import { TipoQuestionarioService } from './../../../servico/tipo-questionario.service';
import { TipoQuestionario } from './../../../modelo/tipo-questionario.model';
import { Periodicidade } from './../../../modelo/enum/periodicidade.model';
import { PerguntaQuestionario } from './../../../modelo/pergunta-questionario.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { environment } from './../../../../environments/environment';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { QuestionarioService } from './../../../servico/questionario.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Questionario } from './../../../modelo/questionario.model';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit } from '@angular/core';
import { StatusQuestionario } from '../../../modelo/enum/enum-status-questionario.model';
import { PerguntaFilter } from '../../../modelo/filtro-pergunta';
import { PerguntaQuestionarioService } from '../../../servico/pergunta-questionario.service';

@Component({
  selector: 'app-cadastro-questionario',
  templateUrl: './cadastro-questionario.component.html',
  styleUrls: ['./cadastro-questionario.component.scss'],
})
export class CadastroQuestionarioComponent extends BaseComponent implements OnInit {

  model: Questionario = new Questionario();
  id: number;
  questionarioForm: FormGroup;
  listaTipoQuestionario = new Array<TipoQuestionario>();
  listaPeriodicidade = new Array<Periodicidade>();
  listaPerguntaQuestionario = new Array<PerguntaQuestionario>();
  status = StatusQuestionario;
  filtro = new PerguntaFilter();

  public avisoPublicado = 'A edição de um questionário Publicado irá gerar uma nova versão.';

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private questionarioService: QuestionarioService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private tipoQuestionarioService: TipoQuestionarioService,
    private periodicidadeService: PeriodicidadeService,
    private perguntaQuestionarioService: PerguntaQuestionarioService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.title = this.activatedRoute.snapshot.data.title;
    this.id = this.activatedRoute.snapshot.params['id'];
    this.emModoConsulta();
    this.carregarPeriodicidades();
    this.carregarTiposQuestionario();
    this.createForm();
    this.setQuestionario();
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/questionario/pesquisar`]);
  }

  goToPerguntas(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/questionario/${this.id}/perguntas`]);
  }

  salvar(): void {
    if (this.validarCampos()) {
      this.questionarioService.salvar(this.prepareSave()).subscribe((response: Questionario) => {
        this.model = response;
        this.converterModelParaForm();
        if (this.model && Number(this.model.id) !== Number(this.id)) {
          this.router.navigate([`${environment.path_raiz_cadastro}/questionario/${this.model.id}`]);
        }
        this.id = this.model.id;
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  abrirModalPergunta() {
  }

  private emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.QUESTIONARIO,
      PermissoesEnum.QUESTIONARIO_CADASTRAR,
      PermissoesEnum.QUESTIONARIO_ALTERAR,
      PermissoesEnum.QUESTIONARIO_DESATIVAR]);
  }

  private carregarPeriodicidades(): void {
    this.periodicidadeService.buscarTodas().subscribe((response) => {
      this.listaPeriodicidade = response;
    }, ((error) => {
      this.mensagemError(error);
    }));
  };

  private carregarTiposQuestionario(): void {
    this.tipoQuestionarioService.buscarTodas().subscribe((response) => {
      this.listaTipoQuestionario = response;
    }, ((error) => {
      this.mensagemError(error);
    }));
  }

  private createForm() {
    this.questionarioForm = this.formBuilder.group({
      nome: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      descricao: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      periodicidade: [
        { value: '', disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
        ]),
      ],
      versao: [
        { value: null, disabled: true },
      ],
      tipoQuestionario: [
        { value: '', disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
        ]),
      ],
      pergunta: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      resposta: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      grupo: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      ordem: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      ordemGrupo: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      ordemPergunta: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      perfis: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
        ]),
      ],
      status: [
        { value: StatusQuestionario.E, disabled: true },
        Validators.compose([
          Validators.required,
        ]),
      ],
    });
  }

  private setQuestionario() {
    if (this.id) {
      this.carregarQuestionario();
    }
  }

  private carregarQuestionario(): void {
    this.questionarioService.buscarPorId(this.id).subscribe((questionario: Questionario) => {
      if (questionario && questionario.id) {
        this.model = questionario;
        this.converterModelParaForm();
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private converterModelParaForm() {
    this.questionarioForm.patchValue({
      nome: this.model.nome,
      descricao: this.model.descricao,
      status: StatusQuestionario[this.model.status],
      versao: this.model.versao,
      tipoQuestionario: this.model.tipoQuestionario.id,
      periodicidade: this.model.periodicidade.id,
    });
  }

  getStatus(value: string): string {
    return StatusQuestionario[value];
  }

  private prepareSave(): Questionario {
    const formModel = this.questionarioForm.controls;
    const saveQuestionario: Questionario = {
      id: this.model.id,
      nome: formModel.nome.value as string,
      descricao: formModel.descricao.value as string,
      periodicidade: { id: formModel.periodicidade.value } as Periodicidade,
      versao: formModel.versao.value as number,
      status: EnumValues.getNameFromValue(StatusQuestionario, formModel.status.value),
      tipoQuestionario: { id: formModel.tipoQuestionario.value } as TipoQuestionario,
    };
    return saveQuestionario;
  }

  private validarCampos(): boolean {
    let isValido = true;

    if (this.questionarioForm.controls['nome'].invalid) {
      if (this.questionarioForm.controls['nome'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.questionarioForm.controls['nome'],
          MensagemProperties.app_rst_labels_nome);
        isValido = false;
      }
    }

    if (this.questionarioForm.controls['descricao'].invalid) {
      if (this.questionarioForm.controls['descricao'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.questionarioForm.controls['descricao'],
          MensagemProperties.app_rst_labels_orientacoes);
        isValido = false;
      }
    }

    if (this.questionarioForm.controls['tipoQuestionario'].invalid) {
      if (this.questionarioForm.controls['tipoQuestionario'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.questionarioForm.controls['tipoQuestionario'],
          MensagemProperties.app_rst_labels_tipo);
        isValido = false;
      }
    }

    if (this.questionarioForm.controls['periodicidade'].invalid) {
      if (this.questionarioForm.controls['periodicidade'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.questionarioForm.controls['periodicidade'],
          MensagemProperties.app_rst_labels_validade);
        isValido = false;
      }
    }

    return isValido;
  }

  selecionar(model: any) {
    if (model && model.id) {
      this.router.navigate([`perguntas/${model.id}`], { relativeTo: this.activatedRoute });
    }
  }

  podePublicar(): boolean {
    return this.model.id && this.questionarioForm.controls['status'].value === StatusQuestionario.E;
  }

  publicar() {
    if (this.validarCampos()) {
      this.questionarioService.publicar(this.prepareSave()).subscribe((response: Questionario) => {
        this.model = response;
        this.converterModelParaForm();
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  ehPublicado(): boolean {
    return this.id && this.model && this.model.status === 'P';
  }

}
