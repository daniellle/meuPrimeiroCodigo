export class Cbo {

    id: number;
    codigo: string;
    descricao: string;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;

    constructor(init?: Partial<Cbo>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
