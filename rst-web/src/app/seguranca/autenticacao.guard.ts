import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanActivateChild,
  CanLoad,
  Route,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Seguranca} from '../compartilhado/utilitario/seguranca.model';

@Injectable()
export class AutenticacaoGuard implements CanLoad, CanActivate, CanActivateChild {

  constructor(private router: Router) {}

  canActivate(next?: ActivatedRouteSnapshot, state?: RouterStateSnapshot): Observable<boolean>
    | Promise<boolean>
    | boolean {

    if (Seguranca.isAutenticado()) {
      this.router.navigate(['/']);
      return false;
    }
    return true;
  }

  canActivateChild(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean>
    | Promise<boolean>
    | boolean {
    return this.canActivate(next, state);
  }

  canLoad(route: Route): Observable<boolean> | Promise<boolean> | boolean {
    return this.canActivate();
  }

}
