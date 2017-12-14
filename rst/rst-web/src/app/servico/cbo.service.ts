
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Cbo } from './../modelo/cbo.model';
import { BaseService } from 'app/servico/base.service';
import { Injectable } from '@angular/core/';
import { Observable } from 'rxjs/Observable';
import { CboFilter } from '../modelo/filtro-cbo.model';
import { Paginacao } from 'app/modelo/paginacao.model';

@Injectable()
export class CboService extends BaseService<Cbo> {

    constructor(
      protected httpClient: HttpClient,
      protected bloqueio: BloqueioService,
      protected autenticacaoService: AutenticacaoService) {
        super(httpClient, bloqueio);
      }

      pesquisar(filtrocbo: CboFilter, paginacao: Paginacao): Observable<ListaPaginada<Cbo>> {
        const params = new HttpParams().append('codigo', typeof filtrocbo.codigo !== 'undefined' ? filtrocbo.codigo : '')
          .append('descricao', typeof filtrocbo.descricao !== 'undefined' ? filtrocbo.descricao : '')
          .append('idCbos', typeof filtrocbo.idCbos !== 'undefined' ? filtrocbo.idCbos : '')
          .append('pagina', paginacao.pagina.toString())
          .append('idEmpresa', filtrocbo.idEmpresa.toString())
          .append('idJornadas', typeof filtrocbo.idCbos !== 'undefined' ? filtrocbo.idCbos : '')
          .append('qtdRegistro', paginacao.qtdRegistro.toString());
        return super.get('/v1/cbo/paginado', params)
          .map((response: Response) => {
            return response;
          }).catch((error: Response) => {
            return Observable.throw(error);
          });
      }

}
