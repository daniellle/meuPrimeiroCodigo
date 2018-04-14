import { Paginacao } from './../modelo/paginacao.model';
import { FiltroRedeCredenciada } from './../modelo/filtro-rede-credenciada';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { RedeCredenciada } from 'app/modelo/rede-credenciada.model';
import { BaseService } from 'app/servico/base.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class RedeCredenciadaService extends BaseService<RedeCredenciada> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService,
              protected autenticacaoService: AutenticacaoService) {
    super(httpClient, bloqueio);
  }

  pesquisarPorId(id: string): Observable<RedeCredenciada> {
    return super.get('/v1/redes-credenciadas/' + id)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  salvar(redeCredenciada: RedeCredenciada): Observable<RedeCredenciada> {
    this.bloqueio.evento.emit(false);
    if (typeof redeCredenciada.id !== 'undefined') {
      return this.editar(redeCredenciada);
    } else {
      return super.post('/v1/redes-credenciadas', redeCredenciada)
        .map((response: Response) => {
          return response;
        }).catch((response) => {
          return Observable.throw(response);
        }).finally(() => {
          this.bloqueio.evento.emit(true);
        });
    }
  }

  editar(redeCredenciada: RedeCredenciada): Observable<RedeCredenciada> {
    return super.put('/v1/redes-credenciadas', redeCredenciada)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }

  private getParams(filtro: FiltroRedeCredenciada, paginacao: Paginacao): HttpParams {
    let params = new HttpParams();

    if (filtro.cnpj) {
      params = params.append('cnpj', filtro.cnpj);
    }

    if (filtro.razaoSocial) {
      params = params.append('razaoSocial', filtro.razaoSocial);
    }

    if (filtro.segmento) {
      params = params.append('segmento', filtro.segmento.toString());
    }

    if (filtro.situacao) {
      params = params.append('situacao', filtro.situacao);
    }

    if (paginacao) {
      params = params.append('pagina', paginacao.pagina.toString());
      params = params.append('qtdRegistro', paginacao.qtdRegistro.toString());
    }
    return params;
  }

  pesquisarPorFiltro (filtroRedeCredenciada: FiltroRedeCredenciada, paginacao: Paginacao) {
    const params =  this.getParams(filtroRedeCredenciada, paginacao);
    return super.get('/v1/redes-credenciadas/paginado' , params)
    .map((response: Response) => {
      return response;
    }).catch((error: Response) => {
      return Observable.throw(error);
    });
  }
}
