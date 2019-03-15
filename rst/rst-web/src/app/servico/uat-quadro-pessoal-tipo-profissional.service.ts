import { BaseService } from "./base.service";
import { Observable } from 'rxjs/Observable';
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BloqueioService } from './bloqueio.service';
import { UatQuadroPessoalTipoProfissional } from "app/modelo/uat-quadro-pessoal-tipo-profissional";

@Injectable()
export class UatQuadroPessoalTipoProfissionalService extends BaseService<UatQuadroPessoalTipoProfissional>{
    constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
        super(httpClient, bloqueio);
    }

    findByTipoServico(idServico: Number): Observable<UatQuadroPessoalTipoProfissional[]> {
        return super.get('/v1/uatquadropessoaltipoprofissional/servico/' + idServico.toString())
            .map((response: UatQuadroPessoalTipoProfissional[]) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            });
    }
}
