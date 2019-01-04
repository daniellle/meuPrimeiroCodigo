import { BaseComponent } from "app/componente/base.component";
import { Component,OnInit, TemplateRef } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { BloqueioService } from "app/servico/bloqueio.service";
import { ToastyService } from "ng2-toasty";
import {NgbModal, NgbActiveModal,NgbModalRef} from '@ng-bootstrap/ng-bootstrap';


@Component({
    selector: 'app-exportar-perfil-usuario',
    templateUrl: './exportar-perfil-usuario.component.html',
    styleUrls: ['./exportar-perfil-usuario.component.scss']
  })
export class ExportarPerfilUsuarioComponent extends BaseComponent implements OnInit {

    private modalRef: NgbModalRef;
    closeResult: string;



    constructor(private router: Router,
        private activatedRoute: ActivatedRoute,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private modalService: NgbModal,
        private activeModal: NgbActiveModal){
            super(bloqueioService,dialogo)
    }

    ngOnInit(){}

    exportar(template){this.modalRef = this.modalService.open(template)}

    fecharModal(){this.modalRef.close()}

    imprimir(){
        console.log("Imprimiu!!!");
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