import { BaseService } from 'app/servico/base.service';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Linha } from './../modelo/linha.model';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { Paginacao } from './../modelo/paginacao.model';
import { BloqueioService } from './bloqueio.service';
import { ProdutoServico } from './../modelo/produto-servico.model';
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { ProdutoServicoFilter } from './../modelo/filtro-produto-servico';

@Injectable()
export class ProdutoServicoService extends BaseService<ProdutoServico> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueio);
  }

  pesquisarPaginado(filtro: ProdutoServicoFilter, paginacao: Paginacao): Observable<ListaPaginada<ProdutoServico>> {

    const params = this.getParams(filtro, paginacao);
    return super.get('/v1/produtos-servicos/paginado', params)
      .map((response: ListaPaginada<ProdutoServico>) => {
        return this.parseResponsePaginado(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  buscarPorId(id: number): Observable<ProdutoServico> {
    return super.get('/v1/produtos-servicos/' + id)
      .map((response: ProdutoServico) => {
        return this.parseResponse(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  salvar(entity: ProdutoServico): Observable<ProdutoServico> {
    if (entity.id) {
      return this.editar(entity);
    } else {
      return super.post('/v1/produtos-servicos', entity)
        .map((response: Response) => {
          return response;
        }).catch((error) => {
          return Observable.throw(error);
        });
    }
  }

  editar(entity: ProdutoServico): Observable<ProdutoServico> {
    return super.put('/v1/produtos-servicos', entity)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  private getParams(filtro: ProdutoServicoFilter, paginacao: Paginacao): HttpParams {
    let params = new HttpParams();

    if (filtro.nome) {
      params = params.append('nome', filtro.nome);
    }

    if (filtro.idLinha) {
      params = params.append('idLinha', filtro.idLinha.toString());
    }

    if (paginacao) {
      params = params.append('pagina', paginacao.pagina.toString());
      params = params.append('qtdRegistro', paginacao.qtdRegistro.toString());
    }
    if (filtro.aplicarDadosFilter) {
      params = params.append('aplicarDadosFilter', filtro.aplicarDadosFilter.toString());
    }
    return params;
  }

  private parseResponse(response: ProdutoServico): ProdutoServico {
    if (response) {
      if (!response.linha) {
        response.linha = new Linha();
      }
      return response;
    }

    return new ProdutoServico();
  }

  private parseResponsePaginado(response: ListaPaginada<ProdutoServico>): ListaPaginada<ProdutoServico> {
    if (!response.list) {
      response.list = new Array<ProdutoServico>();
    }
    return response;
  }

  pesquisarSemPaginacao(): Observable<ProdutoServico[]> {
    return super.get('/v1/produtos-servicos/todos')
      .map((response: ProdutoServico[]) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  buscarProdutoServicoPorIdUat(ids: number[]): Observable<Linha[]> {
    return super.get('/v1/produtos-servicos/unidade-sesi/' + ids.toString())
      .map((response: ProdutoServico) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }
}
