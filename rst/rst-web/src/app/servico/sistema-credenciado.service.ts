import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { BaseService } from './base.service';
import { SistemaCredenciado } from 'app/modelo/sistema-credenciado.model';
import { BloqueioService } from './bloqueio.service';

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

    editar(sistemaCredenciado: SistemaCredenciado): Observable<String> {
        return super.put(SistemaCredenciadoService.ENDPOINT, sistemaCredenciado)
            .map((response: Response) => {
                return response;
            }).catch((error: Response) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueioService.evento.emit(true);
            });
    }

    findById(id: number): Observable<SistemaCredenciado> {
        return super.get(`${SistemaCredenciadoService.ENDPOINT}${id}`)
            .map((response: Response) => {
                return response;
            }).catch((error: Response) => {
                return Observable.throw(error);
            });
    }
}
