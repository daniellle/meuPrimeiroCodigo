import { AutorizacaoGuard } from './../../seguranca/autorizacao.guard';
import { DepartRegionalService } from './../../servico/depart-regional.service';
import { TypeaheadModule, TypeaheadContainerComponent } from 'ngx-bootstrap/typeahead';
import { PaginationModule } from 'ngx-bootstrap';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SindicatosPesquisarComponent } from './sindicatos-pesquisar/sindicatos-pesquisar.component';
import { TextMaskModule } from 'angular2-text-mask/dist/angular2TextMask';
import { DialogService } from 'ng2-bootstrap-modal';
import { SindicatosCadastrarComponent } from './sindicatos-cadastrar/sindicatos-cadastrar.component';
import { SindicatoService } from '../../servico/sindicato.service';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

const routes: Routes = [
  {
    path: '', component: SindicatosPesquisarComponent,
    canActivate: [AutorizacaoGuard],
    data: { title: 'Pesquisar Sindicatos',
    permissoes: [PermissoesEnum.SINDICATO,
      PermissoesEnum.SINDICATO_CADASTRAR,
      PermissoesEnum.SINDICATO_ALTERAR,
      PermissoesEnum.SINDICATO_CONSULTAR,
      PermissoesEnum.SINDICATO_DESATIVAR] },
  },
  {
    path: 'cadastrar', component: SindicatosCadastrarComponent,
    canActivate: [AutorizacaoGuard],
    data: { title: 'Cadastrar Sindicato',
    permissoes: [ PermissoesEnum.SINDICATO,
      PermissoesEnum.SINDICATO_CADASTRAR] },
  },
  {
    path: ':id', component: SindicatosCadastrarComponent,
    canActivate: [AutorizacaoGuard],
    data: { title: 'Alterar Sindicato',
    permissoes: [ PermissoesEnum.SINDICATO,
      PermissoesEnum.SINDICATO_ALTERAR,
      PermissoesEnum.SINDICATO_CONSULTAR,
      PermissoesEnum.SINDICATO_DESATIVAR] },
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    CompartilhadoModule,
    RouterModule,
    TextMaskModule,
    PaginationModule.forRoot(),
    TypeaheadModule.forRoot(),
  ],
  entryComponents: [TypeaheadContainerComponent],
  exports: [SindicatosPesquisarComponent, SindicatosCadastrarComponent],
  declarations: [SindicatosPesquisarComponent, SindicatosCadastrarComponent],
  providers: [DialogService, SindicatoService, DepartRegionalService],
})
export class SindicatosModule { }
