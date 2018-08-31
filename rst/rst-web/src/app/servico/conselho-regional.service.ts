import { BloqueioService } from 'app/servico/bloqueio.service';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { ConselhoRegional } from '../modelo/conselho-regional.models';

@Injectable()
export class ConselhoRegionalService extends BaseService<ConselhoRegional> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  buscarConselhoRegional(): Observable<ConselhoRegional[]> {
    return super.get('/v1/conselhoregionais')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

}
