import { MensagemErro } from './../modelo/mensagem-erro.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ProdutoServico } from './../modelo/produto-servico.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { ProdutoServicoFilter } from './../modelo/filtro-produto-servico';
import { Linha } from './../modelo/linha.model';
import { UatProdutoServico } from './../modelo/uat-produto-servico.model';
import { BaseService } from 'app/servico/base.service';
import { Observable } from 'rxjs/Observable';
import { AutenticacaoService } from './autenticacao.service';
import { BloqueioService } from './bloqueio.service';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable()
export class UatProdutoServicoService extends BaseService<UatProdutoServico> {

    constructor(
        protected httpClient: HttpClient,
        protected bloqueio: BloqueioService,
        protected autenticacaoService: AutenticacaoService,
    ) {
        super(httpClient, bloqueio);
    }

    pesquisarProdutoServico(filtro: ProdutoServicoFilter, idUat: number, paginacao: Paginacao): Observable<ListaPaginada<ProdutoServico>> {
        const params = this.getParams(filtro, idUat, paginacao);
        return super.get('/v1/uat-produto-servico/pesquisa/paginado', params)
            .map((response: ListaPaginada<ProdutoServico>) => {
                return this.parseResponsePaginado(response);
            }).catch((error) => {
                return Observable.throw(error);
            });
    }

    pesquisarUatProdutoServico(id: number, paginacao: Paginacao):
        Observable<ListaPaginada<UatProdutoServico>> {
        const params = new HttpParams().append('id', id.toString())
            .append('pagina', paginacao.pagina.toString())
            .append('qtdRegistro', paginacao.qtdRegistro.toString());
        return super.get('/v1/uat-produto-servico/paginado', params)
            .map((response: ListaPaginada<UatProdutoServico>) => {
                if (!response.list) { response.list = new Array<UatProdutoServico>(); }
                return response;
            }).catch((error) => {
                const erro = error.error.mensagem ? error.error.mensagem : MensagemProperties.app_rst_erro_geral;
                return Observable.throw(erro);
            });
    }

    salvar(id: string, list: UatProdutoServico[]): Observable<UatProdutoServico[]> {
        return super.post(`/v1/uat-produto-servico/uat/${id}`, list)
            .map((response: Response) => {
                return response;
            }).catch((error: HttpResponse<MensagemErro>) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }

    desativarUatProdutoServico(uatProdutoServico:
        UatProdutoServico): Observable<UatProdutoServico> {
        this.bloqueio.evento.emit(false);
        return super.put('/v1/uat-produto-servico/desativar', uatProdutoServico)
            .map((response: Response) => {
                return response;
            }).catch((response: HttpResponse<MensagemErro>) => {
                return Observable.throw(response);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }

    private getParams(filtro: ProdutoServicoFilter, idUat: number, paginacao: Paginacao): HttpParams {
        let params = new HttpParams();

        if (idUat) {
            params = params.append('id', idUat.toString());
        }

        if (filtro.nome) {
            params = params.append('nome', filtro.nome);
        }

        if (filtro.idLinha) {
            params = params.append('idLinha', filtro.idLinha.toString());
        }

        if (paginacao) {
            params = params.append('pagina', paginacao.pagina.toString());
            params = params.append('qtdRegistro', paginacao.qtdRegistro.toString());
        }
        if (filtro.aplicarDadosFilter) {
            params = params.append('aplicarDadosFilter', filtro.aplicarDadosFilter.toString());
        }
        return params;
    }

    private parseResponsePaginado(response: ListaPaginada<ProdutoServico>): ListaPaginada<ProdutoServico> {
        if (!response.list) {
            response.list = new Array<ProdutoServico>();
        }
        return response;
    }

}
