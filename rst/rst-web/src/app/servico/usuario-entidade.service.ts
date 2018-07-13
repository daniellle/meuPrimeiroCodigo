import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { DepartamentoRegional } from './../modelo/departamento-regional.model';
import { MensagemErro } from './../modelo/mensagem-erro.model';
import { FiltroUsuarioEntidade } from './../modelo/filtro-usuario-entidade.model';
import { UsuarioEntidade } from './../modelo/usuario-entidade.model';
import { Observable } from 'rxjs/Observable';
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { Injectable } from '@angular/core';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { BaseService } from 'app/servico/base.service';

@Injectable()
export class UsuarioEntidadeService extends BaseService<UsuarioEntidade> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueio);
  }

  getEndpoint(tipo: string): string {

    if (tipo === MensagemProperties.app_rst_menu_empresa) {
      return '/v1/usuario-entidade/empresas/paginado';
    }

    if (tipo === MensagemProperties.app_rst_sindicatos_title_menu) {
      return '/v1/usuario-entidade/sindicato/paginado';
    }

    if (tipo === MensagemProperties.app_rst_labels_departamento_regional) {
      return '/v1/usuario-entidade/departamento-regional/paginado';
    }
  }

  pesquisarPaginado(filtro: FiltroUsuarioEntidade, paginacao: Paginacao, endpoint: string):
    Observable<ListaPaginada<UsuarioEntidade>> {
    return super.get(this.getEndpoint(endpoint), this.getParams(filtro, paginacao))
      .map((response: ListaPaginada<UsuarioEntidade>) => {
        if (!response.list) {
          response.list = new Array<UsuarioEntidade>();
        }
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  pesquisarPaginadoSindicato(filtro: FiltroUsuarioEntidade, paginacao: Paginacao):
    Observable<ListaPaginada<UsuarioEntidade>> {
    return super.get('/v1/usuario-entidade/sindicato/paginado', this.getParams(filtro, paginacao))
      .map((response: ListaPaginada<UsuarioEntidade>) => {
        if (!response.list) {
          response.list = new Array<UsuarioEntidade>();
        }
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  pesquisarPaginadoDepartamento(filtro: FiltroUsuarioEntidade, paginacao: Paginacao):
    Observable<ListaPaginada<DepartamentoRegional>> {
    return super.get('/v1/usuario-entidade/departamento-regional/paginado', this.getParams(filtro, paginacao))
      .map((response: ListaPaginada<DepartamentoRegional>) => {
        if (!response.list) {
          response.list = new Array<DepartamentoRegional>();
        }
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  private getParams(filtro: FiltroUsuarioEntidade, paginacao: Paginacao): HttpParams {
    let params = new HttpParams();

    if (filtro.cnpj) {
      params = params.append('cnpj',  MascaraUtil.removerMascara(filtro.cnpj));
    }

    if (filtro.cpf) {
      params = params.append('cpf', filtro.cpf);
    }
    if (filtro.perfil) {
      params = params.append('perfil', filtro.perfil);
    }
    if (filtro.razaoSocial) {
      params = params.append('razaoSocial', filtro.razaoSocial);
    }
    if (filtro.nomeFantasia) {
      params = params.append('nomeFantasia', filtro.nomeFantasia);
    }
    if (filtro.idEstado) {
      params = params.append('idEstado', filtro.idEstado);
    }
    if (paginacao) {
      params = params.append('pagina', paginacao.pagina.toString());
      params = params.append('qtdRegistro', paginacao.qtdRegistro.toString());
    }
    return params;
  }

  salvar(usuarioEntidade: UsuarioEntidade[]): Observable<UsuarioEntidade> {
    this.bloqueioService.evento.emit(false);
    return super.post('/v1/usuario-entidade/cadastrar', usuarioEntidade)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueioService.evento.emit(true);
      });
  }

  desativar(usuarioEntidade: UsuarioEntidade): Observable<UsuarioEntidade> {
    return super.put('/v1/usuario-entidade/desativar', usuarioEntidade)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }

  validarAcessoUsuario(cpf: string): Observable<UsuarioEntidade[]> {
    let params = new HttpParams();
    params = params.append('cpf', cpf);
    return super.get('/v1/usuario-entidade/validarAcessoUsuario', params)
      .map((response: UsuarioEntidade[]) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
    });
  }
}
