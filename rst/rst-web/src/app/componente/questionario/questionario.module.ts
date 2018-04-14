import { RespostaQuestionarioTrabalhadorService } from './../../servico/resposta-questionario-trabalhador.service';
import { PerguntaService } from './../../servico/pergunta.service';
import { RespostaService } from './../../servico/resposta.service';
import { ModalSelecionarPerguntaComponent } from './../../compartilhado/modal-selecionar-pergunta/modal-selecionar-pergunta.component';
import { PerguntaQuestionarioComponent } from './pergunta-questionario/pergunta-questionario.component';
import { PerfilService } from './../../servico/perfil.service';
import { TipoQuestionarioService } from './../../servico/tipo-questionario.service';
import { PeriodicidadeService } from './../../servico/periodicidade.service';
import { CadastroQuestionarioComponent } from './cadastro-questionario/cadastro-questionario.component';
import { QuestionarioService } from './../../servico/questionario.service';
import { PaginationModule, AlertModule } from 'ngx-bootstrap';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { AutorizacaoGuard } from './../../seguranca/autorizacao.guard';
import { PesquisaQuestionarioComponent } from './pesquisa-questionario/pesquisa-questionario.component';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { QuestionarioIntermediarioComponent } from './questionario-intermediario/questionario-intermediario.component';
import { GrupoPerguntaService } from '../../servico/grupo-pergunta.service';
import { ModalSelecionarRespostaComponent } from '../../compartilhado/modal-selecionar-resposta/modal-selecionar-resposta.component';
import { PerguntaQuestionarioService } from '../../servico/pergunta-questionario.service';
import { RespostaQuestionaioService } from '../../servico/resposta-questionario.service';
import { IndicadorQuestionarioService } from '../../servico/indicador-questionario.service';
// tslint:disable-next-line:max-line-length
import { ModalSelecionarIndicadorQuestionarioComponent } from 'app/compartilhado/modal-selecionar-indicador/modal-selecionar-indicador.component';

const routes: Routes = [
  {
    path: '', component: QuestionarioIntermediarioComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_menu_questionario,
      permissoes: [PermissoesEnum.QUESTIONARIO,
      PermissoesEnum.QUESTIONARIO_CADASTRAR,
      PermissoesEnum.QUESTIONARIO_ALTERAR,
      PermissoesEnum.QUESTIONARIO_CONSULTAR,
      PermissoesEnum.QUESTIONARIO_DESATIVAR,
      PermissoesEnum.GRUPO_PERGUNTA,
      PermissoesEnum.GRUPO_PERGUNTA_CADASTRAR,
      PermissoesEnum.GRUPO_PERGUNTA_ALTERAR,
      PermissoesEnum.GRUPO_PERGUNTA_CONSULTAR,
      PermissoesEnum.GRUPO_PERGUNTA_DESATIVAR,
      PermissoesEnum.INDICADOR_QUESTIONARIO,
      PermissoesEnum.INDICADOR_QUESTIONARIO_CADASTRAR,
      PermissoesEnum.INDICADOR_QUESTIONARIO_CONSULTAR,
      PermissoesEnum.INDICADOR_QUESTIONARIO_ALTERAR,
      PermissoesEnum.INDICADOR_QUESTIONARIO_DESATIVAR,
      PermissoesEnum.PERGUNTA,
      PermissoesEnum.PERGUNTA_CADASTRAR,
      PermissoesEnum.PERGUNTA_ALTERAR,
      PermissoesEnum.PERGUNTA_CONSULTAR,
      PermissoesEnum.PERGUNTA_DESATIVAR,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_CADASTRAR,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_ALTERAR,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_CONSULTAR,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_DESATIVAR,
      PermissoesEnum.RESPOSTA,
      PermissoesEnum.RESPOSTA_CADASTRAR,
      PermissoesEnum.RESPOSTA_ALTERAR,
      PermissoesEnum.RESPOSTA_CONSULTAR,
      PermissoesEnum.RESPOSTA_DESATIVAR],
    },
  },
  {
    path: 'pesquisar', component: PesquisaQuestionarioComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_menu_questionario,
      permissoes: [PermissoesEnum.QUESTIONARIO,
      PermissoesEnum.QUESTIONARIO_CADASTRAR,
      PermissoesEnum.QUESTIONARIO_ALTERAR,
      PermissoesEnum.QUESTIONARIO_CONSULTAR,
      PermissoesEnum.QUESTIONARIO_DESATIVAR],
    },
  },
  {
    path: 'cadastrar', component: CadastroQuestionarioComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_questionario_title_cadastro,
      permissoes: [PermissoesEnum.QUESTIONARIO,
      PermissoesEnum.QUESTIONARIO_CADASTRAR],
    },
  },
  {
    path: ':id', component: CadastroQuestionarioComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_questionario_title_cadastro,
      permissoes: [PermissoesEnum.QUESTIONARIO,
      PermissoesEnum.QUESTIONARIO_ALTERAR,
      PermissoesEnum.QUESTIONARIO_CONSULTAR,
      PermissoesEnum.QUESTIONARIO_DESATIVAR],
    },
  },
  {
    path: ':id/perguntas', component: PerguntaQuestionarioComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_questionario_title_pergunta,
      permissoes: [PermissoesEnum.QUESTIONARIO,
      PermissoesEnum.QUESTIONARIO_CADASTRAR,
      PermissoesEnum.QUESTIONARIO_ALTERAR,
      PermissoesEnum.QUESTIONARIO_CONSULTAR,
      PermissoesEnum.QUESTIONARIO_DESATIVAR],
    },
  },
  {
    path: ':id/perguntas/:idPergunta', component: PerguntaQuestionarioComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_questionario_title_pergunta,
      permissoes: [PermissoesEnum.QUESTIONARIO,
      PermissoesEnum.QUESTIONARIO_CADASTRAR,
      PermissoesEnum.QUESTIONARIO_ALTERAR,
      PermissoesEnum.QUESTIONARIO_CONSULTAR,
      PermissoesEnum.QUESTIONARIO_DESATIVAR],
    },
  },

];

@NgModule({
  imports: [
    RouterModule.forChild(routes),
    CompartilhadoModule,
    RouterModule,
    PaginationModule.forRoot(),
    AlertModule.forRoot(),
  ],

  declarations: [PesquisaQuestionarioComponent, CadastroQuestionarioComponent,
    QuestionarioIntermediarioComponent, PerguntaQuestionarioComponent, ModalSelecionarPerguntaComponent,
    ModalSelecionarRespostaComponent, ModalSelecionarIndicadorQuestionarioComponent],

  providers: [RespostaQuestionaioService,
    QuestionarioService, PeriodicidadeService, PerguntaService, TipoQuestionarioService, RespostaQuestionarioTrabalhadorService,
    PerfilService, GrupoPerguntaService, RespostaService, PerguntaQuestionarioService, IndicadorQuestionarioService],
})
export class QuestionarioModule { }
