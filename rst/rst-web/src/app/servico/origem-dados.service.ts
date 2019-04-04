import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { OrigemDados } from 'app/modelo/origem-dados.model';
import { HttpClient } from '@angular/common/http';
import { BloqueioService } from './bloqueio.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class OrigemDadosService extends BaseService<OrigemDados>{

    constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
        super(httpClient, bloqueio);
    }

    listarOrigemDados(): Observable<OrigemDados> {
        return super.get('/v1/origemdados')
            .map((response: Response) => {
                return response;
            }).catch((error: Response) => {
                return Observable.throw(error);
            });
    }

    findOrigemDoContrato(): Observable<OrigemDados[]> {
        return super.get('/v1/origemdados/origem-contrato')
        .map((response: Response) => {
            return response;
        }).catch((error: Response) => {
            return Observable.throw(error);
        });
    }
}
