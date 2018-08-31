import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { AutorizacaoGuard } from './../../seguranca/autorizacao.guard';
import { PerguntaService } from './../../servico/pergunta.service';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { PaginationModule } from 'ngx-bootstrap';
import { CadastroPerguntaComponent } from './cadastro-pergunta/cadastro-pergunta.component';
import { PesquisaPerguntaComponent } from './pesquisa-pergunta/pesquisa-pergunta.component';

const routes: Routes = [
  {
    path: '', component: PesquisaPerguntaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_pergunta_title_pesquisar,
      permissoes: [PermissoesEnum.PERGUNTA,
      PermissoesEnum.PERGUNTA_CADASTRAR,
      PermissoesEnum.PERGUNTA_ALTERAR,
      PermissoesEnum.PERGUNTA_CONSULTAR,
      PermissoesEnum.PERGUNTA_DESATIVAR],
    },
  },
  {
    path: 'cadastrar', component: CadastroPerguntaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_pergunta_title_cadastro,
      PERMISSOES: [PermissoesEnum.PERGUNTA,
      PermissoesEnum.PERGUNTA_CADASTRAR],
    },
  },
  {
    path: ':id', component: CadastroPerguntaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_pergunta_title_cadastro,
      permissoes: [PermissoesEnum.PERGUNTA,
      PermissoesEnum.PERGUNTA_ALTERAR,
      PermissoesEnum.PERGUNTA_CONSULTAR,
      PermissoesEnum.PERGUNTA_DESATIVAR],
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
  declarations: [PesquisaPerguntaComponent, CadastroPerguntaComponent],
  providers: [PerguntaService],
})
export class PerguntaModule { }
