import { ListaPaginada } from './../modelo/lista-paginada.model';
import { Paginacao } from './../modelo/paginacao.model';
import { Observable } from 'rxjs/Observable';
import { AutenticacaoService } from './autenticacao.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BloqueioService } from './bloqueio.service';
import { BaseService } from './../servico/base.service';
import { RedeCredenciadaProdutoServico } from './../modelo/rede-credenciada-produto-servico';
import { Injectable } from '@angular/core';

@Injectable()
export class RedeCredenciadaProdutoServicoService extends BaseService<RedeCredenciadaProdutoServico> {
    constructor(
        protected httpClient: HttpClient, protected bloqueio: BloqueioService,
        protected autenticacaoService: AutenticacaoService) {
        super(httpClient, bloqueio);
      }

      salvar(id: string, list: RedeCredenciadaProdutoServico[]): Observable<RedeCredenciadaProdutoServico[]> {
       return super.post('/v1/rede-credenciada-produto-servico/redecredenciada/' + id , list)
          .map((response: Response) => {
            return response;
          }).catch((error) => {
            return Observable.throw(error);
          });
      }

    pesquisarRedeCredenciadaProdutoServico(id: number, paginacao: Paginacao):
    Observable<ListaPaginada<RedeCredenciadaProdutoServico>> {
        const params = new HttpParams().append('id', id.toString())
          .append('pagina', paginacao.pagina.toString())
          .append('qtdRegistro', paginacao.qtdRegistro.toString());
        return super.get('/v1/rede-credenciada-produto-servico/paginado', params)
          .map((response: ListaPaginada<RedeCredenciadaProdutoServico>) => {
            if (!response.list) { response.list = new Array<RedeCredenciadaProdutoServico>(); }
            return response;
          }).catch((error) => {
            return Observable.throw(error);
      });
    }

    desativarDepartamentoRergionalProdutoServico(departamentoRegionalProdutoServico:
        RedeCredenciadaProdutoServico): Observable<RedeCredenciadaProdutoServico> {
      this.bloqueio.evento.emit(false);
      return super.put('/v1/rede-credenciada-produto-servico/desativar', departamentoRegionalProdutoServico)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
    }
  }
