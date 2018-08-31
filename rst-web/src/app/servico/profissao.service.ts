import { Injectable } from '@angular/core';
import { BaseService } from 'app/servico/base.service';
import { Profissao } from 'app/modelo/profissao.model';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class ProfissaoService extends BaseService<Profissao> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  pesquisarProfissoes() {
    return super.get('/v1/profissoes')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }
}
