import { SessaoGuard } from './seguranca/sessao.guard';
import { AutorizacaoGuard } from './seguranca/autorizacao.guard';
import { RouterModule, Routes } from '@angular/router';

export const routes: Routes = [
	{
		path: 'cadastro', loadChildren: './componente/componente.module#ComponenteModule',
		canLoad: [SessaoGuard]
	},
	{
		path: 'res',
		loadChildren: './componente/res/res.module#ResModule',
		canLoad: [SessaoGuard], data: {
			permissoes: ['trabalhador_consultar']
		},
	},
	{
		path: '', loadChildren: './publico/publico.module#PublicoModule',
	},
];

export const AppRoutes = RouterModule.forRoot(routes);
