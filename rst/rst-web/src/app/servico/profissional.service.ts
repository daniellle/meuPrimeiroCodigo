import { MensagemErro } from 'app/modelo/mensagem-erro.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { FiltroProfissionais } from '../modelo/filtro-profissionais.model';
import { Paginacao } from '../modelo/paginacao.model';
import { Profissional } from '../modelo/profissional.model';
import { BaseService } from 'app/servico/base.service';
import { Observable } from 'rxjs/Observable';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';

@Injectable()
export class ProfissionalService extends BaseService<Profissional> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
        super(httpClient, bloqueio);
    }

  pesquisar(filtro: FiltroProfissionais, paginacao: Paginacao): Observable<ListaPaginada<Profissional>> {
    const params = new HttpParams().append('cpf', typeof filtro.cpf !== 'undefined' ? filtro.cpf : '')
    .append('registro', typeof filtro.registro !== 'undefined' ? filtro.registro : '')
    .append('nome', typeof filtro.nome !== 'undefined' ? filtro.nome : '')
    .append('statusProfissional', filtro.situacao)
    .append('pagina', paginacao.pagina.toString())
    .append('qtdRegistro', paginacao.qtdRegistro.toString())
    .append('estado', typeof filtro.idEstado !== 'undefined' ? filtro.idEstado : '0');

    return super.get('/v1/profissionais/paginado', params)
    .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  pesquisarPorId(id: string): Observable<Profissional> {
    return super.get('/v1/profissionais/' + id)
    .map((response: Response) => {
      return response;
    }).catch((error: Response) => {
      return Observable.throw(error);
    });

  }

  salvar(entity: Profissional): Observable<Profissional> {
    this.bloqueio.evento.emit(false);
    if (typeof entity.id !== 'undefined') {
      return this.editar(entity);
    } else {
      return super.post('/v1/profissionais', entity)
        .map((response: Response) => {
          return response;
        }).catch((response: HttpResponse<MensagemErro>) => {
          return Observable.throw(response);
        }).finally(() => {
          this.bloqueio.evento.emit(true);
        });
    }
  }

  editar(entity: Profissional): Observable<Profissional> {
    return super.put('/v1/profissionais', entity)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }
}
