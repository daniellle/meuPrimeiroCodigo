export class Jornada {

    id: number;
    descricao: string;
    qtdHoras: number;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;

    constructor(init?: Partial<Jornada>) {
        if (init) {
            Object.assign(this, init);
        }
    }

}
