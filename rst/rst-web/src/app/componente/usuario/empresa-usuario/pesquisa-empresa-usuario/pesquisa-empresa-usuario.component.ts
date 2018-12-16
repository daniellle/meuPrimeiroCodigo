import { UsuarioBarramentoComponent } from './../../usuario-barramento/usuario-barramento.component';
import {PermissoesEnum} from 'app/modelo/enum/enum-permissoes';
import {Seguranca} from './../../../../compartilhado/utilitario/seguranca.model';
import {EmpresaTrabalhador} from './../../../../modelo/empresa-trabalhador.model';
import {EmpresaTrabalhadorService} from './../../../../servico/empresa-trabalhador.service';
import {PerfilEnum} from 'app/modelo/enum/enum-perfil';
import {environment} from './../../../../../environments/environment';
import {Usuario} from './../../../../modelo/usuario.model';
import {UsuarioService} from './../../../../servico/usuario.service';
import {MensagemProperties} from 'app/compartilhado/utilitario/recurso.pipe';
import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {Paginacao} from './../../../../modelo/paginacao.model';
import {UsuarioEntidadeService} from './../../../../servico/usuario-entidade.service';
import {UsuarioEntidade} from './../../../../modelo/usuario-entidade.model';
import {ListaPaginada} from './../../../../modelo/lista-paginada.model';
import {BloqueioService} from 'app/servico/bloqueio.service';
import {ToastyService} from 'ng2-toasty';
import {ActivatedRoute, Router} from '@angular/router';
import {FiltroUsuarioEntidade} from './../../../../modelo/filtro-usuario-entidade.model';
import {BaseComponent} from 'app/componente/base.component';
import {Perfil} from "../../../../modelo/perfil.model";


@Component({
    selector: 'app-pesquisa-empresa-usuario',
    templateUrl: './pesquisa-empresa-usuario.component.html',
    styleUrls: ['./pesquisa-empresa-usuario.component.scss'],
})
export class PesquisaEmpresaUsuarioComponent extends BaseComponent implements OnInit {

    usuarioBarramentoComponent: UsuarioBarramentoComponent;

    idUsuario: number;
    filtro: FiltroUsuarioEntidade;
    @Output() usuario: Usuario;
    perfis: Perfil[] = new Array<Perfil>();

    hasTrabalhador = false;
    hasProfissionalSaude = false;
    hasGestorEmpresa = false;
    hasRecursoHumano = false;
    hasSegurancaTrabalho = false;
    hasGestorEmpresaMaster = false;

    paginacaoPFS = new Paginacao();
    paginacaoGEEM = new Paginacao();
    paginacaoRH = new Paginacao();
    paginacaoST = new Paginacao();
    paginacaoGEEMMaster = new Paginacao();

    listaEmpresasTRA = new Array<EmpresaTrabalhador>();
    listaEmpresasPFS = new Array<UsuarioEntidade>();
    listaEmpresasGEEM = new Array<UsuarioEntidade>();
    listaEmpresasRH = new Array<UsuarioEntidade>();
    listaEmpresasST = new Array<UsuarioEntidade>();
    listaEmpresasGEEMMaster = new Array<UsuarioEntidade>();
    

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
        this.filtro = new FiltroUsuarioEntidade();
    }

    onAlertListener(_usuariosEntidade) {
        console.log(_usuariosEntidade);
      }

    tipoTela() {
        this.modoConsulta = !Seguranca.isPermitido(
            [PermissoesEnum.USUARIO_ENTIDADE, PermissoesEnum.USUARIO_ENTIDADE_ALTERAR,
                PermissoesEnum.USUARIO_ENTIDADE_CADASTRAR,
                PermissoesEnum.USUARIO_ENTIDADE_DESATIVAR]);
    }

    pesquisar(): void {
        this.paginacao.pagina = 1;
        let filtroSelecionado = new FiltroUsuarioEntidade(this.filtro);
        if(this.perfis){
            this.perfis.forEach(p => {
                this.carregarEmpresasPerfil(PerfilEnum[<string>p.codigo], this.paginacao, filtroSelecionado);
            });
        }
    }

    pageChanged(event: any, perfil: string): void {
        let filtroSelecionado = new FiltroUsuarioEntidade(this.filtro);
        switch (perfil) {
            case PerfilEnum.GEEM:
                this.paginacaoGEEM.pagina = event.page;
                this.carregarEmpresasPerfil(PerfilEnum.GEEM, this.paginacaoGEEM, filtroSelecionado);
                break;
            case PerfilEnum.PFS:
                this.paginacaoPFS.pagina = event.page;
                this.carregarEmpresasPerfil(PerfilEnum.PFS, this.paginacaoPFS, filtroSelecionado);
                break;
            case PerfilEnum.RH:
                this.paginacaoRH.pagina = event.page;
                this.carregarEmpresasPerfil(PerfilEnum.RH, this.paginacaoRH, filtroSelecionado);
                break;
            case PerfilEnum.ST:
                this.paginacaoST.pagina = event.page;
                this.carregarEmpresasPerfil(PerfilEnum.ST, this.paginacaoST, filtroSelecionado);
                break;
            case PerfilEnum.GEEMM:
                this.paginacaoGEEMMaster.pagina = event.page;
                this.carregarEmpresasPerfil(PerfilEnum.GEEMM, this.paginacaoGEEMMaster, filtroSelecionado);
                break;
        }

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
            if (retorno.perfisSistema) {
                this.getPerfis(retorno);
            }
            this.buscarEmpresasUsuario();
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private getPerfis(usuario: Usuario) {
        let map = new Map();
        if (usuario.origemDados == null) {
            usuario.perfisSistema.forEach(value => {
                map.set(value.perfil.codigo, value.perfil.nome);
            });
            this.perfis = new Array<Perfil>();
            map.forEach((value, key) => {
                this.perfis.push(new Perfil(null, value, key));
            });
        }
    }

    private buscarEmpresasUsuario() {
        this.paginacao.pagina = 1;
        if (this.usuario.origemDados == null) {
            this.filtro.cpf = this.usuario.login;
            this.perfis.forEach((perfil) => {
                this.carregarEmpresasPerfil(PerfilEnum[<string>perfil.codigo], this.paginacao, this.filtro);
            });
        }
    }

    private carregarEmpresasPerfil(perfil: PerfilEnum, paginacao: Paginacao, filtro: FiltroUsuarioEntidade) {
        switch (perfil) {
            case PerfilEnum.TRA:
                filtro.perfil = PerfilEnum.TRA;
                this.hasTrabalhador = true;
                this.carregarTabelaEmpresaTrabalhador();
                break;
            case PerfilEnum.GEEM:
                filtro.perfil = PerfilEnum.GEEM;
                this.hasGestorEmpresa = true;
                this.pesquisarEmpGEEM(filtro, paginacao);
                break;
            case PerfilEnum.PFS:
                filtro.perfil = PerfilEnum.PFS;
                this.hasProfissionalSaude = true;
                this.pesquisarEmpPSaude(filtro, paginacao);
                break;
            case PerfilEnum.RH:
                filtro.perfil = PerfilEnum.RH;
                this.hasRecursoHumano = true;
                this.pesquisarEmpRH(filtro, paginacao);
                break;
            case PerfilEnum.ST:
                filtro.perfil = PerfilEnum.ST;
                this.hasSegurancaTrabalho = true;
                this.pesquisarEmpST(filtro, paginacao);
                break;
            case PerfilEnum.GEEMM:
                filtro.perfil = PerfilEnum.GEEMM;
                this.hasGestorEmpresaMaster = true;
                this.pesquisarEmpGEEMMaster(filtro, paginacao);
                break;
        }
    }

    private pesquisarEmpGEEM(filtro: FiltroUsuarioEntidade, paginacao: Paginacao): void {

        this.usuarioEntidadeService.pesquisarPaginado(filtro, paginacao, MensagemProperties.app_rst_menu_empresa).subscribe((retorno: ListaPaginada<UsuarioEntidade>) => {
            if (retorno.quantidade > 0) {
                this.listaEmpresasGEEM = this.tratarResultList(retorno.list);
                this.paginacaoGEEM = this.getPaginacao(this.paginacaoGEEM, retorno);
            } else {
                this.listaEmpresasGEEM = [];
                this.paginacaoGEEM = this.getPaginacao(this.paginacaoGEEM, retorno);
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private pesquisarEmpGEEMMaster(filtro: FiltroUsuarioEntidade, paginacao: Paginacao): void {

        this.usuarioEntidadeService.pesquisarPaginado(filtro, paginacao, MensagemProperties.app_rst_menu_empresa).subscribe((retorno: ListaPaginada<UsuarioEntidade>) => {
            if (retorno.quantidade > 0) {
                this.listaEmpresasGEEMMaster = this.tratarResultList(retorno.list);
                this.paginacaoGEEMMaster = this.getPaginacao(this.paginacaoGEEMMaster, retorno);
            } else {
                this.listaEmpresasGEEMMaster = [];
                this.paginacaoGEEMMaster = this.getPaginacao(this.paginacaoGEEMMaster, retorno);
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private pesquisarEmpPSaude(filtro: FiltroUsuarioEntidade, paginacao: Paginacao): void {

        this.usuarioEntidadeService.pesquisarPaginado(filtro, paginacao, MensagemProperties.app_rst_menu_empresa).subscribe((retorno: ListaPaginada<UsuarioEntidade>) => {
            if (retorno.quantidade > 0) {
                this.listaEmpresasPFS = this.tratarResultList(retorno.list);
                this.paginacaoPFS = this.getPaginacao(this.paginacaoPFS, retorno);
            } else {
                this.listaEmpresasPFS = [];
                this.paginacaoPFS = this.getPaginacao(this.paginacaoPFS, retorno);
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private pesquisarEmpRH(filtro: FiltroUsuarioEntidade, paginacao: Paginacao): void {

        this.usuarioEntidadeService.pesquisarPaginado(filtro, paginacao, MensagemProperties.app_rst_menu_empresa).subscribe((retorno: ListaPaginada<UsuarioEntidade>) => {
            if (retorno.quantidade > 0) {
                this.listaEmpresasRH = this.tratarResultList(retorno.list);
                this.paginacaoRH = this.getPaginacao(this.paginacaoRH, retorno);
            } else {
                this.listaEmpresasRH = [];
                this.paginacaoRH = this.getPaginacao(this.paginacaoRH, retorno);
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private pesquisarEmpST(filtro: FiltroUsuarioEntidade, paginacao: Paginacao): void {

        this.usuarioEntidadeService.pesquisarPaginado(filtro, paginacao, MensagemProperties.app_rst_menu_empresa).subscribe((retorno: ListaPaginada<UsuarioEntidade>) => {
            if (retorno.quantidade > 0) {
                this.listaEmpresasST = this.tratarResultList(retorno.list);
                this.paginacaoST = this.getPaginacao(this.paginacaoST, retorno);
            } else {
                this.listaEmpresasST = [];
                this.paginacaoST = this.getPaginacao(this.paginacaoST, retorno);
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private carregarTabelaEmpresaTrabalhador(): void {
        this.empresaTrabalhadorService.pesquisarPorTrabalhadorCpf(this.usuario.login).subscribe((retorno: EmpresaTrabalhador[]) => {
            this.listaEmpresasTRA = retorno;
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
        switch (PerfilEnum[<string>item.perfil]) {
            case PerfilEnum.GEEM:
                this.remover(this.listaEmpresasGEEM, item);
                break;
            case PerfilEnum.PFS:
                this.remover(this.listaEmpresasPFS, item);
                break;
            case PerfilEnum.RH:
                this.remover(this.listaEmpresasRH, item);
                break;
            case PerfilEnum.ST:
                this.remover(this.listaEmpresasST, item);
                break;
            case PerfilEnum.GEEMM:
                this.remover(this.listaEmpresasGEEMMaster, item);
                break;
        }

    }

    private remover(lista: Array<UsuarioEntidade>, item: UsuarioEntidade) {
        const index: number = lista.indexOf(item);
        if (index !== -1) {
            lista.splice(index, 1);
        }
    }

    private tratarResultList(lista: UsuarioEntidade[]): Array<UsuarioEntidade> {
        if (this.isNotEmpty(lista)) {
            for (let i = 0; i < lista.length; i++) {
                let u = lista[i];
                if (!u || !u.empresa) {
                    lista.splice(i, 1);
                } else {
                    if (!u.empresa.razaoSocial) {
                        u.empresa.razaoSocial = " ";
                    }
                    if (!u.empresa.nomeFantasia) {
                        u.empresa.nomeFantasia = " ";
                    }
                    if (!u.empresa.cnpj) {
                        u.empresa.cnpj = " ";
                    }
                }
            }
            return lista;
        }
    }


    isSomenteTrabalhador(): boolean {
        return this.hasTrabalhador && !this.hasProfissionalSaude && !this.hasSegurancaTrabalho && !this.hasRecursoHumano
            && !this.hasGestorEmpresa && !this.hasGestorEmpresaMaster;
    }

    vemDoBarramento(){
        if(this.usuario){
        if(this.usuario.origemDados != null){
            return true;
        }
        else{
            return false;
        }
        }
    }
}
