import { UatProdutoServico } from './uat-produto-servico.model';
import { EnderecoUnidAtendTrabalhador } from 'app/modelo/endereco-unid-atend-trabalhador.model';
import { EmailUnidAtendTrabalhador } from 'app/modelo/email-unid-atend-trabalhador.model';
import { TelefoneUnidAtendTrabalhador } from 'app/modelo/telefone-unid-atend-trabalhador.model';
import { DepartamentoRegional } from 'app/modelo/departamento-regional.model';

export class Uat {
    id: number;
    cnpj: string;
    razaoSocial: string;
    nomeFantasia: string;
    nomeResponsavel: string;
    departamentoRegional: DepartamentoRegional;
    endereco: EnderecoUnidAtendTrabalhador[];
    email: EmailUnidAtendTrabalhador[];
    telefone: TelefoneUnidAtendTrabalhador[];
    dataAtivacao: Date;
    dataExclusao: Date;
    dataDesativacao: string;

    uatProdutoServico: UatProdutoServico[];
    constructor() {
        this.endereco = new Array<EnderecoUnidAtendTrabalhador>();
        this.email = new Array<EmailUnidAtendTrabalhador>();
        this.telefone = new Array<TelefoneUnidAtendTrabalhador>();
    }
}
