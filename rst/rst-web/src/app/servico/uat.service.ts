import { FiltroEndereco } from './../modelo/filtro-endereco.model';
import { Municipio } from 'app/modelo/municipio.model';
import { Injectable } from '@angular/core';
import { BaseService } from 'app/servico/base.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Response } from '@angular/http';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { Observable } from 'rxjs/Observable';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { Paginacao } from 'app/modelo/paginacao.model';
import { Uat } from 'app/modelo/uat.model';
import { FiltroUat } from 'app/modelo/filtro-uat.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Estado } from 'app/modelo/estado.model';

@Injectable()
export class UatService extends BaseService<Uat> {

  constructor(
    protected httpClient: HttpClient, protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService) {
    super(httpClient, bloqueio);
  }

  pesquisarAtivos(filtroUat: FiltroUat, paginacao: Paginacao): Observable<ListaPaginada<Uat>> {
    const params = new HttpParams().append('cnpj', typeof filtroUat.cnpj !== 'undefined' ? filtroUat.cnpj : '')
      .append('razaoSocial', typeof filtroUat.razaoSocial !== 'undefined' ? filtroUat.razaoSocial.toString() : '')
      .append('idDepRegional', filtroUat.idDepRegional > 0 ? filtroUat.idDepRegional.toString() : '0')
      .append('statusCat', 'A')
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/uats/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  pesquisar(filtroUat: FiltroUat, paginacao: Paginacao): Observable<ListaPaginada<Uat>> {
    const params = new HttpParams().append('cnpj', typeof filtroUat.cnpj !== 'undefined' ? filtroUat.cnpj : '')
      .append('razaoSocial', typeof filtroUat.razaoSocial !== 'undefined' ? filtroUat.razaoSocial.toString() : '')
      .append('idDepRegional', filtroUat.idDepRegional > 0 ? filtroUat.idDepRegional.toString() : '0')
      .append('statusCat', filtroUat.situacao)
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString())
      .append('cpfUsuarioAssociado', filtroUat.cpfUsuarioAssociado);
    return super.get('/v1/uats/paginado', params)
      .map((response: ListaPaginada<Uat>) => {
        return this.parseResponsePaginado(response);
      }).catch((error: Response) => {
        return Observable.throw(error);
      }).finally(() => {
            this.bloqueio.evento.emit(true);
        });
  }

  pesquisarPorEndereco(filtroEndereco: FiltroEndereco): Observable<Uat[]> {
    const params = new HttpParams().append('idEstado', filtroEndereco.idEstado ? filtroEndereco.idEstado : '0')
      .append('idMunicipio', filtroEndereco.idMunicipio ? filtroEndereco.idMunicipio : '0')
      .append('bairro', filtroEndereco.bairro ? filtroEndereco.bairro : '');
    return super.get('/v1/uats/endereco', params)
      .map((response: ListaPaginada<Uat>) => {
        return this.parseResponsePaginado(response);
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  private parseResponsePaginado(response: ListaPaginada<Uat>): ListaPaginada<Uat> {
    if (!response.list) {
      response.list = new Array<Uat>();
    }
    return response;
  }

  pesquisarTodos() {
    return super.get('/v1/uats/')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }
  pesquisarPorId(id: string): Observable<Uat> {
    return super.get('/v1/uats/' + id)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  salvar(uat: Uat): Observable<Uat> {
    if (uat.id) {
      return this.editar(uat);
    } else {
      return super.post(`/v1/uats/`, uat)
        .map((response: Uat) => {
          return response;
        }).catch((error) => {
          return Observable.throw(error);
        });
    }
  }

  editar(uat: Uat): Observable<Uat> {
    return super.put('/v1/uats', uat)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  pesquisarMunicipios(): Observable<Municipio[]> {
    return super.get('/v1/municipios')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
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
}
