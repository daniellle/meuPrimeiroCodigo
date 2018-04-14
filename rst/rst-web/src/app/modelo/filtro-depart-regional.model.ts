export class FiltroDepartRegional {
    cnpj: string;
    razaoSocial: string;
    idEstado: string;
    situacao = '';
    aplicarDadosFilter = true;

    constructor() {
        this.idEstado = undefined;
    }
}
