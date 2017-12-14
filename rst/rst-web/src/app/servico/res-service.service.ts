import { Seguranca } from './../compartilhado/utilitario/seguranca.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { AutenticacaoService } from 'app/servico/autenticacao.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { BaseService } from 'app/servico/base.service';
import { Observable } from 'rxjs/Observable';
import { Path } from '@ezvida/adl-core';

@Injectable()
export class ResService extends BaseService<any> {

    private API = '/v1/res';

    constructor(protected httpClient: HttpClient, protected bloqueio: BloqueioService, protected autenticacaoService: AutenticacaoService) {
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

    buscarValorParaDado(
        dado: string,
        cpf: string,
        todos: boolean,
        quantidade?: number,
        de?: Date, ate?: Date): Observable<Array<{ data: string, informacao: Path }> | Path> {
        const usuario = Seguranca.getUsuario() as any;

        let parametros = new HttpParams().set('cpf', cpf || usuario.sub);

        if (de || ate) {
            if (de) {
                parametros = parametros.set('de', de.toDateString());
            }
            if (ate) {
                parametros = parametros.set('ate', ate.toDateString());
            }
        }

        if (quantidade) {
            parametros = parametros.set('quantidade', quantidade ? quantidade.toString() : '3');
        }

        if (todos) {
            parametros = parametros.set('todos', 'true');
        }

        return this.get(`${this.API}/registro/${encodeURIComponent(dado)}`, parametros)
            .map((resultado: { max: number; result: any[] }) => {
                if (resultado) {
                    if (!quantidade && !todos) {
                        return resultado.result[0].dataValue;
                    } else {
                        return resultado.result.map((elem) => {
                            return { data: elem.date, informacao: elem.dataValue };
                        });
                    }
                }
            })
            .catch((erro) => {
                console.error(erro);
                return null;
            });
    }

    buscarEncontro(encontroId: string): Observable<any> {
        return this.get(`${this.API}/encontro/${encontroId}`).catch((erro) => {
            console.log(erro);
            return null;
        });
    }

    buscarHistorico(paciente: any, aPartirDe?: Date, pagina = 0): Observable<{
        filtrarInformacoes: boolean,
        resultado: {max: number; result: any[]; }
    }> {
        if (!aPartirDe) {
            aPartirDe = new Date(0);
        }
        const params = new HttpParams().set('de', aPartirDe.toString()).set('cpf', paciente).set('pagina', pagina.toString());
        return this.get(`${this.API}/historico`, params);
    }

    buscaAlergias(cpf: string, dado: string): Observable<any> {
        return this.buscarValorParaDado(dado, cpf, true);
    }

    buscaMedicamentos(cpf: string, dado: string): Observable<any> {
        return this.buscarValorParaDado(dado, cpf, true);
    }

    buscaVacinas(cpf: string, dado: string): Observable<any> {
        return this.buscarValorParaDado(dado, cpf, false);
    }

}
