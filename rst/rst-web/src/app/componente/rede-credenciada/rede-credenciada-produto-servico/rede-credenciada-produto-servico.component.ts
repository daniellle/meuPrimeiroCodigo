import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { BaseComponent } from 'app/componente/base.component';
import { ToastyService } from 'ng2-toasty';
import { LinhaService } from './../../../servico/linha.service';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { ProdutoServicoService } from './../../../servico/produto-servico.service';
import { RedeCredenciadaProdutoServicoService } from './../../../servico/rede-credenciada-produto-servico.service';
import { RedeCredenciadaService } from './../../../servico/rede-credenciada.service';
import { Router, ActivatedRoute } from '@angular/router';
import { IHash } from './../../empresa/empresa-funcao/empresa-funcao.component';
import { Paginacao } from 'app/modelo/paginacao.model';
import { Linha } from './../../../modelo/linha.model';
import { ProdutoServico } from './../../../modelo/produto-servico.model';
import { ProdutoServicoFilter } from './../../../modelo/filtro-produto-servico';
import { RedeCredenciada } from './../../../modelo/rede-credenciada.model';
import { RedeCredenciadaProdutoServico } from './../../../modelo/rede-credenciada-produto-servico';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-rede-credenciada-produto-servico',
  templateUrl: './rede-credenciada-produto-servico.component.html',
  styleUrls: ['./rede-credenciada-produto-servico.component.scss'],
})
export class RedeCredenciadaProdutoServicoComponent extends BaseComponent implements OnInit {

  idRedeCredenciada: number;
  model: RedeCredenciadaProdutoServico;
  redeCredenciada: RedeCredenciada;
  filtro: ProdutoServicoFilter;
  listaProdutoServico: ProdutoServico[];
  listaRedeCredenciadaProdutoServico: RedeCredenciadaProdutoServico[];
  edicao: boolean;
  entidadeSelecionada: boolean;
  indexEdicao = -1;
  listaLinhas: Linha[];
  produtoServicoSelecionado = new Array<ProdutoServico>();
  paginacaoRedeCredenciadaProdutoServico: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);
  paginacao: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);
  checks: IHash = {};
  temPrdodutoServico = false;
  temRedeProduto = false;
  temDepartamentoProduto = false;
  listaRedeCredenciadaProdutoServicoSelecionado = new Array<RedeCredenciadaProdutoServico>();
  constructor(
    private router: Router,
    private redeCredenciadaService: RedeCredenciadaService,
    private activatedRoute: ActivatedRoute,
    private redeCredenciadaProdutoServicoSerivce: RedeCredenciadaProdutoServicoService,
    private ProdutoServicoService: ProdutoServicoService,
    protected bloqueioService: BloqueioService,
    protected linhaService: LinhaService,
    protected dialogo: ToastyService) {
    super(bloqueioService, dialogo);
    this.buscarRedeCredenciada();
    this.buscarLinhas();
    this.tipoTela();
  }

  ngOnInit() {
    this.filtro = new ProdutoServicoFilter();
    this.model = new RedeCredenciadaProdutoServico();
    this.listaProdutoServico = new Array<ProdutoServico>();
    this.listaRedeCredenciadaProdutoServico = new Array<RedeCredenciadaProdutoServico>();
  }


  tipoTela() {
    this.modoConsulta = !Seguranca.isPermitido([
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_CADASTRAR,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_ALTERAR,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_DESATIVAR]);
  }

  buscarRedeCredenciada() {
    this.activatedRoute.params.subscribe((params) => {
      this.idRedeCredenciada = params['id'];
      if (this.idRedeCredenciada) {
        this.bucarRedeCredenciadaPorId(this.idRedeCredenciada);
        this.paginacaoRedeCredenciadaProdutoServico.pagina = 1;
        this.pesquisarRedeCredenciadaProdutoServicoPaginadoService(this.idRedeCredenciada);
      }
    });
  }

  buscarLinhas() {
    this.linhaService.buscarTodas().subscribe((retorno) => {
      this.listaLinhas = retorno;
    });
  }

  bucarRedeCredenciadaPorId(id) {
    this.redeCredenciadaService.pesquisarPorId(id).subscribe((retorno) => { this.redeCredenciada = retorno; });
  }
  pesquisarRedeCredenciadaProdutoServicoPaginadoService(idRedeCredenciada: number): void {
    this.redeCredenciadaProdutoServicoSerivce.pesquisarRedeCredenciadaProdutoServico(idRedeCredenciada,
      this.paginacaoRedeCredenciadaProdutoServico).
      subscribe((retorno: ListaPaginada<RedeCredenciadaProdutoServico>) => {
        this.listaRedeCredenciadaProdutoServico = retorno.list;
        if (retorno.quantidade > 0) {
          this.temRedeProduto = true;
        } else {
          this.temRedeProduto = false;
        }
        this.paginacaoRedeCredenciadaProdutoServico = this.getPaginacao(this.paginacaoRedeCredenciadaProdutoServico,
          retorno);
      }, (error) => {
        this.temRedeProduto = false;
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

  pageChangedRedeCredenciadaProdutoServico(event: any): void {
    this.paginacaoRedeCredenciadaProdutoServico.pagina = event.page;
    this.pesquisarRedeCredenciadaProdutoServicoPaginadoService(this.idRedeCredenciada);
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarProdutoServicoPaginadoService();
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/redecredenciada/${this.idRedeCredenciada}`]);
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
        const redeCredenciadaProdutoServico = new RedeCredenciadaProdutoServico();
        redeCredenciadaProdutoServico.produtoServico = a;
        this.listaRedeCredenciadaProdutoServicoSelecionado.push(redeCredenciadaProdutoServico);
      });
      this.redeCredenciadaProdutoServicoSerivce.salvar(this.redeCredenciada.id.toString(),
        this.listaRedeCredenciadaProdutoServicoSelecionado).subscribe((retorno) => {
          this.pesquisarRedeCredenciadaProdutoServicoPaginadoService(this.idRedeCredenciada);
          this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        }, (error) => {
          this.mensagemError(error);
        });
    }
  }

  remover(redeCredenciadaProdutoServico: RedeCredenciadaProdutoServico): void {
    this.redeCredenciadaProdutoServicoSerivce.desativarDepartamentoRergionalProdutoServico(redeCredenciadaProdutoServico).
      subscribe((response: RedeCredenciadaProdutoServico) => {
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.paginacaoRedeCredenciadaProdutoServico.pagina = 1;
        this.pesquisarRedeCredenciadaProdutoServicoPaginadoService(this.idRedeCredenciada);
      }, (error) => {
        this.mensagemError(error);
      });
  }
}
