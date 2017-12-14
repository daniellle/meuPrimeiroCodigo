import { environment } from './../../../../../environments/environment';
import { SimNao } from 'app/modelo/enum/enum-simnao.model';
import { EnumValues } from 'enum-values';
import { UsuarioPerfilSistema } from './../../../../modelo/usuario-perfil-sistema.model';
import { UsuarioService } from './../../../../servico/usuario.service';
import { UsuarioEntidade } from './../../../../modelo/usuario-entidade.model';
import { UsuarioEntidadeService } from './../../../../servico/usuario-entidade.service';
import { IHash } from './../../../empresa/empresa-jornada/empresa-jornada.component';
import { MascaraUtil } from './../../../../compartilhado/utilitario/mascara.util';
import { Empresa } from './../../../../modelo/empresa.model';
import { FiltroEmpresa } from 'app/modelo/filtro-empresa.model';
import { Usuario } from './../../../../modelo/usuario.model';
import { BloqueioService } from './../../../../servico/bloqueio.service';
import { EmpresaService } from 'app/servico/empresa.service';
import { ListaPaginada } from './../../../../modelo/lista-paginada.model';
import { MensagemProperties } from './../../../../compartilhado/utilitario/recurso.pipe';
import { Paginacao } from 'app/modelo/paginacao.model';
import { Router, ActivatedRoute } from '@angular/router';
import { OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { Component } from '@angular/core';
import { ToastyService } from 'ng2-toasty';

@Component({
  selector: 'app-cadastro-empresa-usuario',
  templateUrl: './cadastro-empresa-usuario.component.html',
  styleUrls: ['./cadastro-empresa-usuario.component.scss'],
})
export class CadastroEmpresaUsuarioComponent extends BaseComponent implements OnInit {

  idUsuario: number;
  idUsuarioTrabalhador: number;
  usuario: Usuario;
  filtro: FiltroEmpresa;
  listaEmpresa: Empresa[];
  listaSelecionados: Empresa[];
  paginacao: Paginacao = new Paginacao(1, 10);
  public checks: IHash = {};

  constructor(
    private router: Router,
    private usuarioService: UsuarioService,
    private empresaService: EmpresaService,
    private activatedRoute: ActivatedRoute,
    private usuarioEntidadeService: UsuarioEntidadeService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
    this.buscarUsuario();
  }

  ngOnInit() {
    this.listaSelecionados = new Array<Empresa>();
    this.filtro = new FiltroEmpresa();
    this.listaEmpresa = new Array<Empresa>();
  }

  buscarUsuario(): void {
    this.idUsuario = this.activatedRoute.snapshot.params['id'];
    this.usuarioService.buscarUsuarioById(this.idUsuario).subscribe((retorno: Usuario) => {
      this.usuario = retorno;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  pesquisar(): void {
    if (this.validarPesquisa()) {
      this.paginacao.pagina = 1;
      this.filtro.cnpj = MascaraUtil.removerMascara(this.filtro.cnpj);
      this.pesquisarEmpresaPaginado();
    }
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarEmpresaPaginado();
  }

  limpar() {
    if (this.idUsuarioTrabalhador) {
      this.router.navigate([`${environment.path_raiz_cadastro}/usuario/${this.idUsuario}/empresa`]);
    }
  }

  selecionar(selecionado: Empresa, event: any, i: number) {
    if (selecionado && event.checked) {
      this.listaSelecionados.push(selecionado);
      this.checks[i] = true;
    } else {
      const index: number = this.listaSelecionados.indexOf(selecionado);
      if (index !== -1) {
        this.listaSelecionados.splice(index, 1);
        this.checks[i] = false;
      }
    }
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/usuario/${this.idUsuario}/empresa`]);
  }

  adicionarUsuarioEntidade() {
    this.salvar(this.prepareSave());
  }

  private prepareSave(): UsuarioEntidade[] {
    const lista = new Array<UsuarioEntidade>();
    this.listaSelecionados.forEach((item) => {
      const usuarioEntidade = new UsuarioEntidade();
      usuarioEntidade.nome = this.usuario.nome;
      usuarioEntidade.cpf = this.usuario.login;
      usuarioEntidade.email = this.usuario.email;
      usuarioEntidade.termo = EnumValues.getNameFromValue(SimNao, SimNao.true);
      usuarioEntidade.empresa = item;
      lista.push(usuarioEntidade);
    });
    return lista;
  }

  private salvar(lista: UsuarioEntidade[]): void {
    if (this.validarSelecao()) {
      this.usuarioEntidadeService.salvar(lista).subscribe((response: UsuarioEntidade) => {
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.limpar();
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  validarSelecao() {
    if (this.listaUndefinedOuVazia(this.listaSelecionados)) {
      this.mensagemError(MensagemProperties.app_rst_selecione_um_item);
      return false;
    }
    return true;
  }

  private validarPesquisa(): boolean {
    let verificador = true;

    if (!this.filtro.cnpj && !this.filtro.razaoSocial && !this.filtro.nomeFantasia) {
      this.mensagemErroComParametrosModel('app_rst_msg_pesquisar_todos_vazios');
      verificador = false;
    }

    if (!this.isVazia(this.filtro.cnpj)) {
      if (this.filtro.cnpj.length < 18) {
        this.mensagemError(MensagemProperties.app_rst_labels_cnpj_incompleto);
        verificador = false;
      }
    }
    return verificador;
  }

  private pesquisarEmpresaPaginado(): void {
    this.empresaService.pesquisar(this.filtro, this.paginacao).
      subscribe((retorno: ListaPaginada<Empresa>) => {
        if (retorno.list && retorno.list.length > 0) {
          this.listaEmpresa = retorno.list;
          this.paginacao = this.getPaginacao(this.paginacao, retorno);
        } else {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

}
