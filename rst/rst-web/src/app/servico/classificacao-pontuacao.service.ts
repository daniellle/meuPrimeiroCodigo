import { ClassificacaoPontuacaoFilter } from './../modelo/filtro-classificacao-pontuacao';
import { ClassificacaoPontuacao } from './../modelo/classificacao-pontuacao.model';
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
export class ClassificacaoPontuacaoService extends BaseService<ClassificacaoPontuacao> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueio);
  }

  pesquisarPaginado(filtro: ClassificacaoPontuacaoFilter, paginacao: Paginacao): Observable<ListaPaginada<ClassificacaoPontuacao>> {
   const params =  this.getParams(filtro, paginacao);
   return super.get('/v1/classificacoes/paginado', params)
    .map((response: ListaPaginada<ClassificacaoPontuacao>) => {
        return this.parseResponsePaginado(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  buscarPorId(id: number): Observable<ClassificacaoPontuacao> {
    return super.get('/v1/classificacoes/' + id)
      .map((response: ClassificacaoPontuacao) => {
        return this.parseResponse(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  salvar(entity: ClassificacaoPontuacao): Observable<ClassificacaoPontuacao> {
    if (entity.id) {
      return this.editar(entity);
    } else {
      return super.post('/v1/classificacoes', entity)
        .map((response: Response) => {
          return response;
        }).catch((error) => {
          return Observable.throw(error);
        });
    }
  }

  editar(entity: ClassificacaoPontuacao): Observable<ClassificacaoPontuacao> {
    return super.put('/v1/classificacoes/editar', entity)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  desativar(entity: ClassificacaoPontuacao): Observable<ClassificacaoPontuacao> {
    return super.put('/v1/classificacoes/desativar', entity)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  private getParams(filtro: ClassificacaoPontuacaoFilter, paginacao: Paginacao): HttpParams {
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

  private parseResponse(response: ClassificacaoPontuacao): ClassificacaoPontuacao {
    if (response) {
      return response;
    }

    return new ClassificacaoPontuacao();
  }

  private parseResponsePaginado(response: ListaPaginada<ClassificacaoPontuacao>): ListaPaginada<ClassificacaoPontuacao> {
    if (!response.list) {
      response.list = new Array<ClassificacaoPontuacao>();
    }
    return response;
  }
}
