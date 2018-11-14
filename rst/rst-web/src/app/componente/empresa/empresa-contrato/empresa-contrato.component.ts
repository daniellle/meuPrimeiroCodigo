import {Component, Input, OnInit} from '@angular/core';
import {BaseComponent} from "../../base.component";
import {ToastyService} from "ng2-toasty";
import {SetorService} from "../../../servico/setor.service";
import {EmpresaService} from "../../../servico/empresa.service";
import {DialogService} from "ng2-bootstrap-modal";
import {BloqueioService} from "../../../servico/bloqueio.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MensagemProperties} from "../../../compartilhado/utilitario/recurso.pipe";
import {EmpresaContratoService} from "../../../servico/empresa-contrato.service";
import {environment} from "../../../../environments/environment";
import {FiltroSetor} from "../../../modelo/filtro-setor.model";
import {Paginacao} from "../../../modelo/paginacao.model";
import {FiltroEmpresa} from "../../../modelo/filtro-empresa.model";
import {Contrato} from "../../../modelo/contrato.model";
import {EmpresaContrato} from "../../../modelo/empresa-contrato.model";
import {FiltroEmpresaContrato} from "../../../modelo/filtro-empresa-contrato.model";
import {ListaPaginada} from "../../../modelo/lista-paginada.model";
import {EmpresaFuncao} from "../../../modelo/empresa-funcao.model";

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
    public empresasContrato: EmpresaContrato[];
    public paginacaoEmpresaContrato: Paginacao = new Paginacao(1, 10);
    public checks: IHash = {};
    public contratos: Contrato[];

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
      this.getIdEmpresa();
      this.pesquisarContratos();
  }

  getIdEmpresa(){
      this.route.params.subscribe((params) => {
          this.idEmpresa = params['id'];
          if (this.idEmpresa) {
              this.filtro = new FiltroEmpresaContrato();
              this.filtroPage = new FiltroEmpresaContrato();
              this.filtro.idEmpresa = this.idEmpresa;
          }
      }, (error) => {
          this.mensagemError(error);
      });
  }

  voltar(){
      if (this.route.snapshot.url[0].path === 'minhaempresa') {
          this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa`]);
      } else {
          this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}`]);
      }
  }

  criarCadastro(){
      this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}/contrato/cadastrarcontrato`]);
  }


    public isListaVazia(): boolean {
        return this.contratos === undefined || this.contratos.length === 0;
    }

    pesquisarContratos(){
      this.empresasContrato = new Array<EmpresaContrato>();
      this.filtro.idEmpresa = this.idEmpresa;
      this.empresaContratoService.pesquisarContratos(this.filtro.idEmpresa, new Paginacao(1, 10))
          .subscribe((retorno: ListaPaginada<EmpresaContrato>) =>{
                this.paginacaoEmpresaContrato = this.getPaginacao(this.paginacao, retorno)
              this.verificarRetornoEmpresasContrato(retorno);
          }, (error) => {
              this.mensagemError(error);
          });
    }

    verificarRetornoEmpresasContrato(retorno: ListaPaginada<EmpresaContrato>) {
    if (retorno && retorno.list) {
        this.empresasContrato = retorno.list;
    } else {
        this.empresasContrato = new Array<EmpresaContrato>();
    }
}

}
