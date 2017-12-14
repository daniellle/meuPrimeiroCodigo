import { ValidarDataFutura, CompareDataBefore } from 'app/compartilhado/validators/data.validator';
import { Router } from '@angular/router';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { EmpresaSindicatoService } from './../../servico/empresa.-sindicato.service';
import { Paginacao } from 'app/modelo/paginacao.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { DialogService } from 'ng2-bootstrap-modal';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { EmpresaSindicato } from './../../modelo/empresa-sindicato.model';
import { Component, OnInit, Input, Output } from '@angular/core';

@Component({
  selector: 'app-associacao-empresa-sindicato',
  templateUrl: './associacao-empresa-sindicato.component.html',
  styleUrls: ['./associacao-empresa-sindicato.component.scss'],
})
export class AssociacaoEmpresaSindicatoComponent extends BaseComponent implements OnInit {

  @Input() @Output()
  model: EmpresaSindicato;
  @Input() @Output()
  paginacaoEmpresaSindicato: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);

  @Input() @Output()
  listaEmpresaSindicato: EmpresaSindicato[];
  @Input()
  idEmpresa: number;
  @Input() @Output()
  sindicatoSelecionado: {
    valor: boolean,
  };
  @Input() @Output()
  empresaSindicatoSelecionada: {
    valor: boolean,
  };
  @Input()
  public modoConsulta;
  
  @Input()
  public dataAssociacao: any;
  @Input()
  public dataDesligamento: any;
  public title: string;

  constructor(
    private router: Router,
    private empresaSindicatoService: EmpresaSindicatoService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private dialogService: DialogService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {

    if (this.empresaSindicatoSelecionada.valor) {
      this.title = MensagemProperties.app_rst_empresa_subtitulo_editar_sindicatos_associados;
    }

    if (this.sindicatoSelecionado.valor) {
      this.title = MensagemProperties.app_rst_empresa_subtitulo_associar_sindicatos;
    }


  }

  adicionarEmpresaSindicato(): void {
    if (this.validarEmpresaSindicato()) {
      this.model.dataAssociacao = this.convertDateToString(this.dataAssociacao.date);
      if (this.dataDesligamento) {
        this.model.dataDesligamento = this.convertDateToString(this.dataDesligamento.date);
      }
      this.salvar(this.model);
    }
  }

  private validarEmpresaSindicato(): Boolean {
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
    this.model = new EmpresaSindicato();
    this.sindicatoSelecionado.valor = false;
    this.empresaSindicatoSelecionada.valor = false;
    this.dataAssociacao = null;
    this.dataDesligamento = null;
  }

  private salvar(empresaSindicato: EmpresaSindicato): void {
    this.empresaSindicatoService.salvarEmpresaSindicato(this.idEmpresa, empresaSindicato).subscribe((response: EmpresaSindicato) => {
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.paginacaoEmpresaSindicato.pagina = 1;
      this.pesquisarEmpresaSindicatoPaginadoService(this.idEmpresa);
      this.limpar();
      this.model = new EmpresaSindicato();
    }, (error) => {
      this.mensagemError(error);
    });
  }
  private pesquisarEmpresaSindicatoPaginadoService(idEmpresa: number): void {
    this.empresaSindicatoService.pesquisarEmpresasSindicatos(idEmpresa, this.paginacaoEmpresaSindicato).
      subscribe((retorno: ListaPaginada<EmpresaSindicato>) => {
        this.listaEmpresaSindicato.length = 0;
        retorno.list.forEach((item) => {
          this.listaEmpresaSindicato.push(item);
        });
        this.paginacaoEmpresaSindicato = this.getPaginacao(this.paginacaoEmpresaSindicato, retorno);
      }, (error) => {
        this.mensagemError(error);
      });
  }
}
