import { FiltroDepartRegional } from './../modelo/filtro-depart-regional.model';
import { Estado } from './../modelo/estado.model';
import { Municipio } from './../modelo/municipio.model';
import { MensagemErro } from './../modelo/mensagem-erro.model';
import { HttpParams, HttpResponse } from '@angular/common/http';
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { Observable } from 'rxjs/Observable';
import { Paginacao } from 'app/modelo/paginacao.model';
import { BloqueioService } from './bloqueio.service';
import { HttpClient } from '@angular/common/http';
import { DepartamentoRegional } from './../modelo/departamento-regional.model';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core';

@Injectable()
export class DepartRegionalService extends BaseService<DepartamentoRegional> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  pesquisar(filtroDepartRegional: FiltroDepartRegional, paginacao: Paginacao): Observable<ListaPaginada<DepartamentoRegional>> {
    const params = new HttpParams().append('cnpj', typeof filtroDepartRegional.cnpj !== 'undefined' ? filtroDepartRegional.cnpj : '')
      .append('razaoSocial', typeof filtroDepartRegional.razaoSocial !== 'undefined' ? filtroDepartRegional.razaoSocial.toString() : '')
      .append('idEstado', filtroDepartRegional.idEstado)
      .append('statusDepartamento', filtroDepartRegional.situacao)
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/departamentos-regionais/paginado', params)
      .map((response: ListaPaginada<DepartamentoRegional>) => {
        return this.parseResponsePaginado(response);
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      });
  }

  pesquisarPorId(id: string): Observable<DepartamentoRegional> {
    return super.get('/v1/departamentos-regionais/' + id)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  salvar(departamentoRegional: DepartamentoRegional): Observable<DepartamentoRegional> {
    this.bloqueio.evento.emit(false);
    if (typeof departamentoRegional.id !== 'undefined') {
      return this.editar(departamentoRegional);
    } else {
      return super.post('/v1/departamentos-regionais', departamentoRegional)
        .map((response: Response) => {
          return response;
        }).catch((response: HttpResponse<MensagemErro>) => {
          return Observable.throw(response);
        }).finally(() => {
          this.bloqueio.evento.emit(true);
        });
    }
  }

  editar(departamentoRegional: DepartamentoRegional): Observable<DepartamentoRegional> {
    return super.put('/v1/departamentos-regionais', departamentoRegional)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }

  pesquisarMunicipiosPorEstado(idEstado: number): Observable<Municipio[]> {
    return super.get('/v1/municipios/estados/' + idEstado.toString())
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  buscarEstados(): Observable<Estado[]> {
    return super.get('/v1/estados')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  listarTodosAtivos(filtro?: FiltroDepartRegional): Observable<DepartamentoRegional[]> {
    let params;
    if (filtro) {
      params = new HttpParams()
        .append('aplicarDadosFilter', filtro.aplicarDadosFilter ? 'true' : 'false');
    }
    return super.get('/v1/departamentos-regionais/ativos', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  listarTodos(filtro?: FiltroDepartRegional): Observable<DepartamentoRegional[]> {

    let params;

    if (filtro) {
      params = new HttpParams()
        .append('aplicarDadosFilter', filtro.aplicarDadosFilter ? 'true' : 'false');
    }

    return super.get('/v1/departamentos-regionais', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  private parseResponsePaginado(response: ListaPaginada<DepartamentoRegional>): ListaPaginada<DepartamentoRegional> {
    if (!response.list) {
      response.list = new Array<DepartamentoRegional>();
    }
    return response;
  }

}
