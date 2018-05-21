import { Response } from '@angular/http';
import { BaseService } from '../servico/base.service';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { BloqueioService } from './bloqueio.service';
import { Credencial } from '../modelo/credencial.model';
import { environment } from '../../environments/environment';
import { Seguranca } from '../compartilhado/utilitario/seguranca.model';

@Injectable()
export class AutenticacaoService extends BaseService<Credencial> {

	public static sair() {
		localStorage.clear();
		window.location.href = environment.url_portal + '/Shibboleth.sso/Logout';
	}

	constructor(protected http: HttpClient, protected bloqueio: BloqueioService, protected router: Router) {
		super(http, bloqueio);
	}

	atualizarSessao(): Observable<boolean> {
		return this.post('/v1/oauth/autenticar')
			.map((response: any) => {
				return Seguranca.salvarUsuario(response);
			}).catch((erro: any) => {
				console.log(erro);
				return Observable.of(false);
			});
	}

	autenticar(): Observable<false> {

		this.bloqueio.evento.emit(false);

		const options = {
			headers: new HttpHeaders()
				.set('Content-Type', 'application/json'),
		};

		return this.http.post(environment.api_private + '/v1/oauth/autenticar', options)
			.map((response: HttpResponse<any>) => {
				return Seguranca.salvarUsuario(response);
			}).catch((error: Response) => {
				return Observable.throw(error);
			}).finally(() => {
				this.bloqueio.evento.emit(true);
			});

	}

	recuperarSenha(email: String): Observable<any> {
		return super.postPublic('/v1/oauth/recuperar?email=' + email)
			.map((response: any) => {
				return response;
			}).catch((error) => {
				return Observable.throw(error);
			}).finally(() => {
				this.bloqueio.evento.emit(true);
			});
	}

	alterarSenha(hash: String, senha: String): Observable<String> {
		const propriedades = { hashRecuperacaoSenha: hash, senha };
		return super.postPublic('/v1/oauth/alterarsenha', propriedades)
			.map((response: Response) => {
				return response;
			}).catch((error) => {
				return Observable.throw(error);
			}).finally(() => {
				this.bloqueio.evento.emit(true);
			});
	}

	alterarSenhaRST(senha_antiga: String, senha_nova: String): Observable<String> {
		//var data = [senha_antiga, senha_nova];
		const propriedades = { senha_antiga: senha_antiga, senha_nova: senha_nova };
		return super.post('/v1/usuarios/trocarsenha', propriedades)
			.map((response: Response) => {
				return response;
			}).catch((error) => {
				return Observable.throw(error);
			}).finally(() => {
				this.bloqueio.evento.emit(true);
			});
	}

    validarHash(hash: String): Observable<any> {
        return super.postPublic('/v1/oauth/validarhash?hash='+ hash)
            .map((response: Response) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }

    enivarEmailHash(hash: String): Observable<any> {
        return super.postPublic('/v1/oauth/enviar-email-hash?hash='+ hash)
            .map((response: Response) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }
}

