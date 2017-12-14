import { AutorizacaoGuard } from './../../seguranca/autorizacao.guard';
import { UatService } from 'app/servico/uat.service';
import { EspecialidadeService } from './../../servico/especialidade.service';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PesquisarProfissionaisComponent } from './pesquisar-profissionais/pesquisar-profissionais.component';
import { Routes, RouterModule } from '@angular/router';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { PaginationModule, TypeaheadModule, AlertModule } from 'ngx-bootstrap';
import { DialogService } from 'ng2-bootstrap-modal';
import { TextMaskModule } from 'angular2-text-mask';
import { ProfissionalService } from '../../servico/profissional.service';
import { CadastroProfissionaisComponent } from './cadastro-profissionais/cadastro-profissionais.component';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { MenuModule } from 'app/compartilhado/menu/menu.module';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

const routes: Routes = [
  {
    path: '', component: PesquisarProfissionaisComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_profissionais_title_pesquisar,
      permissoes: [PermissoesEnum.PROFISSIONAL,
        PermissoesEnum.PROFISSIONAL_CADASTRAR,
        PermissoesEnum.PROFISSIONAL_ALTERAR,
        PermissoesEnum.PROFISSIONAL_CONSULTAR,
        PermissoesEnum.PROFISSIONAL_DESATIVAR],
    },
  },
  {
    path: 'cadastrar', component: CadastroProfissionaisComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_profissionais_title_cadastrar,
      permissoes: [PermissoesEnum.PROFISSIONAL,
        PermissoesEnum.PROFISSIONAL_CADASTRAR],
    },
  },
  {
    path: ':id', component: CadastroProfissionaisComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_profissionais_title_cadastrar,
      permissoes: [PermissoesEnum.PROFISSIONAL,
        PermissoesEnum.PROFISSIONAL_ALTERAR,
        PermissoesEnum.PROFISSIONAL_CONSULTAR,
        PermissoesEnum.PROFISSIONAL_DESATIVAR],
    },
  },

];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    CompartilhadoModule,
    MenuModule,
    RouterModule,
    TextMaskModule,
    PaginationModule.forRoot(),
    TypeaheadModule.forRoot(),
    AlertModule.forRoot(),
  ],
  declarations: [PesquisarProfissionaisComponent, CadastroProfissionaisComponent],
  providers: [DialogService, ProfissionalService, EspecialidadeService, UatService],
})
export class ProfissionaisModule { }
