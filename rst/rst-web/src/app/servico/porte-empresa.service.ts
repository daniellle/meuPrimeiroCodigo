import { Observable } from 'rxjs/Observable';
import { PorteEmpresa } from 'app/modelo/porte-empresa.model';
import { Injectable } from '@angular/core';
import { BaseService } from 'app/servico/base.service';
import { HttpClient } from '@angular/common/http';
import { BloqueioService } from 'app/servico/bloqueio.service';

@Injectable()
export class PorteEmpresaService extends BaseService<PorteEmpresa> {

 constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
        super(httpClient, bloqueio);
    }

    pesquisarTodos(): Observable<PorteEmpresa[]> {
      return super.get('/v1/portes-empresas')
    .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
    }
}
