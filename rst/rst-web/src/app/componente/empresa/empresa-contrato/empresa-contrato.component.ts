import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
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
import {FiltroDepartRegional} from "../../../modelo/filtro-depart-regional.model";
import {UsuarioEntidade} from "../../../modelo/usuario-entidade.model";
import {UsuarioEntidadeService} from "../../../servico/usuario-entidade.service";
import {FiltroUsuarioEntidade} from "../../../modelo/filtro-usuario-entidade.model";
import {DepartamentoRegional} from "../../../modelo/departamento-regional.model";
import * as moment from 'moment';

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
    public filtroUsuarioEntidade: FiltroUsuarioEntidade;
    public listaUsuarioEntidade: UsuarioEntidade[];
    public paginacaoEmpresaContrato: Paginacao = new Paginacao(1, 10);
    public checks: IHash = {};
    public contratos: Contrato[];
    public statusForm: FormGroup;
    public usuarioLogado: Usuario;
    public drs: number[] = [];
    public uats: number[] = [];
    public flagUsuario: string;
    public isDr: boolean;
    public isUnidade: boolean;
    public urlFoto: string;
    public isEmpresa: boolean;
    public estadoToggle: boolean;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private empresaService: EmpresaService,
                protected bloqueioService: BloqueioService,
                protected dialogo: ToastyService,
                private dialogService: DialogService,
                private usuarioEntidadeService: UsuarioEntidadeService,
                private empresaContratoService: EmpresaContratoService,
                private cdRef:ChangeDetectorRef) {
        super(bloqueioService, dialogo);
        this.title = MensagemProperties.app_rst_empresa_contrato_title;
    }

    ngOnInit() {
        this.usuarioLogado = Seguranca.getUsuario();
        this.getIdEmpresa();
        this.filtroPorPerfil();
        if(!this.isDr && !this.isUnidade) {
            this.pesquisarContratos();
        }
    }

    getIdEmpresa() {
        this.idEmpresa = this.route.snapshot.params['id'];
        if (this.idEmpresa) {
            this.filtro = new FiltroEmpresaContrato();
            this.filtroPage = new FiltroEmpresaContrato();
            this.filtroUsuarioEntidade = new FiltroUsuarioEntidade();
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
                this.flagUsuario = "2";
            }else if (perfil === PerfilEnum.GUS ) {
                this.flagUsuario = "1";
            }
        });
    }

    filtroPorPerfil(){
        this.usuarioLogado.papeis.forEach(perfil => {
            if (perfil === PerfilEnum.SUDR || perfil === PerfilEnum.DIDR || perfil === PerfilEnum.GDRA
                || perfil === PerfilEnum.GDRM) {
                this.isDr = true;
                this.pegaDrsDoUsuario();
            }
            else if(perfil === PerfilEnum.GUS) {
                this.isUnidade = true;
                this.pegaUatsDoUsuario();
            }
            else if(perfil === PerfilEnum.GEEM || perfil === PerfilEnum.GEEMM){
                this.isEmpresa = true;
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

    pegaUatsDoUsuario(){
        this.filtroUsuarioEntidade.cpf = this.usuarioLogado.sub;
        if(this.isUndefined(this.filtroUsuarioEntidade.idEstado)){
            this.filtroUsuarioEntidade.idEstado = '0';
        }
        this.usuarioEntidadeService.pesquisarPaginado(this.filtroUsuarioEntidade, this.paginacao, 'Unidade SESI')
            .switchMap( (retorno: ListaPaginada<UsuarioEntidade>) => {
                if(this.filtroUsuarioEntidade.idEstado == '0'){
                    this.filtroUsuarioEntidade.idEstado = undefined;
                }
                if(retorno.quantidade > 0){
                    this.listaUsuarioEntidade = retorno.list;
                    this.listaUsuarioEntidade.forEach( usuarioEntidade => {
                        this.uats.push(usuarioEntidade.uat.id);
                    })
                }
                this.filtro.idsUats = this.uats;
                return this.empresaContratoService.pesquisarContratos(this.filtro, new Paginacao(1, 10));
            })
            .subscribe((retorno: ListaPaginada<Contrato>) => {
                this.paginacaoEmpresaContrato = this.getPaginacao(this.paginacao, retorno)
                this.verificarRetornoEmpresasContrato(retorno);
            }, (error) => {
                this.mensagemError(error);
            })

    }

    pegaDrsDoUsuario(){
        this.filtroUsuarioEntidade.cpf = this.usuarioLogado.sub;
        if(this.isUndefined(this.filtroUsuarioEntidade.idEstado)){
            this.filtroUsuarioEntidade.idEstado = '0';
        }
        this.usuarioEntidadeService.pesquisarPaginado(this.filtroUsuarioEntidade, this.paginacao, 'Departamento Regional')
            .switchMap((retorno: ListaPaginada<UsuarioEntidade>) => {
                if(this.filtroUsuarioEntidade.idEstado == '0'){
                    this.filtroUsuarioEntidade.idEstado = undefined;
                }
                if(retorno.quantidade > 0){
                    this.listaUsuarioEntidade = retorno.list;
                    this.listaUsuarioEntidade.forEach( usuarioEntidade => {
                        this.drs.push(usuarioEntidade.departamentoRegional.id);
                    })
                }
                this.filtro.idsDr = this.drs;

                return this.empresaContratoService.pesquisarContratos(this.filtro, new Paginacao(1, 10));
            })
            .subscribe((retorno: ListaPaginada<Contrato>) => {
                this.paginacaoEmpresaContrato = this.getPaginacao(this.paginacao, retorno)
                this.verificarRetornoEmpresasContrato(retorno);
            }, (error) => {
                this.mensagemError(error);
            })
    }

    verificarRetornoEmpresasContrato(retorno: ListaPaginada<Contrato>) {
        if (retorno && retorno.list) {
            this.contratos = retorno.list;
        }
         else {
            this.contratos = new Array<Contrato>();
        }
    }



    mudaTooltip(contrato: Contrato){

        if(this.estadoToggle != undefined){
            if(this.estadoToggle == true){
                return;
            }
            else if(this.estadoToggle == false){
                if(contrato.flagInativo != undefined && contrato.flagInativo != "N"){
                    if(contrato.flagInativo == "3"){
                        this.estadoToggle = undefined;
                        return "Bloqueado por um Administrador/Gestor DN na data: " + contrato.dataInativo;
                    }
                    else if(contrato.flagInativo == "2"){
                        this.estadoToggle = undefined;
                        return "Bloqueado por um Gestor/Superintendente DR na data: " + contrato.dataInativo;
                    }
                    else if (contrato.flagInativo == "1"){
                        this.estadoToggle = undefined;
                        return "Bloqueador por um Gestor Unidade na data: " + contrato.dataInativo;
                    }
                }
            }
        }

       if(contrato.flagInativo != undefined && contrato.flagInativo != "N"){
           if(contrato.flagInativo == "3"){
               return "Bloqueado por um Administrador/Gestor DN na data: " + contrato.dataInativo;
           }
           else if(contrato.flagInativo == "2"){
               return "Bloqueado por um Gestor/Superintendente DR na data: " + contrato.dataInativo;
           }
           else if (contrato.flagInativo == "1"){
               return "Bloqueador por um Gestor Unidade na data: " + contrato.dataInativo;
           }
       }
    }

    mudaStatus(value, contratoId: number, contrato) {
        this.verPerfil();
        const flagContrato = {
            id: contratoId,
            flagInativo: this.flagUsuario
        };
        if (value.checked == true) {
            this.estadoToggle = true;
            this.empresaContratoService.desbloquearContrato(flagContrato).subscribe(
                () => {
                    this.mudaTooltip(contrato);
                    this.mensagemSucesso("Contrato desbloqueado com sucesso");
                }
            )
        }
        else {
            this.estadoToggle = false;
            this.empresaContratoService.bloquearContrato(flagContrato).subscribe(
                () => {
                    this.mensagemSucesso("Contrato bloqueado com sucesso");
                }
            )
            contrato.flagInativo = flagContrato.flagInativo;
            contrato.dataInativo = moment(new Date()).format("DD/MM/YYYY");
            this.mudaTooltip(contrato);
        }
    }
}
