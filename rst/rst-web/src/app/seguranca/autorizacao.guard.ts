import {ToastOptions, ToastyService} from 'ng2-toasty';
import {MensagemProperties} from 'app/compartilhado/utilitario/recurso.pipe';
import {
    ActivatedRouteSnapshot,
    CanActivate,
    CanActivateChild,
    CanLoad,
    Route,
    Router,
    RouterStateSnapshot
} from '@angular/router';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Seguranca} from '../compartilhado/utilitario/seguranca.model';
import {AutenticacaoService} from 'app/servico/autenticacao.service';

@Injectable()
export class AutorizacaoGuard implements CanLoad, CanActivate, CanActivateChild {
    
    constructor(private router: Router, private service: AutenticacaoService, private dialog: ToastyService) {
        
    }
    
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean>
        | Promise<boolean>
        | boolean {
        
        if ( Seguranca.isAutenticado() && this.verificarPermissoes(route) ) {
            return true;
        }
        
        if ( !Seguranca.isAutenticado() ) {
            AutenticacaoService.sair();
        }
        
        if ( !this.verificarPermissoes(route) ) {
            this.router.navigate(['/acessonegado']);
        }
        
        return false;
    }
    
    canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean>
        | Promise<boolean>
        | boolean {
        return this.canActivate(route, state);
    }
    
    canLoad(route: Route): Observable<boolean> | Promise<boolean> | boolean {
        if ( Seguranca.isAutenticado() === null || !Seguranca.isAutenticado() ) {
            this.service.autenticar().subscribe((autenticado: boolean) => {
                if ( autenticado ) {
                    const verificadoPermissoes: boolean = this.verificarPermissoes(route);
                    if ( !verificadoPermissoes ) {
                        this.router.navigate(['/acessonegado']);
                    }
                    return verificadoPermissoes;
                }
            }, (error) => {
                this.router.navigate(['/acessonegado']);
            });
        } else {
            const autorizado: boolean = this.verificarPermissoes(route);
            if ( !autorizado ) {
                this.router.navigate(['/acessonegado']);
            }
            return autorizado;
        }
    }
    
    verificarPermissoes(route: any): any {
        let permissoes: string[];
        if ( route.data && route.data['permissoes'] ) {
            permissoes = route.data['permissoes'] as string[];
        }
        return permissoes == null || Seguranca.isPermitido(permissoes);
    }
    
    private mensagemNaoAutorizado(mensagem: string) {
        const configuracoes: ToastOptions = {
            title: '',
            timeout: 5000,
            msg: mensagem,
            showClose: true,
            theme: 'bootstrap'
        };
        this.dialog.error(configuracoes);
    }
    
    private handlingError(error) {
        if ( !error.error ) {
            return MensagemProperties.app_rst_operacao_nao_realizada;
        } else {
            const errorString = JSON.stringify(error.error);
            let errorObject = JSON.parse(errorString);
            if ( typeof errorObject === 'string' ) {
                errorObject = JSON.parse(errorObject);
            }
            return errorObject.error_description;
        }
    }
    
}
