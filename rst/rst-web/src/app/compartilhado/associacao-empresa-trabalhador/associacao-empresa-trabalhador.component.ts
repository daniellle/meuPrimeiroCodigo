import { DialogService } from 'ng2-bootstrap-modal';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { DatePicker } from './../utilitario/date-picker';
import { EmpresaTrabalhador } from './../../modelo/empresa-trabalhador.model';
import { Component, Input, Output, EventEmitter } from '@angular/core';

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
    if (!this.model.dataAdmissao) {
      this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_dataAdmissao);
      return false;
    }
    return true;
    }

  lotacoes() {
    this.onLotacoes.emit(this.model);
  }

  limpar() {
    this.onLimpar.emit();
  }
}
