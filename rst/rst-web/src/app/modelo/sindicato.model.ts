import { EnderecoSindicato } from './endereco-sindicato.model';
import { EmailSindicato } from './email-sindicato.model';
import { TelefoneSindicato } from './telefoneSindicato.model';
import { SimNao } from 'app/modelo/enum/enum-simnao.model';
import { Situacao } from 'app/modelo/enum/enum-situacao.model';
export class Sindicato {

    id: number;
    cnpj: string;
    razaoSocial: string;
    nomeFantasia: string;
    inscricaoMunicipal: string;
    inscricaoEstadual: string;
    sesmt: SimNao;
    cipa: SimNao;
    qtdMembrosCipa: number;
    designCipa: SimNao;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;
    dataDesativacao: string;
    situacao: Situacao;
    endereco: EnderecoSindicato[];
    email: EmailSindicato[];
    telefone: TelefoneSindicato[];

    constructor() {
        this.endereco = new Array<EnderecoSindicato>();
        this.email = new Array<EmailSindicato>();
        this.telefone = new Array<TelefoneSindicato>();
    }

}
