import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CadastrarVacinaComponent } from './cadastrar-vacina/cadastrar-vacina.component';
import { RouterModule, Routes } from '@angular/router';
import { AutorizacaoGuard } from 'app/seguranca/autorizacao.guard';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

const routes: Routes = [
  {
    path: 'cadastrar', component: CadastrarVacinaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_trabalhador_title_pesquisar,
      permissoes: [PermissoesEnum.TRABALHADOR_CADASTRAR,
      PermissoesEnum.TRABALHADOR_ALTERAR,
      PermissoesEnum.TRABALHADOR_CONSULTAR,
      PermissoesEnum.TRABALHADOR_DESATIVAR],
    },
  },]

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
  ],
  declarations: [CadastrarVacinaComponent]
})
export class ImunizacaoModule { }
