import { ValidarData } from 'app/compartilhado/validators/data.validator';
import { PerfilEnum } from './../../../modelo/enum/enum-perfil';
import { SimNao } from './../../../modelo/enum/enum-simnao.model';
import { DatePicker } from './../../../compartilhado/utilitario/date-picker';
import { Certificado } from './../../../modelo/certificado.model';
import { TrabalhadorService } from 'app/servico/trabalhador.service';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { environment } from './../../../../environments/environment';
import { ParametroService } from './../../../servico/parametro.service';
import { CertificadoService } from './../../../servico/certificado.service';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { TipoCursoService } from './../../../servico/tipo-curso.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup } from '@angular/forms';
import { Trabalhador } from 'app/modelo/trabalhador.model';
import { TipoCurso } from './../../../modelo/tipo-curso.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ModalidadeCurso } from 'app/modelo/enum/enum-modalidade-curso.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import * as FileSaver from 'file-saver';
import * as moment from 'moment';

@Component({
  selector: 'app-trabalhador-certificado',
  templateUrl: './trabalhador-certificado.component.html',
  styleUrls: ['./trabalhador-certificado.component.scss'],
})

export class TrabalhadorCertificadoComponent extends BaseComponent implements OnInit {

  certificado = new Certificado();
  idTrabalhador: number;
  listaTipoCurso = new Array<TipoCurso>();
  public modalidade = ModalidadeCurso;
  listaModalidade = Object.keys(this.modalidade);
  listaCertificados: Certificado[];
  novoTipoCurso = new TipoCurso();
  certificadoForm: FormGroup;
  public dataValidade: any;
  public meusCertificados = false;
  public meusDados = false;
  public nomeArquivo = '';
  public visualizarCertificado: Certificado;
  public nomeArquivoVisualizar = '';
  public nomeArquivoTrabalhador = '';
  public paginacaoTrabalhador = new Paginacao();
  listaCertificadosTrabalhador: Certificado[];
  public tamanho;
  public labelTamanhoArquivo;

  verificaDataModelValidade = true;
  verificaDataModelConclusao = true;

  @ViewChild('modalAdicionarTipoCurso') modalAdicionarTipoCurso: any;
  @ViewChild('inputFile') inputFile: any;
  @ViewChild('inputFileTrabalhador') inputFileTrabalhador: any;
  @ViewChild('modalVisualizarCertificado') modalVisualizarCertificado: any;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private certificadoService: CertificadoService,
    private tipoCursoService: TipoCursoService,
    protected bloqueioService: BloqueioService,
    private modalService: NgbModal,
    protected dialogo: ToastyService,
    private parametroService: ParametroService,
    private trabalhadorService: TrabalhadorService,
  ) {
    super(bloqueioService, dialogo);

  }

  ngOnInit() {
    this.title = this.activatedRoute.snapshot.data.title;
    this.carregarTela();
    this.getListaTipoCurso();
    this.emModoConsulta();
    this.getParametro();
  }

  carregarTela() {
    this.meusCertificados = this.activatedRoute.routeConfig.path.match(/meuscertificados/g) ? true : false;
    this.meusDados = this.activatedRoute.routeConfig.path.match(/meusdados/g) ? true : false;
    if (this.meusCertificados) {
      this.trabalhadorService.buscarMeusDados().subscribe((trabalhador) => {
        this.idTrabalhador = trabalhador.id;
        this.getListaCertificados();
        this.getListaCertificadosTrabalhador();
      }, (error) => {
        this.mensagemError(error);
      });
    } else {
      this.activatedRoute.params.subscribe((params) => {
        this.idTrabalhador = params['id'];
        this.getListaCertificados();
        this.getListaCertificadosTrabalhador();
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }
  salvar(formCertificado) {
    this.certificado.inclusaoTrabalhador = 'N';
    if (formCertificado.valid && this.nomeArquivo) {
      this.certificado.trabalhador = { id: this.idTrabalhador };
      if (formCertificado.value.dataValidade) {
        this.certificado.dataValidade = this.convertDateToString(formCertificado.value.dataValidade.date);
      }
      if (formCertificado.value.dataConclusao) {
        this.certificado.dataConclusao = this.convertDateToString(formCertificado.value.dataConclusao.date);
      }
      this.certificadoService.salvar(this.certificado).subscribe((response: Certificado) => {
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        formCertificado.reset();
        this.removerUpload();
        this.certificado = new Certificado();
        this.getListaCertificados();
      }, (error) => {
        this.mensagemError(error);
      });
    } else {
      this.validarFormCertificado(formCertificado);
    }
  }

  adicionarTipoCurso(modalAdicionarTipoCurso) {
    const tipoCurso = this.novoTipoCurso;
    this.modalService.open(modalAdicionarTipoCurso).result
      .then((result) => {
        if (tipoCurso.descricao) {
          this.salvarTipoCurso(tipoCurso);
        } else {

        }
      }, (reason) => {
        this.limparTipoCurso();
      });
  }

  private limparTipoCurso(): void {
    this.novoTipoCurso = new TipoCurso();
  }

  private salvarTipoCurso(tipoCurso: TipoCurso): void {
    this.tipoCursoService.salvar(tipoCurso).subscribe((response: TipoCurso) => {
      this.getListaTipoCurso();
      this.certificado.tipoCurso = response;
      this.limparTipoCurso();
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private emModoConsulta(): void {
    this.modoConsulta = !Seguranca.isPermitido([PermissoesEnum.TRABALHADOR_CERTIFICADO,
    PermissoesEnum.TRABALHADOR_CERTIFICADO_CADASTRAR,
    PermissoesEnum.TRABALHADOR_CERTIFICADO_ALTERAR,
    PermissoesEnum.TRABALHADOR_CERTIFICADO_DESATIVAR]);
  }

  private getListaTipoCurso(): void {
    this.tipoCursoService.buscarTodos().subscribe((response: TipoCurso[]) => {
      this.listaTipoCurso = response;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private getListaCertificados(): void {
    this.paginacao.pagina = 1;
    this.pesquisarCertficados(this.idTrabalhador, this.paginacao, SimNao.false);
  }

  private getListaCertificadosTrabalhador(): void {
    this.paginacaoTrabalhador.pagina = 1;
    this.pesquisarCertificadosTrabalhador(this.paginacaoTrabalhador);
  }

  private pesquisarCertficados(idTrabalhador: number, paginacao: Paginacao, inclusaoTrabalhador: SimNao): void {

    this.certificadoService.listarPaginado(idTrabalhador, paginacao, inclusaoTrabalhador)
      .subscribe((retorno: ListaPaginada<Certificado>) => {
        if (retorno.quantidade !== 0) {
          this.listaCertificados = retorno.list;
          this.paginacao = this.getPaginacao(paginacao, retorno);
        } else {
          this.listaCertificados = new Array<Certificado>();
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  private validarFormCertificado(formCertificado) {
    if (!formCertificado.value.nome) {
      this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_nome);
    }

    if (!formCertificado.value.tipoCurso) {
      this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_tipo_curso);
    }

    if (!formCertificado.value.modalidade) {
      this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_modalidade);
    }
    if (this.isUndefined(this.certificado.arquivo) || this.isNull(this.certificado.arquivo)) {
      this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_certificado);
    }
  }

  downloadFile(certificado: Certificado) {
    const tipo = certificado.tipoArquivo.split('/')[1];
    const download = `data:${certificado.tipoArquivo};base64,`.concat(certificado.arquivo);
    const arq = new Blob([this.base64ComoArquivo(download), { type: certificado.tipoArquivo }]);
    FileSaver.saveAs(arq, `${certificado.nomeArquivo}.${tipo}`);
  }

  remover(item) {
    const certificado: Certificado = item;
    const trabalhadorC = new Trabalhador();
    trabalhadorC.id = this.idTrabalhador;
    certificado.trabalhador = trabalhadorC;
    this.certificadoService.desativar(item).
      subscribe((response: Certificado) => {
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.pesquisarCertficados(this.idTrabalhador, this.paginacao, SimNao.false);
        this.pesquisarCertificadosTrabalhador(this.paginacaoTrabalhador);
      }, (error) => {
        this.mensagemError(error);
      });
  }

  getParametro() {
    this.parametroService.pesquisar().subscribe((retorno) => {
      this.tamanho = parseInt(retorno, 10);
      this.labelTamanhoArquivo = MensagemProperties.app_rst_label_tamanho_arquivo.replace('{0}', this.tamanho);
    }, (error) => {
      this.mensagemError(error);
    });
  }
  fileChanged($event) {
    if ($event.target.files && $event.target.files.length > 0) {
      const file: File = $event.target.files[0];
      if ((file.size / 1000000) > this.tamanho) {
        this.mensagemErroComParametrosModel('app_rst_tamanho_arquivo', this.tamanho);
        this.removerUpload();
      } else {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          this.nomeArquivo = file.name;
          this.nomeArquivoTrabalhador = file.name;
          this.certificado.nomeArquivo = file.name.split('.')[0];
          this.certificado.tipoArquivo = file.type;
          this.certificado.arquivo = reader.result.split(',')[1];
        };
      }
    }
  }

  voltar() {
    if (this.activatedRoute.snapshot.url[0].path === 'meusdados') {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}`]);
    }
  }
  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarCertficados(this.idTrabalhador, this.paginacao, SimNao.false);
  }
  pageChangedTrabalhador(event: any): void {
    this.paginacaoTrabalhador.pagina = event.page;
    this.pesquisarCertificadosTrabalhador(this.paginacaoTrabalhador);
  }
  base64ComoArquivo(midia: string): Uint8Array | null {
    const base64 = ';base64,';
    const start = midia.indexOf(base64);
    if (start !== -1) {
      const data = midia.substring(start + base64.length);
      const raw = window.atob(data);
      const array = new Uint8Array(new ArrayBuffer(raw.length));
      for (let i = 0; i < raw.length; i++) {
        array[i] = raw.charCodeAt(i);
      }
      return array;
    }
    return null;
  }

  existeArquivo(): boolean {
    return this.certificado.arquivo != null;
  }

  removerUpload() {
    this.certificado.nomeArquivo = null;
    this.certificado.tipoArquivo = null;
    this.certificado.arquivo = null;
    this.nomeArquivo = null;
    this.nomeArquivoTrabalhador = null;
    if (this.inputFile) {
      this.inputFile.nativeElement.value = '';
    }
    if (this.inputFileTrabalhador) {
      this.inputFileTrabalhador.nativeElement.value = '';
    }
  }

  visualizar(item, modalVisualizarCertificado) {
    this.certificadoService.buscarPorId(item.id).subscribe((response) => {
      this.visualizarCertificado = response;
      if (this.visualizarCertificado.dataValidade) {
        this.visualizarCertificado.dataValidade = DatePicker.convertDateForMyDatePicker(this.visualizarCertificado.dataValidade);
      }
      if (this.visualizarCertificado.dataConclusao) {
        this.visualizarCertificado.dataConclusao = DatePicker.convertDateForMyDatePicker(this.visualizarCertificado.dataConclusao);
      }
      this.nomeArquivoVisualizar = this.visualizarCertificado.nomeArquivo.concat('.' +
        this.visualizarCertificado.tipoArquivo.split('/')[1]);
      this.modalService.open(modalVisualizarCertificado, { size: 'lg' }).result
        .then((result) => {
          this.visualizarCertificado = new Certificado();
        }, (reason) => {
          this.visualizarCertificado = new Certificado();
        });
      }, (error) => {
        this.mensagemError(error);
      });
  }

  salvarArquivoTrabalhador(formCertificadoTrabalhador) {
    if (this.nomeArquivoTrabalhador) {
      const certificadoCopia = new Certificado();
      Object.assign(certificadoCopia, this.certificado);
      certificadoCopia.tipoCurso = null;
      certificadoCopia.inclusaoTrabalhador = 'S';
      certificadoCopia.trabalhador = { id: this.idTrabalhador };
      this.certificadoService.salvar(certificadoCopia).subscribe((response: Certificado) => {
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.removerUpload();
        this.certificado = new Certificado();
        this.certificado.tipoCurso = new TipoCurso();
        formCertificadoTrabalhador.reset();
        this.pesquisarCertificadosTrabalhador(this.paginacaoTrabalhador);
      }, (error) => {
        this.mensagemError(error);
      });
    } else {
      this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_certificado);
    }
  }

  pesquisarCertificadosTrabalhador(paginacaoTrabalhador: Paginacao) {
    this.certificadoService.listarPaginado(this.idTrabalhador, this.paginacaoTrabalhador, SimNao.true)
      .subscribe((retorno: ListaPaginada<Certificado>) => {
        if (retorno.quantidade !== 0) {
          this.listaCertificadosTrabalhador = retorno.list;
          this.paginacaoTrabalhador = this.getPaginacao(this.paginacaoTrabalhador, retorno);
        } else {
          this.listaCertificadosTrabalhador = new Array<Certificado>();
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  temPerfilTrabalhador(): boolean {
    let temPerfilTrab = false;
    if (this.usuarioLogado && this.usuarioLogado.papeis) {
      this.usuarioLogado.papeis.forEach((item) => {
        if (item === PerfilEnum.TRA) {
          temPerfilTrab = true;
        }
      });
    }
    return temPerfilTrab;
  }

  getNomeArquivo(item) {
    return item.nomeArquivo.concat('.' +
      item.tipoArquivo.split('/')[1]);
  }

  exibir() {
    if (this.modoConsulta) {
      if (this.temPerfilTrabalhador()) {
        return false; // ele é trabalhador
      }
      return false; // call center
    }
    if (this.meusDados || this.meusCertificados) {
      return false; // gestor DR e trabalhador vendo seus certificados
    }
    return true; // Gestor Dr e trabalhador ou apenas gestor Dr vendo certificados de outros
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
      if (campo === 'V') {
        this.verificaDataModelValidade = false;
      }
      if (campo === 'C') {
        this.verificaDataModelConclusao = false;
      }
    } else if ($event.target.value.length === 10) {
      if (campo === 'V') {
        this.instanciaDataValidade($event.target.value);
      }
      if (campo === 'C') {
        this.instanciaDataConclusao($event.target.value);
      }
    } else {
      this.verificaDataModelValidade = true;
      this.verificaDataModelConclusao = true;
    }

  }

  instanciaDataValidade(value) {
    if (ValidarData(moment(value, 'DD/MM/YYYY').format('YYYY-MM-DD'))) {
      this.certificado.dataValidade = DatePicker.convertDateForMyDatePicker(value);
      this.verificaDataModelValidade = true;
    } else {
      this.verificaDataModelValidade = false;
    }
  }

  instanciaDataConclusao(value) {
    if (ValidarData(moment(value, 'DD/MM/YYYY').format('YYYY-MM-DD'))) {
      this.certificado.dataConclusao = DatePicker.convertDateForMyDatePicker(value);
      this.verificaDataModelConclusao = true;
    } else {
      this.verificaDataModelConclusao = false;
    }
  }
}
