import { GrupoPerguntaFilter } from './../modelo/filtro-grupo-pergunta';
import { GrupoPergunta } from './../modelo/grupo-pergunta.model';
import { BaseService } from 'app/servico/base.service';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { Paginacao } from './../modelo/paginacao.model';
import { BloqueioService } from './bloqueio.service';
import { ListaPaginada } from './../modelo/lista-paginada.model';

@Injectable()
export class GrupoPerguntaService extends BaseService<GrupoPergunta> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueio);
  }

  pesquisarPaginado(filtro: GrupoPerguntaFilter, paginacao: Paginacao): Observable<ListaPaginada<GrupoPergunta>> {
   const params =  this.getParams(filtro, paginacao);
   return super.get('/v1/grupos-perguntas/paginado', params)
    .map((response: ListaPaginada<GrupoPergunta>) => {
        return this.parseResponsePaginado(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  buscarPorId(id: number): Observable<GrupoPergunta> {
    return super.get('/v1/grupos-perguntas/' + id)
      .map((response: GrupoPergunta) => {
        return this.parseResponse(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  salvar(entity: GrupoPergunta): Observable<GrupoPergunta> {
    if (entity.id) {
      return this.editar(entity);
    } else {
      return super.post('/v1/grupos-perguntas', entity)
        .map((response: Response) => {
          return response;
        }).catch((error) => {
          return Observable.throw(error);
        });
    }
  }

  editar(entity: GrupoPergunta): Observable<GrupoPergunta> {
    return super.put('/v1/grupos-perguntas/editar', entity)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  desativar(entity: GrupoPergunta): Observable<GrupoPergunta> {
    return super.put('/v1/grupos-perguntas/desativar', entity)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  private getParams(filtro: GrupoPerguntaFilter, paginacao: Paginacao): HttpParams {
    let params = new HttpParams();

    if (filtro.descricao) {
      params = params.append('descricao', filtro.descricao);
    }

    if (paginacao) {
      params = params.append('pagina', paginacao.pagina.toString());
      params = params.append('qtdRegistro', paginacao.qtdRegistro.toString());
    }
    return params;
  }

  private parseResponse(response: GrupoPergunta): GrupoPergunta {
    if (response) {
      return response;
    }

    return new GrupoPergunta();
  }

  private parseResponsePaginado(response: ListaPaginada<GrupoPergunta>): ListaPaginada<GrupoPergunta> {
    if (!response.list) {
      response.list = new Array<GrupoPergunta>();
    }
    return response;
  }
}
