import { RespostaQuestionaioService } from './../../../servico/resposta-questionario.service';
import { PerguntaQuestionarioFilter } from './../../../modelo/filtro-pergunta-questionario';
import { Pergunta } from './../../../modelo/pergunta.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { environment } from './../../../../environments/environment.prod';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { QuestionarioService } from './../../../servico/questionario.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit } from '@angular/core';
import { GrupoPerguntaService } from '../../../servico/grupo-pergunta.service';
import { GrupoPergunta } from '../../../modelo/grupo-pergunta.model';
import { PerguntaQuestionario } from '../../../modelo/pergunta-questionario.model';
import { EnumTipoResposta } from 'app/modelo/tipo-resposta-enum.model';
import { PerguntaQuestionarioService } from '../../../servico/pergunta-questionario.service';
import { IndicadorQuestionario } from '../../../modelo/indicador-questionario.model';
import { RespostaQuestionario } from '../../../modelo/resposta-questionario.model';
import { ListaPaginada } from '../../../modelo/lista-paginada.model';

@Component({
  selector: 'app-pergunta-questionario',
  templateUrl: './pergunta-questionario.component.html',
  styleUrls: ['./pergunta-questionario.component.scss'],
})
export class PerguntaQuestionarioComponent extends BaseComponent implements OnInit {

  idQuestionario: number;
  perguntaQuestionarioForm: FormGroup;
  perguntaQuestionario = new PerguntaQuestionario();
  listaPerguntaQuestionario = new Array<PerguntaQuestionario>();
  listaRespostasQuestionario = new Array<RespostaQuestionario>();
  filtro = new PerguntaQuestionarioFilter();
  tipoResposta = EnumTipoResposta;
  keysTipoResposta: string[];

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private questionarioService: QuestionarioService,
    private grupoPerguntaService: GrupoPerguntaService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    protected perguntaQuestionarioService: PerguntaQuestionarioService,
    private respostaQuestionaioService: RespostaQuestionaioService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.title = this.activatedRoute.snapshot.data.title;
    this.idQuestionario = this.activatedRoute.snapshot.params['id'];
    this.emModoConsulta();
    this.createForm();
    this.carregarListaPerguntaQuestionario();
    this.keysTipoResposta = Object.keys(this.tipoResposta);
  }

  private carregarListaPerguntaQuestionario() {
    if (this.idQuestionario) {
      this.filtro.idQuestionario = this.idQuestionario;
      this.perguntaQuestionarioService.pesquisarPaginado(this.filtro, this.paginacao)
        .subscribe((retorno: ListaPaginada<PerguntaQuestionario>) => {
          this.paginacao.pagina = 1;
          this.listaPerguntaQuestionario = retorno.list;
          this.paginacao = this.getPaginacao(this.paginacao, retorno);
        }, (error) => {
          this.mensagemError(error);
        });
    }
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/questionario/${this.idQuestionario}`]);
  }

  salvar(): void {
    if (this.validarCampos()) {
      this.perguntaQuestionarioService.salvar(this.prepareSave()).subscribe((response: PerguntaQuestionario) => {
        this.perguntaQuestionario = response;
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        if (this.perguntaQuestionario && Number(this.perguntaQuestionario.questionario.id) !== Number(this.idQuestionario)) {
          this.router.navigate([`${environment.path_raiz_cadastro}/questionario/${this.perguntaQuestionario.questionario.id}/perguntas`]);
        }
        this.idQuestionario = response.questionario.id;
        this.resetarFormulario();
        this.carregarListaPerguntaQuestionario();
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  private emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.QUESTIONARIO,
      PermissoesEnum.QUESTIONARIO_CADASTRAR,
      PermissoesEnum.QUESTIONARIO_ALTERAR,
      PermissoesEnum.QUESTIONARIO_DESATIVAR]);
  }

  private resetarFormulario() {
    this.perguntaQuestionarioForm.reset();
    this.perguntaQuestionario = new PerguntaQuestionario();
  }

  private createForm() {
    this.perguntaQuestionarioForm = this.formBuilder.group({
      pergunta: [
        { value: null, disabled: true },
        Validators.compose([
          Validators.required,
        ]),
      ],
      resposta: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
        ]),
      ],
      indicador: [
        { value: null, disabled: true },
        Validators.compose([
        ]),
      ],
      tipoResposta: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
        ]),
      ],
      grupo: [
        { value: null, disabled: true },
        Validators.compose([
          Validators.required,
        ]),
      ],
      ordemGrupo: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
        ]),
      ],
      ordemPergunta: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
        ]),
      ],
    });
  }

  private editPerguntaQuestionarioForm(perguntaQuestionarioSelecionado: PerguntaQuestionario) {
    this.perguntaQuestionarioForm.patchValue({
      pergunta: perguntaQuestionarioSelecionado.pergunta.descricao,
      indicador: perguntaQuestionarioSelecionado.indicadorQuestionario
        ? perguntaQuestionarioSelecionado.indicadorQuestionario.descricao : '',
      tipoResposta: perguntaQuestionarioSelecionado.tipoResposta,
      grupo: perguntaQuestionarioSelecionado.grupoPergunta.descricao,
      ordemGrupo: perguntaQuestionarioSelecionado.ordemGrupo,
      ordemPergunta: perguntaQuestionarioSelecionado.ordemPergunta,
    });
  }

  private prepareSave(): PerguntaQuestionario {
    let indicador: any;
    if (this.perguntaQuestionario.indicadorQuestionario && this.perguntaQuestionario.indicadorQuestionario.id) {
      indicador = this.perguntaQuestionario.indicadorQuestionario;
    }
    const formModel = this.perguntaQuestionarioForm.controls;
    const saveQuestionariopergunta: any = {
      id: this.perguntaQuestionario.id,
      questionario: { id: this.idQuestionario },
      grupoPergunta: this.perguntaQuestionario.grupoPergunta,
      ordemGrupo: formModel.ordemGrupo.value,
      pergunta: this.perguntaQuestionario.pergunta,
      ordemPergunta: formModel.ordemPergunta.value,
      indicadorQuestionario: indicador,
      tipoResposta: formModel.tipoResposta.value,
      respostaQuestionarios: this.perguntaQuestionario.respostaQuestionarios,
    };
    return saveQuestionariopergunta;
  }

  private validarCampos(): boolean {

    let isValido = true;

    if (!this.perguntaQuestionario.grupoPergunta.id) {
      this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.perguntaQuestionarioForm.controls['grupo'],
        MensagemProperties.app_rst_labels_grupo);
      isValido = false;
    }

    if (!this.perguntaQuestionario.pergunta.id) {
      this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.perguntaQuestionarioForm.controls['pergunta'],
        MensagemProperties.app_rst_labels_pergunta);
      isValido = false;
    }

    if (this.perguntaQuestionarioForm.controls['ordemGrupo'].invalid) {
      this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.perguntaQuestionarioForm.controls['ordemGrupo'],
        MensagemProperties.app_rst_labels_ordem_grupo);
      isValido = false;
    }

    if (this.perguntaQuestionarioForm.controls['ordemPergunta'].invalid) {
      this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.perguntaQuestionarioForm.controls['ordemPergunta'],
        MensagemProperties.app_rst_labels_ordem_pergunta);
      isValido = false;
    }

    if (this.perguntaQuestionarioForm.controls['ordemPergunta'].value && !Number(this.perguntaQuestionarioForm.controls['ordemPergunta'].value)) {
      this.mensagemErroComParametrosModel('app_rst_campo_invalido',
        MensagemProperties.app_rst_labels_ordem_pergunta);
      isValido = false;
    }

    if (this.perguntaQuestionarioForm.controls['ordemGrupo'].value && !Number(this.perguntaQuestionarioForm.controls['ordemGrupo'].value)) {
      this.mensagemErroComParametrosModel('app_rst_campo_invalido',
        MensagemProperties.app_rst_labels_ordem_grupo);
      isValido = false;
    }

    if (this.perguntaQuestionarioForm.controls['tipoResposta'].invalid) {
      this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.perguntaQuestionarioForm.controls['tipoResposta'],
        MensagemProperties.app_rst_labels_tipo_resposta);
      isValido = false;
    }

    if (this.perguntaQuestionario.respostaQuestionarios && this.perguntaQuestionario.respostaQuestionarios.length === 0) {
      this.mensagemError(MensagemProperties.app_rst_erro_nenhuma_resposta_adicionada);
      isValido = false;
    }

    return isValido;
  }

  selecionarGrupo(grupo) {
    this.perguntaQuestionario.grupoPergunta = grupo;
    this.perguntaQuestionarioForm.patchValue({
      grupo: grupo.descricao,
    });
  }

  limparGrupo() {
    this.perguntaQuestionario.grupoPergunta = new GrupoPergunta();
    this.perguntaQuestionarioForm.patchValue({
      grupo: '',
    });
  }

  selecionarPergunta(pergunta) {
    this.perguntaQuestionario.pergunta = pergunta;
    this.perguntaQuestionarioForm.patchValue({
      pergunta: pergunta.descricao,
    });
  }

  limparPergunta() {
    this.perguntaQuestionario.pergunta = new Pergunta();
    this.perguntaQuestionarioForm.patchValue({
      pergunta: '',
    });
  }

  selecionarIndicador(indicador) {
    this.perguntaQuestionario.indicadorQuestionario = indicador;
    this.perguntaQuestionarioForm.patchValue({
      indicador: indicador.descricao,
    });
  }

  limparIndicador() {
    this.perguntaQuestionario.indicadorQuestionario = new IndicadorQuestionario();
    this.perguntaQuestionarioForm.patchValue({
      indicador: '',
    });
  }

  selecionarRespostaQuestionario(respostaQuestionario) {
    if (this.validaRespostaQuestionario(respostaQuestionario)) {
      this.adicionarResposta(respostaQuestionario);
      this.ordenarRespostas();
    }
  }

  private validaRespostaQuestionario(respostaQuestionario): boolean {
    let isValido = true;

    if (this.listaRespostasQuestionario.length > 0
      && this.listaRespostasQuestionario.find((x) => x.resposta.id === respostaQuestionario.resposta.id)) {
      this.mensagemError(MensagemProperties.app_rst_resposta_existente);
      isValido = false;
    }

    if (this.listaRespostasQuestionario.length > 0
      && this.listaRespostasQuestionario.find((x) => x.ordemResposta === respostaQuestionario.ordemResposta)) {
      this.mensagemErroComParametrosModel('app_rst_resposta_questionario_ordem_ja_existe', respostaQuestionario.ordemResposta);
      isValido = false;
    }

    return isValido;
  }

  private ordenarRespostas(): void {
    this.listaRespostasQuestionario.sort((a, b) => {
      if (a.ordemResposta < b.ordemResposta) {
        return -1;
      } else if (a.ordemResposta > b.ordemResposta) {
        return 1;
      } else {
        return 0;
      }
    });
  }

  adicionarResposta(respostaQuestionario) {
    this.perguntaQuestionario.respostaQuestionarios.push(respostaQuestionario);
  }

  removeResposta(index) {
    this.perguntaQuestionario.respostaQuestionarios.splice(index, 1);
  }

  removePergunta(item) {
    const pergunta: PerguntaQuestionario = item;
    this.perguntaQuestionarioService.remover(pergunta).subscribe((retorno) => {
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.paginacao.pagina = 1;
      this.resetarFormulario();
      if (retorno && Number(retorno.id) !== Number(this.idQuestionario)) {
        this.router.navigate([`${environment.path_raiz_cadastro}/questionario/${retorno.id}/perguntas`]);
        this.idQuestionario = retorno.id;
        this.carregarListaPerguntaQuestionario();
      }
      this.carregarListaPerguntaQuestionario();
    }, (error) => {
      this.mensagemError(error);
    });
  }

  selecionar(item: PerguntaQuestionario) {
    this.buscarRespostasPerguntaQuestionario(item.id);
    this.perguntaQuestionario = new PerguntaQuestionario(item);
    this.editPerguntaQuestionarioForm(this.perguntaQuestionario);
  }

  private buscarRespostasPerguntaQuestionario(idDerguntaQuestionario) {
    this.respostaQuestionaioService.buscarPorIdPerguntaQuestionario(idDerguntaQuestionario)
      .subscribe((respostasQuestionario) => {
        this.perguntaQuestionario.respostaQuestionarios = respostasQuestionario;
      }, (error) => {
        this.mensagemError('Error ao carregar respostas para pergunta selecionada');
      });
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.perguntaQuestionarioService.pesquisarPaginado(this.filtro, this.paginacao)
      .subscribe((retorno: ListaPaginada<PerguntaQuestionario>) => {
        this.listaPerguntaQuestionario = retorno.list;
      });
  }
}
