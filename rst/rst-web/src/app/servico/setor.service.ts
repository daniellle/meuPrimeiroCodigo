import { HttpParams } from '@angular/common/http';
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { FiltroSetor } from 'app/modelo/filtro-setor.model';
import { Observable } from 'rxjs/Observable';
import { BloqueioService } from './bloqueio.service';
import { HttpClient } from '@angular/common/http';
import { Setor } from 'app/modelo/setor.model';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core';

@Injectable()
export class SetorService extends BaseService<Setor> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  pesquisarPorId(id: number): Observable<Setor> {
    return super.get('/v1/setor/' + id)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  pesquisar(filtroSetor: FiltroSetor, paginacao: Paginacao): Observable<ListaPaginada<Setor>> {
    const params = new HttpParams()
      .append('sigla', typeof filtroSetor.sigla !== 'undefined' ? filtroSetor.sigla : '')
      .append('descricao', typeof filtroSetor.descricao !== 'undefined' ? filtroSetor.descricao : '')
      .append('idSetor', typeof filtroSetor.idSetor !== 'undefined' ? filtroSetor.idSetor.toString() : '')
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/setores/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }
}
