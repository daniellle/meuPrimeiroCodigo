import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { BloqueioService } from './bloqueio.service';
import { BaseService } from "./base.service";
import { UatInstalacaoFisicaCategoria } from "app/modelo/uat-instalacai-fisica-categoria";

@Injectable()
export class UatInstalacaoFisicaCategoriaService extends BaseService<UatInstalacaoFisicaCategoria>{
    constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
        super(httpClient, bloqueio);
    }

    findAll(): Observable<UatInstalacaoFisicaCategoria> {
        return super.get('/v1/uatinstalacaofisicacategoria')
            .map((response: Response) => {
                return response;
            }).catch((error) => {
                return Observable.throw(error);
            });
    }
}
