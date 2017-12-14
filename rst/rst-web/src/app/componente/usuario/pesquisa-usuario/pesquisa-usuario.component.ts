import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { DepartRegionalService } from 'app/servico/depart-regional.service';
import { FiltroDepartRegional } from 'app/modelo/filtro-depart-regional.model';
import { DepartamentoRegional } from './../../../modelo/departamento-regional.model';
import { Empresa } from 'app/modelo/empresa.model';
import { PerfilService } from './../../../servico/perfil.service';
import { Perfil } from './../../../modelo/perfil.model';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { Router, ActivatedRoute } from '@angular/router';
import { BaseComponent } from './../../../componente/base.component';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

import { DialogService } from 'ng2-bootstrap-modal';
import { ToastyService } from 'ng2-toasty';

import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { UsuarioService } from './../../../servico/usuario.service';
import { FiltroUsuario } from './../../../modelo/filtro-usuario.model';
import { Usuario } from './../../../modelo/usuario.model';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { UsuarioEntidadeService } from 'app/servico/usuario-entidade.service';

@Component({
  selector: 'app-pesquisa-usuario',
  templateUrl: './pesquisa-usuario.component.html',
  styleUrls: ['./pesquisa-usuario.component.scss'],
})
export class PesquisaUsuarioComponent extends BaseComponent implements OnInit {

  public filtro: FiltroUsuario;
  public usuarios: Usuario[];
  public usuarioSelecionado: Usuario;
  public itensCarregados: number;
  public totalItens: number;
  public pesquisaUsuarioForm: FormGroup;
  public departamentos: DepartamentoRegional[];
  public perfis: Perfil[];
  public empresa: Empresa;
  public departamento: DepartamentoRegional;

  constructor(
    private router: Router,
    private usuarioService: UsuarioService,
    private formBuilder: FormBuilder,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private dialogService: DialogService,
    private activatedRoute: ActivatedRoute,
    private departamentoService: DepartRegionalService,
    private perfilService: PerfilService,
    private usuarioEntidadeService: UsuarioEntidadeService,
  ) {
    super(bloqueioService, dialogo);
    this.empresa = new Empresa();
    this.buscarDepartamentos();
    this.departamento = new DepartamentoRegional();
    this.buscarPerfis();
  }

  ngOnInit() {
    this.filtro = new FiltroUsuario();
    this.usuarios = new Array<Usuario>();
    this.title = MensagemProperties.app_rst_usuario_title_pesquisar;
    this.pesquisaUsuarioForm = this.formBuilder.group({});
    this.filtro.codigoPerfil = '';
  }

  buscarPerfis(): void {
    this.perfilService.buscarTodos().subscribe((retorno: any) => {
      this.perfis = retorno;
    });
  }

  public pesquisar() {
    if (this.verificarCampos()) {
      this.usuarios = new Array<Usuario>();
      this.usuarioSelecionado = null;
      this.paginacao.pagina = 1;
      this.usuarioService.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Usuario>) => {
        this.usuarios = retorno.list;
        this.paginacao = this.getPaginacao(this.paginacao, retorno);
        if (retorno.quantidade === 0) {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  buscarDepartamentos() {
    this.departamentoService.listarTodos(new FiltroDepartRegional()).subscribe((dados: any) => {
      this.departamentos = dados;
    });
  }

  public verificarCampos(): boolean {
    let verificador = true;
    if (this.filtrosEmBranco()) {
      this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
      verificador = false;
    }

    if (!this.isVazia(this.filtro.login)) {
      if (this.filtro.login.length < 14) {
        this.mensagemError(MensagemProperties.app_rst_labels_cpf_incompleto);
        verificador = false;
      }

    }
    return verificador;
  }

  private filtrosEmBranco(): boolean {
    return this.filtro && !this.filtro.login && !this.filtro.nome &&
      (this.filtro.empresa && !this.filtro.empresa.id) &&
      (this.filtro.departamentoRegional && !this.filtro.departamentoRegional.id) &&
      (!this.filtro.codigoPerfil || this.isUndefined(this.filtro.codigoPerfil));
  }

  public novo(): void {
    this.router.navigate(['cadastrar'], { relativeTo: this.activatedRoute });
  }

  selecionar(model: any) {
    if (model && model.id) {
      this.router.navigate([model.id], { relativeTo: this.activatedRoute });
    }
  }

  public isListaVazia(): boolean {
    return !(this.usuarios && this.usuarios.length > 0);
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.usuarioService.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Usuario>) => {
      this.usuarios = retorno.list;
    });
  }

  desativarUsuario(usuario: Usuario): void {
    this.usuarioService.desativar(usuario).subscribe((retorno: Usuario) => {
      this.mensagemSucesso(MensagemProperties.app_rst_usuario_desativar_sucesso);
      this.pesquisar();
    });
  }

  hasPermissaoInclusao() {
    return Seguranca.isPermitido([PermissoesEnum.USUARIO, PermissoesEnum.USUARIO_CADASTRAR]);
  }

  temPermissaoDesativar(): boolean {
    return Boolean(Seguranca.isPermitido([PermissoesEnum.USUARIO, PermissoesEnum.USUARIO_DESATIVAR]));
  }

  removerEmpresa() {
    this.filtro.empresa = new Empresa();
  }

  removerDepartamento() {
    this.filtro.departamentoRegional = new DepartamentoRegional();
  }

  adicionarEmpresa(event) {
    this.filtro.empresa = event;
  }

  adicionarDepartamentoRegional(event) {
    this.filtro.departamentoRegional = event;
  }
}
