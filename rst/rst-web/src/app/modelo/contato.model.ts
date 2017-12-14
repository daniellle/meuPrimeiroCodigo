import { Municipio } from 'app/modelo/municipio.model';
import { EstadoCivil } from 'app/modelo/enum/enum-estado-civil.model';

export class Contato {

    id: number;
    endereco: string;
    municipio: Municipio;
    bairro: string;
    cep: string;
    estadoCivil: EstadoCivil;
    email: string;
    telefone: string;
    celular: string;

    constructor() {
        this.municipio = new Municipio();
    }
}
