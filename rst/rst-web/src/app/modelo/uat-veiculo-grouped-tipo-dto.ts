import { UatVeiculoDTO } from "./uat-veiculo-dto";

export class UatVeiculoGroupedTipoDTO {

    public idTipo: Number;
    public descricaoTipo: string;
    public isTipoAtendimento: boolean;
    public veiculos = new Array<UatVeiculoDTO>();

    constructor() {
    }

}
