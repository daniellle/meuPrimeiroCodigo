import { Parametro } from './../modelo/parametro.model';
import { Observable } from 'rxjs/Observable';
import { BloqueioService } from './bloqueio.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core';

@Injectable()
export class ParametroService extends BaseService<Parametro> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  buscarTermoUso(): Observable<Parametro> {
    return super.getPublic('/v1/parametro/termo-uso')
    .map((response: Response) => {
      return response;
    }).catch((error: Response) => {
      return Observable.throw(error);
    });
  }

  pesquisar(): Observable<string> {
    return super.getPublic('/v1/parametro/tamanho-maximo-upload-arquivo')
    .map((response: Response) => {
      return response;
    }).catch((error: Response) => {
      return Observable.throw(error);
    });
  }

  buscarIgev(): Observable<Parametro> {
    return super.getPublic('/v1/parametro/igev')
    .map((response: Response) => {
      return response;
    }).catch((error: Response) => {
      return Observable.throw(error);
    });
  }

}
