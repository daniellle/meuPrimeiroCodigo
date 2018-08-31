import { TipoEmpresa } from 'app/modelo/tipo-empresa.model';
import { PorteEmpresa } from 'app/modelo/porte-empresa.model';
import { Segmento } from 'app/modelo/segmento.model';
import { TelefoneRedeCredenciada } from 'app/modelo/telefone-rede-credenciada.model';
import { EnderecoRedeCredenciada } from 'app/modelo/endereco-rede-credenciada.model';
import { EmailRedeCredenciada } from 'app/modelo/email-rede-credenciada.model';

export class RedeCredenciada {
    id: number;
    numeroCnpj: string;
    razaoSocial: string;
    nomeFantasia: string;
    inscricaoEstadual: string;
    inscricaoMunicipal: string;
    url: string;
    tipoEmpresa: TipoEmpresa;
    porteEmpresa: PorteEmpresa;
    nomeResponsavel: string;
    cargoResponsavel: string;
    numeroNitResponsavel: string;
    numeroTelefoneResponsavel: string;
    emailResponsavel: string;
    dataExclusao: Date;
    dataDesligamento: string;
    segmento: Segmento;
    situacao: string;
    dataCriacao: Date;
    dataAlteracao: Date;
    emailsRedeCredenciada: EmailRedeCredenciada[];
    enderecosRedeCredenciada: EnderecoRedeCredenciada[];
    telefonesRedeCredenciada: TelefoneRedeCredenciada[];

    constructor() {
        this.tipoEmpresa = new TipoEmpresa();
        this.porteEmpresa = new PorteEmpresa();
        this.emailsRedeCredenciada = new Array<EmailRedeCredenciada>();
        this.enderecosRedeCredenciada = new Array<EnderecoRedeCredenciada>();
        this.telefonesRedeCredenciada = new Array<TelefoneRedeCredenciada>();
    }
}
