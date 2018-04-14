import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { MensagemProperties } from './../../compartilhado/utilitario/recurso.pipe';
import { AutorizacaoGuard } from './../../seguranca/autorizacao.guard';
import { PaginationModule } from 'ngx-bootstrap';
import { CompartilhadoModule } from './../../compartilhado/compartilhado.module';
import { RouterModule, Routes } from '@angular/router';
import { GrupoPerguntaService } from './../../servico/grupo-pergunta.service';
import { CadastroGrupoPerguntaComponent } from './cadastro-grupo-pergunta/cadastro-grupo-pergunta.component';
import { PesquisaGrupoPerguntaComponent } from './pesquisa-grupo-pergunta/pesquisa-grupo-pergunta.component';
import { NgModule } from '@angular/core';

const routes: Routes = [
  {
    path: '', component: PesquisaGrupoPerguntaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_grupo_pergunta_title_pesquisar,
      permissoes: [PermissoesEnum.GRUPO_PERGUNTA,
      PermissoesEnum.GRUPO_PERGUNTA_CADASTRAR,
      PermissoesEnum.GRUPO_PERGUNTA_ALTERAR,
      PermissoesEnum.GRUPO_PERGUNTA_CONSULTAR,
      PermissoesEnum.GRUPO_PERGUNTA_DESATIVAR],
    },
  },
  {
    path: 'cadastrar', component: CadastroGrupoPerguntaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_grupo_pergunta_title_cadastro,
      permissoes: [PermissoesEnum.GRUPO_PERGUNTA,
      PermissoesEnum.GRUPO_PERGUNTA_CADASTRAR],
    },
  },
  {
    path: ':id', component: CadastroGrupoPerguntaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_grupo_pergunta_title_cadastro,
      permissoes: [PermissoesEnum.GRUPO_PERGUNTA,
      PermissoesEnum.GRUPO_PERGUNTA_ALTERAR,
      PermissoesEnum.GRUPO_PERGUNTA_CONSULTAR,
      PermissoesEnum.GRUPO_PERGUNTA_DESATIVAR],
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
  declarations: [PesquisaGrupoPerguntaComponent, CadastroGrupoPerguntaComponent],
  providers: [GrupoPerguntaService],
})
export class GrupoPerguntaModule { }
