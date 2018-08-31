import { TrabalhadorDependente } from 'app/modelo/trabalhador-dependente.model';
import { MensagemErro } from 'app/modelo/mensagem-erro.model';
import { Observable } from 'rxjs/Observable';
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { Paginacao } from './../modelo/paginacao.model';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { HttpResponse } from '@angular/common/http';

@Injectable()
export class TrabalhadorDependenteService extends BaseService<TrabalhadorDependente> {

  constructor(
    protected httpClient: HttpClient, protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService) {
    super(httpClient, bloqueio);
  }

  buscarPorId(id: number): Observable<TrabalhadorDependente[]> {
    return super.get('/v1/trabalhadores/' + id + '/dependente')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  pesquisarTrabalhadorDependente(id: number, paginacao: Paginacao): Observable<ListaPaginada<TrabalhadorDependente>> {
    const params = new HttpParams().append('id', id.toString())
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/trabalhadores/dependentes/paginado', params)
      .map((response: ListaPaginada<TrabalhadorDependente>) => {
        if (!response.list) {
          response.list = new Array<TrabalhadorDependente>();
        }
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  buscarPorCpf(cpf: string, idTrabalhador: number): Observable<TrabalhadorDependente> {
    const params = new HttpParams().append('cpf', cpf).append('idTrabalhador', idTrabalhador.toString());
    return super.get('/v1/trabalhadores/dependente', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  salvar(trabalhadorDependente: TrabalhadorDependente, id: number): Observable<TrabalhadorDependente> {
    this.bloqueio.evento.emit(false);
    return super.post('/v1/trabalhadores/' + id + '/dependente', trabalhadorDependente)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }

  desativarTrabalhadorDependente(trabalhadorDependente: TrabalhadorDependente): Observable<TrabalhadorDependente> {
    this.bloqueio.evento.emit(false);
    return super.put('/v1/trabalhadores/dependente/desativar', trabalhadorDependente)
      .map((response: Response) => {
        return response;
      }).catch((response: HttpResponse<MensagemErro>) => {
        return Observable.throw(response);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }
}
