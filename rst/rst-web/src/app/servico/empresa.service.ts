import { UsuarioEntidade } from './../modelo/usuario-entidade.model';
import { EmpresaCbo } from './../modelo/empresa-cbo.model';
import { EmpresaSindicato } from './../modelo/empresa-sindicato.model';
import { MensagemProperties } from './../compartilhado/utilitario/recurso.pipe';
import { Cnae } from './../modelo/cnae.model';
import { FiltroCnae } from './../modelo/filtro-cnae';
import { Injectable } from '@angular/core';
import { BaseService } from 'app/servico/base.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { HttpClient } from '@angular/common/http';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { Paginacao } from 'app/modelo/paginacao.model';
import { Observable } from 'rxjs/Observable';
import { HttpParams } from '@angular/common/http';
import { TipoEmpresa } from 'app/modelo/tipo-empresa.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Empresa } from 'app/modelo/empresa.model';
import { FiltroEmpresa } from 'app/modelo/filtro-empresa.model';
import { HttpResponse } from '@angular/common/http';
import { MensagemErro } from 'app/modelo/mensagem-erro.model';
import { Municipio } from 'app/modelo/municipio.model';
import { EmpresaJornada } from '../modelo/empresaJornada.model';

@Injectable()
export class EmpresaService extends BaseService<Empresa> {

  constructor(
    protected httpClient: HttpClient, protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService) {
    super(httpClient, bloqueio);
  }

  pesquisar(filtroEmpresa: FiltroEmpresa, paginacao: Paginacao): Observable<ListaPaginada<Empresa>> {
    const params = new HttpParams().append('cnpj', typeof filtroEmpresa.cnpj !== 'undefined' ? filtroEmpresa.cnpj : '')
      .append('razaoSocial', typeof filtroEmpresa.razaoSocial !== 'undefined' ? filtroEmpresa.razaoSocial.toString() : '')
      .append('nomeFantasia', typeof filtroEmpresa.nomeFantasia !== 'undefined' ? filtroEmpresa.nomeFantasia.toString() : '')
      .append('situacao', filtroEmpresa.situacao)
      .append('unidadeObra', filtroEmpresa.unidadeObra > 0 ? filtroEmpresa.unidadeObra.toString() : '0')
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/empresas/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  pesquisarMinhaEmpresa(): Observable<UsuarioEntidade[]> {
    return super.get('/v1/empresas/minha-empresa')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  pesquisarTipoCliente(): Observable<TipoEmpresa[]> {
    return super.get('/v1/tipos-empresas')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  pesquisarCnaes(filtroCnae: FiltroCnae, paginacao: Paginacao): Observable<ListaPaginada<Cnae>> {
    console.log('versao ', typeof filtroCnae.versao);
    
    const params = new HttpParams().append('versao', filtroCnae.versao ? filtroCnae.versao : '')
      .append('descricao', filtroCnae.descricao)
      .append('codigo', filtroCnae.codigo)
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/cnaes/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  pesquisarPorId(id: number): Observable<Empresa> {
    return super.get('/v1/empresas/' + id)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  salvar(empresa: Empresa): Observable<Empresa> {
    this.bloqueio.evento.emit(false);
    if (typeof empresa.id !== 'undefined') {
      return this.editar(empresa);
    } else {
      return super.post('/v1/empresas', empresa)
        .map((response: Response) => {
          return response;
        }).catch((response: HttpResponse<MensagemErro>) => {
          return Observable.throw(response);
        }).finally(() => {
          this.bloqueio.evento.emit(true);
        });
    }
  }

  pesquisarVersoes(): Observable<string[]> {
    return super.get('/v1/cnaes/versoes')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  editar(empresa: Empresa): Observable<Empresa> {
    return super.put('/v1/empresas', empresa)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }
  buscarMunicipioPorId(id: any): Observable<Municipio> {
    return super.get('/v1/municipios/' + id)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  salvarJornadas(id: number, list: EmpresaJornada[]): Observable<EmpresaJornada[]> {
    return super.post('/v1/empresas/' + id + '/jornada', list)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }

  pesquisarEmpresasSindicatos(id: number, paginacao: Paginacao): Observable<ListaPaginada<EmpresaSindicato>> {
    const params = new HttpParams().append('id', id.toString())
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/empresas/sindicatos/paginado', params)
      .map((response: ListaPaginada<EmpresaSindicato>) => {
        if (!response.list) { response.list = new Array<EmpresaSindicato>(); }
        return response;
      }).catch((error) => {
        const erro = error.error.mensagem ? error.error.mensagem : MensagemProperties.app_rst_erro_geral;
        return Observable.throw(erro);
      });
  }

  pesquisarEmpresasCbos(filtro: FiltroEmpresa, paginacao: Paginacao): Observable<ListaPaginada<EmpresaCbo>> {
    const params = new HttpParams().append('id', filtro.id.toString())
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/empresa-cbo/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  pesquisarTodasEmpresasJornadas(filtro: FiltroEmpresa): Observable<EmpresaJornada[]> {
    return super.get('/v1/empresas/' + filtro.id + '/jornada')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }
}
