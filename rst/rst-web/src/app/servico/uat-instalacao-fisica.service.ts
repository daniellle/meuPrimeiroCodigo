import { BloqueioService } from './bloqueio.service';
import { BaseService } from "./base.service";
import { UatInstalacaoFisica } from "app/modelo/uat-instalacao-fisica";
import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';

@Injectable()
export class UatInstalacaoFisicaService extends BaseService<UatInstalacaoFisica>{
    constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
        super(httpClient, bloqueio);
    }
}
