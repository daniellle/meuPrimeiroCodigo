import { ListaPaginada } from './../modelo/lista-paginada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { FiltroSegmento } from './../modelo/filtro-segmento';
import { AutenticacaoService } from './autenticacao.service';
import { BloqueioService } from './bloqueio.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Segmento } from './../modelo/segmento.model';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class SegmentoService extends BaseService<Segmento> {

  constructor(
    protected httpClient: HttpClient, protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService) {
    super(httpClient, bloqueio);
  }

  pesquisarSegmentos () {
    return super.get('/v1/segmentos')
    .map((response: Response) => {
      return response;
    }).catch((error: Response) => {
      return Observable.throw(error);
    });
  }

  pesquisarPaginado(filtro: FiltroSegmento, paginacao: Paginacao): Observable<ListaPaginada<Segmento>> {
        const params = new HttpParams()
          .append('codigo', filtro.codigo ? filtro.codigo : '')
          .append('descricao', filtro.descricao ? filtro.descricao : '')
          .append('pagina', paginacao.pagina.toString())
          .append('qtdRegistro', paginacao.qtdRegistro.toString());

        return super.get('/v1/segmentos/paginado', params)
        .map((response: Response) => {
            return response;
          }).catch((error: Response) => {
            return Observable.throw(error);
          });
      }
}
