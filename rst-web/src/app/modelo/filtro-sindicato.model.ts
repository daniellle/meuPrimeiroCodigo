export class FiltroSindicato {
    cnpj: string;
    razaoSocial: string;
    nomeFantasia: string;
    situacao = '';
    aplicarDadosFilter = true;

    constructor(cnpj?: string, razaoSocial?: string, nomeFantasia?: string, situacao?: string) {
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.situacao = situacao;
    }

}
