import { UatEquipamentoArea } from "./uat-equipamento-area";

export class UatEquipamentoTipo {

    public id: Number;
    public descricao: string;
    public uatEquipamentoArea = new UatEquipamentoArea();

    constructor() {
    }

}
