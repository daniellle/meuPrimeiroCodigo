import { LinhaService } from './../../servico/linha.service';
import { ProdutoServicoService } from './../../servico/produto-servico.service';
import { DepartamentoRegionalProdutoServicoService } from './../../servico/departamento-regional-produto-servico.service';
// tslint:disable-next-line:max-line-length
import { DepartamentoRegionalProdutoServicoComponent } from './departamento-regional-produto-servico/departamento-regional-produto-servico.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PesquisaDepartRegionalComponent } from './pesquisa-depart-regional/pesquisa-depart-regional.component';
import { CadastroDepartRegionalComponent } from './cadastro-depart-regional/cadastro-depart-regional.component';
import { DepartRegionalService } from 'app/servico/depart-regional.service';
import { TextMaskModule } from 'angular2-text-mask/dist/angular2TextMask';
import { RouterModule, Routes } from '@angular/router';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { PaginationModule, TypeaheadModule, AlertModule } from 'ngx-bootstrap';
import { DialogService } from 'ng2-bootstrap-modal';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { DepartRegionalIntermediariaComponent } from './depart-regional-intermediaria/depart-regional-intermediaria.component';
import { AutorizacaoGuard } from '../../seguranca/autorizacao.guard';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

const routes: Routes = [
  {
    path: '', component: PesquisaDepartRegionalComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_depart_regional_pesquisar,
      permissoes: [PermissoesEnum.DEPARTAMENTO_REGIONAL,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_CADASTRAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_ALTERAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_CONSULTAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_DESATIVAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CADASTRAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_ALTERAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CONSULTAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_DESATIVAR],
    },
  },
  {
    path: 'cadastrar', component: CadastroDepartRegionalComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_depart_regional_cadastro,
      permissoes: [PermissoesEnum.DEPARTAMENTO_REGIONAL,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_CADASTRAR],
    },
  },
  {
    path: ':id/cadastrar', component: CadastroDepartRegionalComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_depart_regional_cadastro,
      permissoes: [PermissoesEnum.DEPARTAMENTO_REGIONAL,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_ALTERAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_CONSULTAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_DESATIVAR]
    },
  },
  {
    path: ':id', component: DepartRegionalIntermediariaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_depart_regional_alterar,
      permissoes: [PermissoesEnum.DEPARTAMENTO_REGIONAL,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_ALTERAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_CONSULTAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_DESATIVAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CADASTRAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_ALTERAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CONSULTAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_DESATIVAR],
    },
  },
  {
    path: ':id/produtoseservicos', component: DepartamentoRegionalProdutoServicoComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_depart_regional_alterar,
      permissoes: [PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CADASTRAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_ALTERAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CONSULTAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_DESATIVAR],
    },
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(routes),
    CompartilhadoModule,
    RouterModule,
    PaginationModule.forRoot(),
    TypeaheadModule.forRoot(),
    TextMaskModule,
    CommonModule,
    AlertModule.forRoot(),
  ],
  declarations: [PesquisaDepartRegionalComponent, CadastroDepartRegionalComponent, DepartRegionalIntermediariaComponent,
    DepartamentoRegionalProdutoServicoComponent],
  exports: [PesquisaDepartRegionalComponent, CadastroDepartRegionalComponent],
  providers: [DepartRegionalService, DialogService, DepartamentoRegionalProdutoServicoService, ProdutoServicoService, LinhaService],
})
export class DepartRegionalModule { }
