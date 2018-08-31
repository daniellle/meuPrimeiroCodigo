import { SetorModalComponent } from './setor-modal/setor-modal.component';
import { BloqueioModule } from 'app/compartilhado/bloqueio/bloqueio.module';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastyModule } from 'ng2-toasty';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TextMaskModule } from 'angular2-text-mask/dist/angular2TextMask';
import { AccordionModule } from 'ngx-bootstrap/accordion';
import { ModalModule } from 'ngx-bootstrap/modal';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { PaginationModule } from 'ngx-bootstrap';

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
  declarations: [SetorModalComponent],
  exports: [
    SetorModalComponent,
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
  providers: [NgbModal, NgbActiveModal],
  entryComponents: [SetorModalComponent],
})
export class ModalSetorComponentModule { }
