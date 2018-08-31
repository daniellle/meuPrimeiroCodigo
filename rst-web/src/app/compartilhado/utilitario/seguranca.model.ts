import { JwtHelper } from 'angular2-jwt';
import { Observable } from 'rxjs/Observable';
import { deserialize } from 'serializer.ts/Serializer';
import { Usuario } from '../../modelo/usuario.model';
import { Token } from '../../modelo/token.model';

export class Seguranca {

	private static jwtHelper: JwtHelper = new JwtHelper();

	public static isAutenticado() {
		return localStorage.getItem('access_token') && !this.jwtHelper.isTokenExpired(localStorage.getItem('access_token'));
	}

	public static removeUsuario() {
		localStorage.removeItem('access_token');
		localStorage.removeItem('refresh_token');
	}

	public static getUsuario(): Usuario {

		const token = localStorage.getItem('access_token');

		if (!token || this.jwtHelper.isTokenExpired(token)) {
			return null;
		}

		const usuario: Usuario = this.jwtHelper.decodeToken(token);
		usuario.dados = JSON.parse(this.jwtHelper.decodeToken(token).dados);
		return usuario;

	}

	public static isPermitido(permissoes: string[]): Observable<boolean> | Promise<boolean> | boolean {

		const usuario = Seguranca.getUsuario();

		if (usuario == null || usuario.permissoes == null) {
			return false;
		}

		return permissoes.some((permissao) => {
			return usuario.permissoes.indexOf(permissao) >= 0;
		});

	}

	public static salvarUsuario(response: any): boolean {

		const token = deserialize<Token>(Token, response);

		if (token) {

			localStorage.setItem('access_token', token.access_token);
			localStorage.setItem('refresh_token', token.refresh_token);

		}

		return true;

	}

}
