import { AutenticacaoService } from 'app/servico/autenticacao.service';
import {
	ActivatedRouteSnapshot,
	CanActivate,
	CanActivateChild,
	CanLoad,
	Route,
	Router,
	RouterStateSnapshot,
} from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class SessaoGuard implements CanLoad, CanActivate {

	constructor(private router: Router, private service: AutenticacaoService) { }

	canActivate(next?: ActivatedRouteSnapshot, state?: RouterStateSnapshot): Observable<boolean> {
		return this.service.atualizarSessao();
	}

	canLoad(route: Route): Observable<boolean> | Promise<boolean> | boolean {
		return this.service.atualizarSessao();
	}

}
