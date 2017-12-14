import { Municipio } from './municipio.model';
import { TipoSanguineo } from 'app/modelo/enum/enum-tipo-sanguineo.model';
import { EnderecoTrabalhador } from 'app/modelo/endereco-trabalhador.model';
import { TelefoneTrabalhador } from 'app/modelo/telefone-trabalhador.model';
import { EmailTrabalhador } from 'app/modelo/email-trabalhador.model';
import { EstadoCivil } from 'app/modelo/enum/enum-estado-civil.model';
import { BrPdh } from 'app/modelo/enum/enum-br-pdh.model';
import { Escolaridade } from 'app/modelo/enum/enum-escolaridade.model';
import { FaixaSalarial } from 'app/modelo/enum/enum-faixa-salarial.model';
import { Raca } from 'app/modelo/enum/enum-raca.model';
import { SimNao } from 'app/modelo/enum/enum-simnao.model';
import { Profissao } from 'app/modelo/profissao.model';
import { Nacionalidade } from 'app/modelo/enum/enum-nacionalidade.model';
import { SituacaoTrabalhador } from 'app/modelo/enum/enum-situacao-trabalhador.model';
import { Pais } from 'app/modelo/pais.model';

export class Trabalhador {
    id: number;
    nome: string;
    nomePai: string;
    nomeMae: string;
    planoSaude: SimNao;
    automovel: SimNao;
    atividadeFisica: SimNao;
    exameRegular: SimNao;
    notificacao: SimNao;
    profissao: Profissao;
    tipoSanguineo: TipoSanguineo;
    nacionalidade: Nacionalidade;
    situacaoTrabalhador: SituacaoTrabalhador;
    dataNaturalizacao: string;
    dataEntradaPais: string;
    pais: Pais;
    municipio: Municipio;
    dataNascimento: string;
    cpf: string;
    rg: String;
    orgaoRg: string;
    genero: string;
    estadoCivil: EstadoCivil;
    brPdh: BrPdh;
    nit: string;
    ctps: string;
    serieCtps: string;
    ufCtps: string;
    faixaSalarial: FaixaSalarial;
    escolaridade: Escolaridade;
    raca: Raca;
    dataFalecimento: string;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;
    listaTelefoneTrabalhador: TelefoneTrabalhador[];
    listaEmailTrabalhador: EmailTrabalhador[];
    listaEnderecoTrabalhador: EnderecoTrabalhador[];
    termo: string;
    imagem: ByteString;
    tipoImagem: string;
    constructor() {
        this.listaTelefoneTrabalhador = new Array<TelefoneTrabalhador>();
        this.listaEmailTrabalhador = new Array<EmailTrabalhador>();
        this.listaEnderecoTrabalhador = new Array<EnderecoTrabalhador>();
    }
}
