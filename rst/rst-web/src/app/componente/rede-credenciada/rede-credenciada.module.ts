import { AutorizacaoGuard } from './../../seguranca/autorizacao.guard';
import { LinhaService } from './../../servico/linha.service';
import { ProdutoServicoService } from './../../servico/produto-servico.service';
import { RedeCredenciadaProdutoServicoService } from './../../servico/rede-credenciada-produto-servico.service';
import { SegmentoService } from './../../servico/segmento.service';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CadastroRedeCredenciadaComponent } from './cadastro-rede-credenciada/cadastro-rede-credenciada.component';
import { RedeCredenciadaService } from 'app/servico/rede-credenciada.service';
import { Routes, RouterModule } from '@angular/router';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { TextMaskModule } from 'angular2-text-mask/dist/angular2TextMask';
import { PaginationModule } from 'ngx-bootstrap';
import { DialogService } from 'ng2-bootstrap-modal';
import { PorteEmpresaService } from 'app/servico/porte-empresa.service';
import { TipoEmpresaService } from 'app/servico/tipo-empresa.service';
import { RedeCredenciadaPesquisarComponent } from './pesquisa-rede-credenciada/pesquisa-rede-credenciada.component';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { RedeCredenciadaIntermediariaComponent } from './rede-credenciada-intermediaria/rede-credenciada-intermediaria.component';
import { RedeCredenciadaProdutoServicoComponent } from './rede-credenciada-produto-servico/rede-credenciada-produto-servico.component';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

const routes: Routes = [
  {
    path: '', component: RedeCredenciadaPesquisarComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_rede_credenciada_pesquisar,
      permissoes: [PermissoesEnum.REDE_CREDENCIADA,
      PermissoesEnum.REDE_CREDENCIADA_CADASTRAR,
      PermissoesEnum.REDE_CREDENCIADA_ALTERAR,
      PermissoesEnum.REDE_CREDENCIADA_CONSULTAR,
      PermissoesEnum.REDE_CREDENCIADA_DESATIVAR,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_CADASTRAR,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_ALTERAR,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_CONSULTAR,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_DESATIVAR],
    },
  },
  {
    path: 'cadastrar', component: CadastroRedeCredenciadaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_rede_credenciada_cadastro,
      permissoes: [PermissoesEnum.REDE_CREDENCIADA,
      PermissoesEnum.REDE_CREDENCIADA_CADASTRAR],
    },
  },
  {
    path: ':id', component: RedeCredenciadaIntermediariaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_rede_credenciada_cadastro,
      permissoes: [PermissoesEnum.REDE_CREDENCIADA,
      PermissoesEnum.REDE_CREDENCIADA_ALTERAR,
      PermissoesEnum.REDE_CREDENCIADA_CONSULTAR,
      PermissoesEnum.REDE_CREDENCIADA_DESATIVAR,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_CADASTRAR,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_ALTERAR,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_CONSULTAR,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_DESATIVAR],
    },
  },
  {
    path: ':id/cadastrar', component: CadastroRedeCredenciadaComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_rede_credenciada_cadastro,
      permissoes: [PermissoesEnum.REDE_CREDENCIADA,
      PermissoesEnum.REDE_CREDENCIADA_ALTERAR,
      PermissoesEnum.REDE_CREDENCIADA_CONSULTAR,
      PermissoesEnum.REDE_CREDENCIADA_DESATIVAR],
    },
  },
  {
    path: ':id/produtoseservicos', component: RedeCredenciadaProdutoServicoComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_rede_credenciada_cadastro,
      permissoes: [PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO,
        PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_CADASTRAR,
        PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_ALTERAR,
        PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_CONSULTAR,
        PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_DESATIVAR],
    },
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
  ],
  declarations: [CadastroRedeCredenciadaComponent, RedeCredenciadaPesquisarComponent,
    RedeCredenciadaIntermediariaComponent, RedeCredenciadaProdutoServicoComponent],
  providers: [RedeCredenciadaService, RedeCredenciadaProdutoServicoService,
    ProdutoServicoService, LinhaService, DialogService, PorteEmpresaService,
    TipoEmpresaService, SegmentoService],
})
export class RedeCredenciadaModule { }
