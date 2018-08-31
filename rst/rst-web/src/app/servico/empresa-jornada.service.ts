import { HttpResponse } from '@angular/common/http';
import { MensagemErro } from 'app/modelo/mensagem-erro.model';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { EmpresaJornada } from './../modelo/empresaJornada.model';
import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { Observable } from 'rxjs/Observable';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { FiltroEmpresa } from '../modelo/filtro-empresa.model';
import { Paginacao } from 'app/modelo/paginacao.model';

@Injectable()
export class EmpresaJornadaService extends BaseService<EmpresaJornada> {

    constructor(
        protected httpClient: HttpClient, protected bloqueio: BloqueioService,
        protected autenticacaoService: AutenticacaoService) {
        super(httpClient, bloqueio);
      }

      pesquisarPorId(id: number): Observable<EmpresaJornada> {
        return super.get('/v1/empresa-jornada/' + id)
          .map((response: Response) => {
            return response;
          }).catch((error: Response) => {
            return Observable.throw(error);
          });
      }

      pesquisar(filtro: FiltroEmpresa, paginacao: Paginacao): Observable<ListaPaginada<EmpresaJornada>> {
        const params = new HttpParams().append('id', filtro.id.toString())
          .append('pagina', paginacao.pagina.toString())
          .append('qtdRegistro', paginacao.qtdRegistro.toString());
        return super.get('/v1/empresa-jornada/paginado', params)
          .map((response: Response) => {
            return response;
          }).catch((error: Response) => {
            return Observable.throw(error);
          });
        }

        desativarEmpresaJornada(empresaJornada: EmpresaJornada): Observable<EmpresaJornada> {
            return super.put('/v1/empresa-jornada/desativar', empresaJornada)
              .map((response: Response) => {
                return response;
              }).catch((error: HttpResponse<MensagemErro>) => {
                return Observable.throw(error);
              }).finally(() => {
                this.bloqueio.evento.emit(true);
              });
          }

}
