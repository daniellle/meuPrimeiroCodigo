import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { ToastyService } from 'ng2-toasty';
import { FormBuilder } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MascaraUtil } from './../../../compartilhado/utilitario/mascara.util';
import { Component, OnInit, Input } from '@angular/core';
import { FiltroSindicato } from '../../../modelo/filtro-sindicato.model';
import { BaseComponent } from '../../base.component';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { DialogService } from 'ng2-bootstrap-modal';
import { SindicatoService } from '../../../servico/sindicato.service';
import { Sindicato } from '../../../modelo/sindicato.model';
import { Situacao } from 'app/modelo/enum/enum-situacao.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
    selector: 'app-sindicatos-pesquisar',
    templateUrl: './sindicatos-pesquisar.component.html',
    styleUrls: ['./sindicatos-pesquisar.component.scss'],
})
export class SindicatosPesquisarComponent extends BaseComponent implements OnInit {

    @Input() public filtro: FiltroSindicato;

    public mascaraCNPJ = MascaraUtil.mascaraCnpj;

    public sindicatos: Sindicato[];

    public msg: string;

    public situacoes = Situacao;
    public keysSituacao: string[];

    constructor(
        private router: Router, private fb: FormBuilder, private route: ActivatedRoute,
        protected bloqueioService: BloqueioService, private service: SindicatoService,
        protected dialogo: ToastyService, private dialogService: DialogService,
        private activatedRoute: ActivatedRoute) {
        super(bloqueioService, dialogo);
        this.title = MensagemProperties.app_rst_sindicatos_title_pesquisar;
        this.keysSituacao = Object.keys(this.situacoes);

    }

    ngOnInit() {
        this.filtro = new FiltroSindicato();
        this.filtro.situacao = '';
        this.modoConsulta = !Seguranca.isPermitido([PermissoesEnum.SINDICATO,
        PermissoesEnum.SINDICATO_CADASTRAR,
        PermissoesEnum.SINDICATO_ALTERAR,
        PermissoesEnum.SINDICATO_DESATIVAR]);
    }

    pesquisar() {
        if (this.validarCampos()) {
            this.paginacao.pagina = 1;
            this.removerMascara();
            this.service.pesquisar(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Sindicato>) => {
                this.sindicatos = retorno.list;
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
        if (this.filtro.cnpj !== undefined) {
            this.filtro.cnpj = MascaraUtil.removerMascara(this.filtro.cnpj);
        }
    }

    public pageChanged(event: any): void {
        this.paginacao.pagina = event.page;
        this.service.pesquisar(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Sindicato>) => {
            this.sindicatos = retorno.list;
        });
    }

    formatarCampos(pro: Sindicato[]): void {
        pro.forEach((sindicato) => {
            sindicato.dataDesativacao === undefined ? sindicato.situacao = Situacao.A : sindicato.situacao = Situacao.I;
        });
    }

    selecionar(model: any) {
        if (model && model.id) {
            this.router.navigate([model.id], { relativeTo: this.activatedRoute });
        }
    }

    public isVazia(valor: any): boolean {
        return valor === undefined || valor === null || valor === '';
    }

    validarCampos(): Boolean {
        let verificador: Boolean = true;

        if (this.isVazia(this.filtro.cnpj) && this.isVazia(this.filtro.nomeFantasia) && this.isVazia(this.filtro.razaoSocial)) {
            this.mensagemError(MensagemProperties.app_rst_msg_pesquisar_todos_vazios);
            verificador = false;
        }

        if (!this.isVazia(this.filtro.cnpj)) {
            if (MascaraUtil.removerMascara(this.filtro.cnpj).length < 14) {
                this.mensagemError(MensagemProperties.app_rst_labels_cnpj_incompleto);
                verificador = false;
            }
            this.filtro.cnpj = MascaraUtil.removerMascara(this.filtro.cnpj);
        }
        return verificador;
    }

    incluir() {
        this.router.navigate(['cadastrar'], { relativeTo: this.activatedRoute });
    }

    hasPermissaoCadastro() {
        return Seguranca.isPermitido([PermissoesEnum.SINDICATO,
            PermissoesEnum.SINDICATO_CADASTRAR]);
    }
}
