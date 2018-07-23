import { ProximaDoseVacina } from "./proximaDoseVacina.model";

export class Vacina {
    id: number;
    cpf: string;
    nome: string;
    lote: string;
    local: string;
    outrasDoses: boolean;
    dataVacinacao: Date;
    dataProximaDose: Date;
    listaProximasDoses: ProximaDoseVacina[];

    constructor() {
    }
}