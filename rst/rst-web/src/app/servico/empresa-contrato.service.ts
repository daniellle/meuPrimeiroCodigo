import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AutenticacaoService} from "./autenticacao.service";
import {BloqueioService} from "./bloqueio.service";
import {BaseService} from "./base.service";
import {EmpresaContrato} from "../modelo/empresa-contrato.model";

@Injectable()
export class EmpresaContratoService extends BaseService<EmpresaContrato>{

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService,
              protected autenticacaoService: AutenticacaoService) {
      super(httpClient, bloqueio);
  }

  criarNovoContrato(){

  }

  listarContratos(){

  }



}
