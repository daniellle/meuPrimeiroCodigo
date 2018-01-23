import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { environment } from './../../../../environments/environment';
import { CboFilter } from './../../../modelo/filtro-cbo.model';
import { Cbo } from './../../../modelo/cbo.model';
import { EmpresaCbo } from './../../../modelo/empresa-cbo.model';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { DialogService } from 'ng2-bootstrap-modal';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { EmpresaService } from './../../../servico/empresa.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit, Input } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ToastyService } from 'ng2-toasty';
import { CboService } from '../../../servico/cbo.service';
import { EmpresaCboService } from '../../../servico/empresa-cbo.service';
import { Paginacao } from 'app/modelo/paginacao.model';

export interface IHash {
    [details: number]: boolean;
}

@Component({
    selector: 'app-empresa-cbo',
    templateUrl: './empresa-cbo.component.html',
    styleUrls: ['./empresa-cbo.component.scss'],
})
export class EmpresaCboComponent extends BaseComponent implements OnInit {

    public title: string;

    // Cbo
    @Input() public filtro: CboFilter;
    public filtroPage: CboFilter;
    public cbos: Cbo[];
    public cbosSelecionadas = Array<Cbo>();
    public checks: IHash = {};

    // Cbo Empresa
    public idEmpresa: number;
    public empresasCbos: EmpresaCbo[];
    public empresasCbosPorPagina: EmpresaCbo[];
    public paginacaoEmpresaCbo: Paginacao = new Paginacao(1, 10);

    constructor(
        private route: ActivatedRoute, private router: Router,
        private empresaService: EmpresaService, private fb: FormBuilder,
        protected bloqueioService: BloqueioService, protected dialogo: ToastyService,
        private dialogService: DialogService, private serviceCbo: CboService,
        private activatedRoute: ActivatedRoute,
        private serviceEmpresaCbo: EmpresaCboService) {
        super(bloqueioService, dialogo);
        this.emModoConsulta();
        this.iniciarListas();
        this.getIdEmpresa();
        this.title = 'Cargo';
    }

    ngOnInit() {
        this.setEmpresa();
        this.iniciarListas();
    }

    emModoConsulta() {
        this.modoConsulta = !Seguranca.isPermitido(
            [PermissoesEnum.EMPRESA_CARGO_CADASTRAR,
            PermissoesEnum.EMPRESA_CARGO_ALTERAR,
            PermissoesEnum.EMPRESA_CARGO_DESATIVAR]);
    }

    private setEmpresa() {
        this.idEmpresa = this.activatedRoute.snapshot.params['id'];
    }

    private iniciarListas() {
        this.filtro = new CboFilter();
        this.filtroPage = new CboFilter();
        this.cbosSelecionadas = new Array<Cbo>();
        this.empresasCbos = new Array<EmpresaCbo>();
        this.cbos = new Array<Cbo>();
    }

    private getIdEmpresa() {
        this.route.params.subscribe((params) => {
            this.idEmpresa = params['id'];
            if (this.idEmpresa) {
                this.filtro = new CboFilter();
                this.filtro.idEmpresa = this.idEmpresa;
                this.pesquisarEmpresasCbos();
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    private setFiltro(filter: CboFilter) {
        this.filtroPage.codigo = filter.codigo;
        this.filtroPage.descricao = filter.descricao;
    }

    private pesquisarEmpresasCbos() {
        this.empresasCbos = new Array<EmpresaCbo>();
        this.filtro.idEmpresa = this.idEmpresa;
        this.paginacaoEmpresaCbo.pagina = 1;
        this.serviceEmpresaCbo.pesquisarPorEmpresa(this.filtro, this.paginacaoEmpresaCbo)
            .subscribe((retorno: ListaPaginada<EmpresaCbo>) => {
                this.paginacaoEmpresaCbo = this.getPaginacao(this.paginacaoEmpresaCbo, retorno);
                this.verificarRetornoEmpresaCbo(retorno);
            }, (error) => {
                this.mensagemError(error);
            });
    }
    verificarRetornoCbo(retorno: ListaPaginada<Cbo>) {
        if (retorno && retorno.list) {
            this.cbos = retorno.list;
        } else {
            this.cbos = new Array<Cbo>();
            this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
    }
    verificarRetornoEmpresaCbo(retorno: ListaPaginada<EmpresaCbo>) {
        if (retorno && retorno.list) {
            this.empresasCbos = retorno.list;
        } else {
            this.empresasCbos = new Array<EmpresaCbo>();
        }
    }

    private validarCampos(): Boolean {
        let verificador: Boolean = true;

        if (this.isVazia(this.filtro.codigo) && !this.filtro.descricao) {
            this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
            verificador = false;
        }

        return verificador;
    }

    public pesquisarCbo() {
        this.cbos = new Array<Cbo>();
        this.setFiltro(this.filtro);
        if (this.validarCampos()) {
            this.pesquisarCboService(1, true, this.filtro);
        }
    }

    verificarSelecionados(cbo: Cbo) {
        if (this.cbos.indexOf(cbo) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public selectDeselectCbo(cbo: Cbo, event: any, i: number) {
        if (cbo && event.checked) {
            this.cbosSelecionadas.push(cbo);
            this.checks[i] = true;
        } else {
            const index: number = this.cbosSelecionadas.indexOf(cbo);
            if (index !== -1) {
                this.cbosSelecionadas.splice(index, 1);
            }
            this.checks[i] = false;
        }
    }

    public add() {
        if (this.isNotEmpty(this.cbosSelecionadas)) {
            this.cbosSelecionadas.forEach((element) => {
                const obj = new EmpresaCbo();
                obj.cbo = element;
                this.empresasCbos.push(obj);
                this.totalItens = this.totalItens + 1;
            });

            this.cbosSelecionadas = new Array<Cbo>();
            this.checks = {};
            this.salvar();

        } else {
            this.mensagemError(MensagemProperties.app_rst_selecione_um_item);
        }

    }

    public isListaVazia(): boolean {
        return this.cbos === undefined || this.cbos.length === 0;
    }

    public removeCbo(cbo: EmpresaCbo) {
        const index: number = this.empresasCbos.indexOf(cbo);
        if (index !== -1) {
            this.empresasCbos.splice(index, 1);
            this.totalItens = this.totalItens - 1;
            this.checks[cbo.cbo.id] = false;
        }
    }

    pageChanged(event: any): void {
        this.pesquisarCboService(event.page, true, this.filtroPage);
    }

    private pesquisarCboService(page: any, msg: boolean, filtro: CboFilter) {
        filtro.idEmpresa = this.idEmpresa;
        this.paginacao.pagina = page;
        this.serviceCbo.pesquisar(filtro, this.paginacao).subscribe((retorno: ListaPaginada<Cbo>) => {
            this.paginacao = this.getPaginacao(this.paginacao, retorno);
            this.verificarRetornoCbo(retorno);
        }, (error) => {
            this.mensagemError(error);
        }, () => {

        });
    }

    pageChangedEmpresaCbo(event: any): void {
        this.filtro.idEmpresa = this.idEmpresa;
        this.paginacaoEmpresaCbo.pagina = event.page;
        this.serviceEmpresaCbo.pesquisarPorEmpresa(this.filtro, this.paginacaoEmpresaCbo)
            .subscribe((retorno: ListaPaginada<EmpresaCbo>) => {
                this.paginacaoEmpresaCbo = this.getPaginacao(this.paginacaoEmpresaCbo, retorno);
                this.verificarRetornoEmpresaCbo(retorno);
            }, (error) => {
                this.mensagemError(error);
            });
    }

    public salvar() {
        this.serviceEmpresaCbo.salvarCbos(this.idEmpresa, this.empresasCbos).subscribe((response: EmpresaCbo[]) => {
            this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
            this.empresasCbos = response;
        }, (error) => {
            this.mensagemError(error);
            this.pesquisarEmpresasCbos();
        }, () => {
            this.pesquisarEmpresasCbos();
        });
    }

    public desativarEmpresaCbo(empresaCbo: EmpresaCbo) {
        this.serviceEmpresaCbo.desativarEmpresaCbo(empresaCbo).subscribe((response: EmpresaCbo) => {
            this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
            this.removeCbo(empresaCbo);
        }, (error) => {
            this.mensagemError(error);
        }, () => {
            this.pesquisarEmpresasCbos();
        });

    }

    public voltar() {
        if (this.activatedRoute.snapshot.url[0].path === 'minhaempresa') {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa`]);
        } else {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}`]);
        }
    }

    hasPermissaoCadastrar() {
        return this.hasPermissao(PermissoesEnum.EMPRESA_CARGO_CADASTRAR)
            || this.hasPermissao(PermissoesEnum.EMPRESA_CARGO);
    }

    hasPermissaoDesativar() {
        return this.hasPermissao(PermissoesEnum.EMPRESA_CARGO_DESATIVAR)
            || this.hasPermissao(PermissoesEnum.EMPRESA_CARGO);
    }

}
