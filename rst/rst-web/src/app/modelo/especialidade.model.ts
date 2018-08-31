export class Especialidade {

    id: Number;
    descricao: string;
    dataExclusao: Date;

    constructor(id?: Number, descricao?: string, dataExclusao?: Date) {
        this.id = id;
        this.descricao = descricao;
        this.dataExclusao = dataExclusao;
    }

}
