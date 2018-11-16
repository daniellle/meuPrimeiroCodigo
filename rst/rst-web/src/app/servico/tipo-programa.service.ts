import { Injectable } from '@angular/core';
import {BaseService} from "./base.service";
import {TipoPrograma} from "../modelo/tipo-programa.model";
import {HttpClient} from "@angular/common/http";
import {BloqueioService} from "./bloqueio.service";
import {AutenticacaoService} from "./autenticacao.service";
import {Observable} from "rxjs/Observable";
import {Response} from "@angular/http";

@Injectable()
export class TipoProgramaService extends BaseService<TipoPrograma>{

  constructor(
      protected httpClient: HttpClient, protected bloqueio: BloqueioService,
      protected autenticacaoService: AutenticacaoService
  ) {
      super(httpClient, bloqueio);
  }

  pesquisarTodos(): Observable<TipoPrograma[]>{
      return super.get('/v1/tipo-programa')
          .map((response: Response) => {
              return response;
          }).catch((error: Response) => {
              return Observable.throw(error);
          });
  }

}
