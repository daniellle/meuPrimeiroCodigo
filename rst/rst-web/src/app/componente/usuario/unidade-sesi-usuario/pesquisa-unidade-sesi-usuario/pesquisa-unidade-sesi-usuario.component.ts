import {PermissoesEnum} from 'app/modelo/enum/enum-permissoes';
import {Seguranca} from './../../../../compartilhado/utilitario/seguranca.model';
import {environment} from './../../../../../environments/environment';
import {UsuarioService} from './../../../../servico/usuario.service';
import {UsuarioPerfilSistema} from './../../../../modelo/usuario-perfil-sistema.model';
import {Usuario} from './../../../../modelo/usuario.model';
import {DepartRegionalService} from 'app/servico/depart-regional.service';
import {MensagemProperties} from './../../../../compartilhado/utilitario/recurso.pipe';
import {Component, OnInit, SimpleChanges} from '@angular/core';
import {Paginacao} from './../../../../modelo/paginacao.model';
import {UsuarioEntidadeService} from './../../../../servico/usuario-entidade.service';
import {UsuarioEntidade} from './../../../../modelo/usuario-entidade.model';
import {ListaPaginada} from './../../../../modelo/lista-paginada.model';
import {BloqueioService} from 'app/servico/bloqueio.service';
import {ToastyService} from 'ng2-toasty';
import {ActivatedRoute, Router} from '@angular/router';
import {FiltroUsuarioEntidade} from './../../../../modelo/filtro-usuario-entidade.model';
import {BaseComponent} from 'app/componente/base.component';
import {FiltroDepartRegional} from "../../../../modelo/filtro-depart-regional.model";
import {DepartamentoRegional} from "../../../../modelo/departamento-regional.model";

@Component({
  selector: 'app-pesquisa-unidade-sesi-usuario',
  templateUrl: './pesquisa-unidade-sesi-usuario.component.html',
  styleUrls: ['./pesquisa-unidade-sesi-usuario.component.scss'],
})
export class PesquisaUnidadeSESIUsuarioComponent extends BaseComponent implements OnInit {

  idUsuario: number;
  idDepRegional: number;
  filtro: FiltroUsuarioEntidade;
  filtroSelecionado: FiltroUsuarioEntidade;
  listaUsuarioEntidade: UsuarioEntidade[];
  usuario: Usuario;
  public departamentos: DepartamentoRegional[];
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
    this.buscarUsuario();
    this.tipoTela();
    this.buscarDepartamentos();
  }

  ngOnInit() {
    this.listaUsuarioEntidade = new Array<UsuarioEntidade>();
    this.filtro = new FiltroUsuarioEntidade();
    this.filtroSelecionado = new FiltroUsuarioEntidade();
  }

    ngOnChanges(changes: SimpleChanges) {
        if (changes['usuario']) {
            if(this.usuario.origemDados != null){
                this.isBarramento = true;
            }
        }
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
      this.carregarTabelaEmppresaUsuario();
      if (this.usuario && !this.usuario.perfisSistema) {
        this.usuario.perfisSistema = new Array<UsuarioPerfilSistema>();
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  buscarDepartamentos() {
      if (!this.modoConsulta) {
          this.departamentoService.listarTodos(new FiltroDepartRegional()).subscribe((dados: any) => {
              this.departamentos = dados;
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
    this.router.navigate([`${environment.path_raiz_cadastro}/usuario/${this.idUsuario}/unidadesesi/associar`]);
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
    this.usuarioEntidadeService.pesquisarPaginado(filtro, paginacao, MensagemProperties.app_rst_menu_uat).
      subscribe((retorno: ListaPaginada<UsuarioEntidade>) => {
        if (this.filtro.idEstado === '0') {
          this.filtro.idEstado = undefined;
        }
        if (retorno.quantidade > 0) {
          this.listaUsuarioEntidade = retorno.list;
          this.paginacao = this.getPaginacao(this.paginacao, retorno);
        }else {
            this.listaUsuarioEntidade = new Array<UsuarioEntidade>();
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  public removerUnidade(item: UsuarioEntidade) {
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
