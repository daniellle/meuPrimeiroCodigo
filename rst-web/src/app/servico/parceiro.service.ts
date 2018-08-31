import { EmailParceiro } from './../modelo/email-parceiro.model';
import { EnderecoParceiro } from './../modelo/endereco-parceiro.model';
import { Segmento } from 'app/modelo/segmento.model';
import { PorteEmpresa } from 'app/modelo/porte-empresa.model';
import { TipoEmpresa } from './../modelo/tipo-empresa.model';
import { TelefoneParceiro } from 'app/modelo/telefone-parceiro.model';
import { ParceiroEspecialidade } from './../modelo/parceiro-especialidade.model';
import { Especialidade } from 'app/modelo/especialidade.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { Paginacao } from '../modelo/paginacao.model';
import { BaseService } from 'app/servico/base.service';
import { Observable } from 'rxjs/Observable';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Parceiro } from '../modelo/parceiro.model';
import { FiltroParceiro } from '../modelo/filtro-parceiro.model';

@Injectable()
export class ParceiroService extends BaseService<Parceiro> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
  ) {
    super(httpClient, bloqueio);
  }
  pesquisarPorId(id: number): Observable<Parceiro> {
    return super.get(`/v1/parceiros-credenciados/${id}`)
      .map((response: Parceiro) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  pesquisarPaginado(filtro: FiltroParceiro, paginacao: Paginacao): Observable<ListaPaginada<Parceiro>> {
    const params = this.getParams(filtro, paginacao);
    return super.get('/v1/parceiros-credenciados/paginado', params)
      .map((response: ListaPaginada<Parceiro>) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  getEspecialidades(): Observable<Especialidade[]> {
    return super.get('/v1/especialidades')
      .map((response: Especialidade[]) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });

  }

  buscarPorId(id: number): Observable<Parceiro> {
    return super.get('/v1/parceiros-credenciados/' + id)
      .map((response: Parceiro) => {
        return this.parseResponse(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  salvar(entity: Parceiro): Observable<Parceiro> {
    if (entity.id) {
      return this.editar(entity);
    } else {
      return super.post('/v1/parceiros-credenciados', entity)
        .map((response: Parceiro) => {
          return response;
        }).catch((error) => {
          return Observable.throw(error);
        });
    }
  }

  editar(parceiro: Parceiro): Observable<Parceiro> {
    return super.put('/v1/parceiros-credenciados', parceiro)
      .map((response: Parceiro) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  private getParams(filtro: FiltroParceiro, paginacao: Paginacao): HttpParams {
    let params = new HttpParams();

    if (filtro.cpfCnpj) {
      params = params.append('cpfCnpj', filtro.cpfCnpj);
    }

    if (filtro.razaoSocialNome) {
      params = params.append('razaoSocialNome', filtro.razaoSocialNome);
    }

    if (filtro.especialidade) {
      params = params.append('especialidade', filtro.especialidade);
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

  private parseResponse(response: Parceiro): Parceiro {

    if (!response.parceiroEspecialidades) {
      response.parceiroEspecialidades = new Array<ParceiroEspecialidade>();
    }
    if (!response.telefonesParceiro) {
      response.telefonesParceiro = new Array<TelefoneParceiro>();
    }
    if (!response.porteEmpresa) {
      response.porteEmpresa = new PorteEmpresa();
    }
    if (!response.tipoEmpresa) {
      response.tipoEmpresa = new TipoEmpresa();
    }
    if (!response.segmento) {
      response.segmento = new Segmento();
    }
    if (!response.enderecosParceiro) {
      response.enderecosParceiro = new Array<EnderecoParceiro>();
    }
    if (!response.emailsParceiro) {
      response.emailsParceiro = new Array<EmailParceiro>();
    }
    return response;
  }
}
