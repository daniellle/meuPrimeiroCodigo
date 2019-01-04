import * as FileSaver from 'file-saver';
import { BaseComponent } from "app/componente/base.component";
import { Component,OnInit, TemplateRef, Input, Output } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { BloqueioService } from "app/servico/bloqueio.service";
import { ToastyService } from "ng2-toasty";
import {NgbModal, NgbActiveModal,NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import { PerfilUsuarioFilter } from "app/modelo/filter-perfil-usuario.model";
import { Usuario } from "app/modelo";
import { UsuarioService } from "app/servico/usuario.service";


@Component({
    selector: 'app-exportar-perfil-usuario',
    templateUrl: './exportar-perfil-usuario.component.html',
    styleUrls: ['./exportar-perfil-usuario.component.scss']
  })
export class ExportarPerfilUsuarioComponent extends BaseComponent implements OnInit {
    
    @Input() public filtro: PerfilUsuarioFilter;
    private modalRef: NgbModalRef;
    closeResult: string;



    constructor(private router: Router,
        private activatedRoute: ActivatedRoute,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private modalService: NgbModal,
        private activeModal: NgbActiveModal,
        private usuarioService: UsuarioService){
            super(bloqueioService,dialogo)
    }

    ngOnInit(){
    
    }

    exportar(template){this.modalRef = this.modalService.open(template)}

    fecharModal(){this.modalRef.close()}

    imprimir(){
        this.usuarioService.visualizarPdf(this.filtro, this.paginacao)
        .subscribe();  
    }

    exportarPlanilha(){
        console.log("PLANILHA EXPORTADA");
        this.fecharModal();
    }

    exportarPDF(){
        console.log("PDF EXPORTADO");
        this.fecharModal();
    }
}