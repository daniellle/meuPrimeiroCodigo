import { RespostaFilter } from './../modelo/filtro-resposta';
import { Resposta } from './../modelo/resposta.model';
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
export class RespostaService extends BaseService<Resposta> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueio);
  }

  pesquisarPaginado(filtro: RespostaFilter, paginacao: Paginacao): Observable<ListaPaginada<Resposta>> {
   const params =  this.getParams(filtro, paginacao);
   return super.get('/v1/respostas/paginado', params)
    .map((response: ListaPaginada<Resposta>) => {
        return this.parseResponsePaginado(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  buscarPorId(id: number): Observable<Resposta> {
    return super.get('/v1/respostas/' + id)
      .map((response: Resposta) => {
        return this.parseResponse(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  salvar(entity: Resposta): Observable<Resposta> {
    if (entity.id) {
      return this.editar(entity);
    } else {
      return super.post('/v1/respostas', entity)
        .map((response: Response) => {
          return response;
        }).catch((error) => {
          return Observable.throw(error);
        });
    }
  }

  editar(entity: Resposta): Observable<Resposta> {
    return super.put('/v1/respostas/editar', entity)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  desativar(resposta: Resposta): Observable<Resposta> {
    return super.put('/v1/respostas/desativar', resposta)
        .map((response: Response) => {
            return response;
        }).catch((error) => {
            return Observable.throw(error);
        });
}

  private getParams(filtro: RespostaFilter, paginacao: Paginacao): HttpParams {
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

  private parseResponse(response: Resposta): Resposta {
    if (response) {
      return response;
    }

    return new Resposta();
  }

  private parseResponsePaginado(response: ListaPaginada<Resposta>): ListaPaginada<Resposta> {
    if (!response.list) {
      response.list = new Array<Resposta>();
    }
    return response;
  }
}
