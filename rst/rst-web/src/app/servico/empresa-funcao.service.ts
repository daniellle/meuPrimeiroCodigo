import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { FiltroFuncao } from './../modelo/filtro-funcao.model';
import { MensagemErro } from 'app/modelo/mensagem-erro.model';
import { Observable } from 'rxjs/Observable';
import { EmpresaFuncao } from 'app/modelo/empresa-funcao.model';
import { BaseService } from 'app/servico/base.service';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable()
export class EmpresaFuncaoService extends BaseService<EmpresaFuncao> {

  constructor(
    protected httpClient: HttpClient, protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService) {
    super(httpClient, bloqueio);
  }

  pesquisarFuncoesPorEmpresa(filtro: FiltroFuncao, paginacao: Paginacao): Observable<ListaPaginada<EmpresaFuncao>> {
    const params = new HttpParams().append('idEmpresa', filtro.idEmpresa.toString())
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/empresa-funcao/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
    }

  pesquisarEmpresasFuncoes(filtro: FiltroFuncao, paginacao: Paginacao): Observable<ListaPaginada<EmpresaFuncao>> {
    const params = new HttpParams().append('idEmpresa', filtro.idEmpresa.toString())
      .append('codigo', typeof filtro.codigo !== 'undefined' ? filtro.codigo.toString() : '')
      .append('descricao', typeof filtro.descricao !== 'undefined' ? filtro.descricao.toString() : '')
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/empresa-funcao/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
    }

  desativarEmpresaFuncao(empresaFuncao: EmpresaFuncao): Observable<EmpresaFuncao> {
    return super.put('/v1/empresa-funcao/desativar', empresaFuncao)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }

  salvarFuncoes(id: number, list: EmpresaFuncao[]): Observable<EmpresaFuncao> {
    return super.post('/v1/empresa-funcao/empresa/' + id, list)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }
}
