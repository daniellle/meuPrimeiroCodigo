import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { AutorizacaoGuard } from './../../seguranca/autorizacao.guard';
import { PaginationModule } from 'ngx-bootstrap';
import { CompartilhadoModule } from './../../compartilhado/compartilhado.module';
import { RouterModule, Routes } from '@angular/router';
import { IndicadorQuestionarioService } from './../../servico/indicador-questionario.service';
import { CadastroIndicadorQuestionarioComponent } from './cadastro-indicador-questionario/cadastro-indicador-questionario.component';
import { PesquisaIndicadorQuestionarioComponent } from './pesquisa-indicador-questionario/pesquisa-indicador-questionario.component';
import { NgModule } from '@angular/core';

const routes: Routes = [
  {
    path: '', component: PesquisaIndicadorQuestionarioComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_indicador_questionario_title_pesquisar,
      permissoes: [PermissoesEnum.INDICADOR_QUESTIONARIO,
      PermissoesEnum.INDICADOR_QUESTIONARIO_CADASTRAR,
      PermissoesEnum.INDICADOR_QUESTIONARIO_ALTERAR,
      PermissoesEnum.INDICADOR_QUESTIONARIO_CONSULTAR,
      PermissoesEnum.INDICADOR_QUESTIONARIO_DESATIVAR],
    },
  },
  {
    path: 'cadastrar', component: CadastroIndicadorQuestionarioComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_indicador_questionario_title_cadastro,
      permissoes: [PermissoesEnum.INDICADOR_QUESTIONARIO,
      PermissoesEnum.INDICADOR_QUESTIONARIO_CADASTRAR],
    },
  },
  {
    path: ':id', component: CadastroIndicadorQuestionarioComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_indicador_questionario_title_cadastro,
      permissoes: [PermissoesEnum.INDICADOR_QUESTIONARIO,
      PermissoesEnum.INDICADOR_QUESTIONARIO_ALTERAR,
      PermissoesEnum.INDICADOR_QUESTIONARIO_CONSULTAR,
      PermissoesEnum.INDICADOR_QUESTIONARIO_DESATIVAR],
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
  declarations: [PesquisaIndicadorQuestionarioComponent, CadastroIndicadorQuestionarioComponent],
  providers: [IndicadorQuestionarioService],
})
export class IndicadorQuestionarioModule { }
