import { MascaraUtil } from './../compartilhado/utilitario/mascara.util';
import { Injectable } from '@angular/core';
import { AuditoriaFilter } from './../modelo/filtro-auditoria';
import { Observable } from 'rxjs/Observable';
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { Paginacao } from './../modelo/paginacao.model';
import { BloqueioService } from './../servico/bloqueio.service';
import { AutenticacaoService } from './autenticacao.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { AuditoriaModel } from './../modelo/auditoria';
import { BaseService } from './../servico/base.service';

@Injectable()
export class AuditoriaService extends BaseService<AuditoriaModel> {

  constructor(
    protected httpClient: HttpClient, protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService) {
    super(httpClient, bloqueio);
  }

  pesquisarPaginado(filtroAuditoria: AuditoriaFilter, paginacao: Paginacao): Observable<ListaPaginada<AuditoriaModel>> {
    const params = new HttpParams().append('funcionalidade', filtroAuditoria.funcionalidade)
      .append('tipoOperacaoAuditoria', filtroAuditoria.tipoOperacaoAuditoria)
      .append('usuario', typeof filtroAuditoria.usuario !== 'undefined' ? MascaraUtil.removerMascara(filtroAuditoria.usuario) : '')
      .append('dataInicial', filtroAuditoria.dataInicial)
      .append('dataFinal', filtroAuditoria.dataFinal)
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/auditoria/pesquisar', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }
}
