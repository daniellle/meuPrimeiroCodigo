import { SolicitacaoEmail } from './solicitacao-email';
import { Usuario } from './usuario.model';
import { Trabalhador } from 'app/modelo/trabalhador.model';

export class PrimeiroAcesso {

    trabalhador: Trabalhador;
    usuario: Usuario;
    solicitacaoEmail: SolicitacaoEmail;

    constructor(trabalhador?: Trabalhador, usuario?: Usuario, solicitacaoEmail?: SolicitacaoEmail) {
        this.trabalhador = trabalhador;
        this.usuario = usuario;
        this.solicitacaoEmail = solicitacaoEmail;
    }
}
