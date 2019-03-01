import { BaseService } from './base.service';
import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { BloqueioService } from './bloqueio.service';
import { UatQuadroPessoal } from 'app/modelo/uat-quadro-pessoal';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { MensagemErro } from 'app/modelo/mensagem-erro.model';

@Injectable()
export class UatQuadroPessoalService extends BaseService<UatQuadroPessoal> {
    constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
        super(httpClient, bloqueio);
    }

    public salvar(instalacoesFisicas: UatQuadroPessoal[]): Observable<UatQuadroPessoal[]> {
        return super.post('/v1/uatquadropessoal', instalacoesFisicas)
            .map((response: UatQuadroPessoal[]) => {
                return response;
            }).catch((error: HttpResponse<MensagemErro>) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }

    public findByUnidadeAgg(idUnidade: Number): Observable<any> {
        return super.get('/v1/uatquadropessoal/unidade/' + idUnidade.toString())
            .map((response: any) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }

    public desativar(idInstalacaoFisica: Number, idUnidade: Number): Observable<any> {
        return super.put('/v1/uatquadropessoal/desativar/' + idInstalacaoFisica.toString() + '/' + idUnidade.toString())
            .map((response: any) => {
                return response;
            }).catch((error: HttpResponse<MensagemErro>) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }
}
