import { Injectable } from '@angular/core';
import { BaseService } from 'app/servico/base.service';
import { Estado } from 'app/modelo/estado.model';
import { HttpClient } from '@angular/common/http';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { Municipio } from 'app/modelo/municipio.model';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class EstadoService extends BaseService<Estado> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  pesquisarMunicipiosPorEstado(idEstado: number): Observable<Municipio[]> {
    return super.get('/v1/municipios/estados/' + idEstado.toString())
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  buscarEstados(): Observable<Estado[]> {
    return super.get('/v1/estados')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

}
