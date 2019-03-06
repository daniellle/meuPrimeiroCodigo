import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { LinhaService } from './../../servico/linha.service';
import { UatProdutoServicoService } from './../../servico/uat-produto-servico.service';
import { ProdutoServicoService } from './../../servico/produto-servico.service';
import { UatProdutoServicoComponent } from './uat-produto-servico/uat-produto-servico.component';
import { MensagemProperties } from './../../compartilhado/utilitario/recurso.pipe';
import { UatIntermediariaComponent } from './uat-intermediaria/uat-intermediaria.component';
import { DepartRegionalService } from 'app/servico/depart-regional.service';
import { DialogService } from 'ng2-bootstrap-modal';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { TypeaheadContainerComponent } from 'ngx-bootstrap/typeahead';
import { PaginationModule, AlertModule } from 'ngx-bootstrap';
import { PesquisaUatComponent } from 'app/componente/unid-atend-trabalhador/pesquisa-unid-atend-trabalhador/pesquisa-uat.component';
import { CadastroUatComponent } from 'app/componente/unid-atend-trabalhador/cadastro-unid-atend-trabalhador/cadastro-uat.component';
import { TextMaskModule } from 'angular2-text-mask/dist/angular2TextMask';
import { UatService } from 'app/servico/uat.service';
import { EstadoService } from 'app/servico/estado.service';
import { AutorizacaoGuard } from '../../seguranca/autorizacao.guard';
import { UatEstruturaUnidadeComponent } from './uat-estrutura-unidade/uat-estrutura-unidade.component';
import { UatInstalacoesFisicasComponent } from './uat-instalacoes-fisicas/uat-instalacoes-fisicas.component';
import { UatInstalacaoFisicaCategoriaService } from 'app/servico/uat-instalacao-fisica-categoria.service';
import { UatInstalacaoFisicaCategoriaAmbienteService } from 'app/servico/uat-instalacao-fisica-ambiente.service';
import { UatInstalacaoFisicaService } from 'app/servico/uat-instalacao-fisica.service';
import { UatVeiculoComponent } from './uat-veiculo/uat-veiculo.component';
import { UatVeiculoService } from 'app/servico/uat-veiculo.service';
import { UatQuadroPessoalAreaService } from 'app/servico/uat-quadro-pessoal-area.service';
import { UatQuadroPessoaTipoServicoService } from 'app/servico/uat-quadro-pessoal-tipo-servico.service';
import { UatQuadroPessoalTipoProfissionalService } from 'app/servico/uat-quadro-pessoal-tipo-profissional.service';
import { UatQuadroPessoalService } from 'app/servico/uat-quadro-pessoal.service';

const routes: Routes = [
    {
        path: '', component: PesquisaUatComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Pesquisar Unidade Atendimento ao Trabalhador',
            permissoes: [PermissoesEnum.CAT,
            PermissoesEnum.CAT_CADASTRAR,
            PermissoesEnum.CAT_ALTERAR,
            PermissoesEnum.CAT_CONSULTAR,
            PermissoesEnum.CAT_DESATIVAR,
            PermissoesEnum.CAT_PRODUTO_SERVICO,
            PermissoesEnum.CAT_PRODUTO_SERVICO_ALTERAR,
            PermissoesEnum.CAT_PRODUTO_SERVICO_CADASTRAR,
            PermissoesEnum.CAT_PRODUTO_SERVICO_CONSULTAR,
            PermissoesEnum.CAT_PRODUTO_SERVICO_DESATIVAR],
        },
    },
    {
        path: 'cadastrar', component: CadastroUatComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastro Unidade Atendimento ao Trabalhador',
            permissoes: [PermissoesEnum.CAT, PermissoesEnum.CAT_CADASTRAR],
        },
    },
    {
        path: ':id/cadastrar', component: CadastroUatComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastro Unidade Atendimento ao Trabalhador',
            permissoes: [PermissoesEnum.CAT,
            PermissoesEnum.CAT_ALTERAR,
            PermissoesEnum.CAT_CONSULTAR,
            PermissoesEnum.CAT_DESATIVAR],
        },
    },
    {
        path: ':id', component: UatIntermediariaComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_uat_title_alterar,
            permissoes: [PermissoesEnum.CAT,
            PermissoesEnum.CAT_ALTERAR,
            PermissoesEnum.CAT_CONSULTAR,
            PermissoesEnum.CAT_DESATIVAR,
            PermissoesEnum.CAT_PRODUTO_SERVICO,
            PermissoesEnum.CAT_PRODUTO_SERVICO_ALTERAR,
            PermissoesEnum.CAT_PRODUTO_SERVICO_CADASTRAR,
            PermissoesEnum.CAT_PRODUTO_SERVICO_CONSULTAR,
            PermissoesEnum.CAT_PRODUTO_SERVICO_DESATIVAR],
        },
    },
    {
        path: ':id/produtoseservicos', component: UatProdutoServicoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_uat_title_alterar,
            permissoes: [PermissoesEnum.CAT_PRODUTO_SERVICO,
            PermissoesEnum.CAT_PRODUTO_SERVICO_ALTERAR,
            PermissoesEnum.CAT_PRODUTO_SERVICO_CADASTRAR,
            PermissoesEnum.CAT_PRODUTO_SERVICO_CONSULTAR,
            PermissoesEnum.CAT_PRODUTO_SERVICO_DESATIVAR],
        },
    },
    {
        path: ':id/estruturadaunidade', component: UatEstruturaUnidadeComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_estrutura_da_unidade_title_menu,
            permissoes: [
                PermissoesEnum.CAT_ESTRUTURA_CADASTRAR,
                PermissoesEnum.CAT_ESTRUTURA_ALTERAR,
                PermissoesEnum.CAT_ESTRUTURA_DESATIVAR,
                PermissoesEnum.CAT_ESTRUTURA_CONSULTAR],
        },
    },
];

@NgModule({
    imports: [
        RouterModule.forChild(routes),
        CompartilhadoModule,
        RouterModule,
        PaginationModule.forRoot(),
        TextMaskModule,
        AlertModule.forRoot(),
    ],
    entryComponents: [TypeaheadContainerComponent],
    exports: [PesquisaUatComponent, CadastroUatComponent, UatIntermediariaComponent, UatProdutoServicoComponent],
    declarations: [PesquisaUatComponent, CadastroUatComponent, UatIntermediariaComponent, UatProdutoServicoComponent, UatEstruturaUnidadeComponent, UatInstalacoesFisicasComponent, UatVeiculoComponent],
    providers: [UatService, DialogService, CadastroUatComponent, EstadoService, DepartRegionalService,
        ProdutoServicoService, UatProdutoServicoService, LinhaService, UatInstalacaoFisicaCategoriaService, UatInstalacaoFisicaCategoriaAmbienteService, UatInstalacaoFisicaService, UatVeiculoService, UatQuadroPessoalAreaService, UatQuadroPessoaTipoServicoService, UatQuadroPessoalTipoProfissionalService, UatQuadroPessoalService],
})
export class PesquisaUatModule { }
