import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { environment } from './../../../../environments/environment';
import { EmpresaFuncaoService } from 'app/servico/empresa-funcao.service';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { EmpresaFuncao } from './../../../modelo/empresa-funcao.model';
import { ActivatedRoute, Router } from '@angular/router';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { FuncaoService } from './../../../servico/funcao.service';
import { FiltroFuncao } from './../../../modelo/filtro-funcao.model';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit, Input } from '@angular/core';
import { EmpresaService } from 'app/servico/empresa.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { Funcao } from 'app/modelo/funcao.model';
import { Paginacao } from 'app/modelo/paginacao.model';

export interface IHash {
    [details: number]: boolean;
}

@Component({
    selector: 'app-empresa-funcao',
    templateUrl: './empresa-funcao.component.html',
    styleUrls: ['./empresa-funcao.component.scss'],
})
export class EmpresaFuncaoComponent extends BaseComponent implements OnInit {

    public title: string;
    // Funcao
    @Input() public filtro: FiltroFuncao;
    public filtroPage: FiltroFuncao;
    public funcoes: Funcao[];
    public funcaoSelecionadas = Array<Funcao>();

    public idEmpresa: number;
    public empresasFuncao: EmpresaFuncao[];
    public paginacaoEmpresaFuncao: Paginacao = new Paginacao(1, 10);
    public checks: IHash = {};

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private empresaService: EmpresaService,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private dialogService: DialogService,
        private funcaoService: FuncaoService,
        private empresaFuncaoService: EmpresaFuncaoService,
    ) {
        super(bloqueioService, dialogo);

        this.title = MensagemProperties.app_rst_empresa_funcao_title;
    }

    ngOnInit() {
        this.funcaoSelecionadas = new Array<Funcao>();
        this.getIdEmpresa();
    }

    private getIdEmpresa() {
        this.route.params.subscribe((params) => {
            this.idEmpresa = params['id'];
            if (this.idEmpresa) {
                this.filtro = new FiltroFuncao();
                this.filtroPage = new FiltroFuncao();
                this.filtro.idEmpresa = this.idEmpresa;
                this.pesquisarEmpresasFuncao();
            }
        });
    }
    private pesquisarEmpresasFuncao() {
        this.empresasFuncao = new Array<EmpresaFuncao>();
        this.filtro.idEmpresa = this.idEmpresa;
        this.empresaFuncaoService.pesquisarFuncoesPorEmpresa(this.filtro, new Paginacao(1, 10))
            .subscribe((retorno: ListaPaginada<EmpresaFuncao>) => {
                this.paginacaoEmpresaFuncao = this.getPaginacao(this.paginacaoEmpresaFuncao, retorno);
                this.verificarRetornoEmpresasFuncao(retorno);
            }, (error) => {
                this.mensagemError(error);
            });
    }

    private validarCampos(): Boolean {
        let verificador: Boolean = true;

        if (this.isVazia(this.filtro.codigo) && this.isVazia(this.filtro.descricao)) {
            this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
            verificador = false;
        }
        return verificador;
    }

    public pesquisarFuncao() {
        this.funcoes = new Array<Funcao>();
        this.setFiltro(this.filtro);
        if (this.validarCampos()) {
            this.pesquisarFuncaoService(1, true, this.filtro);

        }
    }

    private setFiltro(filter: FiltroFuncao) {
        this.filtroPage.codigo = filter.codigo;
        this.filtroPage.descricao = filter.descricao;
    }

    private limparIdsFiltro() {
        this.filtro.idFuncao = new Array<number>();
    }
    private pesquisarFuncaoService(page: any, msg: boolean, filter: FiltroFuncao) {
        this.paginacao.pagina = page;
        this.funcaoService.pesquisar(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Funcao>) => {
            this.paginacao = this.getPaginacao(this.paginacao, retorno);
            this.verificarRetornoFuncao(retorno);
        }, (error) => {
            this.mensagemError(error);
        }, () => { this.limparIdsFiltro(); });
    }

    public selectDeselectFuncao(funcao: Funcao, event: any, i: number) {
        if (funcao && event.checked) {
            this.funcaoSelecionadas.push(funcao);
            this.checks[i] = true;
        } else {
            const index: number = this.funcaoSelecionadas.indexOf(funcao);
            if (index !== -1) {
                this.funcaoSelecionadas.splice(index, 1);
                this.checks[i] = false;
            }
        }
    }

    public add() {
        if (this.isNotEmpty(this.funcaoSelecionadas)) {
            this.funcaoSelecionadas.forEach((element) => {
                const obj = new EmpresaFuncao();
                obj.funcao = element;
                this.empresasFuncao.push(obj);
            });
            this.funcaoSelecionadas = new Array<Funcao>();
            this.checks = {};
            this.salvar();

        } else {
            this.mensagemError(MensagemProperties.app_rst_selecione_um_item);
        }
    }

    public isListaVazia(): boolean {
        return this.funcoes === undefined || this.funcoes.length === 0;
    }

    public removeFuncao(func: EmpresaFuncao) {
        const index: number = this.empresasFuncao.indexOf(func);
        if (index !== -1) {
            this.empresasFuncao.splice(index, 1);
            this.checks[func.funcao.id] = false;
        }
    }

    public desativarEmpresaFuncao(empresaFuncao: EmpresaFuncao) {
        this.empresaFuncaoService.desativarEmpresaFuncao(empresaFuncao).subscribe((response: EmpresaFuncao) => {
            this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
            this.removeFuncao(empresaFuncao);
        }, (error) => {
            this.mensagemError(error);
        }, () => {
            this.pesquisarEmpresasFuncao();
        });
    }

    pageChanged(event: any): void {
        this.pesquisarFuncaoService(event.page, true, this.filtroPage);
    }

    pageChangedFuncaoEmpresa(event: any): void {
        this.paginacaoEmpresaFuncao.pagina = event.page;
        this.empresaFuncaoService.pesquisarFuncoesPorEmpresa(this.filtro, this.paginacaoEmpresaFuncao)
            .subscribe((retorno: ListaPaginada<EmpresaFuncao>) => {
                this.paginacaoEmpresaFuncao = this.getPaginacao(this.paginacaoEmpresaFuncao, retorno);
                this.verificarRetornoEmpresasFuncao(retorno);
            }, (error) => {
                this.mensagemError(error);
            });
    }
    verificarRetornoFuncao(retorno: ListaPaginada<Funcao>) {
        if (retorno && retorno.list) {
            this.funcoes = retorno.list;
        } else {
            this.funcoes = new Array<Funcao>();
            this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
    }
    verificarRetornoEmpresasFuncao(retorno: ListaPaginada<EmpresaFuncao>) {
        if (retorno && retorno.list) {
            this.empresasFuncao = retorno.list;
        } else {
            this.empresasFuncao = new Array<EmpresaFuncao>();
        }
    }
    public salvar() {
        this.empresaFuncaoService.salvarFuncoes(this.idEmpresa, this.empresasFuncao).subscribe((response: EmpresaFuncao) => {
            this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        }, (error) => {
            this.mensagemError(error);
            this.pesquisarEmpresasFuncao();
        }, () => {
            this.pesquisarEmpresasFuncao();
        });
    }

    voltar(): void {
        if (this.activatedRoute.snapshot.url[0].path === 'minhaempresa') {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa`]);
        } else {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}`]);
        }
    }

    empresasFuncaoVazia() {
        return this.isEmpty(this.empresasFuncao);
    }

    public hasPermissaoCadastrar() {
        return this.hasPermissao(PermissoesEnum.EMPRESA_FUNCAO_CADASTRAR)
            || this.hasPermissao(PermissoesEnum.EMPRESA_FUNCAO);
    }

    public hasPermissaoDesativar() {
        return this.hasPermissao(PermissoesEnum.EMPRESA_FUNCAO_DESATIVAR)
            || this.hasPermissao(PermissoesEnum.EMPRESA_FUNCAO);
    }


}
