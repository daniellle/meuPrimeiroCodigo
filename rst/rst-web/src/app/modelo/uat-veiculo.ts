import { UatInstalacaoFisicaAmbiente } from "./uat-instalacao-fisica-ambiente";
import { UnidadeAtendimentoTrabalhador } from "./unid-atend-trabalhador.model";
import { UatVeiculoTipoAtendimento } from "./uat-veiculo-tipo-atendimento";

export class UatVeiculo {

    public id: Number;
    public quantidade: Number;
    public uatVeiculoTipoAtendimento = new UatVeiculoTipoAtendimento()
    public unidadeAtendimentoTrabalhador =  new UnidadeAtendimentoTrabalhador()

    constructor() {
    }

}
