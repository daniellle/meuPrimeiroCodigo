import { Sindicato } from './sindicato.model';
import { RedeCredenciada } from './rede-credenciada.model';
import { Parceiro } from './parceiro.model';
import { DepartamentoRegional } from 'app/modelo/departamento-regional.model';
import { Empresa } from 'app/modelo/empresa.model';

export class UsuarioEntidade {
    id: number;
    nome: string;
    cpf: string;
    email: string;
    termo: string;
    empresa: Empresa;
    departamentoRegional: DepartamentoRegional;
    parceiro: Parceiro;
    redeCredenciada: RedeCredenciada;
    sindicato: Sindicato;
    perfil: string;

}
