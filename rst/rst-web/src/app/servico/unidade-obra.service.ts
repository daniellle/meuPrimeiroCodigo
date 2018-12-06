import { UnidadeObra } from 'app/modelo/unidade-obra.model';
import { Injectable } from '@angular/core';
import { BaseService } from 'app/servico/base.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { HttpClient } from '@angular/common/http';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class UnidadeObraService extends BaseService<UnidadeObra> {

    constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService,
                protected autenticacaoService: AutenticacaoService) {
                super(httpClient, bloqueio);
    }

    pesquisarTodos(): Observable<UnidadeObra[]> {
        return super.get('/v1/unidades-obras')
            .map((response: Response) => {
                return response;
            }).catch((error: Response) => {
                return Observable.throw(error);
            });
    }

    pesquisarPorEmpresa(id: number): Observable<UnidadeObra[]> {
        return super.get('/v1/unidades-obras/' + id)
          .map((response: Response) => {
            return response;
          }).catch((error: Response) => {
            return Observable.throw(error);
          });
    }

    pesquisarPorNome(nome: string, id: number): Observable<UnidadeObra[]> {
        return super.get('/v1/unidades-obras/' + id + '/' + nome)
            .map((response: Response) => {
                return response;
            }).catch((error: Response) => {
                return Observable.throw(error);
            });
    }

}
