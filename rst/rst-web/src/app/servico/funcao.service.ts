import { Observable } from 'rxjs/Observable';
import { FiltroFuncao } from './../modelo/filtro-funcao.model';
import { Injectable } from '@angular/core';
import { Funcao } from 'app/modelo/funcao.model';
import { BaseService } from 'app/servico/base.service';
import { HttpClient } from '@angular/common/http';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { Paginacao } from 'app/modelo/paginacao.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { HttpParams } from '@angular/common/http';

@Injectable()
export class FuncaoService extends BaseService<Funcao> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  pesquisarPorId(id: number): Observable<Funcao> {
    return super.get('/v1/funcao/' + id)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  pesquisar(filtroFuncao: FiltroFuncao, paginacao: Paginacao): Observable<ListaPaginada<Funcao>> {

    const params = new HttpParams().append('codigo', typeof filtroFuncao.codigo !== 'undefined' ? filtroFuncao.codigo : '')
      .append('descricao', typeof filtroFuncao.descricao !== 'undefined' ? filtroFuncao.descricao : '')
      .append('idFuncao', typeof filtroFuncao.idFuncao !== 'undefined' ? filtroFuncao.idFuncao.toString() : '')
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/funcao/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }
}
