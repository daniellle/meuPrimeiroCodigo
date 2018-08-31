import { SimNao } from 'app/modelo/enum/enum-simnao.model';
import { Observable } from 'rxjs/Observable';
import { Paginacao } from './../modelo/paginacao.model';
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { BloqueioService } from './bloqueio.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BaseService } from 'app/servico/base.service';
import { Certificado } from './../modelo/certificado.model';
import { Injectable } from '@angular/core';

@Injectable()
export class CertificadoService extends BaseService<Certificado> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  buscarPorId(id: number): Observable<Certificado> {
    return super.get('/v1/certificados/' + id )
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  listarPaginado(id: number, paginacao: Paginacao, inclusaoTrabalhador: SimNao ): Observable<ListaPaginada<Certificado>> {
    const params = new HttpParams().append('idTrabalhador', id.toString())
    .append('inclusaoTrabalhador', inclusaoTrabalhador.toString().toUpperCase() === 'SIM' ? 'S' : 'N' )
    .append('pagina', paginacao.pagina.toString())
    .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/certificados/paginado', params)
    .map((response: ListaPaginada<Certificado>) => {
      if (!response.list) {
        response.list = new Array<Certificado>();
      }
      return response;
    }).catch((error: Response) => {
      return Observable.throw(error);
    });
  }

  salvar(certificado: Certificado): Observable<Certificado> {
    this.bloqueio.evento.emit(false);
    return super.post('/v1/certificados', certificado)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }

  desativar(certificado: Certificado): Observable<Certificado> {
    this.bloqueio.evento.emit(false);
    return super.put('/v1/certificados', certificado)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }
}
