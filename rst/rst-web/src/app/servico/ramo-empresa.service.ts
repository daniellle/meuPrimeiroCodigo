import { Observable } from 'rxjs/Observable';
import { BloqueioService } from './bloqueio.service';
import { HttpClient } from '@angular/common/http';
import { BaseService } from 'app/servico/base.service';
import { RamoEmpresa } from 'app/modelo/ramo-empresa.model';
import { Injectable } from '@angular/core';

@Injectable()
export class RamoEmpresaService extends BaseService<RamoEmpresa> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  pesquisarTodos(): Observable<RamoEmpresa[]> {
    return super.get('/v1/ramos-empresa')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

}
