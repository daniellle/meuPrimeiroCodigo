import { ModalSelecionarGrupoPerguntaComponent } from './modal-selecionar-grupo/modal-selecionar-grupo.component';
import { CardTelaIntermediariaComponent } from './../componente/card-tela-intermediaria/card-tela-intermediaria.component';
import { ChartsModule } from 'ng2-charts/ng2-charts';
import { DatePipe } from './utilitario/date-pipe.pipe';
import { CpfPipe } from './utilitario/cpf.pipe';
import { MyDatePickerModule } from 'mydatepicker';
import { AssociacaoEmpresaSindicatoComponent } from './associacao-empresa-sindicato/associacao-empresa-sindicato.component';
import { InativoPipe } from './utilitario/situacao.pipe';
import { SegmentoService } from './../servico/segmento.service';
import { EnderecoModalComponent } from './modal-endereco-component/endereco-modal/endereco-modal.component';
import { TelefonePipe } from './utilitario/telefone.pipe';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastyModule } from 'ng2-toasty';
import { RecursoPipe } from './utilitario/recurso.pipe';
import { BloqueioModule } from './bloqueio/bloqueio.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CnpjPipe } from 'app/compartilhado/utilitario/cnpj.pipe';
import { TextMaskModule } from 'angular2-text-mask/dist/angular2TextMask';
import { AccordionModule } from 'ngx-bootstrap/accordion';
import { ModalModule } from 'ngx-bootstrap/modal';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SelectModule } from 'ng-select';
import { EstadoService } from 'app/servico/estado.service';
import { ConselhoRegionalService } from 'app/servico/conselho-regional.service';
import { TelefoneModalComponent } from 'app/compartilhado/modal-telefone-component/telefone-modal.component';
import { EmailModalComponent } from './modal-email-component/email-modal/email-modal.component';
import { EmailGridComponent } from './modal-email-component/email-grid/email-grid.component';
import { TelefoneGridComponent } from 'app/compartilhado/modal-telefone-component/telefone-grid.component';
import { EnderecoGridComponent } from './modal-endereco-component/endereco-grid/endereco-grid.component';
import { InputCnpjCpfComponent } from './input-cnpj-cpf/input-cnpj-cpf/input-cnpj-cpf.component';
import { SegmentoModalComponent } from './modal-segmento-component/segmento-modal/segmento-modal.component';
import { PaginationModule, TypeaheadModule } from 'ngx-bootstrap';
import { SegmentoGridComponent } from './modal-segmento-component/segmento-grid/segmento-grid.component';
import { UatModalComponent } from './modal-uat-component/uat-modal/uat-modal.component';
import { UatGridComponent } from './modal-uat-component/uat-grid/uat-grid.component';
import { UnidadeModalComponent } from './modal-unidade-component/unidade-modal/unidade-modal.component';
import { UnidadeGridComponent } from './modal-unidade-component/unidade-grid/unidade-grid.component';
import { AssociacaoTrabalhadorLotacaoComponent } from './associacao-trabalhador-lotacao/associacao-trabalhador-lotacao.component';
import { EmpresaTrabalhadorLotacaoService } from '../servico/empresa-trabalhador-lotacao.service';
import {
    MdAutocompleteModule,
    MdButtonModule,
    MdButtonToggleModule,
    MdCardModule,
    MdCheckboxModule,
    MdChipsModule,
    MdCoreModule,
    MdDatepickerModule,
    MdDialogModule,
    MdExpansionModule,
    MdGridListModule,
    MdIconModule,
    MdInputModule,
    MdListModule,
    MdMenuModule,
    MdNativeDateModule,
    MdPaginatorModule,
    MdProgressBarModule,
    MdProgressSpinnerModule,
    MdRadioModule,
    MdRippleModule,
    MdSelectModule,
    MdSidenavModule,
    MdSliderModule,
    MdSlideToggleModule,
    MdSnackBarModule,
    MdSortModule,
    MdTableModule,
    MdTabsModule,
    MdToolbarModule,
    MdTooltipModule,
} from '@angular/material';
import { CALENDARIO_DIRECTIVES } from 'app/diretiva/calendario.directive';
import { VacinaModalComponent } from 'app/compartilhado/modal-vacina-componenet/vacina-modal/vacina-modal.component';
import { VacinaGridComponent } from 'app/compartilhado/modal-vacina-componenet/vacina-grid/vacina-grid.component';


@NgModule({
    imports: [
        CommonModule,
        BloqueioModule,
        ChartsModule,
        FormsModule,
        ModalModule,
        ReactiveFormsModule,
        MyDatePickerModule,
        ToastyModule.forRoot(),
        AccordionModule.forRoot(),
        SelectModule,
        TextMaskModule,
        PaginationModule,
        TypeaheadModule,
        MdAutocompleteModule,
        MdButtonModule,
        MdButtonToggleModule,
        MdCardModule,
        MdCheckboxModule,
        MdChipsModule,
        MdCoreModule,
        MdDatepickerModule,
        MdDialogModule,
        MdExpansionModule,
        MdGridListModule,
        MdIconModule,
        MdInputModule,
        MdListModule,
        MdMenuModule,
        MdNativeDateModule,
        MdPaginatorModule,
        MdProgressBarModule,
        MdProgressSpinnerModule,
        MdRadioModule,
        MdRippleModule,
        MdSelectModule,
        MdSidenavModule,
        MdSliderModule,
        MdSlideToggleModule,
        MdSnackBarModule,
        MdSortModule,
        MdTableModule,
        MdTabsModule,
        MdToolbarModule,
        MdTooltipModule,
    ],
    declarations: [CardTelaIntermediariaComponent, RecursoPipe, CnpjPipe, CpfPipe, DatePipe, TelefonePipe, InativoPipe,
        EmailModalComponent, EmailGridComponent, TelefoneModalComponent, TelefoneGridComponent,
        EnderecoGridComponent, EnderecoModalComponent, InputCnpjCpfComponent, SegmentoModalComponent,
        SegmentoGridComponent, UatModalComponent, UatGridComponent, AssociacaoEmpresaSindicatoComponent,
        UnidadeModalComponent, UnidadeGridComponent, AssociacaoTrabalhadorLotacaoComponent, CALENDARIO_DIRECTIVES,
        ModalSelecionarGrupoPerguntaComponent,
        VacinaModalComponent,
        VacinaGridComponent
    ],
    exports: [
        CommonModule,
        ChartsModule,
        MyDatePickerModule,
        RecursoPipe,
        FormsModule,
        BloqueioModule,
        TextMaskModule,
        ModalModule,
        ToastyModule,
        ReactiveFormsModule,
        TypeaheadModule,
        CnpjPipe,
        CpfPipe,
        TelefonePipe,
        InativoPipe,
        AccordionModule,
        DatePipe,
        SelectModule,
        TelefoneModalComponent,
        TelefoneGridComponent,
        EmailModalComponent,
        EmailGridComponent,
        EnderecoGridComponent,
        EnderecoModalComponent,
        SegmentoModalComponent,
        SegmentoGridComponent,
        InputCnpjCpfComponent,
        PaginationModule,
        UatModalComponent,
        UatGridComponent,
        VacinaModalComponent,
        VacinaGridComponent,
        AssociacaoEmpresaSindicatoComponent,
        UnidadeModalComponent,
        UnidadeGridComponent,
        AssociacaoTrabalhadorLotacaoComponent,
        MdAutocompleteModule,
        MdButtonModule,
        MdButtonToggleModule,
        MdCardModule,
        MdCheckboxModule,
        MdChipsModule,
        MdCoreModule,
        MdDatepickerModule,
        MdDialogModule,
        MdExpansionModule,
        MdGridListModule,
        MdIconModule,
        MdInputModule,
        MdListModule,
        MdMenuModule,
        MdNativeDateModule,
        MdPaginatorModule,
        MdProgressBarModule,
        MdProgressSpinnerModule,
        MdRadioModule,
        MdRippleModule,
        MdSelectModule,
        MdSidenavModule,
        MdSliderModule,
        MdSlideToggleModule,
        MdSnackBarModule,
        MdSortModule,
        MdTableModule,
        MdTabsModule,
        MdToolbarModule,
        MdTooltipModule,
        CALENDARIO_DIRECTIVES,
        CardTelaIntermediariaComponent,
        ModalSelecionarGrupoPerguntaComponent,
    ],
    providers: [EstadoService, ConselhoRegionalService, NgbModal, NgbActiveModal, SegmentoService, EmpresaTrabalhadorLotacaoService,
         { provide: 'Window',  useValue: window }],
    entryComponents: [TelefoneModalComponent, TelefoneGridComponent, SegmentoModalComponent,
        SegmentoGridComponent, UatModalComponent, UatGridComponent, ModalSelecionarGrupoPerguntaComponent],
})
export class CompartilhadoModule {
}
