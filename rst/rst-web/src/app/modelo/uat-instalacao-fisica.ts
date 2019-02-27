import { UatInstalacaoFisicaAmbiente } from "./uat-instalacao-fisica-ambiente";
import { UnidadeAtendimentoTrabalhador } from "./unid-atend-trabalhador.model";

export class UatInstalacaoFisica {
    constructor(
        public id?: Number,
        public area?: Number,
        public quantidade?: Number,
        public uatInstalacaoFisicaAmbiente?: UatInstalacaoFisicaAmbiente,
        public unidadeAtendimentoTrabalhador?: UnidadeAtendimentoTrabalhador
    ) {

    }

}
