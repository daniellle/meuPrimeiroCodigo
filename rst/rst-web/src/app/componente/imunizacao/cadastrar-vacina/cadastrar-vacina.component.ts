import { IMyDpOptions } from 'mydatepicker';
import { Component, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router, Data } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ImunizacaoService } from 'app/servico/imunizacao.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { ToastyService } from 'ng2-toasty/src/toasty.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal/modal';
import { DialogService } from 'ng2-bootstrap-modal/dist/dialog.service';
import { Vacina } from 'app/modelo/vacina.model';
import { ValidateData, ValidateDataFutura } from 'app/compartilhado/validators/data.validator';
import { environment } from 'environments/environment';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ProximaDoseVacina } from '../../../modelo/proximaDoseVacina.model';
import { DatePicker } from 'app/compartilhado/utilitario/date-picker';
import * as moment from 'moment';

@Component({
  selector: 'app-cadastrar-vacina',
  templateUrl: './cadastrar-vacina.component.html',
  styleUrls: ['./cadastrar-vacina.component.scss']
})
export class CadastrarVacinaComponent extends BaseComponent implements OnInit {

  public vacinaForm: FormGroup;

  @Output()
  vacina: Vacina;

  public idTrabalhador: number;
  public idVacina: number;
  public myDatePickerOptions: IMyDpOptions = {
    openSelectorOnInputClick: false,
    indicateInvalidDate: true,
    editableDateField: false,
    dateFormat: 'dd/mm/yyyy',
    showClearDateBtn: true,
    inline: false,
  }

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private service: ImunizacaoService,
    private formBuilder: FormBuilder,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private modalService: NgbModal,
    private dialogService: DialogService) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.vacina = new Vacina();
    this.vacina.listaProximasDoses = new Array<ProximaDoseVacina>();
    this.createForm();
    this.limparForm();
    this.carregarTela();
  }

  carregarTela() {
    this.activatedRoute.params.subscribe((params) => {
      this.idTrabalhador = params['id'];
      this.idVacina = params['idVacina'];
      if (this.idVacina === undefined) {
        this.limparForm();
      } else {
        this.carregarVacina(this.idVacina);
      }
    });
  }

  createForm() {
    this.vacinaForm = this.formBuilder.group({
      nome: [
        { value: '' },
        Validators.compose([
          Validators.required,
          Validators.maxLength(160),
        ]),
      ],
      lote: [
        { value: '' },
        Validators.compose([
          Validators.maxLength(50),
        ]),
      ],
      local: [
        { value: '' },
        Validators.compose([
          Validators.maxLength(150),
        ]),
      ],
      dataVacinacao: [
        { value: null },
        Validators.compose([
          Validators.maxLength(10),
          Validators.required,
          ValidateData,
          ValidateDataFutura,
        ]),
      ],
      proximasDoses: [
        { value: null },
        Validators.compose([]),
      ],
    });
  }

  carregarVacina(id: number) {
    this.service.buscarVacinaPorId(id).subscribe((response: Vacina) => {
      this.vacina = response;
      this.converterModelParaForm();
    }, (error) => {
      this.limparForm();
      this.mensagemError(error);
    });
  }

  converterModelParaForm() {
    this.vacinaForm.patchValue({
      nome: this.vacina.nome ? this.vacina.nome : '',
      lote: this.vacina.lote ? this.vacina.lote : '',
      local: this.vacina.local ? this.vacina.local : '',
      dataVacinacao: this.vacina.dataVacinacao ?
        DatePicker.convertDateForMyDatePicker(moment(this.vacina.dataVacinacao).format("DD/MM/YYYY")) : null,
    });
  }

  existeVacina() {
    return this.vacina.listaProximasDoses && this.vacina.listaProximasDoses.length > 0;
  }

  voltar(): void {
    if (this.idVacina) {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}/vacina-declarada/historico`]);
    } else {
      if (this.idTrabalhador) {
        if (this.activatedRoute.snapshot.url[0].path === 'meusdados') {
          this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados`]);
        } else {
          this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}`]);
        }
      } else {
        this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador`]);
      }
    }
  }



  salvar() {
    if (this.validarCampos()) {
      this.prepareSave();
      if (this.vacina.id) {
        this.service.atualizar(this.vacina).subscribe((response: Vacina) => {
          this.vacina = response;
          this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
          this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}/vacina-declarada/historico`]);
        }, (error) => {
          this.mensagemError(error);
        });
      } else {
        this.service.salvar(this.vacina).subscribe((response: Vacina) => {
          this.vacina = response;
          this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
          this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}/vacina-declarada/historico`]);
        }, (error) => {
          this.mensagemError(error);
        });
      }
    }
  }

  prepareNome(): String {
    const formModel = this.vacinaForm.controls;
    return formModel.nome.value;
  }

  validarCampos(): Boolean {
    let isValido = true;
    if (this.vacinaForm.controls['nome'] && this.vacinaForm.controls['nome'].invalid) {
      if (this.vacinaForm.controls['nome'].errors.required) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.vacinaForm.controls['nome'],
          MensagemProperties.app_rst_labels_nome);
        isValido = false;
      }
    }

    if (this.vacinaForm.controls['dataVacinacao'].value && this.vacinaForm.controls['dataVacinacao'].invalid) {
      if (this.vacinaForm.controls['dataVacinacao'].errors.validData) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.vacinaForm.controls['dataVacinacao'],
          MensagemProperties.aps_rst_labels_data_vacina);
        isValido = false;
      }
      if (this.vacinaForm.controls['dataVacinacao'].errors.validDataFutura) {
        this.mensagemErroComParametros('app_rst_labels_data_futura', this.vacinaForm.controls['dataVacinacao'],
          MensagemProperties.aps_rst_labels_data_vacina);
        isValido = false;
      }
      if (this.vacinaForm.controls['dataVacinacao'].errors.isNullOrUndefined) {
        this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.vacinaForm.controls['dataVacinacao'],
          MensagemProperties.app_rst_label_data_vacinacao);
        isValido = false;
      }
    }
    if (this.vacinaForm.controls['dataVacinacao'].value == null) {
      this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.vacinaForm.controls['dataVacinacao'],
        MensagemProperties.app_rst_label_data_vacinacao);
      isValido = false
    }
    return isValido;
  }

  prepareSave(): Vacina {
    const formModel = this.vacinaForm.controls;
    this.vacina.nome = formModel.nome.value;
    this.vacina.local = formModel.local.value;
    this.vacina.dataVacinacao = new Date(formModel.dataVacinacao.value.jsdate);
    this.vacina.outrasDoses = false;
    if (this.isNotEmpty(this.vacina.listaProximasDoses)) {
      this.vacina.outrasDoses = true;
      this.vacina.listaProximasDoses.forEach(proximaDose => {
        if (!proximaDose.remover)
          proximaDose.remover = false;
      })
    }
    this.vacina.lote = formModel.lote.value;
    return this.vacina;
  }

  limparForm() {
    this.vacinaForm.patchValue({
      nome: '',
      lote: '',
      local: '',
      dataVacinacao: null,
    });
  }

}
