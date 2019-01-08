import { MensagemErro } from './../../app/modelo/mensagem-erro.model';
import { HttpResponse } from '@angular/common/http';
import { Response } from '@angular/http';
import { MascaraUtil } from './../../app/compartilhado/utilitario/mascara.util';
import { HttpParams } from '@angular/common/http';
import { ListaPaginada } from './../../app/modelo/lista-paginada.model';
import { Observable } from 'rxjs/Observable';
import { Paginacao } from './../../app/modelo/paginacao.model';
import { FiltroUsuario } from './../modelo/filtro-usuario.model';
import { AutenticacaoService } from './../../app/servico/autenticacao.service';
import { BloqueioService } from './../../app/servico/bloqueio.service';
import { HttpClient } from '@angular/common/http';
import { Usuario } from './../modelo/usuario.model';
import { BaseService } from './../../app/servico/base.service';
import { Injectable } from '@angular/core';
import { ParametroService } from './parametro.service';
import { PerfilUsuarioFilter } from 'app/modelo/filter-perfil-usuario.model';
import { UsuarioRelatorio } from 'app/modelo/usuario-relatorio.model';

@Injectable()
export class UsuarioService extends BaseService<Usuario> {
  
  tokenClienteRst: string;
  
  constructor(
    protected httpClient: HttpClient,
    protected bloqueioService: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
    protected parametroService: ParametroService
    ) {
      super(httpClient, bloqueioService);
    }
    
    pesquisarPaginado(filtro: FiltroUsuario, paginacao: Paginacao): Observable<ListaPaginada<Usuario>> {
      
      const params = this.getParams(filtro, paginacao);
      return super.get('/v1/usuarios/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
      
    }
    
    pesquisarPaginadoRelatorio(filtro: PerfilUsuarioFilter, paginacao: Paginacao):
    Observable<ListaPaginada<UsuarioRelatorio>> {
      const params = this.getParamsRelatorio(filtro, paginacao);
      return super.get('/v1/perfil-usuario/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
    }

    pesquisarPdf(filtro: PerfilUsuarioFilter, paginacao: Paginacao): any{
      const params = this.getParamsRelatorio(filtro);
      return super.getPDF('/v1/perfil-usuario/pdf', params)
      .map((response: Response) => {
        return response;
      })
      .catch((error: Response) => {
        return Observable.throw(error);
      });
    }

    visualizarPdf(filtro: PerfilUsuarioFilter, paginacao: Paginacao){
      const params = this.getParamsRelatorio(filtro);
      return super.getPDF('/v1/perfil-usuario/pdf', params)
      .map((response: Response) => {
        // imprime pdf (ctrl + P)
        const blobUrl = URL.createObjectURL(response);
        const iframe = document.createElement('iframe');
        iframe.style.display = 'none';
        iframe.src = blobUrl;
        document.body.appendChild(iframe);
        iframe.contentWindow.print();
        // visualiza
        // const fileURL = URL.createObjectURL(response);
        // window.open(fileURL, '_blank');
      })
      .catch((error: Response) => {
        return Observable.throw(error);
      });
    }
    
    pesquisarCSV(filtro: PerfilUsuarioFilter, paginacao: Paginacao): any {
      const params = this.getParamsRelatorio(filtro);
      return super.getCSV('/v1/perfil-usuario/csv', params)
      .map((response: Response) => {
        return response;
      })
      .catch((error: Response) => {
        return Observable.throw(error);
      })
    }
    
    buscarUsuarioById(id: number): Observable<Usuario> {
      
    const params = new HttpParams()
      .append('id', id.toString());

    return super.get('/v1/usuarios/buscar', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  buscarPerfil(): Observable<Usuario> {
    return super.get('/v1/usuarios/buscarperfil')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  salvarUsuario(usuario: Usuario): Observable<Usuario> {
    this.bloqueioService.evento.emit(false);

    if (typeof usuario.id !== 'undefined') {
      return this.editar(usuario);
    } else {
      return super.post('/v1/usuarios', usuario)
        .map((response: Usuario) => {
          return response;
        }).catch((error: HttpResponse<MensagemErro>) => {
          return Observable.throw(error);
        }).finally(() => {
          this.bloqueioService.evento.emit(true);
        });
    }
  }


  editar(usuario: Usuario): Observable<any> {
    return super.put('/v1/usuarios', usuario)
      .map((response: any) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueioService.evento.emit(true);
      });
  }

  desativar(usuario: Usuario): Observable<Usuario> {
    return super.delete('/v1/usuarios/desativar?id=' + usuario.id)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueioService.evento.emit(true);
      });
  }

  recuperarSenha(email: String): Observable<String> {
    return super.post('v1/usuarios/recuperarsenha', email)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueioService.evento.emit(true);
      });
  }

  private getParams(filtro: FiltroUsuario, paginacao: Paginacao): HttpParams {
    let params = new HttpParams();

    if (filtro.nome) {
      params = params.append('nome', filtro.nome);
    }

    if (filtro.login) {
      params = params.append('login', MascaraUtil.removerMascara(filtro.login));
    }

    if (filtro.empresa && filtro.empresa.id) {
      params = params.append('idEmpresa', filtro.empresa.id.toString());
    }

    if (filtro.departamentoRegional && filtro.departamentoRegional.id) {
      params = params.append('idDepartamentoRegional', filtro.departamentoRegional.id.toString());
    }

    if (filtro.codigoPerfil) {
      params = params.append('codigoPerfil', filtro.codigoPerfil);
    }

    if (paginacao) {
      params = params.append('pagina', paginacao.pagina.toString());
      params = params.append('qtdRegistro', paginacao.qtdRegistro.toString());
    }
    return params;
  }

  private getParamsRelatorio(filtro: PerfilUsuarioFilter, paginacao?: Paginacao): HttpParams {
    let params = new HttpParams();

    if (filtro.nome) {
      params = params.append('nome', filtro.nome);
    }

    if (filtro.login) {
      params = params.append('login', MascaraUtil.removerMascara(filtro.login));
    }

    if (filtro.empresa && filtro.empresa.id) {
      params = params.append('idEmpresa', filtro.empresa.id.toString());
    }

    if (filtro.departamentoRegional && filtro.departamentoRegional.id) {
      params = params.append('idDepartamentoRegional', filtro.departamentoRegional.id.toString());
    }

    if (filtro.codigoPerfil) {
      params = params.append('codigoPerfil', filtro.codigoPerfil);
    }

    if (paginacao) {
      params = params.append('pagina', paginacao.pagina.toString());
      params = params.append('qtdRegistro', paginacao.qtdRegistro.toString());
    }
    return params;
  }

  consultarDadosUsuario(login: string): Observable<Usuario> {
    var endpoint = '/v1/usuarios/dados/' + login;

    this.parametroService.buscarTokenAcessoClienteRst().subscribe(token => this.tokenClienteRst);

    return super.getClientCredential(endpoint, this.tokenClienteRst)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }
}
