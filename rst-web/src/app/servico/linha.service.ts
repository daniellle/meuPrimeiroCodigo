import { FiltroLinha } from './../modelo/filtro-linha.model';
import { BloqueioService } from './bloqueio.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BaseService } from './../servico/base.service';
import { Linha } from './../modelo/linha.model';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class LinhaService extends BaseService<Linha>  {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  buscarTodas(filtro?: FiltroLinha): Observable<Linha[]> {

    let params;

    if (filtro) {
      params = new HttpParams()
        .append('aplicarDadosFilter', filtro.aplicarDadosFilter ? 'true' : 'false');
    }
    return super.get('/v1/linhas')
      .map((response: Linha[]) => {
        return this.parseResponse(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  salvar(entity: Linha): Observable<Linha> {
    return super.post('/v1/linhas', entity)
      .map((response: Linha) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  private parseResponse(response: Linha[]): Linha[] {
    if (!response) {
      response = new Array<Linha>();
    }
    return response;
  }

  buscarLinhasAtivasPorIdDepartamentoUat(id: number): Observable<Linha[]> {
    return super.get('/v1/linhas/unidade-sesi/departamento/' + id)
      .map((response: Linha) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  buscarLinhasPorIdUat(ids: number[]): Observable<Linha[]> {
    return super.get('/v1/linhas/unidade-sesi/' + ids.toString())
      .map((response: Linha[]) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

}
