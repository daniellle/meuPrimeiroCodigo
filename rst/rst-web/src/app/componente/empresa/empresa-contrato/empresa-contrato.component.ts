import {Component, Input, OnInit} from '@angular/core';
import {BaseComponent} from "../../base.component";
import {ToastyService} from "ng2-toasty";
import {EmpresaService} from "../../../servico/empresa.service";
import {DialogService} from "ng2-bootstrap-modal";
import {BloqueioService} from "../../../servico/bloqueio.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MensagemProperties} from "../../../compartilhado/utilitario/recurso.pipe";
import {EmpresaContratoService} from "../../../servico/empresa-contrato.service";
import {environment} from "../../../../environments/environment";
import {Paginacao} from "../../../modelo/paginacao.model";
import {FiltroEmpresa} from "../../../modelo/filtro-empresa.model";
import {Contrato} from "../../../modelo/contrato.model";
import {FiltroEmpresaContrato} from "../../../modelo/filtro-empresa-contrato.model";
import {ListaPaginada} from "../../../modelo/lista-paginada.model";
import {FormGroup} from "@angular/forms";
import {Usuario} from "../../../modelo/usuario.model";
import {Seguranca} from "../../../compartilhado/utilitario/seguranca.model";
import {PerfilEnum} from "../../../modelo/enum/enum-perfil";

export interface IHash {
    [details: number]: boolean;
}


@Component({
    selector: 'app-empresa-contrato',
    templateUrl: './empresa-contrato.component.html',
    styleUrls: ['./empresa-contrato.component.scss']
})
export class EmpresaContratoComponent extends BaseComponent implements OnInit {

    public title: string;
    @Input() public filtro: FiltroEmpresaContrato;
    public filtroPage: FiltroEmpresaContrato;
    public idEmpresa: number;
    public emFiltro: FiltroEmpresa;
    public paginacaoEmpresaContrato: Paginacao = new Paginacao(1, 10);
    public checks: IHash = {};
    public contratos: Contrato[];
    public statusForm: FormGroup;
    public usuarioLogado: Usuario;
    public flagUsuario: string;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private empresaService: EmpresaService,
                protected bloqueioService: BloqueioService,
                protected dialogo: ToastyService,
                private dialogService: DialogService,
                private empresaContratoService: EmpresaContratoService) {
        super(bloqueioService, dialogo);
        this.title = MensagemProperties.app_rst_empresa_contrato_title;
    }

    ngOnInit() {
        this.usuarioLogado = Seguranca.getUsuario();
        this.getIdEmpresa();
        this.pesquisarContratos();
    }

    getIdEmpresa() {
        this.idEmpresa = this.route.snapshot.params['id'];
        if (this.idEmpresa) {
            this.filtro = new FiltroEmpresaContrato();
            this.filtroPage = new FiltroEmpresaContrato();
            this.filtro.idEmpresa = this.idEmpresa;
        }
    }

    verPerfil() {
        this.usuarioLogado.papeis.forEach(perfil => {
            if (perfil === PerfilEnum.ADM || perfil === PerfilEnum.GDNA || perfil === PerfilEnum.DIDN) {
                this.flagUsuario = "3";
            }
            else if (perfil === PerfilEnum.SUDR || perfil === PerfilEnum.DIDR || perfil === PerfilEnum.GDRA
                || perfil === PerfilEnum.GDRM) {
                this.flagUsuario == "2";
            }
        });
    }


    voltar() {
        if (this.route.snapshot.url[0].path === 'minhaempresa') {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa`]);
        } else {
            this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}`]);
        }
    }

    criarCadastro() {
        this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}/contrato/cadastrarcontrato`]);
    }


    public isListaVazia(): boolean {
        return this.contratos === undefined || this.contratos.length === 0;
    }

    pesquisarContratos() {
        this.contratos = new Array<Contrato>();
        this.filtro.idEmpresa = this.idEmpresa;
        this.empresaContratoService.pesquisarContratos(this.filtro, new Paginacao(1, 10))
            .subscribe((retorno: ListaPaginada<Contrato>) => {
                this.paginacaoEmpresaContrato = this.getPaginacao(this.paginacao, retorno)
                this.verificarRetornoEmpresasContrato(retorno);
            }, (error) => {
                this.mensagemError(error);
            });
    }

    verificarRetornoEmpresasContrato(retorno: ListaPaginada<Contrato>) {
        if (retorno && retorno.list) {
            this.contratos = retorno.list;
            console.log(this.contratos);
        } else {
            this.contratos = new Array<Contrato>();
        }
    }

    mudaStatus(value, contratoId: number) {
        console.log(value)
        console.log(contratoId)
    }

}
