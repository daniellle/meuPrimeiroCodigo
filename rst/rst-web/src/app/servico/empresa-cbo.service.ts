import { CboFilter } from './../modelo/filtro-cbo.model';
import { EmpresaCbo } from './../modelo/empresa-cbo.model';
import { HttpResponse } from '@angular/common/http';
import { MensagemErro } from 'app/modelo/mensagem-erro.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { BloqueioService } from './bloqueio.service';
import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { AutenticacaoService } from './autenticacao.service';
import { Observable } from 'rxjs/Observable';
import { Paginacao } from 'app/modelo/paginacao.model';

@Injectable()
export class EmpresaCboService extends BaseService<EmpresaCbo> {

    constructor(
        protected httpClient: HttpClient, protected bloqueio: BloqueioService,
        protected autenticacaoService: AutenticacaoService) {
        super(httpClient, bloqueio);
      }

      pesquisarPorId(id: number): Observable<EmpresaCbo> {
        return super.get('/v1/empresa-cbo/' + id)
          .map((response: Response) => {
            return response;
          }).catch((error: Response) => {
            return Observable.throw(error);
          });
      }

      pesquisarPorEmpresa(filtro: CboFilter, paginacao: Paginacao): Observable<ListaPaginada<EmpresaCbo>> {
        const params = new HttpParams().append('idEmpresa', filtro.idEmpresa.toString())
            .append('pagina', paginacao.pagina.toString())
            .append('qtdRegistro', paginacao.qtdRegistro.toString());
        return super.get('/v1/empresa-cbo/paginado', params)
          .map((response: Response) => {
            return response;
          }).catch((error: Response) => {
            return Observable.throw(error);
          });
        }

      pesquisar(filtro: CboFilter, paginacao: Paginacao): Observable<ListaPaginada<EmpresaCbo>> {
        const params = new HttpParams().append('idEmpresa', filtro.idEmpresa.toString())
          .append('codigo', typeof filtro.codigo !== 'undefined' ? filtro.codigo.toString() : '')
          .append('descricao', typeof filtro.descricao !== 'undefined' ? filtro.descricao.toString() : '')
          .append('pagina', paginacao.pagina.toString())
          .append('qtdRegistro', paginacao.qtdRegistro.toString());
        return super.get('/v1/empresa-cbo/paginado', params)
          .map((response: Response) => {
            return response;
          }).catch((error: Response) => {
            return Observable.throw(error);
          });
        }

        desativarEmpresaCbo(empresaCbo: EmpresaCbo): Observable<EmpresaCbo> {
          return super.put('/v1/empresa-cbo/desativar', empresaCbo)
            .map((response: Response) => {
              return response;
            }).catch((error: HttpResponse<MensagemErro>) => {
              return Observable.throw(error);
            }).finally(() => {
              this.bloqueio.evento.emit(true);
            });
        }

        salvarCbos(id: number, list: EmpresaCbo[]): Observable<EmpresaCbo[]> {
          return super.post('/v1/empresa-cbo/empresa/' + id , list)
            .map((response: Response) => {
              return response;
            }).catch((error) => {
              return Observable.throw(error);
            });
        }
}
