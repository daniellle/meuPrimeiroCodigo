import { MensagemProperties } from './../compartilhado/utilitario/recurso.pipe';
import { QuestionarioTrabalhador } from './../modelo/questionario-trabalhador';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Observable } from 'rxjs/Observable';
import { Paginacao } from 'app/modelo/paginacao.model';
import { QuestionarioFilter } from './../modelo/filtro-questionario';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Questionario } from './../modelo/questionario.model';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core/';

@Injectable()
export class QuestionarioService extends BaseService<Questionario> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueio);
  }

  pesquisarPaginado(filtro: QuestionarioFilter, paginacao: Paginacao): Observable<ListaPaginada<Questionario>> {

    const params = this.getParams(filtro, paginacao);
    return super.get('/v1/questionario/paginado', params)
      .map((response: ListaPaginada<Questionario>) => {
        return this.parseResponsePaginado(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  buscarPorId(id: number): Observable<Questionario> {
    return super.get(`/v1/questionario/${id}`)
      .map((response: Questionario) => {
        return this.parseResponse(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  salvar(entity: Questionario): Observable<Questionario> {
    if (entity.id) {
      return this.editar(entity);
    } else {
      return super.post(`/v1/questionario`, entity)
        .map((response: Response) => {
          return response;
        }).catch((error) => {
          return Observable.throw(error);
        });
    }
  }

  publicar(entity: Questionario): Observable<Questionario> {
    return super.post(`/v1/questionario/publicar`, entity)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  editar(entity: Questionario): Observable<Questionario> {
    return super.put(`/v1/questionario`, entity)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  private getParams(filtro: QuestionarioFilter, paginacao: Paginacao): HttpParams {
    let params = new HttpParams();

    if (filtro.nome) {
      params = params.append('nome', filtro.nome);
    }

    if (filtro.situacao) {
      params = params.append('situacao', filtro.situacao);
    }

    if (filtro.tipo) {
      params = params.append('tipo', filtro.tipo.toString());
    }

    if (filtro.versao) {
      params = params.append('versao', filtro.versao);
    }

    if (paginacao) {
      params = params.append('pagina', paginacao.pagina.toString());
      params = params.append('qtdRegistro', paginacao.qtdRegistro.toString());
    }
    return params;
  }

  private parseResponse(response: Questionario): Questionario {
    if (response) {
      return response;
    }

    return new Questionario();
  }

  private parseResponsePaginado(response: ListaPaginada<Questionario>): ListaPaginada<Questionario> {
    if (!response.list) {
      response.list = new Array<Questionario>();
    }
    return response;
  }

  pesquisarVersoes(): Observable<string[]> {
    return super.get('/v1/questionario/versoes')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  remover(entity: Questionario): Observable<Questionario> {
    return super.put(`/v1/questionario/desativar`, entity)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }
}
