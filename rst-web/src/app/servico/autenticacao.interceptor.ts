import { AutenticacaoService } from 'app/servico/autenticacao.service';
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../environments/environment';
import { Seguranca } from '../compartilhado/utilitario/seguranca.model';

@Injectable()
export class AutenticacaoInterceptor implements HttpInterceptor {

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (Seguranca.isAutenticado()) {
      request = request.clone({
        headers: request.headers.set('Authorization', 'Bearer ' + localStorage.getItem('access_token')),
      });

    }

    return next.handle(request).map((event: void | HttpEvent<any>): HttpEvent<any> => {

      if (event instanceof HttpResponse) {
        if (event.url.search('idp/AuthnEngine') > 0) {
          window.parent.location.reload();
        }
        return event;
      }

    }).catch((error: any) => {

      if (error instanceof HttpErrorResponse) {
        if (error.url.search('idp/AuthnEngine') > 0) {
          window.parent.location.reload();
        }

        if (error.status === 403 && !localStorage.getItem('usuario_sem_sessao')) {
          AutenticacaoService.sair();
        }

        if (error.status === 404) {
          window.location.href = '/rst/acessonegado';
        }

        if (error.status === 401) {
          let endpoint;
          if (localStorage.getItem('access_token') === null) {
            endpoint = request.clone({
              url: environment.api_private + '/v1/oauth/autenticar',
              method: 'post',
              headers: request.headers
                .set('Accept', 'application/json')
                .set('Content-Type', 'application/json'),
            });
          } else {
            endpoint = request.clone({
              url: environment.api_private + '/v1/oauth/atualizar',
              method: 'post',
              headers: request.headers
                .set('Accept', 'application/json')
                .set('Content-Type', 'application/x-www-form-urlencoded'),
              body: `assertion=${localStorage.getItem('refresh_token')}&grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer`,
            });
          }
          return next.handle(endpoint).map((event) => {
            if (event instanceof HttpResponse) {
              Seguranca.salvarUsuario(event.body);
            }
          }).catch((error2: any) => {
            return Observable.throw(error2);
          });
        }
      }

      return Observable.throw(error);
    });
  }
}
