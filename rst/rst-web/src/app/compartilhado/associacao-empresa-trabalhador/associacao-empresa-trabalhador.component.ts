import { ValidarData } from 'app/compartilhado/validators/data.validator';
import { DialogService } from 'ng2-bootstrap-modal';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { DatePicker } from './../utilitario/date-picker';
import { EmpresaTrabalhador } from './../../modelo/empresa-trabalhador.model';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import * as moment from 'moment';

@Component({
  selector: 'app-associacao-empresa-trabalhador',
  templateUrl: './associacao-empresa-trabalhador.component.html',
  styleUrls: ['./associacao-empresa-trabalhador.component.scss'],
})
export class AssociacaoEmpresaTrabalhadorComponent extends BaseComponent {

  datePickerOptions = DatePicker.datePickerOptions;

  @Input()
  editar: boolean;

  @Input()
  model: EmpresaTrabalhador;

  @Output()
  onSalvar = new EventEmitter<EmpresaTrabalhador>();

  @Output()
  onLotacoes = new EventEmitter<EmpresaTrabalhador>();

  @Output()
  onLimpar = new EventEmitter();

  @Input()
  modoConsulta: boolean;

  @Input()
  hasPermissaoCadastrar: boolean;

  @Input()
  hasPermissaoAlterar: boolean;

  @Input()
  isEmpresaTrabalhadorSelecionado: boolean;

  verificaDataModelAdmissao = true;
  verificaDataModelDemissao = true;
  constructor(
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private dialogService: DialogService,
  ) {
    super(bloqueioService, dialogo);
  }

  adicionarEmpresaTrabalhador() {
    if (this.validarEmpresaTrabalhador()) {
      this.onSalvar.emit(this.model);
    }
  }

  private validarEmpresaTrabalhador(): Boolean {
    let isValido = true;
    if (!this.model.dataAdmissao && this.verificaDataModelAdmissao) {
      this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_dataAdmissao);
      isValido =  false;
    }

    if (!this.verificaDataModelAdmissao) {
      this.mensagemErroComParametrosModel('app_rst_campo_invalido', MensagemProperties.app_rst_labels_dataAdmissao);
      isValido = false;
    }
    if (!this.verificaDataModelDemissao) {
      this.mensagemErroComParametrosModel('app_rst_campo_invalido', MensagemProperties.app_rst_labels_dataDemissao);
      isValido = false;
    }
    return isValido;
    }

  lotacoes() {
    this.onLotacoes.emit(this.model);
  }

  limpar() {
    this.onLimpar.emit();
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
    if ($event.target.value !== '' && $event.target.value.length < 10) {
      if (campo === 'A') {
        this.verificaDataModelAdmissao = false;
      }
      if (campo === 'D') {
        this.verificaDataModelDemissao = false;
      }
    } else if ($event.target.value.length === 10) {
      if (campo === 'A') {
        this.instanciaDataAdmissao($event.target.value);
      }
      if (campo === 'D') {
        this.instanciaDataDemissao($event.target.value);
      }
    } else {
      this.verificaDataModelAdmissao = true;
      this.verificaDataModelDemissao = true;
    }

  }

  instanciaDataAdmissao(value) {
    if (ValidarData(moment(value, 'DD/MM/YYYY').format('YYYY-MM-DD'))) {
      this.model.dataAdmissao = DatePicker.convertDateForMyDatePicker(value);
      this.verificaDataModelAdmissao = true;
    } else {
      this.verificaDataModelAdmissao = false;
    }
  }

  instanciaDataDemissao(value) {
    if (ValidarData(moment(value, 'DD/MM/YYYY').format('YYYY-MM-DD'))) {
      this.model.dataDemissao = DatePicker.convertDateForMyDatePicker(value);
      this.verificaDataModelDemissao = true;
    } else {
      this.verificaDataModelDemissao = false;
    }
  }
}
