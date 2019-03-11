import { UnidadeAtendimentoTrabalhador } from "./unid-atend-trabalhador.model";
import { UatEquipamentoTipo } from "./uat-equipamento-tipo";

export class UatEquipamento {

    public id: Number;
    public quantidade: Number;
    public uatEquipamentoTipo = new UatEquipamentoTipo()
    public unidadeAtendimentoTrabalhador =  new UnidadeAtendimentoTrabalhador()

    constructor() {
    }

}
