import { Observable } from 'rxjs/Observable';
import { AutenticacaoService } from './autenticacao.service';
import { BloqueioService } from './bloqueio.service';
import { HttpClient } from '@angular/common/http';
import { Perfil } from './../modelo/perfil.model';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core';

@Injectable()
export class PerfilService extends BaseService<Perfil> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueioService: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueioService);
  }

  buscarTodos(nivelHierarquivo): Observable<Perfil[]> {
    return super.get('/v1/perfis/' + nivelHierarquivo)
    .map((response: Response) => {
      return response;
    }).catch((error: Response) => {
      return Observable.throw(error);
    });
  }

}
