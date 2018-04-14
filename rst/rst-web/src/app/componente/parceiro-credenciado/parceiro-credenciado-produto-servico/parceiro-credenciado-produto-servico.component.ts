import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { environment } from './../../../../environments/environment';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { ToastyService } from 'ng2-toasty';
import { LinhaService } from './../../../servico/linha.service';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { ProdutoServicoService } from './../../../servico/produto-servico.service';
import { ParceiroProdutoServicoService } from './../../../servico/parceiro-produto-servico.service';
import { ParceiroService } from './../../../servico/parceiro.service';
import { Router, ActivatedRoute } from '@angular/router';
import { IHash } from './../../empresa/empresa-cbo/empresa-cbo.component';
import { Paginacao } from './../../../modelo/paginacao.model';
import { Linha } from './../../../modelo/linha.model';
import { ProdutoServicoFilter } from './../../../modelo/filtro-produto-servico';
import { ProdutoServico } from './../../../modelo/produto-servico.model';
import { Parceiro } from './../../../modelo/parceiro.model';
import { ParceiroProdutoServico } from './../../../modelo/parceiro-produto-servico';
import { BaseComponent } from './../../../componente/base.component';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-parceiro-credenciado-produto-servico',
  templateUrl: './parceiro-credenciado-produto-servico.component.html',
  styleUrls: ['./parceiro-credenciado-produto-servico.component.scss'],
})
export class ParceiroCredenciadoProdutoServicoComponent extends BaseComponent implements OnInit {

  idParceiro: number;
  model: ParceiroProdutoServico;
  parceiro: Parceiro;
  filtro: ProdutoServicoFilter;
  listaProdutoServico: ProdutoServico[];
  listaParceiroProdutoServico: ParceiroProdutoServico[];
  edicao: boolean;
  entidadeSelecionada: boolean;
  indexEdicao = -1;
  listaLinhas: Linha[];
  produtoServicoSelecionado = new Array<ProdutoServico>();
  paginacaoParceiroProdutoServico: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);
  paginacao: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);
  checks: IHash = {};
  temPrdodutoServico = false;
  temParceiroProduto = false;
  temDepartamentoProduto = false;
  listaParceiroProdutoServicoSelecionado = new Array<ParceiroProdutoServico>();
  constructor(
    private router: Router,
    private parceiroService: ParceiroService,
    private activatedRoute: ActivatedRoute,
    private ParceiroProdutoServicoSerivce: ParceiroProdutoServicoService,
    private ProdutoServicoService: ProdutoServicoService,
    protected bloqueioService: BloqueioService,
    protected linhaService: LinhaService,
    protected dialogo: ToastyService) {
    super(bloqueioService, dialogo);
    this.emModoConsulta();
    this.buscarParceiro();
    this.buscarLinhas();
  }

  ngOnInit() {
    this.filtro = new ProdutoServicoFilter();
    this.model = new ParceiroProdutoServico();
    this.listaProdutoServico = new Array<ProdutoServico>();
    this.listaParceiroProdutoServico = new Array<ParceiroProdutoServico>();
  }

  emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido(
      [PermissoesEnum.PARCEIRO_CREDENCIADA,
      PermissoesEnum.PARCEIRO_CREDENCIADA_CADASTRAR,
      PermissoesEnum.PARCEIRO_CREDENCIADA_ALTERAR,
      PermissoesEnum.PARCEIRO_CREDENCIADA_DESATIVAR]);
  }

  buscarParceiro() {
    this.activatedRoute.params.subscribe((params) => {
      this.idParceiro = params['id'];
      if (this.idParceiro) {
        this.bucarParceiroPorId(this.idParceiro);
        this.paginacaoParceiroProdutoServico.pagina = 1;
        this.pesquisarParceiroProdutoServicoPaginadoService(this.idParceiro);
      }
    }, (error) => {
      this.mensagemError(error);
    });
  }

  buscarLinhas() {
    this.linhaService.buscarTodas().subscribe((retorno) => {
      this.listaLinhas = retorno;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  bucarParceiroPorId(id) {
    this.parceiroService.pesquisarPorId(id).subscribe((retorno) => {
    this.parceiro = retorno;
    }, (error) => {
      this.mensagemError(error);
    });
  }
  pesquisarParceiroProdutoServicoPaginadoService(idParceiro: number): void {
    this.ParceiroProdutoServicoSerivce.pesquisarParceiroProdutoServico(idParceiro,
      this.paginacaoParceiroProdutoServico).
      subscribe((retorno: ListaPaginada<ParceiroProdutoServico>) => {
        this.listaParceiroProdutoServico = retorno.list;
        if (retorno.quantidade > 0) {
          this.temParceiroProduto = true;
        } else {
          this.temParceiroProduto = false;
        }
        this.paginacaoParceiroProdutoServico = this.getPaginacao(this.paginacaoParceiroProdutoServico,
          retorno);
      }, (error) => {
        this.temParceiroProduto = false;
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

  pageChangedParceiroProdutoServico(event: any): void {
    this.paginacaoParceiroProdutoServico.pagina = event.page;
    this.pesquisarParceiroProdutoServicoPaginadoService(this.idParceiro);
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarProdutoServicoPaginadoService();
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/parceirocredenciado/${this.idParceiro}`]);
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
        const redeCredenciadaProdutoServico = new ParceiroProdutoServico();
        redeCredenciadaProdutoServico.produtoServico = a;
        this.listaParceiroProdutoServicoSelecionado.push(redeCredenciadaProdutoServico);
      });
      this.ParceiroProdutoServicoSerivce.salvar(this.parceiro.id.toString(),
        this.listaParceiroProdutoServicoSelecionado).subscribe((retorno) => {
          this.pesquisarParceiroProdutoServicoPaginadoService(this.idParceiro);
          this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        }, (error) => {
          this.mensagemError(error);
        });
    }
  }

  remover(redeCredenciadaProdutoServico: ParceiroProdutoServico): void {
    this.ParceiroProdutoServicoSerivce.desativarDepartamentoRergionalProdutoServico(redeCredenciadaProdutoServico).
      subscribe((response: ParceiroProdutoServico) => {
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.paginacaoParceiroProdutoServico.pagina = 1;
        this.pesquisarParceiroProdutoServicoPaginadoService(this.idParceiro);
      }, (error) => {
        this.mensagemError(error);
      });
  }

  hasPermissaoCadastrar() {
    return Seguranca.isPermitido([PermissoesEnum.PARCEIRO_PRODUTO_SERVICO,
    PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_CADASTRAR]);
  }

}
