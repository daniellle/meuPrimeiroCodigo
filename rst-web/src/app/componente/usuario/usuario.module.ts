import {PermissoesEnum} from 'app/modelo/enum/enum-permissoes';
import {ModalDepartamentoComponentModule} from './../../modal/modal-departamento-component/modal-departamento-component.module';
import {ModalEmpresaComponentModule} from './../../modal/modal-empresa-component/modal-empresa-component.module';
import {EmpresaTrabalhadorService} from './../../servico/empresa-trabalhador.service';
import {AutorizacaoGuard} from './../../seguranca/autorizacao.guard';
import {UsuarioIntermediarioComponent} from './usuario-intermediario/usuario-intermediario.component';
// tslint:disable-next-line:max-line-length
import {CadastroDepartamentoUsuarioComponent} from './departamento-regional-usuario/cadastro-departamento-regional-usuario/cadastro-departamento-regional-usuario.component';
import {PesquisaDepartamentoUsuarioComponent} from './departamento-regional-usuario/pesquisa-departamento-regional-usuario/pesquisa-departamento-regional-usuario.component';
import {CadastroSindicatoUsuarioComponent} from './sindicato-usuario/cadastro-sindicato-usuario/cadastro-sindicato-usuario.component';
import {DepartRegionalService} from './../../servico/depart-regional.service';
import {SindicatoService} from './../../servico/sindicato.service';
import {PesquisaSindicatoUsuarioComponent} from './sindicato-usuario/pesquisa-sindicato-usuario/pesquisa-sindicato-usuario.component';
import {EmpresaService} from './../../servico/empresa.service';
import {CadastroEmpresaUsuarioComponent} from './empresa-usuario/cadastro-empresa-usuario/cadastro-empresa-usuario.component';
import {UsuarioEntidadeService} from './../../servico/usuario-entidade.service';
import {PesquisaEmpresaUsuarioComponent} from './empresa-usuario/pesquisa-empresa-usuario/pesquisa-empresa-usuario.component';
import {PerfilService} from './../../servico/perfil.service';
import {SistemaService} from './../../servico/sistema.service';
import {PaginationModule} from 'ngx-bootstrap';
import {UsuarioService} from './../../servico/usuario.service';
import {ToastyService} from 'ng2-toasty';
import {DialogService} from 'ng2-bootstrap-modal';
import {BloqueioService} from './../../servico/bloqueio.service';
import {CompartilhadoModule} from './../../compartilhado/compartilhado.module';
import {MensagemProperties} from './../../compartilhado/utilitario/recurso.pipe';
import {Routes, RouterModule} from '@angular/router';
import {NgModule} from '@angular/core';
import {PesquisaUsuarioComponent} from './pesquisa-usuario/pesquisa-usuario.component';
import {ManterUsuarioComponent} from './manter-usuario/manter-usuario.component';
import { ParametroService } from '../../servico/parametro.service';

const routes: Routes = [
    {
        path: '', component: PesquisaUsuarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_usuario_title_pesquisar,
            permissoes: [PermissoesEnum.USUARIO, PermissoesEnum.USUARIO_CADASTRAR, PermissoesEnum.USUARIO_ALTERAR,
                PermissoesEnum.USUARIO_CONSULTAR, PermissoesEnum.USUARIO_DESATIVAR,
                PermissoesEnum.USUARIO_ENTIDADE, PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR, PermissoesEnum.USUARIO_ENTIDADE_ALTERAR,
                PermissoesEnum.USUARIO_ENTIDADE_CONSULTAR, PermissoesEnum.USUARIO_ENTIDADE_DESATIVAR],
        },
    },
    {
        path: 'cadastrar', component: ManterUsuarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_usuario_title_cadastrar,
            permissoes: [PermissoesEnum.USUARIO, PermissoesEnum.USUARIO_CADASTRAR],
        },
    },
    {
        path: ':id/cadastrar', component: ManterUsuarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_usuario_title_cadastrar,
            permissoes: [PermissoesEnum.USUARIO, PermissoesEnum.USUARIO_ALTERAR,
                PermissoesEnum.USUARIO_CONSULTAR, PermissoesEnum.USUARIO_DESATIVAR],
        },
    },
    {
        path: ':id', component: UsuarioIntermediarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            permissoes: [PermissoesEnum.USUARIO, PermissoesEnum.USUARIO_ALTERAR,
                PermissoesEnum.USUARIO_CONSULTAR, PermissoesEnum.USUARIO_DESATIVAR, PermissoesEnum.USUARIO_ENTIDADE,
                PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR, PermissoesEnum.USUARIO_ENTIDADE_ALTERAR,
                PermissoesEnum.USUARIO_ENTIDADE_CONSULTAR, PermissoesEnum.USUARIO_ENTIDADE_DESATIVAR],
        },
    },
    {
        path: ':id/empresa', component: PesquisaEmpresaUsuarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_usuario_empresa_empresas_associadas_title,
            permissoes: [PermissoesEnum.USUARIO_ENTIDADE,
                PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR, PermissoesEnum.USUARIO_ENTIDADE_ALTERAR,
                PermissoesEnum.USUARIO_ENTIDADE_CONSULTAR, PermissoesEnum.USUARIO_ENTIDADE_DESATIVAR],
        },
    },
    {
        path: ':id/empresa/associar', component: CadastroEmpresaUsuarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_usuario_empresa_subtitulo_associar_empresa,
            permissoes: [PermissoesEnum.USUARIO_ENTIDADE,
                PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR, PermissoesEnum.USUARIO_ENTIDADE_ALTERAR,
                PermissoesEnum.USUARIO_ENTIDADE_CONSULTAR, PermissoesEnum.USUARIO_ENTIDADE_DESATIVAR],
        },
    },
    {
        path: ':id/sindicato', component: PesquisaSindicatoUsuarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_empresa_subtitulo_sindicatos_associados,
            permissoes: [PermissoesEnum.USUARIO_ENTIDADE,
                PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR, PermissoesEnum.USUARIO_ENTIDADE_ALTERAR,
                PermissoesEnum.USUARIO_ENTIDADE_CONSULTAR, PermissoesEnum.USUARIO_ENTIDADE_DESATIVAR],
        },
    },
    {
        path: ':id/sindicato/associar', component: CadastroSindicatoUsuarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_usuario_sindicato_subtitulo_associar_sindicato,
            permissoes: [PermissoesEnum.USUARIO_ENTIDADE,
                PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR, PermissoesEnum.USUARIO_ENTIDADE_ALTERAR,
                PermissoesEnum.USUARIO_ENTIDADE_CONSULTAR, PermissoesEnum.USUARIO_ENTIDADE_DESATIVAR],
        },
    },
    {
        path: ':id/departamentoregional', component: PesquisaDepartamentoUsuarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_usuario_departamento_regional_associados_title,
            permissoes: [PermissoesEnum.USUARIO_ENTIDADE,
                PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR, PermissoesEnum.USUARIO_ENTIDADE_ALTERAR,
                PermissoesEnum.USUARIO_ENTIDADE_CONSULTAR, PermissoesEnum.USUARIO_ENTIDADE_DESATIVAR],
        },
    },
    {
        path: ':id/departamentoregional/associar', component: CadastroDepartamentoUsuarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_usuario_departamento_regional_subtitulo_associar_departamento,
            permissoes: [PermissoesEnum.USUARIO_ENTIDADE,
                PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR, PermissoesEnum.USUARIO_ENTIDADE_ALTERAR,
                PermissoesEnum.USUARIO_ENTIDADE_CONSULTAR, PermissoesEnum.USUARIO_ENTIDADE_DESATIVAR],
        },
    },
];

@NgModule({
    imports: [
        RouterModule.forChild(routes),
        CompartilhadoModule,
        RouterModule,
        PaginationModule.forRoot(),
        ModalEmpresaComponentModule,
        ModalDepartamentoComponentModule,
    ],
    declarations: [
        PesquisaUsuarioComponent,
        ManterUsuarioComponent,
        UsuarioIntermediarioComponent,
        PesquisaEmpresaUsuarioComponent,
        CadastroEmpresaUsuarioComponent,
        PesquisaSindicatoUsuarioComponent,
        CadastroSindicatoUsuarioComponent,
        PesquisaDepartamentoUsuarioComponent,
        CadastroDepartamentoUsuarioComponent,
    ],
    providers: [
        UsuarioService,
        BloqueioService,
        DialogService,
        ToastyService,
        SistemaService,
        PerfilService,
        UsuarioEntidadeService,
        EmpresaService,
        SindicatoService,
        DepartRegionalService,
        EmpresaTrabalhadorService,
        ParametroService
    ],
})
export class UsuarioModule {
}
