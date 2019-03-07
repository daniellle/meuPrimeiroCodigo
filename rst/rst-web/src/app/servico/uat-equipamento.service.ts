import { Injectable } from "@angular/core";
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { BloqueioService } from './bloqueio.service';
import { BaseService } from "./base.service";
import { MensagemErro } from "app/modelo/mensagem-erro.model";
import { UatEquipamentoTipo } from "app/modelo/uat-equipamento-tipo";
import { UatEquipamentoArea } from "app/modelo/uat-equipamento-area";
import { UatEquipamentoGroupedAreaDTO } from "app/modelo/uat-equipamento-grouped-tipo-dto";
import { UatEquipamento } from "app/modelo/uat-equipamento";

@Injectable()
export class UatEquipamentoService extends BaseService<UatEquipamento>{
    constructor(
        protected httpClient: HttpClient,
        protected bloqueio: BloqueioService
    ) {
        super(httpClient, bloqueio);
    }

    listUatEquipamentoTipoPorArea(idArea: Number): Observable<UatEquipamentoTipo[]> {
        return super.get(`/v1/uat-equipamento/tipo?idArea=${idArea}`)
            .map((response: UatEquipamentoTipo[]) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
        });
    }

    listUatEquipamentoArea(): Observable<UatEquipamentoArea[]> {
        return super.get('/v1/uat-equipamento/area')
            .map((response: UatEquipamentoArea[]) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
        });
    }

    salvar(uatEquipamento: any[]): Observable<any[]> {
        return super.post('/v1/uat-equipamento', uatEquipamento)
            .map((response: any[]) => {
                return response;
            }).catch((error: HttpResponse<MensagemErro>) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }

    excluir(idEquipamento: Number, idUat: Number): Observable<void> {
        return super.delete(`/v1/uat-equipamento?idEquipamento=${idEquipamento}&idUat=${idUat}`)
            .map((response: any) => {
                return response;
            }).catch((error: HttpResponse<MensagemErro>) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }

    listAllUatEquipamentosGroupedByArea(idUat: Number): Observable<UatEquipamentoGroupedAreaDTO[]> {
        return super.get(`/v1/uat-equipamento/?idUat=${idUat}`)
            .map((response: UatEquipamentoGroupedAreaDTO[]) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
        });
    }
}
