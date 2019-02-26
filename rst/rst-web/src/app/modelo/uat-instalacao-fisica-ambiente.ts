import { UatInstalacaoFisicaCategoria } from "./uat-instalacai-fisica-categoria";

export class UatInstalacaoFisicaAmbiente {
    constructor(
        private id?: Number,
        private descricao?: String,
        private instalacaoFisicaCategoria?: UatInstalacaoFisicaCategoria,
    ) {

    }
}
