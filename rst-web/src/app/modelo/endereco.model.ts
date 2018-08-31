import { Municipio } from 'app/modelo/municipio.model';

export class Endereco {
    id: number;
    municipio: Municipio;
    descricao: string;
    numero: number;
    complemento: string;
    cep: string;
    bairro: string;
    tipoEndereco: string;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;

    constructor(init?: Partial<Endereco>) {
        if (init) {
            Object.assign(this, init);
        }
        if (!this.municipio || (this.municipio && !this.municipio.id)) {
        this.municipio = new Municipio();
        }
    }
}
