import { DatePicker } from './../utilitario/date-picker';
import { EmpresaTrabalhadorLotacaoService } from './../../servico/empresa-trabalhador-lotacao.service';
import { ValidarDataFutura, CompareDataBefore, ValidarData } from 'app/compartilhado/validators/data.validator';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { EmpresaTrabalhador } from 'app/modelo/empresa-trabalhador.model';
import { DialogService } from 'ng2-bootstrap-modal';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { Paginacao } from 'app/modelo/paginacao.model';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit, Output, Input } from '@angular/core';
import { EmpresaTrabalhadorLotacao } from '../../modelo/empresa-trabalhador-lotacao.model';
import { Router } from '@angular/router';
import { ToastyService } from 'ng2-toasty';
import { ListaPaginada } from '../../modelo/lista-paginada.model';
import * as moment from 'moment';

@Component({ 
  selector: 'app-associacao-trabalhador-lotacao',
  templateUrl: './associacao-trabalhador-lotacao.component.html',
  styleUrls: ['./associacao-trabalhador-lotacao.component.scss'],
})
export class AssociacaoTrabalhadorLotacaoComponent extends BaseComponent implements OnInit {

  @Input() @Output()
  model: EmpresaTrabalhadorLotacao;

  @Input() @Output()
  paginacaoEmpresaTrabalhadorLotacao: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);

  @Input() @Output()
  listaEmpresaTrabalhadorLotacao: EmpresaTrabalhadorLotacao[];

  @Input() @Output()
  empresaTrabalhador: EmpresaTrabalhador;

  @Input() @Output()
  empresaLotacaoSelecionada: {
    valor: boolean,
  };

  @Input() @Output()
  empresaTrabalhadorLotacaoSelecionada: {
    valor: boolean,
  };

  @Input() @Output()
  public dataAssociacao: any;

  @Input() @Output()
  public dataDesligamento: any;

  @Input()
  hasPermissaoCadastrar: boolean;

  @Input()
  hasPermissaoAlterar: boolean;

  @Input()
  modoConsulta: boolean;

  public title: string;
  verificaDataModelAssociacao = true;
  verificaDataModelDesligamento = true;
  constructor(
    private router: Router,
    private associacaoTrabalhadorLotacaoService: EmpresaTrabalhadorLotacaoService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private dialogService: DialogService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {

    if (this.empresaTrabalhadorLotacaoSelecionada.valor) {
      this.title = MensagemProperties.app_rst_empresa_subtitulo_editar_trabalhador_lotacao_associados;
    }

    if (this.empresaLotacaoSelecionada.valor) {
      this.title = MensagemProperties.app_rst_empresa_subtitulo_associar_trabalhador_lotacao;
    }
  }

  adicionarEmpresaTrabalhadorLotacao(): void {
    if (this.validarEmpresaTrabalhadorLotacao()) {
      this.model.dataAssociacao = this.convertDateToString(this.dataAssociacao.date);
      if (this.dataDesligamento) {
        this.model.dataDesligamento = this.convertDateToString(this.dataDesligamento.date);
      } else {
        this.model.dataDesligamento = null;
      }    
      this.salvar(this.model);
    }
  }

  private validarEmpresaTrabalhadorLotacao(): Boolean {
    if (!this.dataAssociacao) {
      this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_dataAssociacao);
      return false;
    }
    if (ValidarDataFutura(this.dataAssociacao.jsdate)) {
      this.mensagemErroComParametrosModel('app_rst_labels_data_futura', MensagemProperties.app_rst_labels_dataAssociacao);
      return false;
    }

    if (this.dataDesligamento) {
      if (ValidarDataFutura(this.dataDesligamento.jsdate)) {
        this.mensagemErroComParametrosModel('app_rst_labels_data_futura', MensagemProperties.app_rst_labels_dataDesligamento);
        return false;
      }

      if (!CompareDataBefore(this.dataAssociacao.jsdate, this.dataDesligamento.jsdate)) {
        this.mensagemErroComParametrosModel('app_rst_empresa_dataDesligamento_maior_que_dataAssociacao');
        return false;
      }
    }

    return true;
  }

  limpar() {
    this.model = new EmpresaTrabalhadorLotacao();
    this.empresaLotacaoSelecionada.valor = false;
    this.empresaTrabalhadorLotacaoSelecionada.valor = false;
    this.dataAssociacao = null;
    this.dataDesligamento = null;
  }

  private salvar(empresaTrabalhadorLotacao: EmpresaTrabalhadorLotacao): void {
    empresaTrabalhadorLotacao.empresaTrabalhador = this.empresaTrabalhador;
    this.associacaoTrabalhadorLotacaoService.salvarEmpresaTrabalhadorLotacao(empresaTrabalhadorLotacao).
      subscribe((response: EmpresaTrabalhadorLotacao) => {
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.paginacaoEmpresaTrabalhadorLotacao.pagina = 1;
        this.pesquisarEmpresaTrabalhadorLotacaoPaginadoService(this.empresaTrabalhador.id);
        this.listaEmpresaTrabalhadorLotacao.push(response);
        this.limpar();
        this.model = new EmpresaTrabalhadorLotacao();
      }, (error) => {
        this.mensagemError(error);
      });
  }

  private pesquisarEmpresaTrabalhadorLotacaoPaginadoService(idEmpresaTrabalhador: number): void {
    this.associacaoTrabalhadorLotacaoService.pesquisarEmpresaTrabalhadorLotacao(idEmpresaTrabalhador,
      this.empresaTrabalhador.empresa.id, this.paginacaoEmpresaTrabalhadorLotacao).
      subscribe((retorno: ListaPaginada<EmpresaTrabalhadorLotacao>) => {
        this.listaEmpresaTrabalhadorLotacao.length = 0;
        retorno.list.forEach((item) => {
          this.listaEmpresaTrabalhadorLotacao.push(item);
        });
        this.paginacaoEmpresaTrabalhadorLotacao = this.getPaginacao(this.paginacaoEmpresaTrabalhadorLotacao,
          retorno);
      }, (error) => {
        this.mensagemError(error);
      });
  }

  maskDt($event, campo: string) {
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
        this.verificaDataModelAssociacao = false;
      }
      if (campo === 'D') {
        this.verificaDataModelDesligamento = false;
      }
    } else if ($event.target.value.length === 10) {
      if (campo === 'A') {
        this.instanciaDataAssociacao($event.target.value);

      }
      if (campo === 'D') {
        this.instanciaDataDesligamento($event.target.value);
      }
    } else {
      this.verificaDataModelAssociacao = true;
      this.verificaDataModelDesligamento = true;
    }

  }

  instanciaDataAssociacao(value ) {
    if (ValidarData(moment(value, 'DD/MM/YYYY').format('YYYY-MM-DD'))) {
      this.dataAssociacao = DatePicker.convertDateForMyDatePicker(value);
      this.verificaDataModelAssociacao = true;
    } else {
      this.verificaDataModelAssociacao = false;
    }
  }

  instanciaDataDesligamento(value) {
    if (ValidarData(moment(value, 'DD/MM/YYYY').format('YYYY-MM-DD'))) {
      this.dataDesligamento = DatePicker.convertDateForMyDatePicker(value);
      this.verificaDataModelDesligamento = true;
    } else {
      this.verificaDataModelDesligamento = false;
    }
  }

}
