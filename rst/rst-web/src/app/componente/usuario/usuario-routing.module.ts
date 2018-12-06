import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import { PesquisaUsuarioComponent } from './pesquisa-usuario/pesquisa-usuario.component';
import { AutorizacaoGuard } from '../../seguranca/autorizacao.guard';
import { MensagemProperties } from '../../compartilhado/utilitario/recurso.pipe';
import { PermissoesEnum } from '../../modelo/enum/enum-permissoes';
import { ManterUsuarioComponent } from './manter-usuario/manter-usuario.component';
import { UsuarioIntermediarioComponent } from './usuario-intermediario/usuario-intermediario.component';
import { PesquisaEmpresaUsuarioComponent } from './empresa-usuario/pesquisa-empresa-usuario/pesquisa-empresa-usuario.component';
import { CadastroEmpresaUsuarioComponent } from './empresa-usuario/cadastro-empresa-usuario/cadastro-empresa-usuario.component';
import { PesquisaSindicatoUsuarioComponent } from './sindicato-usuario/pesquisa-sindicato-usuario/pesquisa-sindicato-usuario.component';
import { CadastroSindicatoUsuarioComponent } from './sindicato-usuario/cadastro-sindicato-usuario/cadastro-sindicato-usuario.component';
import { PesquisaDepartamentoUsuarioComponent } from './departamento-regional-usuario/pesquisa-departamento-regional-usuario/pesquisa-departamento-regional-usuario.component';
import { CadastroDepartamentoUsuarioComponent } from './departamento-regional-usuario/cadastro-departamento-regional-usuario/cadastro-departamento-regional-usuario.component';
import { PesquisaUnidadeSESIUsuarioComponent } from './unidade-sesi-usuario/pesquisa-unidade-sesi-usuario/pesquisa-unidade-sesi-usuario.component';
import { CadastroUnidadeSESIUsuarioComponent } from './unidade-sesi-usuario/cadastro-unidade-sesi-usuario/cadastro-unidade-sesi-usuario.component';

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
{
    path: ':id/unidadesesi', component: PesquisaUnidadeSESIUsuarioComponent,
    canActivate: [AutorizacaoGuard],
    data: {
        title: MensagemProperties.app_rst_usuario_unidade_sesi_associados_title,
        permissoes: [PermissoesEnum.USUARIO_ENTIDADE,
            PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR, PermissoesEnum.USUARIO_ENTIDADE_ALTERAR,
            PermissoesEnum.USUARIO_ENTIDADE_CONSULTAR, PermissoesEnum.USUARIO_ENTIDADE_DESATIVAR],
    },
},
{
    path: ':id/unidadesesi/associar', component: CadastroUnidadeSESIUsuarioComponent,
    canActivate: [AutorizacaoGuard],
    data: {
        title: MensagemProperties.app_rst_usuario_unidade_sesi_subtitulo_associar_unidade,
        permissoes: [PermissoesEnum.USUARIO_ENTIDADE,
            PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR, PermissoesEnum.USUARIO_ENTIDADE_ALTERAR,
            PermissoesEnum.USUARIO_ENTIDADE_CONSULTAR, PermissoesEnum.USUARIO_ENTIDADE_DESATIVAR],
    },
},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsuarioRoutingModule {}

