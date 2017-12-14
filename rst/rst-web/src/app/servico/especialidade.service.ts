import { BaseService } from 'app/servico/base.service';
import { Especialidade } from 'app/modelo/especialidade.model';
import { Observable } from 'rxjs/Observable';
import { AutenticacaoService } from './autenticacao.service';
import { BloqueioService } from './bloqueio.service';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable()
export class EspecialidadeService extends BaseService<Especialidade> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService, protected autenticacaoService: AutenticacaoService) {
    super(httpClient, bloqueio);
  }

  pesquisarEspecialidades () {
    return super.get('/v1/especialidades')
    .map((response: Response) => {
      return response;
    }).catch((error: Response) => {
      return Observable.throw(error);
    });
  }

}
