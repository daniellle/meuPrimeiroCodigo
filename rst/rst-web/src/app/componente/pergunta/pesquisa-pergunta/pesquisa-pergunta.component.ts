import { environment } from './../../../../environments/environment';
import { PerguntaService } from './../../../servico/pergunta.service';
import { Pergunta } from './../../../modelo/pergunta.model';
import { PerguntaFilter } from './../../../modelo/filtro-pergunta';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastyService } from 'ng2-toasty';
import { BaseComponent } from 'app/componente/base.component';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { Paginacao } from 'app/modelo/paginacao.model';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
  selector: 'app-pesquisa-pergunta',
  templateUrl: './pesquisa-pergunta.component.html',
  styleUrls: ['./pesquisa-pergunta.component.scss'],
})

export class PesquisaPerguntaComponent extends BaseComponent implements OnInit {

  filtro: PerguntaFilter;
  listaPerguntas: Pergunta[];
  perguntaSelecionada: Pergunta;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private PerguntaService: PerguntaService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.filtro = new PerguntaFilter();
    this.listaPerguntas = new Array<Pergunta>();
    this.title = this.activatedRoute.snapshot.data.title;
    this.perguntaSelecionada = new Pergunta();
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
    this.listaPerguntas = new Array<Pergunta>();
    this.paginacao.pagina = 1;
    this.pesquisarPerguntaPaginadoService(this.filtro, this.paginacao);
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarPerguntaPaginadoService(this.filtro, this.paginacao);
  }

  private pesquisarPerguntaPaginadoService(filtro: PerguntaFilter, paginacao: Paginacao): void {
    this.PerguntaService.pesquisarPaginado(filtro, paginacao).
      subscribe((retorno: ListaPaginada<Pergunta>) => {
        if (retorno.quantidade !== 0) {
          this.listaPerguntas = retorno.list;
          this.paginacao = this.getPaginacao(paginacao, retorno);
        } else {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  remover(item) {
    const pergunta: Pergunta = item;
    this.PerguntaService.remover(pergunta).subscribe((retorno) => {
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      this.paginacao.pagina = 1;
      this.pesquisarPerguntaPaginadoService(this.filtro, this.paginacao);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  hasPermissaoCadastro() {
    return Seguranca.isPermitido([PermissoesEnum.PERGUNTA,
    PermissoesEnum.PERGUNTA_CADASTRAR]);
  }
}
