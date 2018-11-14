import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {AutenticacaoService} from "./autenticacao.service";
import {BloqueioService} from "./bloqueio.service";
import {BaseService} from "./base.service";
import {EmpresaContrato} from "../modelo/empresa-contrato.model";
import {EmpresaService} from "./empresa.service";
import {Paginacao} from "../modelo/paginacao.model";
import {Observable} from "rxjs/Observable";
import {ListaPaginada} from "../modelo/lista-paginada.model";
import {MensagemProperties} from "../compartilhado/utilitario/recurso.pipe";
import {EmpresaSindicato} from "../modelo/empresa-sindicato.model";
import {FiltroEmpresa} from "../modelo/filtro-empresa.model";

@Injectable()
export class EmpresaContratoService extends BaseService<EmpresaContrato>{

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService,
              protected autenticacaoService: AutenticacaoService, private empresaService: EmpresaService) {
      super(httpClient, bloqueio);
  }

  pesquisarContratos(filtro: FiltroEmpresa, paginacao: Paginacao): Observable<ListaPaginada<EmpresaContrato>>{
      const params = new HttpParams().append('id', filtro.id.toString())
          .append('pagina', paginacao.pagina.toString())
          .append('qtdRegistro', paginacao.qtdRegistro.toString());
      return super.get('/v1/unidades-obras-contratos-uat/contratos', params)
          .map((response: ListaPaginada<EmpresaContrato>) => {
              if (!response.list) { response.list = new Array<EmpresaContrato>(); }
              return response;
          }).catch((error) => {
              const erro = error.error.mensagem ? error.error.mensagem : MensagemProperties.app_rst_erro_geral;
              return Observable.throw(erro);
          });
  }

  criarNovoContrato(){

  }

  listarContratos(){

  }



}
