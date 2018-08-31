import { HttpParams } from '@angular/common/http';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Paginacao } from './../modelo/paginacao.model';
import { Jornada } from './../modelo/jornada.model';
import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { HttpClient } from '@angular/common/http';
import { BloqueioService } from './bloqueio.service';
import { JornadaFilter } from '../modelo/filtro-jornada.model';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class JornadaService extends BaseService<Jornada> {

    constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
        super(httpClient, bloqueio);
      }

      pesquisarPorId(id: number): Observable<Jornada> {
        return super.get('/v1/jornada/' + id)
          .map((response: Response) => {
            return response;
          }).catch((error: Response) => {
            return Observable.throw(error);
          });
      }

      pesquisar(filtroJornada: JornadaFilter, paginacao: Paginacao): Observable<ListaPaginada<Jornada>> {
        const params = new HttpParams().append('turno', typeof filtroJornada.turno !== 'undefined' ? filtroJornada.turno : '')
          .append('qtdHoras', typeof filtroJornada.qtdHoras !== 'undefined' ? filtroJornada.qtdHoras : '')
          .append('pagina', paginacao.pagina.toString())
          .append('idEmpresa', filtroJornada.idEmpresa.toString())
          .append('idJornadas', typeof filtroJornada.idJornadas !== 'undefined' ? filtroJornada.idJornadas : '')
          .append('qtdRegistro', paginacao.qtdRegistro.toString());
        return super.get('/v1/jornada/paginado', params)
          .map((response: Response) => {
            return response;
          }).catch((error: Response) => {
            return Observable.throw(error);
          });
      }

}
