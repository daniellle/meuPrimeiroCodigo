import { UatEquipamentoArea } from "./uat-equipamento-area";
import { UatEquipamentoDTO } from "./uat-equipamento-dto";

export class UatEquipamentoGroupedAreaDTO {

    public area = new UatEquipamentoArea();
    public equipamentos = new Array<UatEquipamentoDTO>();

    constructor() {
    }

}
