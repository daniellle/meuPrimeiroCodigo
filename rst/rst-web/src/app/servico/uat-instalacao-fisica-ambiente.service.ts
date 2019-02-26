import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BloqueioService } from './bloqueio.service';
import { BaseService } from "./base.service";
import { UatInstalacaoFisicaAmbiente } from "app/modelo/uat-instalacao-fisica-ambiente";
import { Observable } from 'rxjs/Observable';

@Injectable()
export class UatInstalacaoFisicaCategoriaAmbienteService extends BaseService<UatInstalacaoFisicaAmbiente>{
    constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
        super(httpClient, bloqueio);
    }

    findByCategoria(idCategoria: Number): Observable<UatInstalacaoFisicaAmbiente> {
        return super.get('/v1/uatinstalacaofisicaambiente/categoria/' + idCategoria.toString())
            .map((response: Response) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            });
    }
}
