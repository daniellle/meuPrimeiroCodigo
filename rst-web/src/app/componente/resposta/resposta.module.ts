import { RespostaService } from './../../servico/resposta.service';
import { AutorizacaoGuard } from './../../seguranca/autorizacao.guard';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { PaginationModule } from 'ngx-bootstrap';
import { CompartilhadoModule } from './../../compartilhado/compartilhado.module';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { PesquisaRespostaComponent } from './pesquisa-resposta/pesquisa-resposta.component';
import { CadastroRespostaComponent } from './cadastro-resposta/cadastro-resposta.component';

const routes: Routes = [
  {
    path: '', component: PesquisaRespostaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_resposta_title_pesquisar,
      permissoes: [PermissoesEnum.RESPOSTA,
      PermissoesEnum.RESPOSTA_CADASTRAR,
      PermissoesEnum.RESPOSTA_ALTERAR,
      PermissoesEnum.RESPOSTA_CONSULTAR,
      PermissoesEnum.RESPOSTA_DESATIVAR],
    },
  },
  {
    path: 'cadastrar', component: CadastroRespostaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_resposta_title_cadastro,
      permissoes: [PermissoesEnum.RESPOSTA,
      PermissoesEnum.RESPOSTA_CADASTRAR],
    },
  },
  {
    path: ':id', component: CadastroRespostaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_resposta_title_cadastro,
      permissoes: [PermissoesEnum.RESPOSTA,
      PermissoesEnum.RESPOSTA_ALTERAR,
      PermissoesEnum.RESPOSTA_CONSULTAR,
      PermissoesEnum.RESPOSTA_DESATIVAR],
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
  declarations: [PesquisaRespostaComponent, CadastroRespostaComponent],
  providers: [RespostaService],
})
export class RespostaModule { }
