import { LinhaService } from './../../../servico/linha.service';
import { UatProdutoServicoService } from './../../../servico/uat-produto-servico.service';
import { UatService } from 'app/servico/uat.service';
import { ProdutoServico } from './../../../modelo/produto-servico.model';
import { UatProdutoServico } from './../../../modelo/uat-produto-servico.model';
import { Uat } from 'app/modelo/uat.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { IHash } from './../../../compartilhado/modal-uat-component/uat-modal/uat-modal.component';
import { Linha } from './../../../modelo/linha.model';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { BaseComponent } from './../../../componente/base.component';
import { Component, OnInit } from '@angular/core';
import { ProdutoServicoFilter } from './../../../modelo/filtro-produto-servico';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { ActivatedRoute, Router } from '@angular/router';
import { Paginacao } from './../../../modelo/paginacao.model';

@Component({
  selector: 'app-uat-produto-servico',
  templateUrl: './uat-produto-servico.component.html',
  styleUrls: ['./uat-produto-servico.component.scss'],
})
export class UatProdutoServicoComponent extends BaseComponent implements OnInit {

  idUat: number;
  model: UatProdutoServico;
  uat: Uat;
  filtro: ProdutoServicoFilter;
  listaProdutoServico: ProdutoServico[];
  listaUatProdutoServico: UatProdutoServico[];
  edicao: boolean;
  entidadeSelecionada: boolean;
  indexEdicao = -1;
  listaLinhas: Linha[];
  produtoServicoSelecionado = new Array<ProdutoServico>();
  paginacaoUatProdutoServico: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);
  paginacao: Paginacao = new Paginacao(1, Paginacao.qtdRegistos5);
  checks: IHash = {};
  temPrdodutoServico = false;
  temUatProduto = false;
  uatProdutoServicoSelecionado = new Array<UatProdutoServico>();

  constructor(
    private router: Router,
    private uatService: UatService,
    private activatedRoute: ActivatedRoute,
    private uatProdutoServicoSerivce: UatProdutoServicoService,
    private linhaService: LinhaService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService) {
    super(bloqueioService, dialogo);
    this.tipoTela();
    this.carregarTela();
  }

  ngOnInit() {
    this.filtro = new ProdutoServicoFilter();
    this.model = new UatProdutoServico();
    this.listaProdutoServico = new Array<ProdutoServico>();
    this.listaUatProdutoServico = new Array<UatProdutoServico>();
  }

  tipoTela() {
    this.modoConsulta = !Seguranca.isPermitido([
      PermissoesEnum.CAT_PRODUTO_SERVICO,
      PermissoesEnum.CAT_PRODUTO_SERVICO_CADASTRAR,
      PermissoesEnum.CAT_PRODUTO_SERVICO_ALTERAR,
      PermissoesEnum.CAT_PRODUTO_SERVICO_DESATIVAR]);
  }

  carregarTela() {
    this.activatedRoute.params.subscribe((params) => {
      this.idUat = params['id'];
      if (this.idUat) {
        this.buscarLinhas();
        this.bucarUatPorId(this.idUat);
        this.paginacaoUatProdutoServico.pagina = 1;
        this.pesquisarUatProdutoServicoPaginadoService(this.idUat);
      }
    });
  }

  buscarLinhas() {
    this.linhaService.buscarLinhasAtivasPorIdDepartamentoUat(this.idUat).subscribe((retorno) => {
      this.listaLinhas = retorno;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  bucarUatPorId(id) {
    this.uatService.pesquisarPorId(id).subscribe((retorno) => { this.uat = retorno; }, (error) => {
      this.mensagemError(error);
    });
  }

  pesquisarUatProdutoServicoPaginadoService(idUat: number): void {
    this.uatProdutoServicoSerivce.pesquisarUatProdutoServico(idUat,
      this.paginacaoUatProdutoServico).
      subscribe((retorno: ListaPaginada<UatProdutoServico>) => {
        this.listaUatProdutoServico = retorno.list;
        if (retorno.quantidade > 0) {
          this.temUatProduto = true;
        } else {
          this.temUatProduto = false;
        }
        this.paginacaoUatProdutoServico = this.getPaginacao(this.paginacaoUatProdutoServico,
          retorno);
      }, (error) => {
        this.temUatProduto = false;
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
    this.uatProdutoServicoSerivce.pesquisarProdutoServico(this.filtro, this.idUat, this.paginacao).
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

  pageChangedUatProdutoServico(event: any): void {
    this.paginacaoUatProdutoServico.pagina = event.page;
    this.pesquisarUatProdutoServicoPaginadoService(this.idUat);
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarProdutoServicoPaginadoService();
  }
  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/uat/${this.idUat}`]);
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
        const uatProdutoServico = new UatProdutoServico();
        uatProdutoServico.produtoServico = a;
        this.uatProdutoServicoSelecionado.push(uatProdutoServico);
      });
      this.uatProdutoServicoSerivce.salvar(this.uat.id.toString(),
        this.uatProdutoServicoSelecionado).subscribe((retorno) => {
          this.pesquisarUatProdutoServicoPaginadoService(this.idUat);
          this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);

        }, (error) => {
          this.mensagemError(error);
        });
    }
  }

  remover(uatProdutoServico: UatProdutoServico): void {
    this.uatProdutoServicoSerivce.desativarUatProdutoServico(uatProdutoServico).
      subscribe((response: UatProdutoServico) => {
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.paginacaoUatProdutoServico.pagina = 1;
        this.pesquisarUatProdutoServicoPaginadoService(this.idUat);
      }, (error) => {
        this.mensagemError(error);
      });
  }

  hasPermissaoCadastrar() {
    return this.hasPermissao(PermissoesEnum.CAT_PRODUTO_SERVICO_CADASTRAR)
      || this.hasPermissao(PermissoesEnum.CAT_PRODUTO_SERVICO);
  }

}
