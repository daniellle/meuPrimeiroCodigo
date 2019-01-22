import { PesquisaSesiDTO } from './../modelo/pesquisa-sesi-dto.model';
import { FiltroPesquisaSesi } from './../modelo/filtro-pesquisa-sesi.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Observable } from 'rxjs/Observable';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Uat } from './../modelo/uat.model';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core';

@Injectable()
export class PesquisaSesiService extends BaseService<Uat> {

  constructor(
    protected httpClient: HttpClient, protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService) {
    super(httpClient, bloqueio);
  }

  buscarUnidadesSesi() {
    return super.get('/v1/pesquisa-sesi/unidades-sesi/listar')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  buscarEnderecoUnidadeSesi(id: string): Observable<Uat> {
    return super.get('/v1/pesquisa-sesi/2/unidades-sesi/endereco/' + id)
      .map((response: Uat) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  pesquisarPaginado(filtro: FiltroPesquisaSesi, paginacao: Paginacao): Observable<ListaPaginada<PesquisaSesiDTO>> {
    const params = new HttpParams().append('idUnidadeSesi', filtro.idUnidadeSesi ? filtro.idUnidadeSesi : '0')
      .append('idEstado', filtro.idEstado ? filtro.idEstado : '0')
      .append('idMunicipio', filtro.idMunicipio ? filtro.idMunicipio : '0')
      .append('bairro', filtro.bairro ? filtro.bairro : '')
      .append('idsLinha', filtro.idLinha ? filtro.idLinha.toString() : '')
      .append('idsProduto', filtro.idProduto ? filtro.idProduto.toString() : '')
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/pesquisa-sesi/paginado', params)
      .map((response: ListaPaginada<Uat>) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

}
