import { Periodicidade } from './../modelo/enum/periodicidade.model';
import { BloqueioService } from './bloqueio.service';
import { HttpClient } from '@angular/common/http';
import { BaseService } from './../servico/base.service';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class PeriodicidadeService extends BaseService<Periodicidade>  {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  buscarTodas(): Observable<Periodicidade[]> {

    return super.get('/v1/periodicidade')
    .map((response: Periodicidade[]) => {
      return this.parseResponse(response);
    }).catch((error) => {
      return Observable.throw(error);
    });
  }

  private parseResponse(response: Periodicidade[]): Periodicidade[] {
    if (!response) {
      response = new Array<Periodicidade>();
    }
    return response;
  }

}
