import { UatQuadroPessoalTipoProfissional } from './uat-quadro-pessoal-tipo-profissional';
import { UnidadeAtendimentoTrabalhador } from './unid-atend-trabalhador.model';

export class UatQuadroPessoal {
    constructor(
        public id?: Number,
        public uatQuadroPessoalTipoProfissional?: UatQuadroPessoalTipoProfissional,
        public unidadeAtendimentoTrabalhador?: UnidadeAtendimentoTrabalhador,
        public quantidade?: Number,
    ) { }
}
