import { EnderecoParceiro } from './endereco-parceiro.model';
import { Segmento } from './segmento.model';
import { PorteEmpresa } from 'app/modelo/porte-empresa.model';
import { TipoEmpresa } from './tipo-empresa.model';
import { ParceiroEspecialidade } from './parceiro-especialidade.model';
import { TelefoneParceiro } from 'app/modelo/telefone-parceiro.model';
import { EmailParceiro } from 'app/modelo/email-parceiro.model';
export class Parceiro {

    id: Number;
    numeroCnpjCpf: string;
    nome: String;
    nomeFantasia: String;
    inscricaoMunicipal: String;
    inscricaoEstadual: String;
    dataNascimento: string;
    genero: string;
    numeroNit: String;
    url: String;
    porteEmpresa: PorteEmpresa;
    tipoEmpresa: TipoEmpresa;
    segmento: Segmento;
    nomeResponsavel: String;
    cargoResponsavel: String;
    numeroTelefoneResponsavel: String;
    numeroNitResponsavel: String;
    emailResponsavel: String;
    nomeContato: String;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;
    dataDesligamento: string;

    parceiroEspecialidades: ParceiroEspecialidade[];
    telefonesParceiro: TelefoneParceiro[];
    enderecosParceiro: EnderecoParceiro[];
    emailsParceiro: EmailParceiro[];

    constructor() {
        this.porteEmpresa = new PorteEmpresa();
        this.tipoEmpresa = new TipoEmpresa();
        this.segmento = new Segmento();
        this.parceiroEspecialidades = new Array<ParceiroEspecialidade>();
        this.telefonesParceiro = new Array<TelefoneParceiro>();
        this.enderecosParceiro = new Array<EnderecoParceiro>();
        this.emailsParceiro = new Array<EmailParceiro>();
    }
}
