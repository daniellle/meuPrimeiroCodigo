import { UatInstalacaoFisicaAmbiente } from "./uat-instalacao-fisica-ambiente";
import { UnidadeAtendimentoTrabalhador } from "./unid-atend-trabalhador.model";
import { UatVeiculoTipo } from "./uat-veiculo-tipo";

export class UatVeiculoTipoAtendimento {

    public id: Number;
    public descricao: string;
    public uatVeiculoTipo = new UatVeiculoTipo()

    constructor() {
    }

}
