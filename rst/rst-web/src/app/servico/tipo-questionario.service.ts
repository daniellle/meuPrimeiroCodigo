import { TipoQuestionario } from './../modelo/tipo-questionario.model';
import { BloqueioService } from './bloqueio.service';
import { HttpClient } from '@angular/common/http';
import { BaseService } from './../servico/base.service';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class TipoQuestionarioService extends BaseService<TipoQuestionario>  {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  buscarTodas(): Observable<TipoQuestionario[]> {

    return super.get('/v1/tipo-questionario')
    .map((response: TipoQuestionario[]) => {
      return this.parseResponse(response);
    }).catch((error) => {
      return Observable.throw(error);
    });
  }

  private parseResponse(response: TipoQuestionario[]): TipoQuestionario[] {
    if (!response) {
      response = new Array<TipoQuestionario>();
    }
    return response;
  }

}
