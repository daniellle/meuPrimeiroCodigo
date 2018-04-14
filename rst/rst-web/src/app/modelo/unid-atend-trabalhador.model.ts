import { Municipio } from './municipio.model';
export class UnidadeAtendimentoTrabalhador {

    id: Number;
    departamentoRegional: Number;
    municipio: Municipio;
    razaoSocial: String;
    nomeFantasia = String;
    cnpj: String;
    endereco: String;
    bairro: String;
    cep: String;
    telefone: String;
    email: String;
    nomeResponsavel: String;
    dataAtivacao: Date;
    dataExclusao: Date;
    dataDesativacao: string;

    constructor(id?: Number, departamentoRegional?: Number, municipio?: Municipio,
                razaoSocial?: String, nomeFantasia = String, cnpj?: String, endereco?: String,
                bairro?: String, cep?: String, telefone?: String, email?: String,
                nomeResponsavel?: String, dataAtivacao?: Date, dataExclusao?: Date) {
            this.id = id;
            this.departamentoRegional = departamentoRegional;
            this.municipio = municipio;
            this.razaoSocial = razaoSocial;
            this.nomeFantasia = nomeFantasia;
            this.cnpj = cnpj;
            this.endereco = endereco;
            this.bairro = bairro;
            this.cep = cep;
            this.telefone = telefone;
            this.email = email;
            this.nomeResponsavel = nomeResponsavel;
            this.dataAtivacao = dataAtivacao;
            this.dataExclusao = dataExclusao;
        }
}
