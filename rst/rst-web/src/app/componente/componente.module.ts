//import { TesteComponent } from './teste/teste.component';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SegurancaModule } from '../seguranca/seguranca.module';
import { AutorizacaoGuard } from '../seguranca/autorizacao.guard';
import { TemplateModule } from '../compartilhado/template/template.module';
import { TemplateComponent } from '../compartilhado/template/template.component';
import { CalendarModule } from 'angular-calendar';


const routes: Routes = [
	{
		path: '', component: TemplateComponent, data: { title: 'Home' },
		children: [
			{
				path: 'uat',
				loadChildren: './unid-atend-trabalhador/pesquisa-uat.module#PesquisaUatModule',
				canLoad: [AutorizacaoGuard],
				data: {
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
				path: 'empresa',
				loadChildren: './empresa/empresa.module#EmpresaModule',
				canLoad: [AutorizacaoGuard],
				data: {
					permissoes: [PermissoesEnum.EMPRESA,
					PermissoesEnum.EMPRESA_CADASTRAR,
					PermissoesEnum.EMPRESA_ALTERAR,
					PermissoesEnum.EMPRESA_CONSULTAR,
					PermissoesEnum.EMPRESA_DESATIVAR,
					PermissoesEnum.EMPRESA_SINDICATO,
					PermissoesEnum.EMPRESA_SINDICATO_CADASTRAR,
					PermissoesEnum.EMPRESA_SINDICATO_ALTERAR,
					PermissoesEnum.EMPRESA_SINDICATO_CONSULTAR,
					PermissoesEnum.EMPRESA_SINDICATO_DESATIVAR,
					PermissoesEnum.EMPRESA_JORNADA,
					PermissoesEnum.EMPRESA_JORNADA_CADASTRAR,
					PermissoesEnum.EMPRESA_JORNADA_ALTERAR,
					PermissoesEnum.EMPRESA_JORNADA_CONSULTAR,
					PermissoesEnum.EMPRESA_JORNADA_DESATIVAR,
					PermissoesEnum.EMPRESA_CARGO,
					PermissoesEnum.EMPRESA_CARGO_CADASTRAR,
					PermissoesEnum.EMPRESA_CARGO_ALTERAR,
					PermissoesEnum.EMPRESA_CARGO_CONSULTAR,
					PermissoesEnum.EMPRESA_CARGO_DESATIVAR,
					PermissoesEnum.EMPRESA_FUNCAO,
					PermissoesEnum.EMPRESA_FUNCAO_CADASTRAR,
					PermissoesEnum.EMPRESA_FUNCAO_ALTERAR,
					PermissoesEnum.EMPRESA_FUNCAO_CONSULTAR,
					PermissoesEnum.EMPRESA_FUNCAO_DESATIVAR,
					PermissoesEnum.EMPRESA_SETOR,
					PermissoesEnum.EMPRESA_SETOR_CADASTRAR,
					PermissoesEnum.EMPRESA_SETOR_ALTERAR,
					PermissoesEnum.EMPRESA_SETOR_CONSULTAR,
					PermissoesEnum.EMPRESA_SETOR_DESATIVAR,
					PermissoesEnum.EMPRESA_LOTACAO,
					PermissoesEnum.EMPRESA_LOTACAO_CADASTRAR,
					PermissoesEnum.EMPRESA_LOTACAO_ALTERAR,
					PermissoesEnum.EMPRESA_LOTACAO_CONSULTAR,
					PermissoesEnum.EMPRESA_LOTACAO_DESATIVAR,
					PermissoesEnum.EMPRESA_TRABALHADOR,
					PermissoesEnum.EMPRESA_TRABALHADOR_CADASTRAR,
					PermissoesEnum.EMPRESA_TRABALHADOR_ALTERAR,
					PermissoesEnum.EMPRESA_TRABALHADOR_CONSULTAR,
					PermissoesEnum.EMPRESA_TRABALHADOR_DESATIVAR,
					PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO,
					PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CADASTRAR,
					PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_ALTERAR,
					PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CONSULTAR,
					PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_DESATIVAR],
				},
			},
			{
				path: 'trabalhador',
				loadChildren: './trabalhador/trabalhador.module#TrabalhadorModule',
				canLoad: [AutorizacaoGuard], data: {
					permissoes: [PermissoesEnum.TRABALHADOR,
					PermissoesEnum.TRABALHADOR_CADASTRAR,
					PermissoesEnum.TRABALHADOR_ALTERAR,
					PermissoesEnum.TRABALHADOR_CONSULTAR,
					PermissoesEnum.TRABALHADOR_DESATIVAR,
					PermissoesEnum.TRABALHADOR_DEPENDENTE,
					PermissoesEnum.TRABALHADOR_DEPENDENTE_CADASTRAR,
					PermissoesEnum.TRABALHADOR_DEPENDENTE_ALTERAR,
					PermissoesEnum.TRABALHADOR_DEPENDENTE_CONSULTAR,
					PermissoesEnum.TRABALHADOR_DEPENDENTE_DESATIVAR,
					PermissoesEnum.TRABALHADOR_CERTIFICADO,
					PermissoesEnum.TRABALHADOR_CERTIFICADO_CADASTRAR,
					PermissoesEnum.TRABALHADOR_CERTIFICADO_ALTERAR,
					PermissoesEnum.TRABALHADOR_CERTIFICADO_CONSULTAR,
					PermissoesEnum.TRABALHADOR_CERTIFICADO_DESATIVAR],
				},
			},
			{
				path: 'profissional',
				loadChildren: './profissionais/profissionais.module#ProfissionaisModule',
				canLoad: [AutorizacaoGuard],
				data: {
					permissoes: [PermissoesEnum.PROFISSIONAL,
					PermissoesEnum.PROFISSIONAL_CADASTRAR,
					PermissoesEnum.PROFISSIONAL_ALTERAR,
					PermissoesEnum.PROFISSIONAL_CONSULTAR,
					PermissoesEnum.PROFISSIONAL_DESATIVAR],
				},
			},
			{
				path: 'departamentoregional',
				loadChildren: './depart-regional/depart-regional.module#DepartRegionalModule',
				canLoad: [AutorizacaoGuard],
				data: {
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
				path: 'sindicato',
				loadChildren: './sindicatos/sindicatos.module#SindicatosModule',
				canLoad: [AutorizacaoGuard], data: {
					permissoes: [PermissoesEnum.SINDICATO,
					PermissoesEnum.SINDICATO_CADASTRAR,
					PermissoesEnum.SINDICATO_ALTERAR,
					PermissoesEnum.SINDICATO_CONSULTAR,
					PermissoesEnum.SINDICATO_DESATIVAR],
				},
			},
			{
				path: 'perfil-usuario',
				loadChildren: './perfil-usuario/perfil-usuario.module#PerfilUsuarioModule',
				canLoad: [AutorizacaoGuard], data: {
					permissoes: [PermissoesEnum.PERFIL_USUARIO],
				},
			},
			{
				path: 'parceirocredenciado',
				loadChildren: './parceiro-credenciado/parceiro-credenciado.module#ParceiroCredenciadoModule',
				canLoad: [AutorizacaoGuard], data: {
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
				path: 'redecredenciada',
				loadChildren: './rede-credenciada/rede-credenciada.module#RedeCredenciadaModule',
				canLoad: [AutorizacaoGuard],
				data: {
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
				path: 'produtoservico',
				loadChildren: './produto-servico/produto-servico.module#ProdutoServicoModule',
				canLoad: [AutorizacaoGuard], data: {
					permissoes: [PermissoesEnum.PRODUTO_SERVICO,
					PermissoesEnum.PRODUTO_SERVICO_CADASTRAR,
					PermissoesEnum.PRODUTO_SERVICO_CONSULTAR,
					PermissoesEnum.PRODUTO_SERVICO_ALTERAR,
					PermissoesEnum.PRODUTO_SERVICO_DESATIVAR],
				},
			},
			{
				path: 'usuario',
				loadChildren: './usuario/usuario.module#UsuarioModule',
				canLoad: [AutorizacaoGuard], data: {
					permissoes: [PermissoesEnum.USUARIO, PermissoesEnum.USUARIO_CADASTRAR, PermissoesEnum.USUARIO_ALTERAR,
					PermissoesEnum.USUARIO_CONSULTAR, PermissoesEnum.USUARIO_DESATIVAR, PermissoesEnum.USUARIO_ENTIDADE, PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR,
					PermissoesEnum.USUARIO_ENTIDADE_ALTERAR, PermissoesEnum.USUARIO_ENTIDADE_CONSULTAR, PermissoesEnum.USUARIO_ENTIDADE_DESATIVAR]
				},
			},
			{
				path: 'autenticacao',
				loadChildren: './autenticacao/autenticacao.module#AutenticacaoModule',
			},
			{
				path: 'auditoria',
				loadChildren: './auditoria/auditoria.module#AuditoriaModule',
				canLoad: [AutorizacaoGuard], data: {
					permissoes: [PermissoesEnum.AUDITORIA,
					PermissoesEnum.AUDITORIA_CONSULTAR],
				},
			},
			{
				path: 'questionario',
				loadChildren: './questionario/questionario.module#QuestionarioModule',
				canLoad: [AutorizacaoGuard], data: {
					permissoes: [PermissoesEnum.QUESTIONARIO,
					PermissoesEnum.QUESTIONARIO_CADASTRAR,
					PermissoesEnum.QUESTIONARIO_ALTERAR,
					PermissoesEnum.QUESTIONARIO_CONSULTAR,
					PermissoesEnum.QUESTIONARIO_DESATIVAR,
					PermissoesEnum.GRUPO_PERGUNTA,
					PermissoesEnum.GRUPO_PERGUNTA_CADASTRAR,
					PermissoesEnum.GRUPO_PERGUNTA_ALTERAR,
					PermissoesEnum.GRUPO_PERGUNTA_CONSULTAR,
					PermissoesEnum.GRUPO_PERGUNTA_DESATIVAR,
					PermissoesEnum.INDICADOR_QUESTIONARIO,
					PermissoesEnum.INDICADOR_QUESTIONARIO_CADASTRAR,
					PermissoesEnum.INDICADOR_QUESTIONARIO_CONSULTAR,
					PermissoesEnum.INDICADOR_QUESTIONARIO_ALTERAR,
					PermissoesEnum.INDICADOR_QUESTIONARIO_DESATIVAR,
					PermissoesEnum.PERGUNTA,
					PermissoesEnum.PERGUNTA_CADASTRAR,
					PermissoesEnum.PERGUNTA_ALTERAR,
					PermissoesEnum.PERGUNTA_CONSULTAR,
					PermissoesEnum.PERGUNTA_DESATIVAR,
					PermissoesEnum.CLASSIFICACAO_PONTUACAO,
					PermissoesEnum.CLASSIFICACAO_PONTUACAO_CADASTRAR,
					PermissoesEnum.CLASSIFICACAO_PONTUACAO_ALTERAR,
					PermissoesEnum.CLASSIFICACAO_PONTUACAO_CONSULTAR,
					PermissoesEnum.CLASSIFICACAO_PONTUACAO_DESATIVAR,
					PermissoesEnum.RESPOSTA,
					PermissoesEnum.RESPOSTA_CADASTRAR,
					PermissoesEnum.RESPOSTA_ALTERAR,
					PermissoesEnum.RESPOSTA_CONSULTAR,
					PermissoesEnum.RESPOSTA_DESATIVAR],
				},
			},
			{
				path: 'minhaconta',
				loadChildren: './minha-conta/minha-conta.module#MinhaContaModule',
				canLoad: [AutorizacaoGuard],
			},
			{
				path: 'grupopergunta',
				loadChildren: './grupo-pergunta/grupo-pergunta.module#GrupoPerguntaModule',
				canLoad: [AutorizacaoGuard], data: {
					permissoes: [PermissoesEnum.GRUPO_PERGUNTA,
					PermissoesEnum.GRUPO_PERGUNTA_CADASTRAR,
					PermissoesEnum.GRUPO_PERGUNTA_ALTERAR,
					PermissoesEnum.GRUPO_PERGUNTA_CONSULTAR,
					PermissoesEnum.GRUPO_PERGUNTA_DESATIVAR],
				},
			},
			{
				path: 'indicadorquestionario',
				loadChildren: './indicador-questionario/indicador-questionario.module#IndicadorQuestionarioModule',
				canLoad: [AutorizacaoGuard], data: {
					permissoes: [PermissoesEnum.INDICADOR_QUESTIONARIO,
					PermissoesEnum.INDICADOR_QUESTIONARIO_CADASTRAR,
					PermissoesEnum.INDICADOR_QUESTIONARIO_CONSULTAR,
					PermissoesEnum.INDICADOR_QUESTIONARIO_ALTERAR,
					PermissoesEnum.INDICADOR_QUESTIONARIO_DESATIVAR],
				},
			},
			{
				path: 'pergunta',
				loadChildren: './pergunta/pergunta.module#PerguntaModule',
				canLoad: [AutorizacaoGuard],
				data: {
					permissoes: [PermissoesEnum.PERGUNTA,
					PermissoesEnum.PERGUNTA_CADASTRAR,
					PermissoesEnum.PERGUNTA_ALTERAR,
					PermissoesEnum.PERGUNTA_CONSULTAR,
					PermissoesEnum.PERGUNTA_DESATIVAR],
				},
			},
			{
				path: 'classificacaopontuacao',
				loadChildren: './classificacao-pontuacao/classificacao-pontuacao.module#ClassificacaoPontuacaoModule',
				canLoad: [AutorizacaoGuard], data: {
					permissoes: [PermissoesEnum.CLASSIFICACAO_PONTUACAO,
					PermissoesEnum.CLASSIFICACAO_PONTUACAO_CADASTRAR,
					PermissoesEnum.CLASSIFICACAO_PONTUACAO_ALTERAR,
					PermissoesEnum.CLASSIFICACAO_PONTUACAO_CONSULTAR,
					PermissoesEnum.CLASSIFICACAO_PONTUACAO_DESATIVAR],
				},
			},
			{
				path: 'resposta',
				loadChildren: './resposta/resposta.module#RespostaModule',
				canLoad: [AutorizacaoGuard], data: {
					permissoes: [PermissoesEnum.RESPOSTA,
					PermissoesEnum.RESPOSTA_CADASTRAR,
					PermissoesEnum.RESPOSTA_ALTERAR,
					PermissoesEnum.RESPOSTA_CONSULTAR,
					PermissoesEnum.RESPOSTA_DESATIVAR],
				},
			},
			{
				path: 'pesquisa-sesi',
				loadChildren: './pesquisa-sesi/pesquisa-sesi.module#PesquisaSesiModule',
				canLoad: [AutorizacaoGuard], data: {
					permissoes: [PermissoesEnum.PESQUISA_SESI_CONSULTAR],
				},
			},
			{
				path: 'dashboard',
				loadChildren: './dashboard/dashboard.module#DashboardModule',
				canLoad: [AutorizacaoGuard], data: {
					permissoes: [PermissoesEnum.PESQUISA_SESI_CONSULTAR],
				},
			},
			{
				path: 'imunizacao',
				loadChildren: './imunizacao/imunizacao.module#ImunizacaoModule',
				canLoad: [AutorizacaoGuard], data: {
					permissoes: [PermissoesEnum.TRABALHADOR,
					PermissoesEnum.TRABALHADOR_CADASTRAR,
					PermissoesEnum.TRABALHADOR_ALTERAR,
					PermissoesEnum.TRABALHADOR_CONSULTAR,
					PermissoesEnum.TRABALHADOR_DESATIVAR,],
				},
            },

            {
                path: 'sistema-credenciado',
                loadChildren: './sistema-credenciado/sistema-credenciado.module#SistemaCredenciadoModule',
                canLoad: [AutorizacaoGuard], data: {
                    permissoes: [
                        PermissoesEnum.SISTEMA_CREDENCIADO_CADASTRAR,
                        PermissoesEnum.SISTEMA_CREDENCIADO_ALTERAR,
                        PermissoesEnum.SISTEMA_CREDENCIADO_PESQUISAR,
                        PermissoesEnum.SISTEMA_CREDENCIADO_ATIVAR_DESATIVAR,
                    ],
                },
            },

			// {
			// 	path: 'teste', component: TesteComponent,

			// }
		],
	},
];

@NgModule({
	imports: [TemplateModule, SegurancaModule, RouterModule.forChild(routes), CalendarModule.forRoot()],
	declarations: [],
	//	declarations: [TesteComponent]
})
export class ComponenteModule {
	constructor() {
	}
}
