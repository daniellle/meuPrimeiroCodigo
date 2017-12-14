import { ResService } from './../../../servico/res-service.service';
import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable()
export class ResHistoricoResolver implements Resolve<any> {

    DADOS_HISTORICOS = [
        'PRESSAO_SISTOLICA',
        'PRESSAO_DIASTOLICA',
        'PESO',
    ];

    DADOS_BASICOS = [
        'ALTURA',
        'TEMPERATURA',
        'CIRCUNFERENCIA_ABDOMINAL',
        'PESSOA_DEFICIENTE',
        'DEFICIENCIA',
        'RECOMENDACOES_EDUCATIVO_PREVENTIVA_OBSERVACOES',
        'RECOMENDACOES_EDUCATIVO_PREVENTIVA_ASO',
        'IMC',
    ];

    constructor(private service: ResService) {
    }

    resolve(router: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        return Observable.empty();

        //     let cpf = (Seguranca.getUsuario() as any).sub;
        // if(localStorage.getItem('trabalhador_cpf')){
        //     cpf = localStorage.getItem('trabalhador_cpf');
        //     localStorage.removeItem('trabalhador_cpf');
        // }

        // const chamadas: any[] = this.DADOS_BASICOS.map((dado) => this.service.buscarValorParaDado(dado,cpf,false)
        //     .catch((err) => Observable.of(null)));
        // chamadas.push(...this.DADOS_HISTORICOS.map((dado) => this.service.buscarValorParaDado(dado, cpf,false,3)
        //     .catch((err) => Observable.of(null))));
        // chamadas.push(this.service.buscarHistorico(cpf)
        //     .catch((err) => Observable.of(null)));
        // chamadas.push(this.service.buscarPaciente(cpf));
        // chamadas.push(this.service.buscaAlergias(cpf, 'ALERGIA')
        //     .catch((err) => Observable.of(null)));
        // chamadas.push(this.service.buscaMedicamentos(cpf,'MEDICAMENTO')
        //     .catch((err) => Observable.of(null)));
        // return Observable.forkJoin(chamadas).map((result) => {
        //     return {
        //         altura: result[0],
        //         temperatura: result[1],
        //         circunferenciaAbdominal: result[2],
        //         pessoaDeficiente: result[3],
        //         deficiencia: result[4],
        //         recomendacoesEducativoObservacoes: result[5],
        //         recomendacoesEducativoASO: result[6],
        //         imc: result[7],
        //         sistolica: result[8],
        //         diastolica: result[9],
        //         peso: result[10],
        //         encontrosMedicos: result[11],
        //         paciente: result[12],
        //         alergias: result[13],
        //         medicamentos: result[14]
        //     };
        // });
    }
}
