import { HttpClient, HttpParams } from '@angular/common/http';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { Injectable } from '@angular/core';
import { BaseService } from 'app/servico/base.service';
import { Vacina } from 'app/modelo/vacina.model';
import { Observable } from 'rxjs/Observable';
import { FiltroVacina } from '../modelo/filtro-vacina.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import {ProximaDoseDTO} from "../modelo/proximaDoseDTO";
import {QueryBindingType} from "@angular/core/src/view";

@Injectable()
export class ImunizacaoService extends BaseService<Vacina> {

  constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService) {
    super(httpClient, bloqueio);
  }

  salvar(vacina: Vacina): Observable<Vacina> {
    return super.post('/v1/vacinas-autodeclaradas/', vacina)
      .map((response: Vacina) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  atualizar(vacina: Vacina): Observable<Vacina> {
    return super.put('/v1/vacinas-autodeclaradas/', vacina)
      .map((response: Vacina) => {
        return response;
      }).catch((error) => {
        return Observable.throw(error);
      });
  }

  pesquisar(filtro: FiltroVacina, paginacao: Paginacao): Observable<ListaPaginada<Vacina>> {
    const params = new HttpParams().append('nome', filtro.nome)
      .append('periodo', filtro.codPeriodo)
      .append('pagina', (paginacao.pagina - 1).toString())
      .append('qtdRegistro', paginacao.qtdRegistro.toString());
    return super.get('/v1/vacinas-autodeclaradas/historico', params)
      .map((response: Response) => {
        console.log(response);
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }
  buscarVacinas(): Observable<Vacina[]> {
    return super.get('/v1/vacinas-autodeclaradas/')
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  buscarVacinaPorId(id: number): Observable<Vacina> {
    return super.get('/v1/vacinas-autodeclaradas/' + id)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  remover(id: number): Observable<Vacina> {
    return super.delete('/v1/vacinas-autodeclaradas/' + id)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  buscaVacinasAutodeclaradas(pesquisa: number, cpf: string) : Observable<Vacina[]>{
      const params = new HttpParams()
          .append('login', cpf);
    return super.get('/v1/vacinas-autodeclaradas/ultimas-vacinas/' + pesquisa, params)
      .map((response: Response) => {
        return response;
      }).catch((error: Response) => {
        return Observable.throw(error);
      });
  }

  ultimasProximasDoses(): Observable<ProximaDoseDTO[]>{
      return super.get('/v1/vacinas-autodeclaradas/ultimas-proximas-doses')
          .map((response: Response) => {
            //console.log(response);
              return response;
          }).catch((error: Response) => {
              return Observable.throw(error);
          })
  }

  proximasDosesDoMes(mes: number, ano: number) : Observable<ProximaDoseDTO[]>{
      return super.get('/v1/vacinas-autodeclaradas/mensal/'+ mes + '/' + ano)
          .map((response: Response) => {
              //console.log(response);
              return response;
          }).catch((error: Response) => {
              return Observable.throw(error);
          })
  }


}
