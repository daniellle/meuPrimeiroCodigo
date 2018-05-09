import {PermissoesEnum} from 'app/modelo/enum/enum-permissoes';
import {FiltroDepartRegional} from 'app/modelo/filtro-depart-regional.model';
import {Seguranca} from './../../../compartilhado/utilitario/seguranca.model';
import {DepartRegionalService} from './../../../servico/depart-regional.service';
import {Component, Input, OnInit} from '@angular/core';
import {Uat} from 'app/modelo/uat.model';
import {ActivatedRoute, Router} from '@angular/router';
import {BloqueioService} from 'app/servico/bloqueio.service';
import {ToastyService} from 'ng2-toasty';
import {DialogService} from 'ng2-bootstrap-modal';
import {BaseComponent} from 'app/componente/base.component';
import {MensagemProperties} from 'app/compartilhado/utilitario/recurso.pipe';
import {FormBuilder} from '@angular/forms';
import {FiltroUat} from 'app/modelo/filtro-uat.model';
import {MascaraUtil} from '../../../compartilhado/utilitario/mascara.util';
import {ListaPaginada} from 'app/modelo/lista-paginada.model';
import {UatService} from 'app/servico/uat.service';
import {DepartamentoRegional} from 'app/modelo/departamento-regional.model';
import {Situacao} from 'app/modelo/enum/enum-situacao.model';

@Component({
    selector: 'app-pesquisa-uat',
    templateUrl: './pesquisa-uat.component.html',
    styleUrls: ['./pesquisa-uat.component.scss'],
})
export class PesquisaUatComponent extends BaseComponent implements OnInit {

    @Input() public filtroUat: FiltroUat;
    public uats: Uat[];
    public uatPorPagina: Uat[];
    public departamentos: DepartamentoRegional[];

    public situacoes = Situacao;
    public keysSituacao: string[];

    constructor(
        private router: Router, private service: UatService, private fb: FormBuilder,
        protected bloqueioService: BloqueioService,
        private departamentoService: DepartRegionalService,
        protected dialogo: ToastyService,
        private dialogService: DialogService,
        private activatedRoute: ActivatedRoute) {
        super(bloqueioService, dialogo);
        this.keysSituacao = Object.keys(this.situacoes);
    }

    ngOnInit() {
        this.filtroUat = new FiltroUat();
        this.filtroUat.idDepRegional = undefined;
        this.title = MensagemProperties.app_rst_uat_title_pesquisar;
        this.buscarDepartamentos();
    }

    hasPermissaoCadastro() {
        return Seguranca.isPermitido([PermissoesEnum.CAT,
            PermissoesEnum.CAT_CADASTRAR]);
    }

    selecionar(model: any) {
        if (model && model.id) {
            this.router.navigate([model.id], {relativeTo: this.activatedRoute});
        }
    }

    incluir() {
        this.router.navigate(['cadastrar'], {relativeTo: this.activatedRoute});
    }

    buscarDepartamentos() {
        this.departamentoService.listarTodos(new FiltroDepartRegional()).subscribe((dados: any) => {
            this.departamentos = dados;
        }, (error) => {
            this.mensagemError(error);
        });
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

    pesquisar() {
        this.uats = new Array<Uat>();
        if (this.validarCampos()) {
            this.removerMascara();
            this.paginacao.pagina = 1;
            this.service.pesquisar(this.filtroUat, this.paginacao).subscribe((retorno: ListaPaginada<Uat>) => {
                if (retorno.quantidade === 0) {
                    this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
                }else{
                    this.uats = retorno.list;
                    this.paginacao = this.getPaginacao(this.paginacao, retorno);
                }
            }, (error) => {
                this.mensagemError(error);
            });
        }
    }

    removerMascara() {
        if (this.filtroUat.cnpj !== undefined) {
            this.filtroUat.cnpj = MascaraUtil.removerMascara(this.filtroUat.cnpj);
        }
    }

    public pageChanged(event: any): void {
        this.removerMascara();
        this.paginacao.pagina = event.page;
        this.service.pesquisar(this.filtroUat, this.paginacao).subscribe((retorno: ListaPaginada<Uat>) => {
            this.uats = retorno.list;
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
