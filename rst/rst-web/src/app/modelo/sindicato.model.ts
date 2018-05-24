import { EnderecoSindicato } from './endereco-sindicato.model';
import { EmailSindicato } from './email-sindicato.model';
import { TelefoneSindicato } from './telefoneSindicato.model';
import { SimNao } from 'app/modelo/enum/enum-simnao.model';
import { Situacao } from 'app/modelo/enum/enum-situacao.model';
export class Sindicato {

    id: number;
    cd_siga: number;
    federacao: string;    
    sigla: string; 
    filiado: string;
    tipo_abrangencia: number;
    nome_presidente: string;
    sexo: string;
    home_page: string;
    nm_contato: string;
    area_contato: string;
    dt_fundacao: string;
    sede_sindicato_nacional: string;
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
