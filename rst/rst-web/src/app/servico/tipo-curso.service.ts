import { TipoCurso } from './../modelo/tipo-curso.model';
import { BloqueioService } from './bloqueio.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BaseService } from './../servico/base.service';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class TipoCursoService extends BaseService<TipoCurso>  {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  buscarTodos(): Observable<TipoCurso[]> {
    return super.get('/v1/tipo-cursos')
    .map((response: TipoCurso[]) => {
      return this.parseResponse(response);
    }).catch((error) => {
      return Observable.throw(error);
    });
  }

  salvar(entity: TipoCurso): Observable<TipoCurso> {
    return super.post('/v1/tipo-cursos', entity)
      .map((response: TipoCurso) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  private parseResponse(response: TipoCurso[]): TipoCurso[] {
    if (!response) {
      response = new Array<TipoCurso>();
    }
    return response;
  }

}
