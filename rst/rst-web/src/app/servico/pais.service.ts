import { Observable } from 'rxjs/Observable';
import { BloqueioService } from './bloqueio.service';
import { HttpClient } from '@angular/common/http';
import { BaseService } from 'app/servico/base.service';
import { Pais } from 'app/modelo/pais.model';
import { Injectable } from '@angular/core';

@Injectable()
export class PaisService extends BaseService<Pais> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  buscarPaises(): Observable<Pais[]> {
    return super.get('/v1/pais')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

}
