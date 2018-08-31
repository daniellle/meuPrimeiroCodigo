import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { FiltroSetor } from './../modelo/filtro-setor.model';
import { MensagemErro } from 'app/modelo/mensagem-erro.model';
import { Observable } from 'rxjs/Observable';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { EmpresaSetor } from './../modelo/empresa-setor.model';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core';

@Injectable()
export class EmpresaSetorService extends BaseService<EmpresaSetor> {

  constructor(
    protected httpClient: HttpClient, protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService) {
    super(httpClient, bloqueio);
  }

  pesquisarSetoresPorEmpresa(filtro: FiltroSetor, paginacao: Paginacao): Observable<ListaPaginada<EmpresaSetor>> {
    const params = new HttpParams().append('idEmpresa', filtro.idEmpresa.toString())
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/empresa-setor/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
    }

  pesquisarEmpresasSetores(filtro: FiltroSetor, paginacao: Paginacao): Observable<ListaPaginada<EmpresaSetor>> {
    const params = new HttpParams().append('idEmpresa', filtro.idEmpresa.toString())
      .append('sigla', typeof filtro.sigla !== 'undefined' ? filtro.sigla.toString() : '')
      .append('descricao', typeof filtro.descricao !== 'undefined' ? filtro.descricao.toString() : '')
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/empresa-setor/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
    }

  desativarEmpresaSetor(empresaSetor: EmpresaSetor): Observable<EmpresaSetor> {
    return super.put('/v1/empresa-setor/desativar', empresaSetor)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }

  salvarSetores(id: number, list: EmpresaSetor[]): Observable<EmpresaSetor> {
    return super.post('/v1/empresa-setor/empresa/' + id, list)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }

}
