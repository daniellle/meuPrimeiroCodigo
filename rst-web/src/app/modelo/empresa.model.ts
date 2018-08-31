import { EmpresaCnae } from './empresa-cnae';
import { Segmento } from 'app/modelo/segmento.model';
import { EmpresaFuncao } from './empresa-funcao.model';
import { EmpresaSindicato } from './empresa-sindicato.model';
import { TelefoneEmpresa } from 'app/modelo/telefone-empresa.model';
import { EmailEmpresa } from 'app/modelo/email-empresa.model';
import { EnderecoEmpresa } from 'app/modelo/endereco-empresa.model';
import { Situacao } from 'app/modelo/enum/enum-situacao.model';
import { SimNao } from 'app/modelo/enum/enum-simnao.model';
import { UnidadeObra } from 'app/modelo/unidade-obra.model';
import { EmpresaJornada } from './empresaJornada.model';
import { TipoEmpresa } from 'app/modelo/tipo-empresa.model';
import { PorteEmpresa } from 'app/modelo/porte-empresa.model';
import { EmpresaUnidadeAtendimentoTrabalhador } from 'app/modelo/empresa-unidade-atendimento-trabalhador.model';
import { RamoEmpresa } from 'app/modelo/ramo-empresa.model';

export class Empresa {
    id: number;
    ramoEmpresa: RamoEmpresa;
    cnpj: string;
    razaoSocial: string;
    nomeFantasia: String;
    nomeResponsavel: string;
    dataExclusao: Date;
    dataDesativacao: string;
    cargoResp: string;
    numeroTelefone: string;
    numeroNitResponsavel: string;
    segmento: Segmento;
    nomeContato: string;
    descricaCargoContato: string;
    numeroTelefoneContato: string;
    numeroNitContato: string;
    emailContato: string;
    numeroInscricaoMunicipal: string;
    numeroInscricaoEstadual: string;
    sesmt: SimNao;
    cipa: SimNao;
    matriz: SimNao;
    qtdMembrosCipa: number;
    emailResponsavel: string;
    designCipa: SimNao;
    tipoEmpresa: TipoEmpresa;
    porteEmpresa: PorteEmpresa;
    url: string;
    situacao: Situacao;

    enderecosEmpresa: EnderecoEmpresa[];
    emailsEmpresa: EmailEmpresa[];
    telefoneEmpresa: TelefoneEmpresa[];
    empresaJornada: EmpresaJornada[];
    empresaUats: EmpresaUnidadeAtendimentoTrabalhador[];
    listaEmpresaSindicato: EmpresaSindicato[];
    listaEmpresaFuncao: EmpresaFuncao[];
    empresaCnaes: EmpresaCnae[];
    unidadeObra: UnidadeObra[];

    constructor() {
        this.tipoEmpresa = new TipoEmpresa();
        this.porteEmpresa = new PorteEmpresa();
        this.enderecosEmpresa = new Array<EnderecoEmpresa>();
        this.emailsEmpresa = new Array<EmailEmpresa>();
        this.telefoneEmpresa = new Array<TelefoneEmpresa>();
        this.unidadeObra = new Array<UnidadeObra>();
        this.empresaCnaes = new Array<EmpresaCnae>();
        this.empresaUats = new Array<EmpresaUnidadeAtendimentoTrabalhador>();
    }
}
