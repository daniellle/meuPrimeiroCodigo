import { Observable } from 'rxjs/Observable';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { EmpresaService } from 'app/servico/empresa.service';
import { Injectable } from '@angular/core';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BaseService } from 'app/servico/base.service';
import { EmpresaSindicato } from './../modelo/empresa-sindicato.model';

@Injectable()
export class EmpresaSindicatoService extends BaseService<EmpresaSindicato> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
    private empresaService: EmpresaService,
  ) {
    super(httpClient, bloqueio);
  }

  pesquisarEmpresasSindicatos(id: number, paginacao: Paginacao): Observable<ListaPaginada<EmpresaSindicato>> {
    const params = new HttpParams().append('id', id.toString())
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/empresa-sindicatos/paginado', params)
      .map((response: ListaPaginada<EmpresaSindicato>) => {
        if (!response.list) { response.list = new Array<EmpresaSindicato>(); }
        return response;
      }).catch((error) => {
        const erro = error.error.mensagem ? error.error.mensagem : MensagemProperties.app_rst_erro_geral;
        return Observable.throw(erro);
      });
    }

  salvarEmpresaSindicato(idEmpresa: number, empresaSindicato: EmpresaSindicato): Observable<EmpresaSindicato> {
    return super.post(`/v1/empresa-sindicatos/${idEmpresa}`, empresaSindicato)
    .map((response: Response) => {
      return response;
    }).catch((error) => {
        return Observable.throw(error);
    });
  }

  removerEmpresaSindicato(empresaSindicato: EmpresaSindicato): Observable<EmpresaSindicato> {
    return super.put(`/v1/empresa-sindicatos/remover`, empresaSindicato)
    .map((response: Response) => {
      return response;
    }).catch((error) => {
      const erro = error.error.mensagem ? error.error.mensagem : MensagemProperties.app_rst_erro_geral;
      return Observable.throw(erro);
    }).finally(() => {
      this.bloqueio.evento.emit(true);
    });
  }

}
