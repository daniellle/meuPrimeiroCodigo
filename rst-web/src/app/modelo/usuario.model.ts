import {DadosFilter} from './dados-filter.model';
import {UsuarioPerfilSistema} from './usuario-perfil-sistema.model';

export class Usuario {

  id: number;
  nome: string;
  email: string;
  permissoes: string[];
  login: string;
  senha: string;
  perfisSistema: UsuarioPerfilSistema[];
  papeis: string[];
  dados: DadosFilter;
  sub: string;
  apelido: string;
  exibirApelido: boolean;
  foto: ByteString;
  nivel: number;

  constructor(
    id?: number,
    nome?: string,
    email?: string,
    permissoes?: string[],
    login?: string,
    senha?: string,
    dados?: DadosFilter,
    apelido?: string,
    exibirApelido?: boolean,
    foto?: ByteString,
    nivel?: number,
  ) {

    this.id = id;
    this.nome = nome;
    this.email = email;
    this.permissoes = permissoes;
    this.login = login;
    this.senha = senha;
    this.perfisSistema = new Array<UsuarioPerfilSistema>();
    this.dados = new DadosFilter();
    this.apelido = apelido;
    this.exibirApelido = exibirApelido;
    this.foto = foto;
    this.nivel = nivel;
  }

}
