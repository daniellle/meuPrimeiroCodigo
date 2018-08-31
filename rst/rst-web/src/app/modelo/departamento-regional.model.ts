
import { EnderecoDepartamentoRegional } from 'app/modelo/endereco-departamento-regional.model';
import { TelefoneDepartamentoRegional } from 'app/modelo/telefone-departamento-regional.model';
import { EmailDepartamentoRegional } from 'app/modelo/email-departamento-regional.model';

export class DepartamentoRegional {
    id: number;
    cnpj: string;
    siglaDR: string;
    nomeFantasia: string;
    razaoSocial: string;
    nomeResponsavel: string;
    listaEndDepRegional: EnderecoDepartamentoRegional[];
    listaTelDepRegional: TelefoneDepartamentoRegional[];
    listaEmailDepRegional: EmailDepartamentoRegional[];
    dataDesativacao: string;
    dataExclusao: string;

    constructor () {
        this.listaEndDepRegional = new Array<EnderecoDepartamentoRegional>();
        this.listaTelDepRegional = new Array<TelefoneDepartamentoRegional>();
        this.listaEmailDepRegional = new Array<EmailDepartamentoRegional>();
    }
}
