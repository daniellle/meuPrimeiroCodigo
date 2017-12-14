import { Estado } from 'app/modelo/estado.model';
import { ProfissionalEspecialidade } from './profissional-especialidade.model';
import { UatProfissional } from './unid-atend-trabalhador-profissional.model';
import { EnderecoProfissional } from './endereco-profissional.model';
import { EmailProfissional } from './email-profissional.model';
import { TelefoneProfissional } from './telefone-profissional.model';
import { TipoProfissional } from 'app/modelo/enum/enum-tipo-profissional';
import { Situacao } from 'app/modelo/enum/enum-situacao.model';

export class Profissional {
    id: number;
    nome: string;
    registro: string;
    dataNascimento: string;
    genero: string;
    rg: String;
    cpf: String;
    nit: string;
    estado: Estado;
    tipoProfissional: TipoProfissional;
    dataCriacao: Date;
    dataAlteracao: Date;
    dataExclusao: Date;
    situacao: Situacao;
    listaEnderecoProfissional: EnderecoProfissional[];
    listaEmailProfissional: EmailProfissional[];
    listaTelefoneProfissional: TelefoneProfissional[];
    listaProfissionalEspecialidade: ProfissionalEspecialidade[];
    listaUnidadeAtendimentoTrabalhadorProfissional: UatProfissional[];

    constructor() {
        this.listaEmailProfissional = new Array<EmailProfissional>();
        this.listaTelefoneProfissional = new Array<TelefoneProfissional>();
        this.listaProfissionalEspecialidade = new Array<ProfissionalEspecialidade>();
        this.listaUnidadeAtendimentoTrabalhadorProfissional = new Array<UatProfissional>();
    }
}
