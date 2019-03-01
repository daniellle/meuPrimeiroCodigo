import { BloqueioService } from './bloqueio.service';
import { Injectable } from "@angular/core";
import { BaseService } from "./base.service";
import { UatQuadroPessoalArea } from "app/modelo/uat-quadro-pessoal-area";
import { HttpClient } from "@angular/common/http";
import { Observable } from 'rxjs/Observable';

@Injectable()
export class UatQuadroPessoalAreaService extends BaseService<UatQuadroPessoalArea> {

    constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
        super(httpClient, bloqueio);
    }

    findAll(): Observable<UatQuadroPessoalArea[]> {
        return super.get('/v1/uatquadropessoalarea')
            .map((response: UatQuadroPessoalArea[]) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            });
    }

}
