import { MensagemProperties } from './../compartilhado/utilitario/recurso.pipe';
import { Paginacao } from './../modelo/paginacao.model';
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { HttpParams } from '@angular/common/http';
import { BaseService } from 'app/servico/base.service';
import { DepartamentoRegionalProdutoServico } from './../modelo/departamento-regional-produto-servico';
import { Observable } from 'rxjs/Observable';
import { MensagemErro } from './../modelo/mensagem-erro.model';
import { AutenticacaoService } from './autenticacao.service';
import { BloqueioService } from './bloqueio.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable()
export class DepartamentoRegionalProdutoServicoService extends BaseService<DepartamentoRegionalProdutoServico> {
    constructor(
      protected httpClient: HttpClient, protected bloqueio: BloqueioService,
      protected autenticacaoService: AutenticacaoService) {
      super(httpClient, bloqueio);
    }

    salvar(id: string, list: DepartamentoRegionalProdutoServico[]): Observable<DepartamentoRegionalProdutoServico[]> {
      return super.post(`/v1/departamento-regional-produto-servico/departamentoregional/${id}` , list)
        .map((response: Response) => {
          return response;
        }).catch((error: HttpResponse<MensagemErro>) => {
          return Observable.throw(error);
        }).finally(() => {
          this.bloqueio.evento.emit(true);
        });
    }

pesquisarDepartamentoRegionalProdutoServico(id: number, paginacao: Paginacao):
  Observable<ListaPaginada<DepartamentoRegionalProdutoServico>> {
      const params = new HttpParams().append('id', id.toString())
        .append('pagina', paginacao.pagina.toString())
        .append('qtdRegistro', paginacao.qtdRegistro.toString());
      return super.get('/v1/departamento-regional-produto-servico/paginado', params)
        .map((response: ListaPaginada<DepartamentoRegionalProdutoServico>) => {
          if (!response.list) { response.list = new Array<DepartamentoRegionalProdutoServico>(); }
          return response;
        }).catch((error) => {
          const erro = error.error.mensagem ? error.error.mensagem : MensagemProperties.app_rst_erro_geral;
          return Observable.throw(erro);
    });
  }

  desativarDepartamentoRergionalProdutoServico(departamentoRegionalProdutoServico:
   DepartamentoRegionalProdutoServico): Observable<DepartamentoRegionalProdutoServico> {
    this.bloqueio.evento.emit(false);
    return super.put('/v1/departamento-regional-produto-servico/desativar', departamentoRegionalProdutoServico)
    .map((response: Response) => {
      return response;
    }).catch((response: HttpResponse<MensagemErro>) => {
      return Observable.throw(response);
    }).finally(() => {
      this.bloqueio.evento.emit(true);
    });
  }
}
