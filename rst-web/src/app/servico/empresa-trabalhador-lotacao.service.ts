import { MensagemProperties } from './../compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from './../modelo/lista-paginada.model';
import { HttpResponse, HttpParams } from '@angular/common/http';
import { BloqueioService } from './bloqueio.service';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EmpresaTrabalhadorLotacao } from '../modelo/empresa-trabalhador-lotacao.model';
import { Observable } from 'rxjs/Observable';
import { BaseService } from './base.service';
import { MensagemErro } from '../modelo/mensagem-erro.model';
import { Paginacao } from 'app/modelo/paginacao.model';

@Injectable()
export class EmpresaTrabalhadorLotacaoService extends BaseService<EmpresaTrabalhadorLotacao> {

  constructor(
    protected httpClient: HttpClient,
    protected bloqueio: BloqueioService,
    protected autenticacaoService: AutenticacaoService,
  ) {
    super(httpClient, bloqueio);
  }

  salvarEmpresaTrabalhadorLotacao(empresaTrabalhadorLotacao: EmpresaTrabalhadorLotacao): Observable<EmpresaTrabalhadorLotacao> {
    return super.post('/v1/empresa-trabalhador-lotacoes', empresaTrabalhadorLotacao)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }

  pesquisarEmpresaTrabalhadorLotacao(
    id: number, idEmpresa: number, paginacao: Paginacao): Observable<ListaPaginada<EmpresaTrabalhadorLotacao>> {
    const params = new HttpParams()
      .append('id', id.toString())
      .append('idEmpresa', idEmpresa ? idEmpresa.toString() : '')
      .append('pagina', paginacao.pagina.toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/empresa-trabalhador-lotacoes/paginado', params)
      .map((response: ListaPaginada<EmpresaTrabalhadorLotacao>) => {
        if (!response.list) { response.list = new Array<EmpresaTrabalhadorLotacao>(); }
        return response;
      }).catch((error) => {
        const erro = error.error.mensagem ? error.error.mensagem : MensagemProperties.app_rst_erro_geral;
        return Observable.throw(erro);
      });
  }

  desativarEmpresaTrabalhadorLotacao(empresaTrabalhadorLotacao: EmpresaTrabalhadorLotacao): Observable<EmpresaTrabalhadorLotacao> {
    return super.put('/v1/empresa-trabalhador-lotacoes/remover', empresaTrabalhadorLotacao)
      .map((response: Response) => {
        return response;
      }).catch((error: HttpResponse<MensagemErro>) => {
        return Observable.throw(error);
      }).finally(() => {
        this.bloqueio.evento.emit(true);
      });
  }

}
