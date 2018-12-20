import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../../environments/environment';
import { UsuarioService } from './../../../../servico/usuario.service';
import { UsuarioPerfilSistema } from './../../../../modelo/usuario-perfil-sistema.model';
import { Usuario } from './../../../../modelo/usuario.model';
import { DepartRegionalService } from 'app/servico/depart-regional.service';
import { MensagemProperties } from './../../../../compartilhado/utilitario/recurso.pipe';
import { Component } from '@angular/core';
import { Paginacao } from './../../../../modelo/paginacao.model';
import { UsuarioEntidadeService } from './../../../../servico/usuario-entidade.service';
import { UsuarioEntidade } from './../../../../modelo/usuario-entidade.model';
import { ListaPaginada } from './../../../../modelo/lista-paginada.model';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { Router, ActivatedRoute } from '@angular/router';
import { FiltroUsuarioEntidade } from './../../../../modelo/filtro-usuario-entidade.model';
import { OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';

@Component({
  selector: 'app-pesquisa-departamento-regional-usuario',
  templateUrl: './pesquisa-departamento-regional-usuario.component.html',
  styleUrls: ['./pesquisa-departamento-regional-usuario.component.scss'],
})
export class PesquisaDepartamentoUsuarioComponent extends BaseComponent implements OnInit {

  idUsuario: number;
  filtro: FiltroUsuarioEntidade;
  filtroSelecionado: FiltroUsuarioEntidade;
  listaUsuarioEntidade: UsuarioEntidade[];
  usuario: Usuario;
  public estados: any[];
  isBarramento: boolean;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private usuarioService: UsuarioService,
    private departamentoService: DepartRegionalService,
    private usuarioEntidadeService: UsuarioEntidadeService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
    this.buscarEstados();
    this.buscarUsuario();
    this.tipoTela();
  }

  ngOnInit() {
    this.listaUsuarioEntidade = new Array<UsuarioEntidade>();
    this.filtro = new FiltroUsuarioEntidade();
  }

  tipoTela() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.USUARIO_ENTIDADE, PermissoesEnum.USUARIO_ENTIDADE_ALTERAR,
        PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR,
        PermissoesEnum.USUARIO_ENTIDADE_DESATIVAR]);
  }

  buscarUsuario(): void {
    this.idUsuario = this.activatedRoute.snapshot.params['id'];
    this.usuarioService.buscarUsuarioById(this.idUsuario).subscribe((retorno: Usuario) => {
      this.usuario = retorno;
        if (retorno.clientId) {
            this.isBarramento = true;
        }
      this.carregarTabelaEmppresaUsuario();
      if (this.usuario && !this.usuario.perfisSistema) {
        this.usuario.perfisSistema = new Array<UsuarioPerfilSistema>();
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  buscarEstados() {
    if (!this.modoConsulta) {
      this.departamentoService.buscarEstados().subscribe((dados: any) => {
        this.estados = dados;
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  pesquisar(): void {
    this.paginacao.pagina = 1;
    this.filtroSelecionado = new FiltroUsuarioEntidade(this.filtro);
    this.pesquisarUsuarioEntidadePaginado(this.filtroSelecionado, this.paginacao);
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarUsuarioEntidadePaginado(this.filtroSelecionado, this.paginacao);
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/usuario/${this.idUsuario}`]);
  }

  novo(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/usuario/${this.idUsuario}/departamentoregional/associar`]);
  }

  private carregarTabelaEmppresaUsuario() {
    this.paginacao.pagina = 1;
    if (this.usuario) {
      this.filtro.cpf = this.usuario.login;
    }
    this.pesquisarUsuarioEntidadePaginado(this.filtro, this.paginacao);
  }

  private pesquisarUsuarioEntidadePaginado(filtro: FiltroUsuarioEntidade, paginacao: Paginacao): void {
    if (this.isUndefined(filtro.idEstado)) {
      filtro.idEstado = '0';
    }
    this.usuarioEntidadeService.pesquisarPaginado(filtro, paginacao, MensagemProperties.app_rst_labels_departamento_regional).
      subscribe((retorno: ListaPaginada<UsuarioEntidade>) => {
        if (this.filtro.idEstado === '0') {
          this.filtro.idEstado = undefined;
        }
        if (retorno.quantidade > 0) {
          this.listaUsuarioEntidade = retorno.list;
          this.paginacao = this.getPaginacao(this.paginacao, retorno);
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  public removerDepartamento(item: UsuarioEntidade) {
    this.usuarioEntidadeService.desativar(item).subscribe((response: UsuarioEntidade) => {
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.removerItem(item);
    }, (error) => {
      this.mensagemError(error);
    }, () => {
      this.pesquisar();
    });
  }

  public removerItem(item: UsuarioEntidade) {
    const index: number = this.listaUsuarioEntidade.indexOf(item);
    if (index !== -1) {
      this.listaUsuarioEntidade.splice(index, 1);
    }
  }
  hasPermissao() {
   return  super.hasPermissao(PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR) || super.hasPermissao(PermissoesEnum.USUARIO_ENTIDADE);
  }
}
