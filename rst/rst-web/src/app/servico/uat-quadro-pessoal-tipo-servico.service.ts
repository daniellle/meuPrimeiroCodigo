import { UatQuadroPessoalTipoServico } from "app/modelo/uat-quadro-pessoal-tipo-servico";
import { BaseService } from "./base.service";
import { Observable } from 'rxjs/Observable';
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BloqueioService } from './bloqueio.service';

@Injectable()
export class UatQuadroPessoaTipoServicoService extends BaseService<UatQuadroPessoalTipoServico>{

    constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
        super(httpClient, bloqueio);
    }

    findByArea(idArea: Number): Observable<UatQuadroPessoalTipoServico[]> {
        return super.get('/v1/uatquadropessoaltiposervico/area/' + idArea.toString())
            .map((response: UatQuadroPessoalTipoServico[]) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            });
    }
}
