import { PrimeiroAcesso } from './../modelo/primeiro-acesso.model';
import { Observable } from 'rxjs/Observable';
import { Sistema } from './../modelo/sistema.model';
import { AutenticacaoService } from './autenticacao.service';
import { BloqueioService } from './bloqueio.service';
import { HttpClient } from '@angular/common/http';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core';

@Injectable()
export class SolicitarEmailService extends BaseService<PrimeiroAcesso> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueioService: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueioService);
  }

  mandarEmail(solicitacaoEmail: PrimeiroAcesso): Observable<Sistema[]> {
    return super.postPublic('/v1/trabalhador/solicitar-email', solicitacaoEmail)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }
}
