import {environment} from './../../../../../environments/environment';
import {SimNao} from 'app/modelo/enum/enum-simnao.model';
import {EnumValues} from 'enum-values';
import {UsuarioPerfilSistema} from './../../../../modelo/usuario-perfil-sistema.model';
import {UsuarioService} from './../../../../servico/usuario.service';
import {UsuarioEntidade} from './../../../../modelo/usuario-entidade.model';
import {UsuarioEntidadeService} from './../../../../servico/usuario-entidade.service';
import {IHash} from './../../../empresa/empresa-jornada/empresa-jornada.component';
import {MascaraUtil} from './../../../../compartilhado/utilitario/mascara.util';
import {Usuario} from './../../../../modelo/usuario.model';
import {BloqueioService} from './../../../../servico/bloqueio.service';
import {ListaPaginada} from './../../../../modelo/lista-paginada.model';
import {MensagemProperties} from './../../../../compartilhado/utilitario/recurso.pipe';
import {Paginacao} from 'app/modelo/paginacao.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Component, OnInit} from '@angular/core';
import {BaseComponent} from 'app/componente/base.component';
import {ToastyService} from 'ng2-toasty';
import {Uat} from "../../../../modelo/uat.model";
import {UatService} from "../../../../servico/uat.service";
import {FiltroUat} from "../../../../modelo/filtro-uat.model";

@Component({
    selector: 'app-cadastro-unidade-sesi-usuario',
    templateUrl: './cadastro-unidade-sesi-usuario.component.html',
    styleUrls: ['./cadastro-unidade-sesi-usuario.component.scss'],
})
export class CadastroUnidadeSESIUsuarioComponent extends BaseComponent implements OnInit {

    idUsuario: number;
    idUsuarioTrabalhador: number;
    usuario: Usuario;
    filtroUat: FiltroUat;
    listaUnidade: Uat[];
    listaSelecionados: Uat[];
    paginacao: Paginacao = new Paginacao(1, 10);
    public checks: IHash = {};
    public estados: any[];

    constructor(
        private router: Router,
        private usuarioService: UsuarioService,
        private uatService: UatService,
        private activatedRoute: ActivatedRoute,
        private usuarioEntidadeService: UsuarioEntidadeService,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
    ) {
        super(bloqueioService, dialogo);
    }

    ngOnInit() {
        this.listaSelecionados = new Array<Uat>();
        this.filtroUat = new FiltroUat();
        this.filtroUat.idDepRegional = undefined;
        this.listaUnidade = new Array<Uat>();
        this.setIdUsuario();
        this.buscarUsuario();
    }

    buscarUsuario(): void {
        this.usuarioService.buscarUsuarioById(this.idUsuario).subscribe((retorno: Usuario) => {
            this.usuario = retorno;
            if (this.usuario && !this.usuario.perfisSistema) {
                this.usuario.perfisSistema = new Array<UsuarioPerfilSistema>();
            }
            this.filtroUat.cpfUsuarioAssociado = this.usuario.login;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    pesquisar(): void {
        if (this.validarCampos()) {
            this.paginacao.pagina = 1;
            this.pesquisarUnidadePaginado();
        }
    }

    pageChanged(event: any): void {
        this.paginacao.pagina = event.page;
        this.pesquisarUnidadePaginado();
    }

    limpar() {
        if (this.idUsuarioTrabalhador) {
            this.router.navigate([`${environment.path_raiz_cadastro}/usuario/${this.idUsuario}/unidadesesi`]);
        }
    }

    selecionar(selecionado: Uat, event: any, i: number) {
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
        this.router.navigate([`${environment.path_raiz_cadastro}/usuario/${this.idUsuario}/unidadesesi`]);
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
            usuarioEntidade.uat = item;
            lista.push(usuarioEntidade);
        });
        return lista;
    }

    private salvar(lista: any): void {
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

    private setIdUsuario() {
        this.idUsuario = this.activatedRoute.snapshot.params['id'];
    }

    validarCampos(): Boolean {
        let verificador: Boolean = true;

        if (this.isVazia(this.filtroUat.cnpj) && this.isVazia(this.filtroUat.razaoSocial)
            && (!this.filtroUat.idDepRegional || this.filtroUat.idDepRegional.toString() === 'undefined')) {
            this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
            verificador = false;
        }

        if (!this.isVazia(this.filtroUat.cnpj)) {
            if (MascaraUtil.removerMascara(this.filtroUat.cnpj).length < 14) {
                this.mensagemError(MensagemProperties.app_rst_labels_cnpj_incompleto);
                verificador = false;
            }
        }
        return verificador;
    }

    private pesquisarUnidadePaginado(): void {
        this.uatService.pesquisar(this.filtroUat, this.paginacao).subscribe((retorno: ListaPaginada<Uat>) => {
            if (retorno.list && retorno.list.length > 0) {
                this.listaUnidade = retorno.list;
                this.paginacao = this.getPaginacao(this.paginacao, retorno);
            } else {
                this.listaUnidade = new Array<Uat>();
                this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    getEndereco(uat: Uat) {
        if (uat.endereco && uat.endereco.length > 0 && uat.endereco[0].endereco.municipio) {
            return uat.endereco[0].endereco.municipio.descricao;
        }
    }
}
