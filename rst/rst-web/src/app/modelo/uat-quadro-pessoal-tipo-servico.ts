import { UatQuadroPessoalArea } from "./uat-quadro-pessoal-area";

export class UatQuadroPessoalTipoServico {
    constructor(
        public id?: Number,
        public descricao?: string,
        public uatQuadroPessoalArea?: UatQuadroPessoalArea
    ) { }
}
