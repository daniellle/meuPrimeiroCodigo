import { QuestionarioDTO } from './../modelo/questionario-dto.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Observable } from 'rxjs/Observable';
import { Paginacao } from 'app/modelo/paginacao.model';
import { QuestionarioFilter } from './../modelo/filtro-questionario';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Questionario } from './../modelo/questionario.model';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core/';

@Injectable()
export class MontarQuestionarioService extends BaseService<QuestionarioDTO> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueio);
  }

  buscarQuestionario(): Observable<QuestionarioDTO> {
    return super.get(`/v1/montar-questionario/`)
      .map((response: QuestionarioDTO) => {
        return this.parseResponse(response);
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  private parseResponse(response: QuestionarioDTO): QuestionarioDTO {
    if (response) {
      return response;
    }

    return new QuestionarioDTO();
  }
}
