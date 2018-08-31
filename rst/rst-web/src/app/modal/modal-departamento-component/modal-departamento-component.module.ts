import { DepartamentoModalComponent } from './departamento-modal/departamento-modal.component';
import { DepartRegionalService } from 'app/servico/depart-regional.service';
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
  declarations: [DepartamentoModalComponent],
  exports: [
    DepartamentoModalComponent,
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
  providers: [NgbModal, NgbActiveModal, DepartRegionalService],
  entryComponents: [DepartamentoModalComponent],
})
export class ModalDepartamentoComponentModule { }
