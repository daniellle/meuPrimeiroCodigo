import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { PaginationModule } from 'ngx-bootstrap';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { PerfilService } from 'app/servico/perfil.service';
import { SistemaCredenciadoService } from 'app/servico/sistema-credenciado.service';
import { ManterSistemaCredenciadoComponent } from './manter-sistema-credenciado/manter-sistema-credenciado.component';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { AutorizacaoGuard } from 'app/seguranca/autorizacao.guard';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { PesquisaSistemaCredenciadoComponent } from './pesquisa-sistema-credenciado/pesquisa-sistema-credenciado.component';
import { OrigemDadosService } from 'app/servico/origem-dados.service';

const routes: Routes = [

    {
        path: '', component: PesquisaSistemaCredenciadoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_sistema_credenciado_pesquisar,
            permissoes: [PermissoesEnum.SISTEMA_CREDENCIADO_PESQUISAR],
        },
    },

    {
        path: 'cadastrar', component: ManterSistemaCredenciadoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_sistema_credenciado_cadastrar,
            permissoes: [PermissoesEnum.SISTEMA_CREDENCIADO_CADASTRAR],
        },
    },

    {
        path: ':id', component: ManterSistemaCredenciadoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_sistema_credenciado_alterar,
            permissoes: [PermissoesEnum.SISTEMA_CREDENCIADO_ALTERAR],
        },
    },
];

@NgModule({
    imports: [
        CommonModule,
        RouterModule.forChild(routes),
        CompartilhadoModule,
        RouterModule,
        PaginationModule.forRoot(),
    ],
    declarations: [ManterSistemaCredenciadoComponent, PesquisaSistemaCredenciadoComponent],
    providers: [
        BloqueioService,
        PerfilService,
        SistemaCredenciadoService,
        ToastyService,
        DialogService,
        OrigemDadosService,
    ],
})
export class SistemaCredenciadoModule { }
