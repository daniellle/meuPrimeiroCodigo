import { MascaraUtil } from './../compartilhado/utilitario/mascara.util';
import { FiltroEmpresaTrabalhador } from './../modelo/filtro-empresa-trabalhador.model';
import { Trabalhador } from 'app/modelo/trabalhador.model';
import { Empresa } from './../modelo/empresa.model';
import { EmpresaTrabalhador } from './../modelo/empresa-trabalhador.model';
import { Observable } from 'rxjs/Observable';
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { Injectable } from '@angular/core';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BaseService } from 'app/servico/base.service';

@Injectable()
export class EmpresaTrabalhadorService extends BaseService<EmpresaTrabalhador> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueio);
  }

  pesquisarPaginado(idEmpresa: number, filtro: FiltroEmpresaTrabalhador, paginacao: Paginacao):
  Observable<ListaPaginada<EmpresaTrabalhador>> {
    return super.get('/v1/empresa-trabalhadores/paginado', this.getParams(idEmpresa, filtro, paginacao))
      .map((response: ListaPaginada<EmpresaTrabalhador>) => {
        return this.parseResponsePaginado(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  salvar(empresaTrabalhador: EmpresaTrabalhador): Observable<EmpresaTrabalhador> {
    return super.post(`/v1/empresa-trabalhadores/`, empresaTrabalhador)
      .map((response: EmpresaTrabalhador) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  remover(empresaTrabalhador: EmpresaTrabalhador): Observable<EmpresaTrabalhador> {
    return super.put(`/v1/empresa-trabalhadores/remover`, empresaTrabalhador)
      .map((response: EmpresaTrabalhador) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  buscarPorId(id: number): Observable<EmpresaTrabalhador> {
    return super.get(`/v1/empresa-trabalhadores/${id}`)
      .map((response: EmpresaTrabalhador) => {
        return this.parseResponse(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  private getParams(idEmpresa: number, filtro: FiltroEmpresaTrabalhador, paginacao: Paginacao): HttpParams {
    let params = new HttpParams();

    if (idEmpresa) {
      params = params.append('idEmpresa', idEmpresa.toString());
    }
    if (filtro.cpf) {
      params = params.append('cpf', MascaraUtil.removerMascara(filtro.cpf));
    }
    if (filtro.nome) {
      params = params.append('nome', filtro.nome);
    }
    if (paginacao) {
      params = params.append('pagina', paginacao.pagina.toString());
      params = params.append('qtdRegistro', paginacao.qtdRegistro.toString());
    }
    return params;
  }

  private parseResponse(response: EmpresaTrabalhador): EmpresaTrabalhador {
    if (!response.empresa) {
      response.empresa = new Empresa();
    }
    if (!response.trabalhador) {
      response.trabalhador = new Trabalhador();
    }
    return response;
  }

  private parseResponsePaginado(response: ListaPaginada<EmpresaTrabalhador>): ListaPaginada<EmpresaTrabalhador> {
    if (!response.list) {
      response.list = new Array<EmpresaTrabalhador>();
    }
    return response;
  }

  pesquisarPorTrabalhadorCpf(cpf: string): Observable<EmpresaTrabalhador[]> {
    return super.get(`/v1/empresa-trabalhadores/trabalhador/${cpf}`)
    .map((response: Response) => {
      return response;
    }).catch((error: Response) => {
      return Observable.throw(error);
    });
  }

}
