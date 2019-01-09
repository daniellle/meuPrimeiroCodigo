import { Seguranca } from './../compartilhado/utilitario/seguranca.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { BaseService } from 'app/servico/base.service';
import { Observable } from 'rxjs/Observable';
import { Cluster, Element, Event, History, ItemList, TypedJSON, DvDate, DvText } from '@ezvida/adl-core';
import { maxBy } from 'lodash';
import { O_NONBLOCK } from 'constants';
import { Imunizacao } from '../modelo/imunizacao.model';
import { ValueSetItem } from '@ezvida/adl-core/am/ValueSetItem';

@Injectable()
export class ResService extends BaseService<any> {

    private API = '/v1/saude';

    constructor(protected httpClient: HttpClient,
        protected bloqueio: BloqueioService,
        protected autenticacaoService: AutenticacaoService) {
        super(httpClient, bloqueio);
    }

    getMidia(...params: any[]): Observable<any> {
        return null;
    }

    buscarPaciente(cpf?: string) {
        if (!cpf) {
            const usuario = Seguranca.getUsuario() as any;
            cpf = usuario.sub;
        }

        return this.get(`${this.API}/paciente/${encodeURIComponent(cpf)}`);
    }

    buscarImunizacao(dado: string[], cpf: string): Observable<any> {
        const usuario = Seguranca.getUsuario() as any;
        let parametros = new HttpParams().set('cpf', cpf || usuario.sub);

        dado.forEach((d: string) => parametros = parametros.append('informacao', d));
        return this.get(`${this.API}/informacao/historico/`, parametros)
            .map((resultado: History) => {
                if (resultado) {
                    const history = TypedJSON.parse(JSON.stringify(resultado), History);
                    const lista: any[] = [];
                    if (history.events) {
                        history.events.map((event: Event) => {
                            let objeto = {
                                data: null,
                                informacao: null,
                            };
                            (<ItemList>event.data).items.map((elemento: Element) => {
                                if (elemento.name.value.toString() == 'Tipo de imunobiológico') {
                                   objeto.informacao = elemento.value;
                                }else{
                                    objeto.data = elemento.value;
                                }
                            });
                            lista.push(objeto);
                        });
                    }
                    return lista;
                }
            }).catch((error) => {
                //console.log(error);
                return Observable.empty();
            });
    }




    buscarHistoricoParaInformacaoSaude(dado: string, cpf: string): Observable<any> {
        const usuario = Seguranca.getUsuario() as any;
        let parametros = new HttpParams().set('cpf', cpf || usuario.sub);
        parametros = parametros.append('informacao', encodeURIComponent(dado));
        return this.get(`${this.API}/informacao/historico/`, parametros)
            .map((resultado: History) => {
                if (resultado) {
                    const history = TypedJSON.parse(JSON.stringify(resultado), History);

                    const lista: any[] = [];
                    if (history.events) {
                        history.events.map((event: Event) => {
                            (<ItemList>event.data).items.map((elemento: Element) => {
                                lista.push({
                                    data: event.time.toDate(),
                                    informacao: elemento.value
                                });
                            });
                        });
                    }
                    return lista;
                }
            }).catch((error) => {
                //console.log(error);
                return Observable.empty();
            });
    }

    buscarValorParaInformacaoSaude(dado: string | string[], cpf: string): Observable<any> {
        const usuario = Seguranca.getUsuario() as any;
        let parametros = new HttpParams().set('cpf', cpf || usuario.sub);
        if (dado instanceof Array) {
            dado.forEach((d: string) => parametros = parametros.append('informacao', d));
        } else {
            parametros = parametros.append('informacao', encodeURIComponent(dado));
        }
        return this.get(`${this.API}/informacao`, parametros)
            .map((resultado: Cluster) => {
                if (resultado) {
                    const elemento: Element = <Element>resultado.items[0];
                    return elemento;
                }

                return null;
            }).catch((erro) => {
               //console.error(erro);
                return null;
            });
    }


    buscarEncontro(encontroId: string): Observable<any> {
        return this.get(`${this.API}/fichaMedica/${encontroId}`).catch((erro) => {
            //console.log(erro);
            return null;
        });
    }

    buscarHistorico(paciente: any, aPartirDe?: Date, pagina = 0): Observable<{
        filtrarInformacoes: boolean,
        resultado: { max: number; result: any[]; }
    }> {
        if (!aPartirDe) {
            aPartirDe = new Date(0);
        }
        const params = new HttpParams().set('de', aPartirDe.toString()).set('cpf', paciente).set('pagina',
            pagina.toString());
        return this.get(`${this.API}/fichaMedica`, params);
    }

    buscaAlergias(cpf: string, dado: string): Observable<any> {
        return this.buscarHistoricoParaInformacaoSaude(dado, cpf);
    }

    buscaMedicamentos(cpf: string, dado: string): Observable<any> {
        return this.buscarHistoricoParaInformacaoSaude(dado, cpf);
    }

    buscaVacinas(cpf: string, dado: string): Observable<any> {
        return this.buscarHistoricoParaInformacaoSaude(dado, cpf);
    }

}
