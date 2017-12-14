import { ClassificacaoPontuacaoService } from './../../servico/classificacao-pontuacao.service';
import { PaginationModule } from 'ngx-bootstrap';
import { CompartilhadoModule } from './../../compartilhado/compartilhado.module';
import { CadastroClassificacaoPontuacaoComponent } from './cadastro-classificacao-pontuacao/cadastro-classificacao-pontuacao.component';
import { PesquisaClassificacaoPontuacaoComponent } from './pesquisa-classificacao-pontuacao/pesquisa-classificacao-pontuacao.component';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { AutorizacaoGuard } from './../../seguranca/autorizacao.guard';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';

const routes: Routes = [
  {
    path: '', component: PesquisaClassificacaoPontuacaoComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_classificacao_pontuacao_title_pesquisar,
      permissoes: [PermissoesEnum.GRUPO_PERGUNTA,
      PermissoesEnum.GRUPO_PERGUNTA_CADASTRAR,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_ALTERAR,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_CONSULTAR,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_DESATIVAR],
    },
  },
  {
    path: 'cadastrar', component: CadastroClassificacaoPontuacaoComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_classificacao_pontuacao_title_cadastro,
      permissoes: [PermissoesEnum.GRUPO_PERGUNTA,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_CADASTRAR],
    },
  },
  {
    path: ':id', component: CadastroClassificacaoPontuacaoComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_classificacao_pontuacao_title_cadastro,
      permissoes: [PermissoesEnum.GRUPO_PERGUNTA,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_ALTERAR,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_CONSULTAR,
      PermissoesEnum.CLASSIFICACAO_PONTUACAO_DESATIVAR],
    },
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(routes),
    CompartilhadoModule,
    RouterModule,
    PaginationModule.forRoot(),
  ],
  declarations: [CadastroClassificacaoPontuacaoComponent, PesquisaClassificacaoPontuacaoComponent],
  providers: [ClassificacaoPontuacaoService],
})
export class ClassificacaoPontuacaoModule { }
