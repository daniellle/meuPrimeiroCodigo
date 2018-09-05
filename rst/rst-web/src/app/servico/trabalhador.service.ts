import { QuestionarioTrabalhador } from './../modelo/questionario-trabalhador';

import { QuestionarioTrabalhadorFilter } from './../modelo/filter-questionario-trabalhador-filter';
import { PrimeiroAcesso } from './../modelo/primeiro-acesso.model';
import { MensagemErro } from 'app/modelo/mensagem-erro.model';
import { MascaraUtil } from './../compartilhado/utilitario/mascara.util';
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { FiltroTrabalhador } from './../modelo/filtro-trabalhador.model';
import { Trabalhador } from './../modelo/trabalhador.model';
import { Injectable } from '@angular/core';
import { BaseService } from 'app/servico/base.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Paginacao } from 'app/modelo/paginacao.model';
import { Observable } from 'rxjs/Observable';
import { HttpParams } from '@angular/common/http';
@Injectable()
export class TrabalhadorService extends BaseService<Trabalhador> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  pesquisarPaginado(filtro: FiltroTrabalhador, paginacao: Paginacao): Observable<ListaPaginada<Trabalhador>> {

    const params = new HttpParams()
      .append('cpf', filtro.cpf ? MascaraUtil.removerMascara(filtro.cpf) : '')
      .append('nome', filtro.nome ? filtro.nome : '')
      .append('nit', filtro.nit ? MascaraUtil.removerMascara(filtro.nit) : '')
      .append('situacao', filtro.situacao ? filtro.situacao : '')
      .append('falecidos', filtro.falecidos ? 'true' : 'false')
      .append('aplicarDadosFilter', filtro.aplicarDadosFilter ? 'true' : 'false')
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString())
      .append('estado', typeof filtro.idEstado !== 'undefined' ? filtro.idEstado : '0');

    return super.get('/v1/trabalhadores/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  buscarPorId(filtro: FiltroTrabalhador): Observable<Trabalhador> {

    const params = new HttpParams()
      .append('id', filtro.id)
      .append('aplicarDadosFilter', filtro.aplicarDadosFilter ? 'true' : 'false')
      .append('fromMinhaConta', filtro.fromMinhaConta ? 'true' : 'false')
      .append('cpf', filtro.cpf);

    return super.get('/v1/trabalhadores/', params)
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  buscarMeusDados(): Observable<Trabalhador> {
    return super.get('/v1/trabalhadores/meus-dados')
      .map((response: Response) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  salvar(entity: Trabalhador): Observable<Trabalhador> {
    this.bloqueio.evento.emit(false);
    if (typeof entity.id !== 'undefined') {
      return this.editar(entity);
    } else {
      return super.post('/v1/trabalhadores', entity)
        .map((response: Response) => {
          return response;
        }).catch((response: HttpResponse<MensagemErro>) => {
          return Observable.throw(response);
        }).finally(() => {
          this.bloqueio.evento.emit(true);
        });
    }
  }

  salvarPrimeiroAcesso(entity: PrimeiroAcesso): Observable<PrimeiroAcesso> {
    this.bloqueio.evento.emit(false);
    return super.postPublic('/v1/trabalhador/primeiro-acesso', entity)
      .map((response: Response) => {
        return response;
      }).catch((response: HttpResponse<MensagemErro>) => {
        return Observable.throw(response);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }

  editar(entity: Trabalhador): Observable<Trabalhador> {
    return super.put('/v1/trabalhadores', entity)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }

  buscarTrabalhadorCpfDataNascimento(cpf: string, dataNascimento: string): Observable<Trabalhador> {
    const params = new HttpParams().append('cpf', cpf)
      .append('dataNascimento', dataNascimento);
    return super.getPublic('/v1/trabalhador/primeiro-acesso', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  questionarioPaginado(filtro: QuestionarioTrabalhadorFilter, paginacao: Paginacao): Observable<ListaPaginada<QuestionarioTrabalhador>> {

    const params = new HttpParams()
      .append('id', filtro.id.toString());
    return super.get('/v1/trabalhadores/historico/paginado', params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  buscarVacinasAlergiasMedicamentosAutoDeclarados(cpf: string): Observable<Trabalhador> {
    return super.get('/v1/trabalhadores/vacinas-alergias-medicamentos-auto-declarados/' + cpf)
        .map((response: Response) => {
            return response;
        }).catch((error: Response) => {
            return Observable.throw(error);
        });
    }

  buscarVidaAtivaTrabalhador(id: string): Observable<string> {
    return super.get('/v1/trabalhadores/vidaativa/' + id)
        .map((response: Response) => {
            return response;
        }).catch((error: Response) => {
            return Observable.throw(error);
        });
    }
}
