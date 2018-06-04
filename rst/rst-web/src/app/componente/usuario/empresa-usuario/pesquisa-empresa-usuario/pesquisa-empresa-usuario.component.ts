import {MascaraUtil} from './../../../../compartilhado/utilitario/mascara.util';
import {PermissoesEnum} from 'app/modelo/enum/enum-permissoes';
import {Seguranca} from './../../../../compartilhado/utilitario/seguranca.model';
import {EmpresaTrabalhador} from './../../../../modelo/empresa-trabalhador.model';
import {EmpresaTrabalhadorService} from './../../../../servico/empresa-trabalhador.service';
import {PerfilEnum} from 'app/modelo/enum/enum-perfil';
import {environment} from './../../../../../environments/environment';
import {UsuarioPerfilSistema} from './../../../../modelo/usuario-perfil-sistema.model';
import {Usuario} from './../../../../modelo/usuario.model';
import {UsuarioService} from './../../../../servico/usuario.service';
import {MensagemProperties} from 'app/compartilhado/utilitario/recurso.pipe';
import {Component} from '@angular/core';
import {Paginacao} from './../../../../modelo/paginacao.model';
import {UsuarioEntidadeService} from './../../../../servico/usuario-entidade.service';
import {UsuarioEntidade} from './../../../../modelo/usuario-entidade.model';
import {ListaPaginada} from './../../../../modelo/lista-paginada.model';
import {BloqueioService} from 'app/servico/bloqueio.service';
import {ToastyService} from 'ng2-toasty';
import {Router, ActivatedRoute} from '@angular/router';
import {FiltroUsuarioEntidade} from './../../../../modelo/filtro-usuario-entidade.model';
import {OnInit} from '@angular/core';
import {BaseComponent} from 'app/componente/base.component';
import {Empresa} from "../../../../modelo/empresa.model";

@Component({
    selector: 'app-pesquisa-empresa-usuario',
    templateUrl: './pesquisa-empresa-usuario.component.html',
    styleUrls: ['./pesquisa-empresa-usuario.component.scss'],
})
export class PesquisaEmpresaUsuarioComponent extends BaseComponent implements OnInit {

    idUsuario: number;
    filtro: FiltroUsuarioEntidade;
    filtroSelecionado: FiltroUsuarioEntidade;
    listaUsuarioEntidade: UsuarioEntidade[];
    usuario: Usuario;
    listaEmpresasTrabalhador: EmpresaTrabalhador[];
    hasProfissionalSaude: Boolean;

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private usuarioService: UsuarioService,
        private empresaTrabalhadorService: EmpresaTrabalhadorService,
        private usuarioEntidadeService: UsuarioEntidadeService,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
    ) {
        super(bloqueioService, dialogo);
        this.buscarUsuario();
        this.tipoTela();
    }

    ngOnInit() {
        this.listaUsuarioEntidade = new Array<UsuarioEntidade>();
        this.listaEmpresasTrabalhador = new Array<EmpresaTrabalhador>();
        this.filtro = new FiltroUsuarioEntidade();
        this.hasProfissionalSaude = false;
    }

    tipoTela() {
        this.modoConsulta = !Seguranca.isPermitido(
            [PermissoesEnum.USUARIO_ENTIDADE, PermissoesEnum.USUARIO_ENTIDADE_ALTERAR,
                PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR,
                PermissoesEnum.USUARIO_ENTIDADE_DESATIVAR]);
    }

    pesquisar(): void {
        this.paginacao.pagina = 1;
        this.filtroSelecionado = new FiltroUsuarioEntidade(this.filtro);
        this.pesquisarUsuarioEntidadePaginado(this.filtroSelecionado, this.paginacao);
    }

    pageChanged(event: any): void {
        this.paginacao.pagina = event.page;
        this.filtroSelecionado = new FiltroUsuarioEntidade(this.filtro);
        this.pesquisarUsuarioEntidadePaginado(this.filtroSelecionado, this.paginacao);
    }

    voltar(): void {
        this.router.navigate([`${environment.path_raiz_cadastro}/usuario/${this.idUsuario}`]);
    }

    novo(): void {
        this.router.navigate([`${environment.path_raiz_cadastro}/usuario/${this.idUsuario}/empresa/associar`]);
    }

    buscarUsuario(): void {
        this.idUsuario = this.activatedRoute.snapshot.params['id'];
        this.usuarioService.buscarUsuarioById(this.idUsuario).subscribe((retorno: Usuario) => {
            this.usuario = retorno;
            this.carregarTabelaEmppresaUsuario();
            this.carregarTabelaEmpresaTrabalhador();
            if (this.usuario && !this.usuario.perfisSistema) {
                this.usuario.perfisSistema = new Array<UsuarioPerfilSistema>();
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private carregarTabelaEmppresaUsuario() {
        this.paginacao.pagina = 1;
        if (this.usuario) {
            this.filtro.cpf = this.usuario.login;
        }
        this.pesquisarUsuarioEntidadePaginado(this.filtro, this.paginacao);
    }

    private pesquisarUsuarioEntidadePaginado(filtro: FiltroUsuarioEntidade, paginacao: Paginacao): void {

        this.usuarioEntidadeService.pesquisarPaginado(filtro, paginacao, MensagemProperties.app_rst_menu_empresa).subscribe((retorno: ListaPaginada<UsuarioEntidade>) => {
            if (retorno.quantidade > 0) {
                this.listaUsuarioEntidade = retorno.list;
                this.isEmptyEmpresasSaude();
                this.paginacao = this.getPaginacao(this.paginacao, retorno);
            } else {
                this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private carregarTabelaEmpresaTrabalhador(): void {
        this.empresaTrabalhadorService.pesquisarPorTrabalhadorCpf(this.usuario.login).subscribe((retorno: EmpresaTrabalhador[]) => {
            this.listaEmpresasTrabalhador = retorno;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    public removerEmpresa(item: UsuarioEntidade) {
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

    temPerfilTrabalhador(): boolean {
        let temPerfilTrab = false;
        if (this.usuario && this.usuario.perfisSistema) {
            this.usuario.perfisSistema.forEach((item) => {
                if (item.perfil.codigo === PerfilEnum.TRA) {
                    temPerfilTrab = true;
                }
            });
        }
        return temPerfilTrab;
    }

    temPerfilGestorEmpresa(): boolean {
        let temPerfilGeem = false;
        if (this.usuario && this.usuario.perfisSistema) {
            this.usuario.perfisSistema.forEach((item) => {
                if (item.perfil.codigo === PerfilEnum.GEEM) {
                    temPerfilGeem = true;
                }
            });
        }
        return temPerfilGeem;
    }

    temPerfilProfissonalSaude(): boolean {
        let temPerfilPfs = false;
        if (this.usuario && this.usuario.perfisSistema) {
            this.usuario.perfisSistema.forEach((item) => {
                if (item.perfil.codigo === PerfilEnum.PFS) {
                    temPerfilPfs = true;
                }
            });
        }
        return temPerfilPfs;
    }

    isEmptyEmpresasSaude() {
        let empresas = new Array<Empresa>();
        this.listaUsuarioEntidade.forEach((emp) => {
            if (emp.empresaProfissionalSaude != null) {
                empresas.push(emp.empresaProfissionalSaude);
            }
        });
        this.hasProfissionalSaude = !this.isEmpty(empresas);
    }

    isSomenteTrabalhador(): boolean {
        return this.temPerfilTrabalhador() && !this.temPerfilGestorEmpresa();
    }

    hasPermissao() {
        return super.hasPermissao(PermissoesEnum.USUARIO_ENTIDADE) ||
            super.hasPermissao(PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR);
    }
}
