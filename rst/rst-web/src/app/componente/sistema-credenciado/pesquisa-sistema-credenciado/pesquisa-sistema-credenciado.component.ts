import { Component, OnInit } from '@angular/core';
import { FiltroSistemaCredenciado } from 'app/modelo/filtro-sistema-credenciado.model';
import { SistemaCredenciado } from 'app/modelo/sistema-credenciado.model';
import { BaseComponent } from 'app/componente/base.component';
import { DialogService } from 'ng2-bootstrap-modal';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { SistemaCredenciadoService } from 'app/servico/sistema-credenciado.service';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { Router, ActivatedRoute } from '@angular/router';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
    selector: 'app-pesquisa-sistema-credenciado',
    templateUrl: './pesquisa-sistema-credenciado.component.html',
    styleUrls: ['./pesquisa-sistema-credenciado.component.scss'],
})
export class PesquisaSistemaCredenciadoComponent extends BaseComponent implements OnInit {

    public filtroSistemaCredenciado: FiltroSistemaCredenciado;
    public sistemasCredenciados: SistemaCredenciado[];

    constructor(
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private dialogService: DialogService,
        private sistemaCredenciadoService: SistemaCredenciadoService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
    ) {
        super(bloqueioService, dialogo);
    }

    ngOnInit() {
        this.filtroSistemaCredenciado = new FiltroSistemaCredenciado();
        this.title = MensagemProperties.app_rst_sistema_credenciado_pesquisar;
    }

    pesquisar() {
        this.sistemasCredenciados = new Array<SistemaCredenciado>();
        if (this.validarCampos()) {
            this.removerMascara();
            this.paginacao.pagina = 1;
            this.sistemaCredenciadoService.findPaginado(this.filtroSistemaCredenciado, this.paginacao)
                .subscribe((retorno: ListaPaginada<SistemaCredenciado>) => {
                    this.sistemasCredenciados = retorno.list;
                    this.paginacao = this.getPaginacao(this.paginacao, retorno);
                    if (retorno.quantidade === 0) {
                        this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
                    }
                }, (error) => {
                    this.mensagemError(error);
                });
        }
    }

    removerMascara() {
        if (this.filtroSistemaCredenciado.cnpj !== undefined) {
            this.filtroSistemaCredenciado.cnpj = MascaraUtil.removerMascara(this.filtroSistemaCredenciado.cnpj);
        }
    }

    validarCampos(): Boolean {
        let verificador: Boolean = true;
        if (!this.isVazia(this.filtroSistemaCredenciado.cnpj)) {
            if (MascaraUtil.removerMascara(this.filtroSistemaCredenciado.cnpj).length < 14) {
                this.mensagemError(MensagemProperties.app_rst_labels_cnpj_incompleto);
                verificador = false;
            }
            this.filtroSistemaCredenciado.cnpj = MascaraUtil.removerMascara(this.filtroSistemaCredenciado.cnpj);
        }
        return verificador;
    }

    incluir() {
        this.router.navigate(['cadastrar'], { relativeTo: this.activatedRoute });
    }

    hasPermissaoCadastro() {
        return Seguranca.isPermitido([PermissoesEnum.SISTEMA_CREDENCIADO_CADASTRAR]);
    }

    pageChanged(event: any): void {
        this.removerMascara();
        this.paginacao.pagina = event.page;
        this.sistemaCredenciadoService.findPaginado(this.filtroSistemaCredenciado, this.paginacao)
            .subscribe((retorno: ListaPaginada<SistemaCredenciado>) => {
                this.sistemasCredenciados = retorno.list;
            }, (error) => {
                this.mensagemError(error);
            });
    }

    selecionar(model: any) {
        if (model && model.id) {
            this.router.navigate([model.id], { relativeTo: this.activatedRoute });
        }
    }
}
