import { Sistema } from './sistema.model';
import { Perfil } from './perfil.model';

export class UsuarioPerfilSistema {

    perfil: Perfil;
    sistema: Sistema;

    constructor(perfil?: Perfil, sistema?: Sistema) {
        this.perfil = perfil;
        this.sistema = sistema;
    }

}
