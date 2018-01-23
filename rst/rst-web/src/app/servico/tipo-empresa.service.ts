import { TipoEmpresa } from './../modelo/tipo-empresa.model';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class TipoEmpresaService extends BaseService<TipoEmpresa> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
        super(httpClient, bloqueio);
    }
    pesquisarTodos(): Observable<TipoEmpresa[]> {
    return super.get('/v1/tipos-empresas')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }
}
