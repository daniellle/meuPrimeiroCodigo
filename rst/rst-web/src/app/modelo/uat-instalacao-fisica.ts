import { UatInstalacaoFisicaAmbiente } from "./uat-instalacao-fisica-ambiente";
import { UnidadeAtendimentoTrabalhador } from "./unid-atend-trabalhador.model";

export class UatInstalacaoFisica {
    constructor(
        private id?: Number,
        private area?: Number,
        private quantidade?: Number,
        private uatInstalacaoFisicaAmbiente?: UatInstalacaoFisicaAmbiente,
        private unidadeAtendimentoTrabalhador?: UnidadeAtendimentoTrabalhador
    ) {

    }

}
