import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CadastrarVacinaComponent } from './cadastrar-vacina/cadastrar-vacina.component';
import { RouterModule, Routes } from '@angular/router';
import { AutorizacaoGuard } from 'app/seguranca/autorizacao.guard';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { ImunizacaoService } from '../../servico/imunizacao.service';
import { DialogService } from 'ng2-bootstrap-modal/dist';

const routes: Routes = [
  {
    path: 'cadastrar', component: CadastrarVacinaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_imunizacao_title_cadastrar_vacina,
      permissoes: [PermissoesEnum.VACINA_AUTODECLARADA,
      PermissoesEnum.VACINA_AUTODECLARADA_ALTERAR,
      PermissoesEnum.VACINA_AUTODECLARADA_CADASTRAR,
      PermissoesEnum.VACINA_AUTODECLARADA_CONSULTAR,
      PermissoesEnum.VACINA_AUTODECLARADA_DESATIVAR],
    },
  },]

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    CompartilhadoModule,
  ],
  declarations: [CadastrarVacinaComponent],
  providers:[ImunizacaoService, DialogService]
})
export class ImunizacaoModule { }
