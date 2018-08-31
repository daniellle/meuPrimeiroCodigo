import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { environment } from './../../../../environments/environment';
import { EmpresaSetorService } from './../../../servico/empresa-setor.service';
import { SetorService } from './../../../servico/setor.service';
import { Setor } from './../../../modelo/setor.model';
import { FiltroSetor } from './../../../modelo/filtro-setor.model';
import { Component, OnInit, Input } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { Paginacao } from 'app/modelo/paginacao.model';
import { FiltroEmpresa } from 'app/modelo/filtro-empresa.model';
import { EmpresaSetor } from 'app/modelo/empresa-setor.model';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { EmpresaService } from 'app/servico/empresa.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';

export interface IHash {
    [details: number]: boolean;
}

@Component({
    selector: 'app-empresa-setor',
    templateUrl: './empresa-setor.component.html',
    styleUrls: ['./empresa-setor.component.scss'],
})
export class EmpresaSetorComponent extends BaseComponent implements OnInit {
    public title: string;
    @Input() public filtro: FiltroSetor;
    public filtroPage: FiltroSetor;
    public setores: Setor[];
    public setoresSelecionados = Array<Setor>();

    public idEmpresa: number;
    public emFiltro: FiltroEmpresa;
    public empresasSetor: EmpresaSetor[];
    public paginacaoEmpresaSetor: Paginacao = new Paginacao(1, 10);
    public checks: IHash = {};

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private empresaService: EmpresaService,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private dialogService: DialogService,
        private setorService: SetorService,
        private empresaSetorService: EmpresaSetorService) {
        super(bloqueioService, dialogo);
        this.getIdEmpresa();
        this.title = MensagemProperties.app_rst_empresa_setor_title;
    }

    ngOnInit() {
        this.setoresSelecionados = new Array<Setor>();
    }

    private getIdEmpresa() {
        this.route.params.subscribe((params) => {
            this.idEmpresa = params['id'];
            if (this.idEmpresa) {
                this.filtro = new FiltroSetor();
                this.filtroPage = new FiltroSetor();
                this.filtro.idEmpresa = this.idEmpresa;
                this.pesquisarEmpresasSetor();
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private setFiltro(filter: FiltroSetor) {
        this.filtroPage.sigla = filter.sigla;
        this.filtroPage.descricao = filter.descricao;
    }

    private pesquisarEmpresasSetor() {
        this.empresasSetor = new Array<EmpresaSetor>();
        this.filtro.idEmpresa = this.idEmpresa;
        this.paginacaoEmpresaSetor.pagina = 1;
        this.empresaSetorService.pesquisarSetoresPorEmpresa(this.filtro, this.paginacaoEmpresaSetor)
            .subscribe((retorno: ListaPaginada<EmpresaSetor>) => {
                this.paginacaoEmpresaSetor = this.getPaginacao(this.paginacaoEmpresaSetor, retorno);
                this.verificarRetornoEmpresasSetor(retorno);
            }, (error) => {
                this.mensagemError(error);
            });
    }

    verificarRetornoSetor(retorno: ListaPaginada<Setor>) {
        if (retorno && retorno.list) {
            this.setores = retorno.list;
        } else {
            this.setores = new Array<Setor>();
            this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
    }
    verificarRetornoEmpresasSetor(retorno: ListaPaginada<EmpresaSetor>) {
        if (retorno && retorno.list) {
            this.empresasSetor = retorno.list;
        } else {
            this.empresasSetor = new Array<EmpresaSetor>();
        }
    }

    private validarCampos(): Boolean {
        let verificador: Boolean = true;

        if (this.isVazia(this.filtro.sigla) && this.isVazia(this.filtro.descricao)) {
            this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
            verificador = false;
        }
        return verificador;
    }

    public pesquisarSetor() {
        this.setores = new Array<Setor>();
        if (this.validarCampos()) {
            this.setFiltro(this.filtro);
            this.pesquisarSetorService(1, true, this.filtro);
        }
    }

    private limparIdsFiltro() {
        this.filtro.idSetor = new Array<number>();
    }

    private pesquisarSetorService(page: any, msg: boolean, filter: FiltroSetor) {
        this.paginacao.pagina = page;
        this.setorService.pesquisar(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Setor>) => {
            this.paginacao = this.getPaginacao(this.paginacao, retorno);
            this.verificarRetornoSetor(retorno);
        }, (error) => {
            this.mensagemError(error);
        }, () => { this.limparIdsFiltro(); });
    }

    public selectDeselectSetor(setor: Setor, event: any, i: number) {
        if (setor && event.checked) {
            this.setoresSelecionados.push(setor);
            this.checks[i] = true;
        } else {
            const index: number = this.setoresSelecionados.indexOf(setor);
            if (index !== -1) {
                this.setoresSelecionados.splice(index, 1);
                this.checks[i] = false;
            }
        }
    }

    public add() {
        if (this.isNotEmpty(this.setoresSelecionados)) {
            this.setoresSelecionados.forEach((element) => {
                const obj = new EmpresaSetor();
                obj.setor = element;
                this.empresasSetor.push(obj);
            });

            this.setoresSelecionados = new Array<Setor>();
            this.checks = {};
            this.salvar();

        } else {
            this.mensagemError(MensagemProperties.app_rst_selecione_um_item);
        }
    }

    public isListaVazia(): boolean {
        return this.setores === undefined || this.setores.length === 0;
    }

    public removeSetor(set: EmpresaSetor) {
        const index: number = this.empresasSetor.indexOf(set);
        if (index !== -1) {
            this.empresasSetor.splice(index, 1);
            this.checks[set.setor.id] = false;
        }
    }

    public desativarEmpresaSetor(empresaSetor: EmpresaSetor) {
        this.empresaSetorService.desativarEmpresaSetor(empresaSetor).subscribe((response: EmpresaSetor) => {
            this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
            this.removeSetor(empresaSetor);
        }, (error) => {
            this.mensagemError(error);
        }, () => {
            this.pesquisarEmpresasSetor();
        });
    }

    pageChanged(event: any): void {
        this.pesquisarSetorService(event.page, true, this.filtroPage);
    }

    pageChangedSetorEmpresa(event: any): void {
        this.paginacaoEmpresaSetor.pagina = event.page;
        this.empresaSetorService.pesquisarSetoresPorEmpresa(this.filtro, this.paginacaoEmpresaSetor)
            .subscribe((retorno: ListaPaginada<EmpresaSetor>) => {
                this.paginacaoEmpresaSetor = this.getPaginacao(this.paginacaoEmpresaSetor, retorno);
                this.verificarRetornoEmpresasSetor(retorno);
            }, (error) => {
                this.mensagemError(error);
            });
    }

    public salvar() {
        this.empresaSetorService.salvarSetores(this.idEmpresa, this.empresasSetor).subscribe((response: EmpresaSetor) => {
            this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        }, (error) => {
            this.mensagemError(error);
            this.pesquisarEmpresasSetor();
        }, () => {
            this.pesquisarEmpresasSetor();
        });
    }

    voltar(): void {
        if (this.route.snapshot.url[0].path === 'minhaempresa') {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa`]);
        } else {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}`]);
        }
    }

    empresasSetorVazia() {
        return this.isEmpty(this.empresasSetor);
    }

    hasPermissaoCadastrar() {
        return this.hasPermissao(PermissoesEnum.EMPRESA_SETOR_CADASTRAR)
            || this.hasPermissao(PermissoesEnum.EMPRESA_SETOR);
    }

    hasPermissaoDesativar() {
        return this.hasPermissao(PermissoesEnum.EMPRESA_SETOR_DESATIVAR)
            || this.hasPermissao(PermissoesEnum.EMPRESA_SETOR);
    }
}
