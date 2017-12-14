import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { BloqueioService } from './bloqueio.service';
import { Observable } from 'rxjs/Observable';

export abstract class BaseService<T> {

  constructor(protected http: HttpClient, protected bloqueioService: BloqueioService) {

  }

  protected get<K>(endpoint: string, criteria: HttpParams = new HttpParams()): Observable<K> {
    return this.getApi(environment.api_private, endpoint, criteria);
  }

  protected getPublic<K>(endpoint: string, criteria: HttpParams = new HttpParams()): Observable<K> {
    return this.getApi(environment.api_public, endpoint, criteria);
  }

  protected getApi<K>(baseUrl: string, endpoint: string, criteria: HttpParams = new HttpParams()): Observable<K> {

    this.bloqueioService.bloquear();

    const options = {
      headers: new HttpHeaders()
        .set('Accept', 'application/json')
        .set('Content-Type', 'application/json'),
      params: criteria,
    };

    return this.http.get(baseUrl + endpoint, options)
      .catch((error: HttpResponse<T>) => {
        return Observable.throw(this.handlingError(error));
      }).finally(() => {
        this.bloqueioService.desbloquear();
      });

  }

  protected post<K>(endpoint: string, criteria?: any, desbloquear?: boolean): Observable<K> {
    return this.postApi(environment.api_private, endpoint, criteria, desbloquear);
  }

  protected postPublic<K>(endpoint: string, criteria?: any, desbloquear?: boolean): Observable<K> {
    return this.postApi(environment.api_public, endpoint, criteria, desbloquear);
  }

  protected postApi<K>(baseUrl: string, endpoint: string, criteria?: any, desbloquear?: boolean): Observable<K> {

    this.bloqueioService.bloquear();

    const options = {
      headers: new HttpHeaders()
        .set('Accept', 'application/json')
        .set('Content-Type', 'application/json'),
    };

    return this.http.post(baseUrl + endpoint, criteria ? JSON.stringify(criteria) : null, options)
      .catch((error: HttpResponse<T>) => {
        return Observable.throw(this.handlingError(error));
      }).finally(() => {
        this.bloqueioService.desbloquear();
      });

  }

  protected put<K>(endpoint: string, criteria?: any, desbloquear?: boolean): Observable<K> {

    this.bloqueioService.bloquear();
    const options = {
      headers: new HttpHeaders()
        .set('Accept', 'application/json')
        .set('Content-Type', 'application/json'),
    };

    return this.http.put(environment.api_private + endpoint, criteria ? JSON.stringify(criteria) : null, options)
      .catch((error: HttpResponse<T>) => {
        return Observable.throw(this.handlingError(error));
      }).finally(() => {
        this.bloqueioService.desbloquear();
      });

  }

  protected delete<K>(endpoint: string): Observable<K> {
    this.bloqueioService.bloquear();

    const options = {
      headers: new HttpHeaders()
        .set('Accept', 'application/json')
        .set('Content-Type', 'application/json'),
    };

    return this.http.delete(environment.api_private + endpoint, options)
      .catch((error: HttpResponse<T>) => {
        return Observable.throw(this.handlingError(error));
      }).finally(() => {
        this.bloqueioService.desbloquear();
      });

  }

  private handlingError(error) {
    if (!error.error) {
      return MensagemProperties.app_rst_operacao_nao_realizada;
    } else {
      const errorString = JSON.stringify(error.error);
      let errorObject = JSON.parse(errorString);
      if (typeof errorObject === 'string') {
        errorObject = JSON.parse(errorObject);
      }
      return errorObject.mensagem;
    }
  }
}
