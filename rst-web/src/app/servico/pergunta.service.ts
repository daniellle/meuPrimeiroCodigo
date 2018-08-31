import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';
import { Paginacao } from './../modelo/paginacao.model';
import { BloqueioService } from './bloqueio.service';
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { PerguntaFilter } from './../modelo/filtro-pergunta';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { Pergunta } from '../modelo/pergunta.model';

@Injectable()
export class PerguntaService extends BaseService<Pergunta> {

    constructor(
        protected httpClient: HttpClient,
        protected bloqueio: BloqueioService,
        protected autenticacaoService: AutenticacaoService,
    ) {
        super(httpClient, bloqueio);
    }

    pesquisarPaginado(filtro: PerguntaFilter, paginacao: Paginacao): Observable<ListaPaginada<Pergunta>> {
        const params = this.getParams(filtro, paginacao);

        return super.get('/v1/pergunta/paginado', params)
            .map((response: ListaPaginada<Pergunta>) => {
                return this.parseResponsePaginado(response);
            }).catch((error) => {
                return Observable.throw(error);
            });
    }

    buscarPorId(id: number): Observable<Pergunta> {
        return super.get('/v1/pergunta/' + id)
            .map((response: Pergunta) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            });
    }

    salvar(entity: Pergunta): Observable<Pergunta> {
        if (entity.id) {
            return this.editar(entity);
        } else {
            return super.post('/v1/pergunta', entity)
                .map((response: Response) => {
                    return response;
                }).catch((error) => {
                    return Observable.throw(error);
                });
        }
    }

    editar(entity: Pergunta): Observable<Pergunta> {
        return super.put('/v1/pergunta/editar', entity)
            .map((response: Response) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            });
    }

    desativar(pergunta: Pergunta): Observable<Pergunta> {
        return super.put('/v1/pergunta/desativar', pergunta)
            .map((response: Response) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            });
    }

    private getParams(filtro: PerguntaFilter, paginacao: Paginacao): HttpParams {
        let params = new HttpParams();

        if (filtro.descricao) {
            params = params.append('descricao', filtro.descricao);
        }

        if (filtro.id) {
            params = params.append('id', filtro.id.toString());
        }

        if (paginacao) {
            params = params.append('pagina', paginacao.pagina.toString());
            params = params.append('qtdRegistro', paginacao.qtdRegistro.toString());
        }
        return params;
    }

    private parseResponsePaginado(response: ListaPaginada<Pergunta>): ListaPaginada<Pergunta> {
        if (!response.list) {
            response.list = new Array<Pergunta>();
        }
        return response;
    }

    remover(entity: Pergunta): Observable<Pergunta> {
        return super.put(`/v1/pergunta/desativar`, entity)
        .map((response: Response) => {
          return response;
        }).catch((error) => {
          return Observable.throw(error);
        }).finally(() => {
          this.bloqueio.evento.emit(true);
        });
      }
}