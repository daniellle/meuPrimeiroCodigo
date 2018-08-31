import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { HttpResponse } from '@angular/common/http';
import { FiltroSindicato } from './../modelo/filtro-sindicato.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { Paginacao } from '../modelo/paginacao.model';
import { BaseService } from 'app/servico/base.service';
import { Observable } from 'rxjs/Observable';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { MensagemErro } from '../modelo/mensagem-erro.model';
import { Sindicato } from '../modelo/sindicato.model';

@Injectable()
export class SindicatoService extends BaseService<Sindicato> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
        super(httpClient, bloqueio);
    }

    pesquisarAtivos(filtro: FiltroSindicato, paginacao: Paginacao): Observable<ListaPaginada<Sindicato>> {
        const params = new HttpParams().append('cnpj', typeof filtro.cnpj !== 'undefined' ? MascaraUtil.removerMascara(filtro.cnpj) : '')
        .append('razaoSocial', typeof filtro.razaoSocial !== 'undefined' ? filtro.razaoSocial : '')
        .append('nomeFantasia', typeof filtro.nomeFantasia !== 'undefined' ? filtro.nomeFantasia : '')
        .append('situacao', 'A')
        .append('aplicarDadosFilter', filtro.aplicarDadosFilter ? 'true' : 'false')
        .append('pagina', paginacao.pagina.toString())
        .append('qtdRegistro', paginacao.qtdRegistro.toString());
        return super.get('/v1/sindicatos/paginado', params)
        .map((response: ListaPaginada<Sindicato>) => {
            if (!response.list) { response.list = new Array<Sindicato>(); }
            return response;
          }).catch((error: Response) => {
            return Observable.throw(error);
          });
      }

    pesquisar(filtro: FiltroSindicato, paginacao: Paginacao): Observable<ListaPaginada<Sindicato>> {
        const params = new HttpParams().append('cnpj', typeof filtro.cnpj !== 'undefined' ? MascaraUtil.removerMascara(filtro.cnpj) : '')
        .append('razaoSocial', typeof filtro.razaoSocial !== 'undefined' ? filtro.razaoSocial : '')
        .append('nomeFantasia', typeof filtro.nomeFantasia !== 'undefined' ? filtro.nomeFantasia : '')
        .append('situacao', filtro.situacao)
        .append('aplicarDadosFilter', filtro.aplicarDadosFilter ? 'true' : 'false')
        .append('pagina', paginacao.pagina.toString())
        .append('qtdRegistro', paginacao.qtdRegistro.toString());
        return super.get('/v1/sindicatos/paginado', params)
        .map((response: ListaPaginada<Sindicato>) => {
            if (!response.list) { response.list = new Array<Sindicato>(); }
            return response;
          }).catch((error: Response) => {
            return Observable.throw(error);
          });
      }

    pesquisarPorId(id: string): Observable<Sindicato> {
            return super.get('/v1/sindicatos/' + id)
            .map((response: Response) => {
              return response;
            }).catch((error: Response) => {
              return Observable.throw(error);
            });
      }

      pesquisarPorCnpj(cnpj: string): Observable<Sindicato> {
        return super.get('/v1/sindicatos/verificar/' + cnpj)
        .map((response: Response) => {
          return response;
        }).catch((error: Response) => {
          return Observable.throw(error);
        });
  }

      salvar(sindicato: Sindicato): Observable<Sindicato> {
        this.bloqueio.evento.emit(false);
        if (typeof sindicato.id !== 'undefined') {
          return this.editar(sindicato);
        } else {
          return super.post('/v1/sindicatos', sindicato)
            .map((response: Response) => {
              return response;
            }).catch((response: HttpResponse<MensagemErro>) => {
              return Observable.throw(response);
            }).finally(() => {
              this.bloqueio.evento.emit(true);
            });
        }
      }

      editar(sindicato: Sindicato): Observable<Sindicato> {
        return super.put('/v1/sindicatos', sindicato)
          .map((response: Response) => {
            return response;
          }).catch((error: HttpResponse<MensagemErro>) => {
            return Observable.throw(error);
          }).finally(() => {
            this.bloqueio.evento.emit(true);
          });
      }

}
