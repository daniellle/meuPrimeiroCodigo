import { Injectable } from "@angular/core";
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { BloqueioService } from './bloqueio.service';
import { BaseService } from "./base.service";
import { UatVeiculo } from "app/modelo/uat-veiculo";
import { UatVeiculoTipo } from "app/modelo/uat-veiculo-tipo";
import { UatVeiculoTipoAtendimento } from "app/modelo/uat-veiculo-tipo-atendimento";
import { MensagemErro } from "app/modelo/mensagem-erro.model";
import { UatVeiculoGroupedTipoDTO } from "app/modelo/uat-veiculo-grouped-tipo-dto";
import { UatVeiculoDTO } from "app/modelo/uat-veiculo-dto";

@Injectable()
export class UatVeiculoService extends BaseService<UatVeiculo>{
    constructor(
        protected httpClient: HttpClient,
        protected bloqueio: BloqueioService
    ) {
        super(httpClient, bloqueio);
    }

    listUatVeiculoTipo(): Observable<UatVeiculoTipo[]> {
        return super.get('/v1/uat-veiculo/tipo')
            .map((response: UatVeiculoTipo[]) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
        });
    }

    listUatVeiculoTipoAtendimento(): Observable<UatVeiculoTipoAtendimento[]> {
        return super.get('/v1/uat-veiculo/tipo-atendimento')
            .map((response: UatVeiculoTipoAtendimento[]) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
        });
    }

    salvar(uatVeiculo: any[]): Observable<any[]> {
        return super.post('/v1/uat-veiculo', uatVeiculo)
            .map((response: any[]) => {
                return response;
            }).catch((error: HttpResponse<MensagemErro>) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }

    excluir(idVeiculo: Number, idUat: Number): Observable<void> {
        return super.delete(`/v1/uat-veiculo?idVeiculo=${idVeiculo}&idUat=${idUat}`)
            .map((response: any) => {
                return response;
            }).catch((error: HttpResponse<MensagemErro>) => {
                return Observable.throw(error);
            }).finally(() => {
                this.bloqueio.evento.emit(true);
            });
    }

    listUatVeiculoByIdUatAndGroupedByTipo(idUat: Number): Observable<UatVeiculoGroupedTipoDTO[]> {
        return super.get(`/v1/uat-veiculo/?idUat=${idUat}`)
            .map((response: UatVeiculoGroupedTipoDTO[]) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
        });
    }
}
