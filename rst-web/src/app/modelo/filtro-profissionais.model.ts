export class FiltroProfissionais {
    cpf: string;
    registro: string;
    nome: string;
    situacao = '';
    idEstado: '';

    constructor(cpf?: string, registro?: string, nome?: string) {
        this.cpf = cpf;
        this.registro = registro;
        this.nome = nome;
    }

}
