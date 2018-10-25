import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { BaseService } from './base.service';
import { SistemaCredenciado } from 'app/modelo/sistema-credenciado.model';
import { BloqueioService } from './bloqueio.service';
import { FiltroSistemaCredenciado } from 'app/modelo/filtro-sistema-credenciado.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';

@Injectable()
export class SistemaCredenciadoService extends BaseService<SistemaCredenciado> {

    private static readonly ENDPOINT = '/v1/sistemascredenciados/';

    constructor(
        protected httpClient: HttpClient,
        protected bloqueioService: BloqueioService,
    ) {
        super(httpClient, bloqueioService);
    }

    public salvar(sistemaCredenciado: SistemaCredenciado): Observable<any> {
        if (typeof sistemaCredenciado.id !== 'undefined') {
            return this.editar(sistemaCredenciado);
        } else {
            return super.post(SistemaCredenciadoService.ENDPOINT, sistemaCredenciado)
                .map((response: any) => {
                    return response;
                }).catch((error: any) => {
                    return Observable.throw(error);
                }).finally(() => {
                    this.bloqueioService.evento.emit(true);
                });
        }
    }

    public editar(sistemaCredenciado: SistemaCredenciado): Observable<String> {
        return super.put(SistemaCredenciadoService.ENDPOINT, sistemaCredenciado)
            .map((response: any) => {
                return response;
            }).catch((error: any) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueioService.evento.emit(true);
            });
    }

    public findById(id: number): Observable<SistemaCredenciado> {
        return super.get(`${SistemaCredenciadoService.ENDPOINT}${id}`)
            .map((response: Response) => {
                return response;
            }).catch((error: Response) => {
                return Observable.throw(error);
            });
    }

    public findPaginado(filtro: FiltroSistemaCredenciado, paginacao: Paginacao): Observable<ListaPaginada<SistemaCredenciado>> {
        const params = this.getParams(filtro, paginacao);
        return super.get(`${SistemaCredenciadoService.ENDPOINT}paginado`, params)
            .map((response: Response) => {
                return response;
            }).catch((error: any) => {
                return Observable.throw(error);
            });
    }

    private getParams(filtro: FiltroSistemaCredenciado, paginacao: Paginacao): HttpParams {
        let params = new HttpParams();

        if (filtro.cnpj) {
            params = params.append('cnpj', filtro.cnpj);
        }

        if (filtro.nomeResponsavel) {
            params = params.append('nomeResponsavel', filtro.nomeResponsavel);
        }

        if (filtro.sistema) {
            params = params.append('sistema', filtro.sistema);
        }
        if (filtro.bloqueado !== '') {
            params = params.append('bloqueado', filtro.bloqueado);
        }

        if (paginacao) {
            params = params.append('pagina', paginacao.pagina.toString());
            params = params.append('qtdRegistro', paginacao.qtdRegistro.toString());
        }
        return params;
    }
}
