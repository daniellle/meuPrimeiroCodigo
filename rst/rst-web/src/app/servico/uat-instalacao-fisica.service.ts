import { BloqueioService } from './bloqueio.service';
import { BaseService } from "./base.service";
import { UatInstalacaoFisica } from "app/modelo/uat-instalacao-fisica";
import { Injectable } from "@angular/core";
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { MensagemErro } from 'app/modelo/mensagem-erro.model';

@Injectable()
export class UatInstalacaoFisicaService extends BaseService<UatInstalacaoFisica> {
    constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
        super(httpClient, bloqueio);
    }

    public salvar(instalacoesFisicas: UatInstalacaoFisica[]): Observable<UatInstalacaoFisica[]> {
        return super.post('/v1/uatinstalacaofisica', instalacoesFisicas)
            .map((response: UatInstalacaoFisica[]) => {
                return response;
            }).catch((error: HttpResponse<MensagemErro>) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }

    public findByUnidadeAgg(idUnidade: Number): Observable<any> {
        return super.get('/v1/uatinstalacaofisica/unidade/' + idUnidade.toString())
            .map((response: any) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }

    public desativar(idInstalacaoFisica: Number): Observable<any> {
        return super.put('/v1/uatinstalacaofisica/desativar/' + idInstalacaoFisica.toString())
            .map((response: any) => {
                return response;
            }).catch((error: HttpResponse<MensagemErro>) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }
}
