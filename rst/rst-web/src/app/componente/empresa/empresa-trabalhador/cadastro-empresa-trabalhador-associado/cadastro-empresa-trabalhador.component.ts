import { Seguranca } from './../../../../compartilhado/utilitario/seguranca.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { environment } from './../../../../../environments/environment';
import { Empresa } from './../../../../modelo/empresa.model';
import { DatePicker } from './../../../../compartilhado/utilitario/date-picker';
import { Trabalhador } from 'app/modelo/trabalhador.model';
import { FiltroTrabalhador } from './../../../../modelo/filtro-trabalhador.model';
import { TrabalhadorService } from './../../../../servico/trabalhador.service';
import { BloqueioService } from './../../../../servico/bloqueio.service';
import { EmpresaService } from 'app/servico/empresa.service';
import { ListaPaginada } from './../../../../modelo/lista-paginada.model';
import { MensagemProperties } from './../../../../compartilhado/utilitario/recurso.pipe';
import { EmpresaTrabalhadorService } from './../../../../servico/empresa-trabalhador.service';
import { EmpresaTrabalhador } from 'app/modelo/empresa-trabalhador.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { Router, ActivatedRoute } from '@angular/router';
import { OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { Component } from '@angular/core';
import { ToastyService } from 'ng2-toasty';

@Component({
  selector: 'app-cadastro-empresa-trabalhador',
  templateUrl: './cadastro-empresa-trabalhador.component.html',
  styleUrls: ['./cadastro-empresa-trabalhador.component.scss'],
})
export class CadastroEmpresaTrabalhadorComponent extends BaseComponent implements OnInit {

  idEmpresa: number;
  empresa: Empresa;
  idEmpresaTrabalhador: number;
  trabalhador: Trabalhador;
  model: EmpresaTrabalhador;
  filtro: FiltroTrabalhador;
  listaTrabalhador: Trabalhador[];
  edicao: boolean;
  isEmpresaTrabalhadorSelecionado: boolean;
  isTrabalhadorSelecionado: boolean;
  indexEdicao = -1;
  paginacao: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);

  constructor(
    private router: Router,
    private empresaService: EmpresaService,
    private activatedRoute: ActivatedRoute,
    private empresaTrabalhadorService: EmpresaTrabalhadorService,
    private trabalhadorService: TrabalhadorService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.trabalhador = new Trabalhador();
    this.empresa = new Empresa();
    this.filtro = new FiltroTrabalhador();
    this.model = new EmpresaTrabalhador();
    this.listaTrabalhador = new Array<Trabalhador>();
    this.emModoConsulta();
    this.setEmpresaAndEmpresaTrabalhador();
    this.carregarEmpresaTrabalhador(this.idEmpresaTrabalhador);
    this.setEmpresa();
  }

  private emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.EMPRESA_TRABALHADOR,
      PermissoesEnum.EMPRESA_TRABALHADOR_CADASTRAR,
      PermissoesEnum.EMPRESA_TRABALHADOR_ALTERAR,
      PermissoesEnum.EMPRESA_TRABALHADOR_DESATIVAR]);
  }

  private setEmpresa() {
    this.idEmpresa = this.activatedRoute.snapshot.params['id'];
    this.empresaService.pesquisarPorId(this.idEmpresa).subscribe((retorno: Empresa) => {
      this.empresa = retorno;
    }, (error) => { this.mensagemErroComParametros(MensagemProperties.app_rst_erro_geral); });
  }

  pesquisar(): void {
    if (this.validarPesquisa()) {
      this.paginacao.pagina = 1;
      this.pesquisarTrabalhadorPaginado();
    }
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarTrabalhadorPaginado();
  }

  selecionarTrabalhador(item: Trabalhador): void {
    this.model = new EmpresaTrabalhador();
    this.model.trabalhador = item;
    this.isTrabalhadorSelecionado = true;
    this.isEmpresaTrabalhadorSelecionado = false;
  }

  limpar() {
    if (this.idEmpresaTrabalhador) {
      if (this.activatedRoute.snapshot.url[0].path === 'minhaempresa') {
        this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa/${this.idEmpresa}/trabalhador`]);
      } else {
        this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}/trabalhador`]);
      }
    } else {
      this.trabalhador = new Trabalhador();
      this.model = new EmpresaTrabalhador();
      this.isTrabalhadorSelecionado = false;
      this.isEmpresaTrabalhadorSelecionado = false;
    }
  }

  voltar(): void {
    if (this.activatedRoute.snapshot.url[0].path === 'minhaempresa') {
      this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa/${this.idEmpresa}/trabalhador`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}/trabalhador`]);
    }
  }

  adicionarEmpresaTrabalhador() {
    this.salvar(this.prepareSave(this.model));
  }

  lotacoes(): void {
    if (this.activatedRoute.snapshot.url[0].path === 'minhaempresa') {
      // tslint:disable-next-line:max-line-length
      this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa/${this.idEmpresa}/trabalhador/lotacao/${this.model.id}`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}/trabalhador/lotacao/${this.model.id}`]);
    }
  }

  removerEmpresaTrabalhador(item: any): void {
    this.remover(item);
    this.limpar();
  }

  hasPermissaoCadastrar() {
    return this.hasPermissao(PermissoesEnum.EMPRESA_TRABALHADOR_CADASTRAR)
      || this.hasPermissao(PermissoesEnum.EMPRESA_TRABALHADOR);
  }

  hasPermissaoAlterar() {
    return this.hasPermissao(PermissoesEnum.EMPRESA_TRABALHADOR_ALTERAR)
      || this.hasPermissao(PermissoesEnum.EMPRESA_TRABALHADOR);
  }

  private prepareSave(model: EmpresaTrabalhador): EmpresaTrabalhador {
    const empresaTrabalhador = {
      id: model.id,
      dataCriacao: model.dataCriacao,
      empresa: { id: model.empresa.id },
      trabalhador: { id: model.trabalhador.id },
      dataAdmissao: model.dataAdmissao ? this.convertDateToString(model.dataAdmissao.date) : null,
      dataDemissao: model.dataDemissao ? this.convertDateToString(model.dataDemissao.date) : null,
    };
    return empresaTrabalhador as EmpresaTrabalhador;
  }

  private salvar(empresaTraabalhador: any): void {
    empresaTraabalhador.empresa.id = this.idEmpresa;
    this.empresaTrabalhadorService.salvar(empresaTraabalhador).subscribe((response: EmpresaTrabalhador) => {
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.model.id = response.id;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private setEmpresaAndEmpresaTrabalhador() {
    this.idEmpresa = this.activatedRoute.snapshot.params['id'];
    this.idEmpresaTrabalhador = this.activatedRoute.snapshot.params['idEmpresaTrabalhador'];
  }

  private validarPesquisa(): boolean {

    if (!this.isVazia(this.filtro.cpf)) {
      if (this.filtro.cpf.length < 14) {
        this.mensagemError(MensagemProperties.app_rst_labels_cpf_incompleto);
        return false;
      }
    }

    if (!this.filtro.nome && !this.filtro.cpf) {
      this.mensagemErroComParametrosModel('app_rst_msg_pesquisar_todos_vazios');
      return false;
    }
    return true;
  }

  private carregarEmpresaTrabalhador(idEmpresaTrabalhador: number) {
    if (idEmpresaTrabalhador) {
      this.empresaTrabalhadorService.buscarPorId(idEmpresaTrabalhador).subscribe((response: EmpresaTrabalhador) => {
        this.parseResponseModel(response);
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  private parseResponseModel(response: EmpresaTrabalhador) {
    this.model = {
      id: response.id,
      empresa: response.empresa,
      trabalhador: response.trabalhador,
      dataAdmissao: DatePicker.convertDateForMyDatePicker(response.dataAdmissao),
      dataDemissao: DatePicker.convertDateForMyDatePicker(response.dataDemissao),
      dataCriacao: response.dataCriacao,
      dataAlteracao: response.dataAlteracao,
      dataExclusao: response.dataExclusao,
    };
  }

  private pesquisarTrabalhadorPaginado(): void {
    this.filtro.falecidos = false;
    this.filtro.aplicarDadosFilter = false;
    this.trabalhadorService.pesquisarPaginado(this.filtro, this.paginacao).
      subscribe((retorno: ListaPaginada<Trabalhador>) => {
        this.listaTrabalhador = retorno.list;
        this.paginacao = this.getPaginacao(this.paginacao, retorno);
        if (retorno.quantidade === 0) {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
        this.isTrabalhadorSelecionado = false;
      }, (error) => {
        this.mensagemError(error);
      });
  }

  private remover(empresaTrabalhador: EmpresaTrabalhador): void {
    this.empresaTrabalhadorService.remover(empresaTrabalhador).subscribe((response: EmpresaTrabalhador) => {
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
    }, (error) => {
      this.mensagemError(error);
    });
  }

}
