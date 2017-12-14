import { environment } from './../../../../../environments/environment.prod';
import { UsuarioPerfilSistema } from './../../../../modelo/usuario-perfil-sistema.model';
import { Usuario } from './../../../../modelo/usuario.model';
import { UsuarioService } from './../../../../servico/usuario.service';
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
  selector: 'app-pesquisa-sindicato-usuario',
  templateUrl: './pesquisa-sindicato-usuario.component.html',
  styleUrls: ['./pesquisa-sindicato-usuario.component.scss'],
})
export class PesquisaSindicatoUsuarioComponent extends BaseComponent implements OnInit {

  idUsuario: number;
  filtro: FiltroUsuarioEntidade;
  filtroSelecionado: FiltroUsuarioEntidade;
  listaUsuarioEntidade: UsuarioEntidade[];
  usuario: Usuario;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private usuarioService: UsuarioService,
    private usuarioEntidadeService: UsuarioEntidadeService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
    this.buscarUsuario();
  }

  ngOnInit() {
    this.listaUsuarioEntidade = new Array<UsuarioEntidade>();
    this.filtro = new FiltroUsuarioEntidade();
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
    this.router.navigate([`${environment.path_raiz_cadastro}/usuario/${this.idUsuario}/sindicato/associar`]);
  }

  private carregarTabelaEmppresaUsuario() {
    this.paginacao.pagina = 1;
    if (this.usuario) {
      this.filtro.cpf = this.usuario.login;
    }
    this.pesquisarUsuarioEntidadePaginado(this.filtro, this.paginacao);
  }

  private pesquisarUsuarioEntidadePaginado(filtro: FiltroUsuarioEntidade, paginacao: Paginacao): void {
    this.usuarioEntidadeService.pesquisarPaginado(filtro, paginacao, MensagemProperties.app_rst_sindicatos_title_menu).
      subscribe((retorno: ListaPaginada<UsuarioEntidade>) => {
        if (retorno.quantidade > 0) {
          this.listaUsuarioEntidade = retorno.list;
          this.paginacao = this.getPaginacao(this.paginacao, retorno);
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  public removerSindicato(item: UsuarioEntidade) {
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

}
