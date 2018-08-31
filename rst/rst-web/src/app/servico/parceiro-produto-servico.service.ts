import { ParceiroProdutoServico } from './../modelo/parceiro-produto-servico';
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { Paginacao } from './../modelo/paginacao.model';
import { MensagemErro } from './../modelo/mensagem-erro.model';
import { Observable } from 'rxjs/Observable';
import { AutenticacaoService } from './autenticacao.service';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { BloqueioService } from './bloqueio.service';
import { BaseService } from './../servico/base.service';
import { Injectable } from '@angular/core';

@Injectable()
export class ParceiroProdutoServicoService extends BaseService<ParceiroProdutoServico> {
  constructor(
    protected httpClient: HttpClient, protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService) {
    super(httpClient, bloqueio);
  }

  salvar(id: string, list: ParceiroProdutoServico[]): Observable<ParceiroProdutoServico[]> {
    return super.post('/v1/parceiro-produto-servico/redecredenciada/' + id, list)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  pesquisarParceiroProdutoServico(id: number, paginacao: Paginacao):
    Observable<ListaPaginada<ParceiroProdutoServico>> {
    const params = new HttpParams().append('id', id.toString())
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/parceiro-produto-servico/paginado', params)
      .map((response: ListaPaginada<ParceiroProdutoServico>) => {
        if (!response.list) { response.list = new Array<ParceiroProdutoServico>(); }
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  desativarDepartamentoRergionalProdutoServico(parceiroProdutoServico:
    ParceiroProdutoServico): Observable<ParceiroProdutoServico> {
    this.bloqueio.evento.emit(false);
    return super.put('/v1/parceiro-produto-servico/desativar', parceiroProdutoServico)
      .map((response: Response) => {
        return response;
      }).catch((response: HttpResponse<MensagemErro>) => {
        return Observable.throw(response);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }
}
