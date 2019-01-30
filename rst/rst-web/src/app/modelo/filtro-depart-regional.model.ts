export class FiltroDepartRegional {
    cnpj: string;
    razaoSocial: string;
    idEstado: string;
    situacao = '';
    aplicarDadosFilter = true;
    perfil: string

    constructor() {
        this.idEstado = undefined;
    }
}
