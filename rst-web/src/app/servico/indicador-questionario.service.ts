import { IndicadorQuestionario } from './../modelo/indicador-questionario.model';
import { IndicadorQuestionarioFilter } from './../modelo/filtro-indicador-questionario';
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
export class IndicadorQuestionarioService extends BaseService<IndicadorQuestionario> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueio);
  }

  pesquisarPaginado(filtro: IndicadorQuestionarioFilter, paginacao: Paginacao): Observable<ListaPaginada<IndicadorQuestionario>> {
   const params =  this.getParams(filtro, paginacao);
   return super.get('/v1/indicadores-questionarios/paginado', params)
    .map((response: ListaPaginada<IndicadorQuestionario>) => {
        return this.parseResponsePaginado(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  buscarPorId(id: number): Observable<IndicadorQuestionario> {
    return super.get('/v1/indicadores-questionarios/' + id)
      .map((response: IndicadorQuestionario) => {
        return this.parseResponse(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  salvar(entity: IndicadorQuestionario): Observable<IndicadorQuestionario> {
    if (entity.id) {
      return this.editar(entity);
    } else {
      return super.post('/v1/indicadores-questionarios', entity)
        .map((response: Response) => {
          return response;
        }).catch((error) => {
          return Observable.throw(error);
        });
    }
  }

  editar(entity: IndicadorQuestionario): Observable<IndicadorQuestionario> {
    return super.put('/v1/indicadores-questionarios/editar', entity)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  desativar(entity: IndicadorQuestionario): Observable<IndicadorQuestionario> {
    return super.put('/v1/indicadores-questionarios/desativar', entity)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  private getParams(filtro: IndicadorQuestionarioFilter, paginacao: Paginacao): HttpParams {
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

  private parseResponse(response: IndicadorQuestionario): IndicadorQuestionario {
    if (response) {
      return response;
    }

    return new IndicadorQuestionario();
  }

  private parseResponsePaginado(response: ListaPaginada<IndicadorQuestionario>): ListaPaginada<IndicadorQuestionario> {
    if (!response.list) {
      response.list = new Array<IndicadorQuestionario>();
    }
    return response;
  }
}
