import { Profissional } from './profissional.model';
import { UnidadeAtendimentoTrabalhador } from './unid-atend-trabalhador.model';
export class UatProfissional {

    id: number;
    profissional: Profissional;
    unidadeAtendimentoTrabalhador: UnidadeAtendimentoTrabalhador;

    constructor( id?: number, profissional?: Profissional, unidadeAtendimentoTrabalhador?: UnidadeAtendimentoTrabalhador) {
        this.id = id;
        this.profissional = profissional;
        this.unidadeAtendimentoTrabalhador = unidadeAtendimentoTrabalhador;
    }

}
