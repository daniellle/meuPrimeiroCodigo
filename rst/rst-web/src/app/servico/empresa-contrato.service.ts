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
import {Contrato} from "../modelo/contrato.model";
import {EmpresaTrabalhador} from "../modelo/empresa-trabalhador.model";
import {FiltroEmpresaContrato} from "../modelo/filtro-empresa-contrato.model";

@Injectable()
export class EmpresaContratoService extends BaseService<EmpresaContrato>{

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService,
              protected autenticacaoService: AutenticacaoService, private empresaService: EmpresaService) {
      super(httpClient, bloqueio);
  }

  pesquisarContratos(filtro: FiltroEmpresaContrato, paginacao: Paginacao): Observable<ListaPaginada<Contrato>>{
      const params = new HttpParams().append('id', filtro.idEmpresa.toString())
          .append('pagina', paginacao.pagina.toString())
          .append('qtdRegistro', paginacao.qtdRegistro.toString());
      return super.get('/v1/unidades-obras-contratos-uat/contratos/' + filtro.idEmpresa)
          .map((response: ListaPaginada<Contrato>) => {
              if (!response.list) { response.list = new Array<Contrato>(); }
              return response;
          }).catch((error) => {
              const erro = error.error.mensagem ? error.error.mensagem : MensagemProperties.app_rst_erro_geral;
              return Observable.throw(erro);
          });
  }

  salvar(contrato: Contrato): Observable<Contrato>{
      return super.post(`/v1/unidades-obras-contratos-uat/`, contrato)
          .map((response: EmpresaTrabalhador) => {
              return response;
          }).catch((error) => {
              return Observable.throw(error);
          });
  }

 /* desbloquearContrato(contrato: Contrato): Observable<Contrato>{

  }*/

 /*bloquearContrato(contrato: Contrato): Observable<Contrato>{

 }
*/
  listarContratos(){

  }



}
