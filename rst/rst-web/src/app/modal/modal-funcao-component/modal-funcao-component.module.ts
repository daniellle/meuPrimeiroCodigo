import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FuncaoModalComponent } from './funcao-modal/funcao-modal.component';
import { BloqueioModule } from 'app/compartilhado/bloqueio/bloqueio.module';
import { ModalModule, AccordionModule, PaginationModule } from 'ngx-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TextMaskModule } from 'angular2-text-mask/dist/angular2TextMask';
import { ToastyModule } from 'ng2-toasty';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

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
  declarations: [FuncaoModalComponent],
  exports: [
    FuncaoModalComponent,
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
  entryComponents: [FuncaoModalComponent],
})
export class ModalFuncaoComponentModule { }
