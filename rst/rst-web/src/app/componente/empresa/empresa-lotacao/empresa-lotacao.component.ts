import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { environment } from './../../../../environments/environment';
import { UnidadeObra } from 'app/modelo/unidade-obra.model';
import { UnidadeObraService } from 'app/servico/unidade-obra.service';
import { EmpresaCbo } from './../../../modelo/empresa-cbo.model';
import { EmpresaLotacaoService } from './../../../servico/empresa-lotacao.service';
import { EmpresaJornadaService } from './../../../servico/empresa-jornada.service';
import { EmpresaJornada } from './../../../modelo/empresaJornada.model';
import { EmpresaFuncao } from './../../../modelo/empresa-funcao.model';
import { EmpresaSetor } from 'app/modelo/empresa-setor.model';
import { Component, OnInit } from '@angular/core';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ActivatedRoute, Router } from '@angular/router';
import { EmpresaService } from 'app/servico/empresa.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { DialogService } from 'ng2-bootstrap-modal';
import { ToastyService } from 'ng2-toasty';
import { BaseComponent } from 'app/componente/base.component';
import { Paginacao } from 'app/modelo/paginacao.model';
import { FiltroEmpresa } from 'app/modelo/filtro-empresa.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { EmpresaLotacao } from 'app/modelo/empresa-lotacao.model';

@Component({
    selector: 'app-empresa-lotacao',
    templateUrl: './empresa-lotacao.component.html',
    styleUrls: ['./empresa-lotacao.component.scss'],
})
export class EmpresaLotacaoComponent extends BaseComponent implements OnInit {
    public title: string;
    public idEmpresa: number;
    public emFiltro: FiltroEmpresa;

    public unidadeObraList: UnidadeObra[];
    public unidadeObra: UnidadeObra;

    public setor: EmpresaSetor;
    public cargo: EmpresaCbo;
    public funcao: EmpresaFuncao;
    public jornada: EmpresaJornada;
    public jornadas: EmpresaJornada[];

    public empresasLotacao: EmpresaLotacao[];

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private empresaService: EmpresaService,
        private empresaLotacaoService: EmpresaLotacaoService,
        private serviceEmpresaJornada: EmpresaJornadaService,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private unidadeObraService: UnidadeObraService,
        private dialogService: DialogService) {
        super(bloqueioService, dialogo);
        this.getIdEmpresa();
        this.buscarJornadas();
        this.setUnidadeObras();
        this.setUnidadeObras();
        this.title = MensagemProperties.app_rst_empresa_lotacao_title;
    }

    ngOnInit() {
        this.unidadeObraList = new Array<UnidadeObra>();
        this.setor = new EmpresaSetor();
        this.cargo = new EmpresaCbo();
        this.funcao = new EmpresaFuncao();
        this.jornada = new EmpresaJornada();
    }

    private setUnidadeObras() {
        this.unidadeObraService.pesquisarPorEmpresa(this.idEmpresa).subscribe((retorno: UnidadeObra[]) => {
            this.unidadeObraList = retorno;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    public selecionarUnidadeObra() {
        if (this.unidadeObra) {
            this.pesquisarEmpresasLotacao();
        }
    }

    pesquisarEmpresasLotacao() {
        this.empresasLotacao = new Array<EmpresaLotacao>();
        this.paginacao.pagina = 1;
        this.emFiltro.idUnidadeObra = this.unidadeObra.id;
        this.empresaLotacaoService.pesquisarEmpresasLotacoes(this.emFiltro, new Paginacao(1, 10))
            .subscribe((retorno: ListaPaginada<EmpresaLotacao>) => {
                this.paginacao = this.getPaginacao(this.paginacao, retorno);
                this.empresasLotacao = retorno.list;
            }, (error) => {
                this.mensagemError(error);
            });
    }

    private getIdEmpresa() {
        this.route.params.subscribe((params) => {
            this.idEmpresa = params['id'];
            if (this.idEmpresa) {
                this.empresaService.pesquisarPorId(this.idEmpresa).subscribe(() => { }, (error) => { this.mensagemError(error); });
                this.emFiltro = new FiltroEmpresa();
                this.emFiltro.id = this.idEmpresa;
            }
        }, (error) => {
            this.mensagemError(error);
          });
    }

    private buscarJornadas() {
        this.empresaService.pesquisarTodasEmpresasJornadas(this.emFiltro).subscribe((retorno: EmpresaJornada[]) => {
            this.jornadas = retorno;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    pageChanged(event: any): void {
        this.emFiltro.idUnidadeObra = this.unidadeObra.id;
        this.empresaLotacaoService.pesquisarEmpresasLotacoes(this.emFiltro, new Paginacao(event.page, 10))
            .subscribe((retorno: ListaPaginada<EmpresaLotacao>) => {
                this.paginacao = this.getPaginacao(this.paginacao, retorno);
                this.empresasLotacao = retorno.list;
            }, (error) => {
                this.mensagemError(error);
            });
    }

    voltar(): void {
        if (this.route.snapshot.url[0].path === 'minhaempresa') {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa`]);
        } else {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}`]);
        }
    }

    preencherLotacao() {
        this.empresasLotacao = new Array<EmpresaLotacao>();
        const empresaLotacao = new EmpresaLotacao();
        empresaLotacao.empresaSetor = this.setor;
        empresaLotacao.empresaCbo = this.cargo;
        empresaLotacao.empresaFuncao = this.funcao;
        empresaLotacao.unidadeObra = this.unidadeObra;
        empresaLotacao.empresaJornada = this.jornada;
        this.empresasLotacao.push(empresaLotacao);
    }

    salvar() {
        if (this.validarCampos()) {
            this.preencherLotacao();
            this.empresaLotacaoService.salvarLotacoes(this.empresasLotacao).subscribe((response: EmpresaLotacao) => {
                this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
                this.pesquisarEmpresasLotacao();
            }, (error) => {
                this.mensagemError(error);
                this.pesquisarEmpresasLotacao();
            });
        }
    }

    public removerLotacao(lot: EmpresaLotacao) {
        this.empresaLotacaoService.desativarEmpresaLotacao(lot).subscribe((response: EmpresaLotacao) => {
            this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
            this.removerItem(lot);
        }, (error) => {
            this.mensagemError(error);
        }, () => {
            this.pesquisarEmpresasLotacao();
        });
    }

    public removerItem(lot: EmpresaLotacao) {
        const index: number = this.empresasLotacao.indexOf(lot);
        if (index !== -1) {
            this.empresasLotacao.splice(index, 1);
        }
    }

    validarCampos() {
        let retorno = true;
        if (!this.existeSetor()) {
            this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio',
                MensagemProperties.app_rst_empresa_setor_title);
            retorno = false;
        }
        if (!this.existeCargo()) {
            this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio',
                MensagemProperties.app_rst_labels_cargo);
            retorno = false;
        }
        if (!this.existeFuncao()) {
            this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio',
                MensagemProperties.app_rst_empresa_funcao_title);
            retorno = false;
        }
        if (!this.jornada || this.isUndefined(this.jornada.id)) {
            this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio',
                'Jornada');
            retorno = false;
        }
        return retorno;
    }

    descricaoSiglaSetor(): string {
        return this.setor && this.setor.setor ? this.setor.setor.descricao : '';
    }

    descricaoCargo(): string {
        return this.cargo && this.cargo.cbo ? this.cargo.cbo.descricao : '';
    }

    descricaoFuncao(): string {
        return this.funcao && this.funcao.funcao ? this.funcao.funcao.descricao : '';
    }

    existeSetor() {
        return this.setor && this.setor.id !== undefined;
    }

    existeCargo() {
        return this.cargo && this.cargo.id !== undefined;
    }

    existeFuncao() {
        return this.funcao && this.funcao.id !== undefined;
    }

    removerSetor() {
        this.setor = new EmpresaSetor();
    }

    removerCargo() {
        this.cargo = new EmpresaCbo();
    }

    removerFuncao() {
        this.funcao = new EmpresaFuncao();
    }

    public mostrarContainer() {
        return !this.isVazia(this.unidadeObra);
    }

    hasPermissaoCadastrar() {
        return this.hasPermissao(PermissoesEnum.EMPRESA_LOTACAO_CADASTRAR)
            || this.hasPermissao(PermissoesEnum.EMPRESA_LOTACAO);
    }

    hasPermissaoDesativar() {
        return this.hasPermissao(PermissoesEnum.EMPRESA_LOTACAO_DESATIVAR)
            || this.hasPermissao(PermissoesEnum.EMPRESA_LOTACAO);
    }

}
