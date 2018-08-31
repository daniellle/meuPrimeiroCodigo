import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { RespostaService } from './../../../servico/resposta.service';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Resposta } from './../../../modelo/resposta.model';
import { RespostaFilter } from './../../../modelo/filtro-resposta';
import { BaseComponent } from 'app/componente/base.component';
import { Component, OnInit } from '@angular/core';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
  selector: 'app-pesquisa-resposta',
  templateUrl: './pesquisa-resposta.component.html',
  styleUrls: ['./pesquisa-resposta.component.scss']
})
export class PesquisaRespostaComponent extends BaseComponent implements OnInit {

  filtro: RespostaFilter;
  filtroSelecionado: RespostaFilter;
  listaResposta: Resposta[];
  respostaSelecionada: Resposta;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private respostaService: RespostaService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.filtro = new RespostaFilter();
    this.filtroSelecionado = new RespostaFilter();
    this.listaResposta = new Array<Resposta>();
    this.title = this.activatedRoute.snapshot.data.title;
    this.respostaSelecionada = new Resposta();
    this.pesquisar();
  }

  selecionar(model: any) {
    if (model && model.id) {
      this.router.navigate([model.id], { relativeTo: this.activatedRoute },
      );
    }
  }

  incluir() {
    this.router.navigate(['cadastrar'], { relativeTo: this.activatedRoute });
  }

  voltar() {
    this.router.navigate([`${environment.path_raiz_cadastro}/questionario`]);
  }

  pesquisar(): void {
    this.listaResposta = new Array<Resposta>();
    this.filtroSelecionado = new RespostaFilter(this.filtro);
    this.paginacao.pagina = 1;
    this.pesquisarProdutoServicoPaginadoService(this.filtroSelecionado, this.paginacao);
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarProdutoServicoPaginadoService(this.filtroSelecionado, this.paginacao);
  }

  private pesquisarProdutoServicoPaginadoService(filtro: RespostaFilter, paginacao: Paginacao): void {
    this.respostaService.pesquisarPaginado(filtro, paginacao).
      subscribe((retorno: ListaPaginada<Resposta>) => {
        if (retorno.quantidade !== 0) {
          this.listaResposta = retorno.list;
          this.paginacao = this.getPaginacao(paginacao, retorno);
        } else {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  remover(item) {
    const resposta: Resposta = item;
    this.respostaService.desativar(resposta).subscribe((response: Resposta) => {
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.paginacao.pagina = 1;
      this.pesquisarProdutoServicoPaginadoService(this.filtro, this.paginacao);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  hasPermissaoCadastro() {
    return Seguranca.isPermitido([PermissoesEnum.RESPOSTA, PermissoesEnum.RESPOSTA_CADASTRAR]);
  }
}
