import { AutorizacaoGuard } from './../../seguranca/autorizacao.guard';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LinhaService } from './../../servico/linha.service';
import { ProdutoServicoService } from './../../servico/produto-servico.service';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { PaginationModule } from 'ngx-bootstrap';
import { CadastroProdutoServicoComponent } from './cadastro-produo-servico/cadastro-produto-servico.component';
import { PesquisaProdutoServicoComponent } from './pesquisa-produto-servico/pesquisa-produto-servico.component';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

const routes: Routes = [
  {
    path: '', component: PesquisaProdutoServicoComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_produto_servico_title_pesquisar,
      permissoes: [PermissoesEnum.PRODUTO_SERVICO,
      PermissoesEnum.PRODUTO_SERVICO_CADASTRAR,
      PermissoesEnum.PRODUTO_SERVICO_CONSULTAR,
      PermissoesEnum.PRODUTO_SERVICO_ALTERAR,
      PermissoesEnum.PRODUTO_SERVICO_DESATIVAR],
    },
  },
  {
    path: 'cadastrar', component: CadastroProdutoServicoComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_produto_servico_title_cadastro,
      permissoes: [PermissoesEnum.PRODUTO_SERVICO,
      PermissoesEnum.PRODUTO_SERVICO_CADASTRAR],
    },
  },
  {
    path: ':id', component: CadastroProdutoServicoComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_produto_servico_title_cadastro,
      permissoes: [PermissoesEnum.PRODUTO_SERVICO,
      PermissoesEnum.PRODUTO_SERVICO_CONSULTAR,
      PermissoesEnum.PRODUTO_SERVICO_ALTERAR,
      PermissoesEnum.PRODUTO_SERVICO_DESATIVAR],
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
  declarations: [PesquisaProdutoServicoComponent, CadastroProdutoServicoComponent],
  providers: [ProdutoServicoService, LinhaService, NgbModal],
})
export class ProdutoServicoModule { }
