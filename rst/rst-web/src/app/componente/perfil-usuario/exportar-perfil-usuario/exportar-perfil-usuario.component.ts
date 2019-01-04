import { BaseComponent } from "app/componente/base.component";
import { Component,OnInit, TemplateRef, Input, Output } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { BloqueioService } from "app/servico/bloqueio.service";
import { ToastyService } from "ng2-toasty";
import {NgbModal, NgbActiveModal,NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import { UsuarioService } from "app/servico/usuario.service";
import { PerfilUsuarioFilter } from "app/modelo/filter-perfil-usuario.model";
import { MensagemProperties } from "app/compartilhado/utilitario/recurso.pipe";
import * as FileSaver from 'file-saver';

@Component({
    selector: 'app-exportar-perfil-usuario',
    templateUrl: './exportar-perfil-usuario.component.html',
    styleUrls: ['./exportar-perfil-usuario.component.scss']
  })
export class ExportarPerfilUsuarioComponent extends BaseComponent implements OnInit {

    private modalRef: NgbModalRef;
    closeResult: string;
    @Input() @Output() public filtro: PerfilUsuarioFilter;



    constructor(private router: Router,
        private activatedRoute: ActivatedRoute,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private modalService: NgbModal,
        private activeModal: NgbActiveModal,
        protected usuarioService: UsuarioService){
            super(bloqueioService,dialogo)
    }

    ngOnInit(){}

    exportar(template){this.modalRef = this.modalService.open(template)}

    fecharModal(){this.modalRef.close()}

    imprimir(){
        console.log("Imprimiu!!!");
    }

    exportarPlanilha(){
        if(this.verificarCampos()){
            this.usuarioService.pesquisarCSV(this.filtro, this.paginacao)
            .subscribe((retorno) => FileSaver.saveAs(retorno, 'teste.csv')
            );
          }
        this.fecharModal();
    }

    exportarPDF(){
        if(this.verificarCampos()){
            this.usuarioService.pesquisarCSV(this.filtro, this.paginacao)
            .subscribe((retorno) => FileSaver.saveAs(retorno, 'teste.csv')
            );
          }
        this.fecharModal();
    }

    public verificarCampos(): boolean {
        let verificador = true;
        if (this.filtrosEmBranco()) {
          this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
          verificador = false;
        }
      
        if (!this.isVazia(this.filtro.login)) {
          if (this.filtro.login.length < 14) {
            this.mensagemError(MensagemProperties.app_rst_labels_cpf_incompleto);
            verificador = false;
          }
      
        }
        return verificador;
      }
      
      private filtrosEmBranco(): boolean {
        return this.filtro && !this.filtro.login && !this.filtro.nome && this.isVazia(this.filtro.idUnidadeSesi) &&
          (this.filtro.empresa && !this.filtro.empresa.id) &&
          (this.filtro.departamentoRegional && !this.filtro.departamentoRegional.id) &&
          (!this.filtro.codigoPerfil || this.isUndefined(this.filtro.codigoPerfil));
      }
}