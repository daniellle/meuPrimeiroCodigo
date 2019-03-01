import { UatQuadroPessoalTipoServico } from './uat-quadro-pessoal-tipo-servico';

export class UatQuadroPessoalTipoProfissional {
    constructor(
        public id?: Number,
        public uatQuadroPessoalTipoServico?: UatQuadroPessoalTipoServico,
        public descricao?: string,
    ) { }
}
