import { Component, OnInit, Input, Output, ViewChild } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty/src/toasty.service';
import { DialogService } from 'ng2-bootstrap-modal/dist/dialog.service';
import { ProximaDoseVacina } from '../../../modelo/proximaDoseVacina.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';


import * as moment from 'moment';

@Component({
  selector: 'app-vacina-modal',
  templateUrl: './vacina-modal.component.html',
  styleUrls: ['./vacina-modal.component.scss']
})
export class VacinaModalComponent extends BaseComponent implements OnInit {

  model: ProximaDoseVacina;

  @Input() @Output()
  list: any[];

  @Input()
  adicionar: any;

  data: any;

  @Input()
  nomeDaVacina: String;

  @ViewChild('modalVacina') modalVacina;

  public modal;
  public modalRef;

  // tslint:disable-next-line:max-line-length

  constructor(
    public activeModal: NgbActiveModal,
    private modalService: NgbModal,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private dialogService: DialogService,
  ) {
    super(bloqueioService, dialogo);
    this.model = new ProximaDoseVacina();
  }

  ngOnInit() {
  }

  existeVacina() {
    return this.list && this.list.length > 0;
  }

  adicionarVacina() {
    this.modalRef = this.modalService.open(this.modalVacina);
    this.modalRef.result.then((result) => {
      if (this.isDataValida(this.data)) {
          const proximaDoseVacina = new ProximaDoseVacina();
          proximaDoseVacina.dtProximaDoseVacina = new Date(this.data.jsdate);
          proximaDoseVacina.remover = false;
          this.list.push(proximaDoseVacina);
      }
      this.limparModal();
    }, (reason) => {
      this.limparModal();
    });

  }

  limparModal() {
    this.data = undefined;
  }

  isDataValida(data: any): Boolean {
    let isValido = true;
    if (!data) {
      this.mensagemErroComParametrosModel('app_rst_data_proxima_campo_obrigatorio', MensagemProperties.app_rst_labels_data_proxima_dose);
      isValido = false;
      this.limparModal();
    } else {
      if (new Date() > data.jsdate) {
        this.mensagemErroComParametrosModel('app_rst_data_proxima_campo');
        isValido = false;
        this.limparModal();
      }
    }
    return isValido;
  }

  maskDt($event, campo?: string) {
    let str: string = $event.target.value;
    str = str.replace(new RegExp('/', 'g'), '');
    $event.target.value = this.retiraLetra($event, str);
    if ($event.target.value.length < 10 && $event.keyCode !== 8) {
      if ($event.target.value.length === 2 || $event.target.value.length === 5) {
        $event.target.value = $event.target.value += '/';
      }
    }
  }

  convertDateToString(data: any): string {
    return data.day + '/' + data.month + '/' + data.year;
  }

  // selecionarVacina(item: any, index: any) {
  //   this.model = new ProximaDoseVacina(item);
  //   this.adicionarVacina(index);
  // }
}
