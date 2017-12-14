import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { DepartamentoRegional } from './../../../modelo/departamento-regional.model';
import { IHash } from './../../../compartilhado/modal-uat-component/uat-modal/uat-modal.component';
import { Linha } from './../../../modelo/linha.model';
import { LinhaService } from './../../../servico/linha.service';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { BaseComponent } from './../../../componente/base.component';
import { Component, OnInit } from '@angular/core';
import { DepartamentoRegionalProdutoServicoService } from './../../../servico/departamento-regional-produto-servico.service';
import { ProdutoServicoService } from './../../../servico/produto-servico.service';
import { ProdutoServicoFilter } from './../../../modelo/filtro-produto-servico';
import { DepartRegionalService } from './../../../servico/depart-regional.service';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { ActivatedRoute, Router } from '@angular/router';
import { Paginacao } from './../../../modelo/paginacao.model';
import { DepartamentoRegionalProdutoServico } from './../../../modelo/departamento-regional-produto-servico';
import { ProdutoServico } from './../../../modelo/produto-servico.model';

@Component({
  selector: 'app-departamento-regional-produto-servico',
  templateUrl: './departamento-regional-produto-servico.component.html',
  styleUrls: ['./departamento-regional-produto-servico.component.scss'],
})
export class DepartamentoRegionalProdutoServicoComponent extends BaseComponent implements OnInit {

  idDepartamentoRegional: number;
  model: DepartamentoRegionalProdutoServico;
  departaentoRegional: DepartamentoRegional;
  filtro: ProdutoServicoFilter;
  listaProdutoServico: ProdutoServico[];
  listaDepartamentoRegionalProdutoServico: DepartamentoRegionalProdutoServico[];
  edicao: boolean;
  entidadeSelecionada: boolean;
  indexEdicao = -1;
  listaLinhas: Linha[];
  produtoServicoSelecionado = new Array<ProdutoServico>();
  paginacaoDepartamentoRegionalProdutoServico: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);
  paginacao: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);
  checks: IHash = {};
  temPrdodutoServico = false;
  temDepartamentoProduto = false;
  departamentoRegionalProdutoServicoSelecionado = new Array<DepartamentoRegionalProdutoServico>();
  constructor(
    private router: Router,
    private departamentoRegionalService: DepartRegionalService,
    private activatedRoute: ActivatedRoute,
    private DepartamentoRegionalProdutoServicoSerivce: DepartamentoRegionalProdutoServicoService,
    private ProdutoServicoService: ProdutoServicoService,
    protected bloqueioService: BloqueioService,
    protected linhaService: LinhaService,
    protected dialogo: ToastyService) {
    super(bloqueioService, dialogo);
    this.tipoTela();
    this.buscarDepartamentoRegional();
    this.buscarLinhas();
  }

  ngOnInit() {
    this.filtro = new ProdutoServicoFilter();
    this.model = new DepartamentoRegionalProdutoServico();
    this.listaProdutoServico = new Array<ProdutoServico>();
    this.listaDepartamentoRegionalProdutoServico = new Array<DepartamentoRegionalProdutoServico>();
  }

  tipoTela() {
    this.modoConsulta = !Seguranca.isPermitido([
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CADASTRAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_ALTERAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_DESATIVAR]);
  }

  buscarDepartamentoRegional() {
    this.activatedRoute.params.subscribe((params) => {
      this.idDepartamentoRegional = params['id'];
      if (this.idDepartamentoRegional) {
        this.bucarDepartamentoRegionalPorId(this.idDepartamentoRegional);
        this.paginacaoDepartamentoRegionalProdutoServico.pagina = 1;
        this.pesquisarDepartamentoRegionalProdutoServicoPaginadoService(this.idDepartamentoRegional);
      }
    });
  }

  buscarLinhas() {
    this.linhaService.buscarTodas().subscribe((retorno) => {
      this.listaLinhas = retorno;
    });
  }

  bucarDepartamentoRegionalPorId(id) {
    this.departamentoRegionalService.pesquisarPorId(id).subscribe((retorno) => { this.departaentoRegional = retorno; });
  }
  pesquisarDepartamentoRegionalProdutoServicoPaginadoService(idDepartamentoRegional: number): void {
    this.DepartamentoRegionalProdutoServicoSerivce.pesquisarDepartamentoRegionalProdutoServico(idDepartamentoRegional,
      this.paginacaoDepartamentoRegionalProdutoServico).
      subscribe((retorno: ListaPaginada<DepartamentoRegionalProdutoServico>) => {
        this.listaDepartamentoRegionalProdutoServico = retorno.list;
        if (retorno.quantidade > 0) {
          this.temDepartamentoProduto = true;
        } else {
          this.temDepartamentoProduto = false;
        }
        this.paginacaoDepartamentoRegionalProdutoServico = this.getPaginacao(this.paginacaoDepartamentoRegionalProdutoServico,
          retorno);
      }, (error) => {
        this.temDepartamentoProduto = false;
        this.mensagemError(error);
      });
  }
  pesquisar(): void {
    if (this.validarPesquisa()) {
      this.listaProdutoServico = new Array<ProdutoServico>();
      this.paginacao.pagina = 1;
      this.pesquisarProdutoServicoPaginadoService();
    }
  }

  validarPesquisa(): boolean {
    if (!this.filtro.idLinha && this.isVazio(this.filtro.nome)) {
      this.mensagemErroComParametrosModel('app_rst_msg_pesquisar_todos_vazios');
      return false;
    }
    return true;
  }

  isVazio(nome: any) {
    if (this.isUndefined(nome) || nome === '') {
      return true;
    }
    return false;
  }
  pesquisarProdutoServicoPaginadoService(): void {
    this.filtro.aplicarDadosFilter = false;
    this.ProdutoServicoService.pesquisarPaginado(this.filtro, this.paginacao).
      subscribe((retorno: ListaPaginada<ProdutoServico>) => {
        if (retorno.list.length > 0) {
          this.listaProdutoServico = retorno.list;
          this.paginacao = this.getPaginacao(this.paginacao, retorno);
          this.temPrdodutoServico = true;
        } else {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
          this.temPrdodutoServico = false;
        }
      }, (error) => {
        this.temPrdodutoServico = false;
        this.mensagemError(error);
      });
  }

  pageChangedDepartamentoRegionalProdutoServico(event: any): void {
    this.paginacaoDepartamentoRegionalProdutoServico.pagina = event.page;
    this.pesquisarDepartamentoRegionalProdutoServicoPaginadoService(this.idDepartamentoRegional);
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarProdutoServicoPaginadoService();
  }
  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/departamentoregional/${this.idDepartamentoRegional}`]);
  }

  selectDeselectProdutoServico(ProdutoServico: ProdutoServico, event: any, i: number) {
    if (ProdutoServico && event.checked) {
      this.produtoServicoSelecionado.push(ProdutoServico);
      this.checks[i] = true;
    } else {
      const index: number = this.produtoServicoSelecionado.indexOf(ProdutoServico);
      if (index !== -1) {
        this.produtoServicoSelecionado.splice(index, 1);
      }
      this.checks[i] = false;
    }
  }

  salvar() {
    if (this.produtoServicoSelecionado) {
      this.produtoServicoSelecionado.map((a) => {
        const departamentoRegionalProdutoServico = new DepartamentoRegionalProdutoServico();
        departamentoRegionalProdutoServico.produtoServico = a;
        this.departamentoRegionalProdutoServicoSelecionado.push(departamentoRegionalProdutoServico);
      });
      this.DepartamentoRegionalProdutoServicoSerivce.salvar(this.departaentoRegional.id.toString(),
        this.departamentoRegionalProdutoServicoSelecionado).subscribe((retorno) => {
          this.pesquisarDepartamentoRegionalProdutoServicoPaginadoService(this.idDepartamentoRegional);
          this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);

        }, (error) => {
          this.mensagemError(error);
        });
    }
  }

  remover(departamentoRegionalProdutoServico: DepartamentoRegionalProdutoServico): void {
    this.DepartamentoRegionalProdutoServicoSerivce.desativarDepartamentoRergionalProdutoServico(departamentoRegionalProdutoServico).
      subscribe((response: DepartamentoRegionalProdutoServico) => {
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.paginacaoDepartamentoRegionalProdutoServico.pagina = 1;
        this.pesquisarDepartamentoRegionalProdutoServicoPaginadoService(this.idDepartamentoRegional);
      }, (error) => {
        this.mensagemError(error);
      });
  }

  hasPermissaoCadastrar() {
    return Seguranca.isPermitido([PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CADASTRAR]);
  }

}
