import { QuestionarioTrabalhadorFilter } from './../modelo/filter-questionario-trabalhador-filter';
import { QuestionarioTrabalhador } from './../modelo/questionario-trabalhador';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Observable } from 'rxjs/Observable';
import { Paginacao } from 'app/modelo/paginacao.model';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core/';

@Injectable()
export class QuestionarioTrabalhadorService extends BaseService<QuestionarioTrabalhador> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueio);
  }

  pesquisarPaginado(filtro: QuestionarioTrabalhadorFilter, paginacao: Paginacao): Observable<ListaPaginada<QuestionarioTrabalhador>> {

    const params = this.getParams(filtro, paginacao);
    return super.get('/v1/questionario-trabalhador/paginado', params)
      .map((response: ListaPaginada<QuestionarioTrabalhador>) => {
        return this.parseResponsePaginado(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  buscarPorId(id: number): Observable<QuestionarioTrabalhador> {
    return super.get(`/v1/questionario-trabalhador/${id}`)
      .map((response: QuestionarioTrabalhador) => {
        return this.parseResponse(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  consultarPeriodicidade(id: number): Observable<Boolean> {

    return super.get(`/v1/questionario-trabalhador/${id}/periodo`)
      .map((response: Boolean) => {

        return response;
      }).catch((error) => {

        return Observable.throw(error);
      });
  }

  salvar(entity: QuestionarioTrabalhador): Observable<QuestionarioTrabalhador> {
    if (entity.id) {
      return this.editar(entity);
    } else {
      return super.post(`/v1/questionario-trabalhador`, entity)
        .map((response: Response) => {
          return response;
        }).catch((error) => {
          return Observable.throw(error);
        });
    }
  }

  editar(entity: QuestionarioTrabalhador): Observable<QuestionarioTrabalhador> {
    return super.put(`/v1/questionario-trabalhador`, entity)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  private getParams(filtro: QuestionarioTrabalhadorFilter, paginacao: Paginacao): HttpParams {
    let params = new HttpParams();

    if (filtro.nome) {
      params = params.append('nome', filtro.nome);
    }

    if (paginacao) {
      params = params.append('pagina', paginacao.pagina.toString());
      params = params.append('qtdRegistro', paginacao.qtdRegistro.toString());
    }
    return params;
  }

  private parseResponse(response: QuestionarioTrabalhador): QuestionarioTrabalhador {
    if (response) {
      return response;
    }

    return new QuestionarioTrabalhador();
  }

  private parseResponsePaginado(response: ListaPaginada<QuestionarioTrabalhador>): ListaPaginada<QuestionarioTrabalhador> {
    if (!response.list) {
      response.list = new Array<QuestionarioTrabalhador>();
    }
    return response;
  }
}
