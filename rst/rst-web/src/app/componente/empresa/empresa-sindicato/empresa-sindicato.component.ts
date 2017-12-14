import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { environment } from './../../../../environments/environment';
import { DatePicker } from './../../../compartilhado/utilitario/date-picker';
import { EmpresaSindicatoService } from './../../../servico/empresa.-sindicato.service';
import { Paginacao } from 'app/modelo/paginacao.model';
import { SindicatoService } from './../../../servico/sindicato.service';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { Sindicato } from './../../../modelo/sindicato.model';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { EmpresaSindicato } from './../../../modelo/empresa-sindicato.model';
import { FiltroSindicato } from './../../../modelo/filtro-sindicato.model';
import { Router, ActivatedRoute } from '@angular/router';
import { EmpresaService } from './../../../servico/empresa.service';
import { OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { Component } from '@angular/core';

import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';

@Component({
  selector: 'app-empresa-sindicato',
  templateUrl: './empresa-sindicato.component.html',
  styleUrls: ['./empresa-sindicato.component.scss'],
})
export class EmpresaSindicatoComponent extends BaseComponent implements OnInit {

  dataAssociacao: any;
  dataDesligamento: any;
  idEmpresa: number;
  model: EmpresaSindicato;
  modelEmpresaSindicato: EmpresaSindicato;
  filtro: FiltroSindicato;
  listaSindicato: Sindicato[];
  listaEmpresaSindicato: EmpresaSindicato[];
  edicao: boolean;
  sindicatoSelecionado = {
    valor: false,
  };

  empresaSindicatoSelecionada = {
    valor: false,
  };
  indexEdicao = -1;
  paginacaoEmpresaSindicato: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);
  paginacao: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);

  constructor(
    private router: Router,
    private empresaService: EmpresaService,
    private activatedRoute: ActivatedRoute,
    private empresaSindicatoSerivce: EmpresaSindicatoService,
    private sindicatoService: SindicatoService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.filtro = new FiltroSindicato();
    this.model = new EmpresaSindicato();
    this.modelEmpresaSindicato = new EmpresaSindicato();
    this.listaSindicato = new Array<Sindicato>();
    this.listaEmpresaSindicato = new Array<EmpresaSindicato>();
    this.getIdEmpresa();
    this.carregarEmpresaSindicato();
    this.emModoConsulta();
  }

  private getIdEmpresa() {
    this.activatedRoute.params.subscribe((params) => {
      this.idEmpresa = params['id'];
    });
  }
  private carregarEmpresaSindicato() {
    if (this.idEmpresa) {
      this.paginacaoEmpresaSindicato.pagina = 1;
      this.pesquisarEmpresaSindicatoPaginadoService(this.idEmpresa);
    }
  }

  pesquisar(): void {
    if (this.validarPesquisa()) {
      this.listaSindicato = new Array<Sindicato>();
      this.paginacao.pagina = 1;
      this.pesquisarSindicatoPaginadoService();
      this.sindicatoSelecionado.valor = false;
    }
  }

  pageChangedEmpresaSindicato(event: any): void {
    this.paginacaoEmpresaSindicato.pagina = event.page;
    this.pesquisarEmpresaSindicatoPaginadoService(this.idEmpresa);
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarSindicatoPaginadoService();
  }

  selecionarEmpresaSindicato(item: EmpresaSindicato, index: any) {
    if (index != null) {
      this.modelEmpresaSindicato = new EmpresaSindicato(item);
      this.edicao = true;
      this.empresaSindicatoSelecionada.valor = true;
      this.sindicatoSelecionado.valor = false;
      this.indexEdicao = index;
      this.listaSindicato = new Array<Sindicato>();
      this.dataAssociacao = this.modelEmpresaSindicato.dataAssociacao ?
        DatePicker.convertDateForMyDatePicker(this.modelEmpresaSindicato.dataAssociacao) : null;
      this.dataDesligamento = this.modelEmpresaSindicato.dataDesligamento ?
        DatePicker.convertDateForMyDatePicker(this.modelEmpresaSindicato.dataDesligamento) : null;
    }
  }


  private emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.EMPRESA_SINDICATO,
      PermissoesEnum.EMPRESA_SINDICATO_CADASTRAR,
      PermissoesEnum.EMPRESA_SINDICATO_ALTERAR,
      PermissoesEnum.EMPRESA_SINDICATO_DESATIVAR]);
  }

  selecionarSindicato(item: any): void {
    this.model = new EmpresaSindicato();
    this.model.sindicato = item;
    this.sindicatoSelecionado.valor = true;
    this.empresaSindicatoSelecionada.valor = false;
  }

  removerEmpresaSindicato(item: any): void {
    this.remover(item);
    this.limpar();
  }

  limpar() {
    this.model = new EmpresaSindicato();
    this.sindicatoSelecionado.valor = false;
    this.empresaSindicatoSelecionada.valor = false;
  }

  voltar(): void {
    if (this.activatedRoute.snapshot.url[0].path === 'minhaempresa') {
      this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}`]);
    }
  }

  private validarPesquisa(): boolean {
    if (!this.filtro.razaoSocial && !this.filtro.cnpj) {
      this.mensagemErroComParametrosModel('app_rst_msg_pesquisar_todos_vazios');
      return false;
    }

    return true;
  }

  private pesquisarEmpresaSindicatoPaginadoService(idEmpresa: number): void {
    this.empresaSindicatoSerivce.pesquisarEmpresasSindicatos(idEmpresa, this.paginacaoEmpresaSindicato).
      subscribe((retorno: ListaPaginada<EmpresaSindicato>) => {
        this.listaEmpresaSindicato = retorno.list;
        this.paginacaoEmpresaSindicato = this.getPaginacao(this.paginacaoEmpresaSindicato, retorno);
      }, (error) => {
        this.mensagemError(error);
      });
  }

  private pesquisarSindicatoPaginadoService(): void {
    this.filtro.aplicarDadosFilter = false;
    this.sindicatoService.pesquisarAtivos(this.filtro, this.paginacao).
      subscribe((retorno: ListaPaginada<Sindicato>) => {
        if (retorno.list.length > 0) {
          this.listaSindicato = retorno.list;
          this.paginacao = this.getPaginacao(this.paginacao, retorno);
        } else {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  private remover(empresaSindicato: EmpresaSindicato): void {
    this.empresaSindicatoSerivce.removerEmpresaSindicato(empresaSindicato).subscribe((response: EmpresaSindicato) => {
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.paginacaoEmpresaSindicato.pagina = 1;
      this.pesquisarEmpresaSindicatoPaginadoService(this.idEmpresa);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  hasPermissaoCadastrar() {
    return this.hasPermissao(PermissoesEnum.EMPRESA_SINDICATO_CADASTRAR)
      || this.hasPermissao(PermissoesEnum.EMPRESA_SINDICATO);
  }

  hasPermissaoDesativar() {
    return this.hasPermissao(PermissoesEnum.EMPRESA_SINDICATO_DESATIVAR)
      || this.hasPermissao(PermissoesEnum.EMPRESA_SINDICATO);
  }
}
