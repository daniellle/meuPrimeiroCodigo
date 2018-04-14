import { TrabalhadorService } from 'app/servico/trabalhador.service';
import { Trabalhador } from 'app/modelo/trabalhador.model';
import { FiltroTrabalhador } from './../../../modelo/filtro-trabalhador.model';
import { PermissoesEnum } from './../../../modelo/enum/enum-permissoes';
import { PerguntaDTO } from './../../../modelo/pergunta-dto.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { QuestionarioTrabalhadorService } from './../../../servico/questionario-trabalhador.service';
import { RespostaQuestionarioTabalhador } from './../../../modelo/resposta-questionario-trabalhador.model';
import { RespostaQuestionario } from './../../../modelo/resposta-questionario.model';
import { QuestionarioTrabalhador } from './../../../modelo/questionario-trabalhador';
import { environment } from './../../../../environments/environment';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { QuestionarioDTO } from './../../../modelo/questionario-dto.model';
import { MontarQuestionarioService } from './../../../servico/montar-questionario.service';
import { DialogService } from 'ng2-bootstrap-modal';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-aplicar-questionario',
  templateUrl: './aplicar-questionario.component.html',
  styleUrls: ['./aplicar-questionario.component.scss'],
})

export class AplicarQuestionarioComponent extends BaseComponent implements OnInit {

  public questionario: QuestionarioDTO;
  public questionarioTrabalhador: QuestionarioTrabalhador;
  aplicarQuestionarioForm: FormGroup;
  perguntaRespostasMap: Map<number, number[]> = new Map<number, number[]>();
  idRespostas: number[];
  public idTrabalhador: number;
  private meusdados: boolean;
  private rastroIgev: boolean;

  constructor(
    private router: Router, private formBuilder: FormBuilder, private route: ActivatedRoute,
    protected bloqueioService: BloqueioService, private service: MontarQuestionarioService,
    protected dialogo: ToastyService, private dialogService: DialogService,
    private activatedRoute: ActivatedRoute,
    private questionarioTrabalhadorService: QuestionarioTrabalhadorService,
    private trabalhadorService: TrabalhadorService,
  ) {
    super(bloqueioService, dialogo);
    this.buscarQuestionario();
  }

  ngOnInit() {
    this.title = this.activatedRoute.snapshot.data.title;
    this.meusdados = this.activatedRoute.snapshot.url[0].toString() === 'meusdados';
    this.rastroIgev = this.activatedRoute.snapshot.url[0].toString() === 'igev';
    this.questionario = new QuestionarioDTO();
    this.modoConsulta = !Seguranca.isPermitido([PermissoesEnum.TRABALHADOR_CADASTRAR,
      PermissoesEnum.TRABALHADOR_ALTERAR,  PermissoesEnum.TRABALHADOR_DESATIVAR]);
    this.idTrabalhador = this.activatedRoute.snapshot.params['id'];
    this.verificarPermisaoTrabalhador();
    this.verificarPeriodicidade();
    this.createForm();
  }

  verificarPermisaoTrabalhador() {
    const filtroTrabalhador = new FiltroTrabalhador();
    filtroTrabalhador.id = this.idTrabalhador.toString();
    filtroTrabalhador.aplicarDadosFilter = false;
    this.trabalhadorService.buscarPorId(filtroTrabalhador).subscribe((retorno: Trabalhador) => {
      if (!(Seguranca.getUsuario().papeis.indexOf('TRA') > -1 &&
        Seguranca.getUsuario().sub === retorno.cpf)) {
          this.router.navigate(['/acessonegado']);
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  voltar(): void {
    if (this.meusdados) {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados/${this.idTrabalhador}/igev`]);
    } else if (this.rastroIgev) {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/igev`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}/igev`]);
    }
  }

  verificarPeriodicidade() {
    this.questionarioTrabalhadorService.consultarPeriodicidade(this.idTrabalhador)
      .subscribe((retorno: boolean) => {
        if (!retorno) {
          if (this.meusdados) {
            this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados/${this.idTrabalhador}/historico`]);
          } else if (this.rastroIgev) {
            this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/igev/${this.idTrabalhador}/historico`]);
          } else {
            this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}/historico`]);
          }
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  buscarQuestionario() {
    this.service.buscarQuestionario().subscribe((retorno: QuestionarioDTO) => {
      this.questionario = retorno;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  public selectDeselectCheck(idPergunta: any, idResposta: any, event: any) {
    let array = new Array<number>();
    if (this.perguntaRespostasMap.get(idPergunta)) {
      array = this.perguntaRespostasMap.get(idPergunta);
    }
    if (event.checked) {
      array.push(idResposta);
    } else {
      const index: number = array.indexOf(idResposta);
      array.splice(index, 1);
    }
    this.perguntaRespostasMap.set(idPergunta, array);
  }

  public selectDeselectRadio(idPergunta: any, idResposta: any) {
    const array = new Array<number>();
    array.push(idResposta);
    this.perguntaRespostasMap.set(idPergunta, array);
  }

  private createForm() {
    this.aplicarQuestionarioForm = this.formBuilder.group({
      descricao: [
        { value: null, disabled: this.modoConsulta },
      ],
    });
  }

  isTipoRespostaSimples(tipo: string): boolean {
    return tipo === 'S';
  }

  salvar() {
    this.questionarioTrabalhador = new QuestionarioTrabalhador();
    this.questionarioTrabalhador.questionario.id = this.questionario.id;
    this.questionarioTrabalhador.trabalhador.id = this.idTrabalhador;

    this.perguntaRespostasMap.forEach((perguntaResposta) => {
      perguntaResposta.forEach((resposta) => {
        const respostaQuestionario = new RespostaQuestionario();
        const respostaQuestionarioTrabalhador = new RespostaQuestionarioTabalhador();
        respostaQuestionario.id = resposta;
        respostaQuestionarioTrabalhador.respostaQuestionario = respostaQuestionario;
        this.questionarioTrabalhador.listaRespostaQuestionarioTrabalhador.push(respostaQuestionarioTrabalhador);
      });
    });
    if (this.validar()) {
      this.questionarioTrabalhadorService.salvar(this.questionarioTrabalhador).subscribe((response: QuestionarioTrabalhador) => {
        this.questionarioTrabalhador = response;
        const idQuest = this.questionarioTrabalhador.id;
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        if (this.meusdados) {
          // tslint:disable-next-line:max-line-length
          this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados/${this.idTrabalhador}/questionario/${idQuest}/resultado`]);
        } else if (this.rastroIgev) {
          // tslint:disable-next-line:max-line-length
          this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/igev/${this.idTrabalhador}/questionario/${idQuest}/resultado`]);
        } else {
          this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}/questionario/${idQuest}/resultado`]);
        }
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  validar(): Boolean {
    let isValido = true;
    let perguntas = '';
    const listaPerguntaDTO = new Array<PerguntaDTO>();

    this.questionario.listaGrupoPerguntaQuestionario.forEach((grupo) => {
      grupo.listaPerguntaQuestionarioDTO.forEach((pergunta, i) => {
        listaPerguntaDTO.push(pergunta);
      });
    });

    listaPerguntaDTO.forEach((element, i) => {
      if (!this.perguntaRespostasMap.has(element.id) ||
        (this.perguntaRespostasMap.has(element.id) &&
          this.perguntaRespostasMap.get(element.id).length === 0)) {
        if (perguntas === '') {
          perguntas += element.numeracao;
        } else {
          if (i === (listaPerguntaDTO.length - 1)) {
            perguntas += ' e ' + element.numeracao;
          } else {
            perguntas += ', ' + element.numeracao;
          }
        }
        isValido = false;
      }
    });

    if (!isValido) {
      this.mensagemErroComParametrosModel('app_rst_questionario_pergunta_obrigatoria', perguntas);
    }

    return isValido;
  }

}
