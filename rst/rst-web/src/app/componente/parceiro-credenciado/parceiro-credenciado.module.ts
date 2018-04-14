// tslint:disable-next-line:max-line-length
import { ParceiroCredenciadoProdutoServicoComponent } from './parceiro-credenciado-produto-servico/parceiro-credenciado-produto-servico.component';
import { ParceiroCredenciadoIntermediarioComponent } from './parceiro-credenciado-intermediario/parceiro-credenciado-intermediario.component';
import { ParceiroProdutoServicoService } from './../../servico/parceiro-produto-servico.service';
import { LinhaService } from './../../servico/linha.service';
import { ProdutoServicoService } from './../../servico/produto-servico.service';
import { ParceiroService } from './../../servico/parceiro.service';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PesquisarParceiroCredenciadoComponent } from './pesquisar-parceiro-credenciado/pesquisar-parceiro-credenciado.component';
import { TextMaskModule } from 'angular2-text-mask';
import { PaginationModule, TypeaheadModule } from 'ngx-bootstrap';
import { DialogService } from 'ng2-bootstrap-modal';
import { CadastroParceiroCredenciadoComponent } from './cadastro-parceiro-credenciado/cadastro-parceiro-credenciado.component';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { EspecialidadeService } from 'app/servico/especialidade.service';
import { PorteEmpresaService } from 'app/servico/porte-empresa.service';
import { TipoEmpresaService } from 'app/servico/tipo-empresa.service';
import { AutorizacaoGuard } from '../../seguranca/autorizacao.guard';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

const routes: Routes = [
    {
        path: '', component: PesquisarParceiroCredenciadoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Pesquisa Parceiro Credenciado',
            permissoes: [PermissoesEnum.PARCEIRO_CREDENCIADA,
            PermissoesEnum.PARCEIRO_CREDENCIADA_CADASTRAR,
            PermissoesEnum.PARCEIRO_CREDENCIADA_ALTERAR,
            PermissoesEnum.PARCEIRO_CREDENCIADA_CONSULTAR,
            PermissoesEnum.PARCEIRO_CREDENCIADA_DESATIVAR,
            PermissoesEnum.PARCEIRO_PRODUTO_SERVICO,
            PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_CADASTRAR,
            PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_ALTERAR,
            PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_CONSULTAR,
            PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_DESATIVAR],
        },
    },
    {
        path: 'cadastrar', component: CadastroParceiroCredenciadoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_parceiro_credenciado_title_cadastro,
            permissoes: [PermissoesEnum.PARCEIRO_CREDENCIADA,
                PermissoesEnum.PARCEIRO_CREDENCIADA_CADASTRAR],
        },
    },
    {
        path: ':id', component: ParceiroCredenciadoIntermediarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_parceiro_credenciado_title_cadastro,
            permissoes: [PermissoesEnum.PARCEIRO_CREDENCIADA,
                PermissoesEnum.PARCEIRO_CREDENCIADA_ALTERAR,
                PermissoesEnum.PARCEIRO_CREDENCIADA_CONSULTAR,
                PermissoesEnum.PARCEIRO_CREDENCIADA_DESATIVAR,
                PermissoesEnum.PARCEIRO_PRODUTO_SERVICO,
                PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_CADASTRAR,
                PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_ALTERAR,
                PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_CONSULTAR,
                PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_DESATIVAR],
        },
    },
    {
        path: ':id/cadastrar', component: CadastroParceiroCredenciadoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_parceiro_credenciado_title_cadastro,
            permissoes: [PermissoesEnum.PARCEIRO_CREDENCIADA,
                PermissoesEnum.PARCEIRO_CREDENCIADA_ALTERAR,
                PermissoesEnum.PARCEIRO_CREDENCIADA_CONSULTAR,
                PermissoesEnum.PARCEIRO_CREDENCIADA_DESATIVAR],
        },
    },
    {
        path: ':id/produtoseservicos', component: ParceiroCredenciadoProdutoServicoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_parceiro_credenciado_title_cadastro,
            permissoes: [PermissoesEnum.PARCEIRO_PRODUTO_SERVICO,
                PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_CADASTRAR,
                PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_ALTERAR,
                PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_CONSULTAR,
                PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_DESATIVAR],
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
        TypeaheadModule.forRoot(),
    ],
    declarations: [PesquisarParceiroCredenciadoComponent, CadastroParceiroCredenciadoComponent,
        ParceiroCredenciadoIntermediarioComponent, ParceiroCredenciadoProdutoServicoComponent],
    providers: [DialogService, ParceiroService, ProdutoServicoService, LinhaService,
        ParceiroProdutoServicoService, EspecialidadeService, PorteEmpresaService, TipoEmpresaService],
})
export class ParceiroCredenciadoModule { }
