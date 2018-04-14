import { EmpresaService } from 'app/servico/empresa.service';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { BloqueioModule } from 'app/compartilhado/bloqueio/bloqueio.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ModalModule } from 'ngx-bootstrap/modal';
import { ToastyModule } from 'ng2-toasty';
import { TextMaskModule } from 'angular2-text-mask/dist/angular2TextMask';
import { PaginationModule, AccordionModule } from 'ngx-bootstrap';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmpresaModalComponent } from './empresa-modal/empresa-modal.component';

@NgModule({
  imports: [
    CommonModule,
    BloqueioModule,
    FormsModule,
    ModalModule,
    ReactiveFormsModule,
    ToastyModule.forRoot(),
    AccordionModule.forRoot(),
    TextMaskModule,
    PaginationModule,
    CompartilhadoModule,
  ],
  declarations: [EmpresaModalComponent],
  exports: [
    EmpresaModalComponent,
    CommonModule,
    FormsModule,
    BloqueioModule,
    TextMaskModule,
    ModalModule,
    ToastyModule,
    ReactiveFormsModule,
    AccordionModule,
    PaginationModule,
  ],
  providers: [NgbModal, NgbActiveModal, EmpresaService],
  entryComponents: [EmpresaModalComponent],
})
export class ModalEmpresaComponentModule { }
