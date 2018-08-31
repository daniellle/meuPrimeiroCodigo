import { Questionario } from './../modelo/questionario.model';

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { BaseService } from 'app/servico/base.service';
import { Observable } from 'rxjs/Observable';
import { PerguntaQuestionario } from '../modelo/pergunta-questionario.model';
import { Paginacao } from '../modelo/paginacao.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { PerguntaQuestionarioFilter } from '../modelo/filtro-pergunta-questionario';

@Injectable()
export class PerguntaQuestionarioService extends BaseService<PerguntaQuestionario> {

    constructor(
        protected httpClient: HttpClient,
        protected bloqueio: BloqueioService,
    ) {
        super(httpClient, bloqueio);
    }

    pesquisarPaginado(filtro: PerguntaQuestionarioFilter, paginacao: Paginacao): Observable<ListaPaginada<PerguntaQuestionario>> {
        const params = this.getParams(filtro, paginacao);
        return super.get('/v1/montar-questionario/paginado', params)
            .map((response: ListaPaginada<PerguntaQuestionario>) => {
                return this.parseResponsePaginado(response);
            }).catch((error) => {
                return Observable.throw(error);
            });
    }

    salvar(entity: PerguntaQuestionario): Observable<PerguntaQuestionario> {
        return super.post('/v1/montar-questionario/pergunta-resposta', entity)
            .map((response: PerguntaQuestionario) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            });

    }

    editar(perguntaQuestionario: PerguntaQuestionario): Observable<PerguntaQuestionario> {
        return super.put('/v1/montar-questionario', perguntaQuestionario)
            .map((response: PerguntaQuestionario) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            });
    }

    private getParams(filtro: PerguntaQuestionarioFilter, paginacao: Paginacao): HttpParams {
        let params = new HttpParams();
        if (filtro.idQuestionario) {
            params = params.append('idQuestionario', filtro.idQuestionario.toString());
        }
        return params;
    }

    private parseResponsePaginado(response: ListaPaginada<PerguntaQuestionario>): ListaPaginada<PerguntaQuestionario> {
        if (!response.list) {
            response.list = new Array<PerguntaQuestionario>();
        }
        return response;
    }

    remover(entity: PerguntaQuestionario): Observable<Questionario> {
        return super.put(`/v1/montar-questionario/pergunta-desativar`, entity)
            .map((response: Response) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }
}