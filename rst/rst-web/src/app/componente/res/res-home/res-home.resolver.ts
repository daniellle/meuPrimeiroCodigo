import { Observable } from 'rxjs/Observable';
import { ResService } from './../../../servico/res-service.service';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable()
export class ResHomeResolve implements Resolve<any> {

    constructor(private service: ResService) {
    }

    resolve(router: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> {
        // const cpf = (Seguranca.getUsuario() as any).sub;
        // const chamadas: any[] = this.DADOS_BASICOS.map((dado) => this.service.buscarValorParaDado(dado,cpf,false)
        //     .catch((err) => Observable.of(null)));
        // chamadas.push(...this.DADOS_HISTORICOS.map((dado) => this.service.buscarValorParaDado(dado,cpf,false, 3)
        //     .catch((err) => Observable.of(null))));
        // chamadas.push(this.service.buscarPaciente());
        // return Observable.forkJoin(chamadas).map((result) => {
        //     return {
        //         altura: result[0],
        //         temperatura: result[1],
        //         circunferenciaAbdominal: result[2],
        //         imc: result[3],
        //         sistolica: result[4],
        //         diastolica: result[5],
        //         peso: result[6],
        //         paciente: result[7]
        //     };
        // });
        return Observable.empty();
    }
}
