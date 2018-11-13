import {Component, Input, OnInit} from '@angular/core';
import {BaseComponent} from "../../base.component";
import {ToastyService} from "ng2-toasty";
import {SetorService} from "../../../servico/setor.service";
import {EmpresaService} from "../../../servico/empresa.service";
import {DialogService} from "ng2-bootstrap-modal";
import {EmpresaSetorService} from "../../../servico/empresa-setor.service";
import {BloqueioService} from "../../../servico/bloqueio.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MensagemProperties} from "../../../compartilhado/utilitario/recurso.pipe";
import {EmpresaContratoService} from "../../../servico/empresa-contrato.service";
import {environment} from "../../../../environments/environment";
import {FiltroSetor} from "../../../modelo/filtro-setor.model";
import {Paginacao} from "../../../modelo/paginacao.model";
import {EmpresaSetor} from "../../../modelo/empresa-setor.model";
import {Setor} from "../../../modelo/setor.model";
import {FiltroEmpresa} from "../../../modelo/filtro-empresa.model";
import {Contrato} from "../../../modelo/contrato.model";

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
    @Input() public filtro: FiltroSetor;
    public filtroPage: FiltroSetor;
    public idEmpresa: number;
    public emFiltro: FiltroEmpresa;
    public empresasSetor: EmpresaSetor[];
    public paginacaoEmpresaSetor: Paginacao = new Paginacao(1, 10);
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
      this.getIdEmpresa();
      this.title = MensagemProperties.app_rst_empresa_contrato_title;
  }

  ngOnInit() {
  }

  getIdEmpresa(){
      this.route.params.subscribe((params) => {
          this.idEmpresa = params['id'];
          if (this.idEmpresa) {
              this.filtro = new FiltroSetor();
              this.filtroPage = new FiltroSetor();
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

}
