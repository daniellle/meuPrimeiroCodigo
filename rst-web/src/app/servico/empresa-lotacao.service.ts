import { ListaPaginada } from './../modelo/lista-paginada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { FiltroEmpresa } from './../modelo/filtro-empresa.model';
import { EmpresaLotacao } from 'app/modelo/empresa-lotacao.model';
import { BaseService } from 'app/servico/base.service';
import { BloqueioService } from './bloqueio.service';
import { AutenticacaoService } from './autenticacao.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpResponse } from '@angular/common/http';
import { MensagemErro } from 'app/modelo/mensagem-erro.model';
import { FiltroLotacao } from '../modelo/filttro-lotacao.model';

@Injectable()
export class EmpresaLotacaoService extends BaseService<EmpresaLotacao> {

  constructor(
    protected httpClient: HttpClient, protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService) {
    super(httpClient, bloqueio);
  }

  desativarEmpresaLotacao(empresaLotacao: EmpresaLotacao): Observable<EmpresaLotacao> {
    return super.put('/v1/empresa-lotacao/desativar', empresaLotacao);
  }

  salvarLotacoes(list: EmpresaLotacao[]): Observable<EmpresaLotacao> {
    return super.post('/v1/empresa-lotacao/', list)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }

  pesquisarEmpresasLotacoes(filtro: FiltroEmpresa, paginacao: Paginacao): Observable<ListaPaginada<EmpresaLotacao>> {
    const params = new HttpParams().append('idEmpresa', filtro.id.toString())
    .append('idUnidadeObra', filtro.idUnidadeObra.toString())
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/empresa-lotacao/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  pesquisarEmpresasLotacoesTrabalhador(filtro: FiltroLotacao, paginacao: Paginacao): Observable<ListaPaginada<EmpresaLotacao>> {
    const params = new HttpParams().append('idEmpresa', filtro.idEmpresa.toString())
    .append(filtro.idCargo ? 'idCargo' : '',  filtro.idCargo ? filtro.idCargo.toString() : '')
    .append(filtro.idFuncao ? 'idFuncao' : '',  filtro.idFuncao ? filtro.idFuncao.toString() : '')
    .append( filtro.idJornada ? 'idJornada' : '', filtro.idJornada ? filtro.idJornada.toString() : '')
    .append(filtro.idSetor ? 'idSetor' : '', filtro.idSetor ? filtro.idSetor.toString() : '')
    .append(filtro.idUnidadeObra ? 'idUnidadeObra' : '', filtro.idUnidadeObra ? filtro.idUnidadeObra.toString() : '')
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/empresa-lotacao/paginado', params)  // esperando endereco correto
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

}
