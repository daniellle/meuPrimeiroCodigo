import { ResultadoQuestionarioDTO } from './../modelo/resultado-questionario-dto.model';
import { Resposta } from './../modelo/resposta.model';
import { BaseService } from 'app/servico/base.service';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { BloqueioService } from './bloqueio.service';

@Injectable()
export class RespostaQuestionarioTrabalhadorService extends BaseService<Resposta> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueio);
  }

  buscarPorId(id: number): Observable<ResultadoQuestionarioDTO> {
    return super.get(`/v1/respostas-questionario/${id}`)
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
