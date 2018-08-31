import { RespostaQuestionario } from './../modelo/resposta-questionario.model';
import { ResultadoQuestionarioDTO } from './../modelo/resultado-questionario-dto.model';
import { BaseService } from 'app/servico/base.service';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { BloqueioService } from './bloqueio.service';

@Injectable()
export class RespostaQuestionaioService extends BaseService<RespostaQuestionario> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueio);
  }

  buscarPorIdPerguntaQuestionario(id: number): Observable<RespostaQuestionario[]> {
    return super.get(`/v1/resposta/${id}`)
      .map((response: ResultadoQuestionarioDTO) => {
        return this.parseResponse(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  private parseResponse(response: ResultadoQuestionarioDTO): ResultadoQuestionarioDTO {
    if (response) {
      return response;
    }

    return new ResultadoQuestionarioDTO();
  }
}
