export class Segmento {

    id: number;
    codigo: string;
    descricao: string;
    segmento: Segmento;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;

    constructor(init?: Partial<Segmento>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
