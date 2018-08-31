export class Dependente {
    id: number;
    nome: string;
    cpf: string;
    dataNascimento: string;
    genero: string;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;
    dataFimDependencia: Date;

    constructor(init?: Partial<Dependente>) {
        if (init) {
            Object.assign(this, init);
        }
    }
}
