import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { Empresa } from './../../../modelo/empresa.model';
import { DatePicker } from './../../../compartilhado/utilitario/date-picker';
import { EmpresaTrabalhadorLotacaoService } from './../../../servico/empresa-trabalhador-lotacao.service';
import { EmpresaTrabalhador } from 'app/modelo/empresa-trabalhador.model';
import { FiltroLotacao } from './../../../modelo/filttro-lotacao.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ToastyService } from 'ng2-toasty';
import { EmpresaService } from 'app/servico/empresa.service';
import { ActivatedRoute, Router } from '@angular/router';
import { EmpresaJornada } from './../../../modelo/empresaJornada.model';
import { EmpresaFuncao } from './../../../modelo/empresa-funcao.model';
import { EmpresaCbo } from './../../../modelo/empresa-cbo.model';
import { FiltroEmpresa } from 'app/modelo/filtro-empresa.model';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit } from '@angular/core';
import { UnidadeObra } from '../../../modelo/unidade-obra.model';
import { EmpresaSetor } from 'app/modelo/empresa-setor.model';
import { EmpresaLotacao } from 'app/modelo/empresa-lotacao.model';
import { Paginacao } from '../../../modelo/paginacao.model';
import { EmpresaLotacaoService } from '../../../servico/empresa-lotacao.service';
import { EmpresaJornadaService } from '../../../servico/empresa-jornada.service';
import { BloqueioService } from '../../../servico/bloqueio.service';
import { UnidadeObraService } from 'app/servico/unidade-obra.service';
import { DialogService } from 'ng2-bootstrap-modal';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { EmpresaTrabalhadorService } from '../../../servico/empresa-trabalhador.service';
import { EmpresaTrabalhadorLotacao } from '../../../modelo/empresa-trabalhador-lotacao.model';

@Component({
    selector: 'app-lotacao-trabalhador',
    templateUrl: './lotacao-trabalhador.component.html',
    styleUrls: ['./lotacao-trabalhador.component.scss'],
})
export class LotacaoTrabalhadorComponent extends BaseComponent implements OnInit {
    public title: string;
    public idEmpresa: number;
    public empresa: Empresa;
    public emFiltro: FiltroEmpresa;
    private lotacaoFiltro: FiltroLotacao;

    public idEmpresaTrabalhador: number;
    public empresaTrabalhador: EmpresaTrabalhador;
    public empresaTrabalhadorLotacaoSelected: EmpresaTrabalhadorLotacao;
    public listEmpresaTrabalhadorLotacao: EmpresaTrabalhadorLotacao[];
    public modelEmpresaTrabalhadorLotacao: EmpresaTrabalhadorLotacao;
    dataAssociacao: any;
    dataDesligamento: any;

    public unidadeObraList: UnidadeObra[];
    public unidadeObra: UnidadeObra;

    public setor: EmpresaSetor;
    public cargo: EmpresaCbo;
    public funcao: EmpresaFuncao;
    public jornada: EmpresaJornada;
    public jornadas: EmpresaJornada[];
    edicao: boolean;

    public empresasLotacao: EmpresaLotacao[];
    public empresasLotacaoPorPagina: EmpresaLotacao[];
    public paginacao: Paginacao = new Paginacao(1, 10);
    public itensCarregados: number;
    public totalItens: number;

    empresaLotacaoSelecionada = {
        valor: false,
    };

    empresaTrabalhadorLotacaoSelecionada = {
        valor: false,
    };

    indexEdicao = -1;
    paginacaoEmpresaLotacao: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);

    paginacaoEmpresaTrabalhadorLotacao: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);
    public itensCarregadosEmpresaTrabalhadorLotacao: number;
    public totalItensEmpresaTrabalhadorLotacao: number;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private empresaService: EmpresaService,
        private empresaLotacaoService: EmpresaLotacaoService,
        private serviceEmpresaJornada: EmpresaJornadaService,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private associacaoTrabalhadorLotacaoService: EmpresaTrabalhadorLotacaoService,
        private empresaTrabalhadorService: EmpresaTrabalhadorService,
        private unidadeObraService: UnidadeObraService,
        private dialogService: DialogService) {
        super(bloqueioService, dialogo);
        this.getIdEmpresa();
        this.emModoConsulta();
        this.setEmpresa();
        this.buscarJornadas();
        this.setUnidadeObras();
        this.setUnidadeObras();
        this.title = MensagemProperties.app_rst_empresa_trabalhador_lotacao_title;
    }

    public getListEmpresaTrabalhadorLotacao() {
        this.listEmpresaTrabalhadorLotacao = new Array<EmpresaTrabalhadorLotacao>();
        this.associacaoTrabalhadorLotacaoService.pesquisarEmpresaTrabalhadorLotacao(this.idEmpresaTrabalhador,
            this.idEmpresa, this.paginacaoEmpresaTrabalhadorLotacao).subscribe((retorno: ListaPaginada<EmpresaTrabalhadorLotacao>) => {
                this.setListaEmpresaTrabalhadorLotacao(retorno);
            }, (error) => {
                this.mensagemError(error);
            });
    }

    private emModoConsulta() {
        this.modoConsulta = !Seguranca.isPermitido([
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CADASTRAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_ALTERAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_DESATIVAR]);
    }

    private setListaEmpresaTrabalhadorLotacao(retorno: ListaPaginada<EmpresaTrabalhadorLotacao>) {
        if (retorno.list != null) {
            this.totalItensEmpresaTrabalhadorLotacao = retorno.quantidade;
            this.listEmpresaTrabalhadorLotacao = retorno.list;
            this.totalItensEmpresaTrabalhadorLotacao = this.listEmpresaTrabalhadorLotacao.length;
            this.setItensPorPagina();
        } else {
            this.totalItensEmpresaTrabalhadorLotacao = 0;
            this.listEmpresaTrabalhadorLotacao = new Array<EmpresaTrabalhadorLotacao>();
            this.totalItensEmpresaTrabalhadorLotacao = 0;
            this.totalItensEmpresaTrabalhadorLotacao = 0;
            this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
    }

    ngOnInit() {
        this.empresaLotacaoSelecionada.valor = false;
        this.unidadeObraList = new Array<UnidadeObra>();
        this.setor = new EmpresaSetor();
        this.cargo = new EmpresaCbo();
        this.funcao = new EmpresaFuncao();
        this.jornada = new EmpresaJornada();
        this.empresa = new Empresa();
    }

    private setEmpresa() {
        this.empresaService.pesquisarPorId(this.idEmpresa).subscribe((retorno: Empresa) => {
            this.empresa = retorno;
        }, (error) => { this.mensagemErroComParametros(MensagemProperties.app_rst_erro_geral); });
    }

    private setUnidadeObras() {
        this.unidadeObraService.pesquisarPorEmpresa(this.idEmpresa).subscribe((retorno: UnidadeObra[]) => {
            this.unidadeObraList = retorno;
        }, (error) => {
            this.mensagemError(error);
        });
    }

    pesquisarEmpresasLotacao() {
        this.empresasLotacao = new Array<EmpresaLotacao>();
        this.setItensPorPagina();
        this.emFiltro.idUnidadeObra = this.unidadeObra.id;
    }

    private getIdEmpresa() {
        this.route.params.subscribe((params) => {
            this.idEmpresaTrabalhador = params['idEmpresaTrabalhador'];
            this.idEmpresa = params['id'];
            if (this.idEmpresa) {
                this.emFiltro = new FiltroEmpresa();
                this.emFiltro.id = this.idEmpresa;
            }

            this.empresaTrabalhador = new EmpresaTrabalhador();
            if (this.idEmpresaTrabalhador) {
                this.empresaTrabalhadorService.buscarPorId(this.idEmpresaTrabalhador).
                    subscribe((retorno: EmpresaTrabalhador) => {
                        this.empresaTrabalhador = retorno;
                        this.getListEmpresaTrabalhadorLotacao();
                    }, (error) => {
                        this.mensagemError(error);
                    });
            }
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
        this.paginacaoEmpresaLotacao = new Paginacao(event.page, 10);
        this.empresaLotacaoService.pesquisarEmpresasLotacoes(this.emFiltro, this.paginacaoEmpresaLotacao).
            subscribe((retorno: ListaPaginada<EmpresaLotacao>) => {
                this.setLista(retorno);
            }, (error) => {
                this.mensagemError(error);
            });
    }

    private setLista(retorno: ListaPaginada<EmpresaLotacao>) {
        if (retorno.list != null) {
            this.totalItens = retorno.quantidade;
            this.empresasLotacao = retorno.list;
            this.itensCarregados = this.empresasLotacao.length;
            this.setItensPorPagina();
        } else {
            this.totalItens = 0;
            this.empresasLotacao = undefined;
            this.itensCarregados = 0;
            this.setItensPorPagina();
            this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
    }

    private setItensPorPagina() {
        this.empresasLotacaoPorPagina = this.empresasLotacao;
    }

    voltar(): void {
        if (this.route.snapshot.url[0].path === 'minhaempresa') {
            // tslint:disable-next-line:max-line-length
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa/${this.emFiltro.id}/trabalhador/associar/${this.idEmpresaTrabalhador}`]);
        } else {
            // tslint:disable-next-line:max-line-length
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.emFiltro.id}/trabalhador/associar/${this.idEmpresaTrabalhador}`]);
        }
    }

    prepareFilter() {
        this.lotacaoFiltro = new FiltroLotacao();
        this.lotacaoFiltro.idEmpresa = this.idEmpresa;
        if (this.unidadeObra) {
            this.lotacaoFiltro.idUnidadeObra = this.unidadeObra.id;
        } else {
            this.lotacaoFiltro.idUnidadeObra = undefined;
        }
        if (this.setor) {
            this.lotacaoFiltro.idSetor = this.setor.id;
        } else {
            this.lotacaoFiltro.idSetor = undefined;
        }
        if (this.cargo) {
            this.lotacaoFiltro.idCargo = this.cargo.id;
        } else {
            this.lotacaoFiltro.idCargo = undefined;
        }
        if (this.funcao) {
            this.lotacaoFiltro.idFuncao = this.funcao.id;
        } else {
            this.lotacaoFiltro.idFuncao = undefined;
        }
        if (this.cargo && this.jornada) {
            this.lotacaoFiltro.idJornada = this.jornada.id;
        } else {
            this.lotacaoFiltro.idJornada = undefined;
        }
    }

    validarCampos() {        
        let retorno = true;
        if (!this.lotacaoFiltro.idUnidadeObra && !this.lotacaoFiltro.idCargo
            && !this.lotacaoFiltro.idSetor && !this.lotacaoFiltro.idJornada
            && !this.lotacaoFiltro.idFuncao) {
            this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
            retorno = false;
        }
        return retorno;
    }

    selecionarLotacao(item: EmpresaLotacao) {
        this.empresaTrabalhadorLotacaoSelected = new EmpresaTrabalhadorLotacao();
        this.empresaTrabalhadorLotacaoSelected.empresaLotacao = item;
        this.empresaTrabalhadorLotacaoSelected.empresaTrabalhador = this.empresaTrabalhador;
        this.empresaLotacaoSelecionada.valor = true;
        this.empresaTrabalhadorLotacaoSelecionada.valor = false;
    }

    pesquisar() {
        this.prepareFilter();
        if (this.validarCampos()) {
            this.empresaLotacaoService.pesquisarEmpresasLotacoesTrabalhador(this.lotacaoFiltro, new Paginacao(1, 10))
                .subscribe((retorno: ListaPaginada<EmpresaLotacao>) => {
                    this.setLista(retorno);
                }, (error) => {
                    this.mensagemError(error);
                });

        }
    }

    public removerItem(lot: EmpresaLotacao) {
        const index: number = this.empresasLotacao.indexOf(lot);
        if (index !== -1) {
            this.empresasLotacao.splice(index, 1);
        }
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

    selecionarEmpresaTrabalhadorLotacao(item: EmpresaTrabalhadorLotacao, index: any) {
        if (index != null) {
            this.modelEmpresaTrabalhadorLotacao = new EmpresaTrabalhadorLotacao(item);
            this.dataAssociacao = this.modelEmpresaTrabalhadorLotacao.dataAssociacao ?
                DatePicker.convertDateForMyDatePicker(this.modelEmpresaTrabalhadorLotacao.dataAssociacao) : null;
            this.dataDesligamento = this.modelEmpresaTrabalhadorLotacao.dataDesligamento ?
                DatePicker.convertDateForMyDatePicker(this.modelEmpresaTrabalhadorLotacao.dataDesligamento) : null;
            this.edicao = true;
            this.empresaTrabalhadorLotacaoSelecionada.valor = true;
            this.empresaLotacaoSelecionada.valor = false;
            this.indexEdicao = index;
        }
    }

    pageChangedEmpresaTrabalhadorLotacao(event: any): void {
        this.paginacaoEmpresaTrabalhadorLotacao.pagina = event.page;
        this.pesquisarEmpresaTrabalhadorLotacao();
    }

    private pesquisarEmpresaTrabalhadorLotacao() {
        this.associacaoTrabalhadorLotacaoService.pesquisarEmpresaTrabalhadorLotacao(this.idEmpresaTrabalhador,
            this.idEmpresa, this.paginacaoEmpresaTrabalhadorLotacao).subscribe((retorno: ListaPaginada<EmpresaTrabalhadorLotacao>) => {
                this.setListaEmpresaTrabalhadorLotacao(retorno);
            }, (error) => {
                this.mensagemError(error);
            });
    }

    removerEmpresaTrabalhadorLotacao(item: any): void {
        this.associacaoTrabalhadorLotacaoService.desativarEmpresaTrabalhadorLotacao(item)
            .subscribe((retorno: EmpresaTrabalhadorLotacao) => {
                this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
                this.paginacaoEmpresaTrabalhadorLotacao.pagina = 1;
                this.pesquisarEmpresaTrabalhadorLotacao();
            }, (error) => {
                this.mensagemError(error);
            }, () => {

            });
    }

}
