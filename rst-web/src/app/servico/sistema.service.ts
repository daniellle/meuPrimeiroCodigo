import {Observable} from 'rxjs/Observable';
import {Sistema} from './../modelo/sistema.model';
import {AutenticacaoService} from './autenticacao.service';
import {BloqueioService} from './bloqueio.service';
import {HttpClient} from '@angular/common/http';
import {BaseService} from 'app/servico/base.service';
import {Injectable} from '@angular/core';
import {Usuario} from '../modelo/usuario.model';

@Injectable()
export class SistemaService extends BaseService<Sistema> {
    
    constructor(
        protected httpClient: HttpClient,
        protected bloqueioService: BloqueioService,
        protected autenticacaoService: AutenticacaoService
    ) {
        super(httpClient, bloqueioService);
    }
    
    buscarTodos(): Observable<Sistema[]> {
        return super.get('/v1/sistemas')
                    .map((response: Response) => {
                        return response;
                    }).catch((error: Response) => {
                return Observable.throw(error);
            });
    }
    
    buscarSistemasPermitidos(usuario: Usuario): Observable<Sistema[]> {
        
        if ( usuario == null ) {
            return Observable.throw('usuário nulo');
        }
        
        return super.get(`/v1/sistemas?login=${usuario.sub}`)
                    .map((response: Response) => {
                        return response;
                    }).catch((error: Response) => {
                return Observable.throw(error);
            });
    }
    
}
