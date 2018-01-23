import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { environment } from './../../../../environments/environment';
import { Jornada } from 'app/modelo/jornada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { EmpresaJornadaService } from './../../../servico/empresa-jornada.service';
import { FiltroEmpresa } from './../../../modelo/filtro-empresa.model';
import { Input } from '@angular/core';
import { JornadaFilter } from './../../../modelo/filtro-jornada.model';
import { EmpresaService } from './../../../servico/empresa.service';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { BloqueioService } from '../../../servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { JornadaService } from '../../../servico/jornada.service';
import { EmpresaJornada } from '../../../modelo/empresaJornada.model';
import { ListaPaginada } from '../../../modelo/lista-paginada.model';

export interface IHash {
    [details: number]: boolean;
}

@Component({
    selector: 'app-empresa-jornada',
    templateUrl: './empresa-jornada.component.html',
    styleUrls: ['./empresa-jornada.component.scss'],
})
export class EmpresaJornadaComponent extends BaseComponent implements OnInit {

    public title: string;

    // Jornada
    @Input() public filtro: JornadaFilter;
    public filtroPage: JornadaFilter;
    public jornadas: Jornada[];
    public jornadasSelecionadas = Array<Jornada>();
    public checks: IHash = {};

    // Jornada Empresa
    public idEmpresa: number;
    public emFiltro: FiltroEmpresa;
    public empresasJornadas: EmpresaJornada[];
    public paginacaoEmpresaJornada: Paginacao = new Paginacao(1, 10);

    constructor(
        private route: ActivatedRoute, private router: Router,
        private empresaService: EmpresaService, private fb: FormBuilder,
        protected bloqueioService: BloqueioService, protected dialogo: ToastyService,
        private dialogService: DialogService, private serviceJornada: JornadaService,
        private serviceEmpresaJornada: EmpresaJornadaService) {
        super(bloqueioService, dialogo);
        this.iniciarListas();
        this.title = 'Jornada';
    }

    ngOnInit() {
        this.iniciarListas();
        this.getIdEmpresa();
    }

    private iniciarListas() {
        this.filtro = new JornadaFilter();
        this.filtroPage = new JornadaFilter();
        this.emFiltro = new FiltroEmpresa();
        this.jornadasSelecionadas = new Array<Jornada>();
        this.empresasJornadas = new Array<EmpresaJornada>();
        this.jornadas = new Array<Jornada>();
    }

    private getIdEmpresa() {
        this.route.params.subscribe((params) => {
            this.idEmpresa = params['id'];
            if (this.idEmpresa) {
                this.emFiltro = new FiltroEmpresa();
                this.emFiltro.id = this.idEmpresa;
                this.pesquisarEmpresasJornadas();
            }
        }, (error) => {
            this.mensagemError(error);
          });
    }

    private pesquisarEmpresasJornadas() {
        this.empresasJornadas = new Array<EmpresaJornada>();
        this.emFiltro.id = this.idEmpresa;
        this.paginacaoEmpresaJornada.pagina = 1;
        this.serviceEmpresaJornada.pesquisar(this.emFiltro, this.paginacaoEmpresaJornada)
            .subscribe((retorno: ListaPaginada<EmpresaJornada>) => {
                this.paginacaoEmpresaJornada = this.getPaginacao(this.paginacaoEmpresaJornada, retorno);
                this.verificarRetornoEmpresasJornada(retorno);
            }, (error) => {
                this.mensagemError(error);
            });
    }

    verificarRetornoJornada(retorno: ListaPaginada<Jornada>) {
        if (retorno && retorno.list) {
            this.jornadas = retorno.list;
        } else {
            this.jornadas = new Array<Jornada>();
            this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
    }
    verificarRetornoEmpresasJornada(retorno: ListaPaginada<EmpresaJornada>) {
        if (retorno && retorno.list) {
            this.empresasJornadas = retorno.list;
        } else {
            this.empresasJornadas = new Array<EmpresaJornada>();
        }
    }

    private validarCampos(): Boolean {
        let verificador: Boolean = true;

        if (this.isVazia(this.filtro.turno) && !this.filtro.qtdHoras) {
            this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
            verificador = false;
        }

        return verificador;
    }

    public pesquisarJornada() {
        this.jornadas = new Array<Jornada>();
        this.setFiltro(this.filtro);
        if (this.validarCampos()) {
            this.pesquisarJornadaService(1, true, this.filtroPage);
        }
    }

    private setFiltro(filter: JornadaFilter) {
        this.filtroPage.turno = filter.turno;
        this.filtroPage.qtdHoras = filter.qtdHoras;
    }

    verificarSelecionados(jornada: Jornada) {
        if (this.jornadasSelecionadas.indexOf(jornada) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public selectDeselectJornada(jornada: Jornada, event: any, i: number) {
        if (jornada && event.checked) {
            this.jornadasSelecionadas.push(jornada);
            this.checks[i] = true;
        } else {
            const index: number = this.jornadasSelecionadas.indexOf(jornada);
            if (index !== -1) {
                this.jornadasSelecionadas.splice(index, 1);
            }
            this.checks[i] = false;

        }
    }

    public add() {
        if (!this.isEmpty(this.jornadasSelecionadas)) {
            this.jornadasSelecionadas.forEach((element) => {
                const obj = new EmpresaJornada();
                obj.jornada = element;
                this.empresasJornadas.push(obj);
                this.totalItens = this.totalItens + 1;
            });
            this.jornadasSelecionadas = new Array<Jornada>();
            this.checks = {};
            this.salvar();

        } else {
            this.mensagemError(MensagemProperties.app_rst_selecione_um_item);
        }
    }

    public isListaVazia(): boolean {
        return this.jornadas === undefined || this.jornadas.length === 0;
    }

    public removeJornada(Jor: EmpresaJornada) {
        const index: number = this.empresasJornadas.indexOf(Jor);
        if (index !== -1) {
            this.empresasJornadas.splice(index, 1);
            this.totalItens = this.totalItens - 1;
            this.checks[Jor.jornada.id] = false;
        }
    }

    pageChanged(event: any): void {
        this.pesquisarJornadaService(event.page, true, this.filtroPage);
    }

    private pesquisarJornadaService(page: any, msg: boolean, filter: JornadaFilter) {
        filter.idEmpresa = this.idEmpresa;
        this.paginacao.pagina = page;
        this.serviceJornada.pesquisar(filter, this.paginacao).subscribe((retorno: ListaPaginada<Jornada>) => {
            this.paginacao = this.getPaginacao(this.paginacao, retorno);
            this.verificarRetornoJornada(retorno);
        }, (error) => {
            this.mensagemError(error);
        }, () => {

        });
    }

    pageChangedJornadaEmpresa(event: any): void {
        this.emFiltro.id = this.idEmpresa;
        this.paginacaoEmpresaJornada.pagina = event.page;
        this.serviceEmpresaJornada.pesquisar(this.emFiltro, this.paginacaoEmpresaJornada)
            .subscribe((retorno: ListaPaginada<EmpresaJornada>) => {
                this.paginacaoEmpresaJornada = this.getPaginacao(this.paginacaoEmpresaJornada, retorno);
                this.verificarRetornoEmpresasJornada(retorno);
            }, (error) => {
                this.mensagemError(error);
            });
    }

    public salvar() {
        this.empresaService.salvarJornadas(this.idEmpresa, this.empresasJornadas).subscribe((response: EmpresaJornada[]) => {
            this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
            this.empresasJornadas = response;
        }, (error) => {
            this.mensagemError(error);
            this.pesquisarEmpresasJornadas();
        }, () => {
            this.pesquisarEmpresasJornadas();
        });
    }

    public desativarEmpresaJornada(empresaJornada: EmpresaJornada) {
        this.serviceEmpresaJornada.desativarEmpresaJornada(empresaJornada).subscribe((response: EmpresaJornada) => {
            this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
            this.removeJornada(empresaJornada);
        }, (error) => {
            this.mensagemError(error);
        }, () => {
            this.pesquisarEmpresasJornadas();
        });

    }

    voltar(): void {
        if (this.route.snapshot.url[0].path === 'minhaempresa') {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa`]);
        } else {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}`]);
        }
    }

    hasPermissaoCadastrar() {
        return this.hasPermissao(PermissoesEnum.EMPRESA_JORNADA_CADASTRAR)
            || this.hasPermissao(PermissoesEnum.EMPRESA_JORNADA);
    }

    hasPermissaoDesativar() {
        return this.hasPermissao(PermissoesEnum.EMPRESA_JORNADA_DESATIVAR)
            || this.hasPermissao(PermissoesEnum.EMPRESA_JORNADA);
    }
}
