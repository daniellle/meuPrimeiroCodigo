import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { environment } from './../../../../../environments/environment';
import { Empresa } from './../../../../modelo/empresa.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { Paginacao } from 'app/modelo/paginacao.model';
import { FiltroEmpresaTrabalhador } from './../../../../modelo/filtro-empresa-trabalhador.model';
import { TrabalhadorService } from './../../../../servico/trabalhador.service';
import { BloqueioService } from './../../../../servico/bloqueio.service';
import { EmpresaService } from 'app/servico/empresa.service';
import { ListaPaginada } from './../../../../modelo/lista-paginada.model';
import { EmpresaTrabalhadorService } from './../../../../servico/empresa-trabalhador.service';
import { EmpresaTrabalhador } from 'app/modelo/empresa-trabalhador.model';
import { Router, ActivatedRoute } from '@angular/router';
import { OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { Component } from '@angular/core';
import { ToastyService } from 'ng2-toasty';

@Component({
  selector: 'app-pesquisa-empresa-trabalhador',
  templateUrl: './pesquisa-empresa-trabalhador.component.html',
  styleUrls: ['./pesquisa-empresa-trabalhador.component.scss'],
})
export class PesquisaEmpresaTrabalhadorComponent extends BaseComponent implements OnInit {

  idEmpresa: number;
  filtro: FiltroEmpresaTrabalhador;
  filtroSelecionado: FiltroEmpresaTrabalhador;
  listaEmpresaTrabalhador: EmpresaTrabalhador[];
  empresa: Empresa;

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
    this.listaEmpresaTrabalhador = new Array<EmpresaTrabalhador>();
    this.filtro = new FiltroEmpresaTrabalhador();
    this.filtroSelecionado = new FiltroEmpresaTrabalhador();
    this.empresa = new Empresa();
    this.setEmpresa();
    this.carregarTabelaEmppresaTrabalhador();
  }

  pesquisar(): void {
    if (this.verificarCampos()) {
      this.paginacao.pagina = 1;
      this.filtroSelecionado = new FiltroEmpresaTrabalhador(this.filtro);
      this.pesquisarEmpresaTrabalhadorPaginado(this.idEmpresa, this.filtroSelecionado, this.paginacao);
    }
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarEmpresaTrabalhadorPaginado(this.idEmpresa, this.filtroSelecionado, this.paginacao);
  }

  selecionar(model: any) {
    if ((this.hasPermissao(PermissoesEnum.EMPRESA_TRABALHADOR_ALTERAR)
      || this.hasPermissao(PermissoesEnum.EMPRESA_TRABALHADOR_CONSULTAR))
      || this.hasPermissao(PermissoesEnum.EMPRESA_TRABALHADOR) &&
      model && model.id) {
      this.router.navigate([`associar/${model.id}`], { relativeTo: this.activatedRoute },
      );
    }
  }

  voltar(): void {
    if (this.activatedRoute.snapshot.url[0].path === 'minhaempresa') {
      this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.idEmpresa}`]);
    }
  }

  novo() {
    this.router.navigate(['associar'], { relativeTo: this.activatedRoute });
  }

  hasPermissaoCadastrar() {
    return this.hasPermissao(PermissoesEnum.EMPRESA_TRABALHADOR_CADASTRAR)
      || this.hasPermissao(PermissoesEnum.EMPRESA_TRABALHADOR);
  }

  hasPermissaoAlterar() {
    return this.hasPermissao(PermissoesEnum.EMPRESA_TRABALHADOR_ALTERAR)
      || this.hasPermissao(PermissoesEnum.EMPRESA_TRABALHADOR);
  }

  private setEmpresa() {
    this.idEmpresa = this.activatedRoute.snapshot.params['id'];
    this.empresaService.pesquisarPorId(this.idEmpresa).subscribe((retorno: Empresa) => {
      this.empresa = retorno;
    }, (error) => { this.mensagemErroComParametros(MensagemProperties.app_rst_erro_geral); });

  }

  private carregarTabelaEmppresaTrabalhador() {
    this.paginacao.pagina = 1;
    this.pesquisarEmpresaTrabalhadorPaginado(this.idEmpresa, this.filtro, this.paginacao, false);
  }

  private pesquisarEmpresaTrabalhadorPaginado(
    idEmpresa: number, filtro: FiltroEmpresaTrabalhador, paginacao: Paginacao, isPesquisa?: boolean): void {
    this.empresaTrabalhadorService.pesquisarPaginado(idEmpresa, filtro, paginacao).
      subscribe((retorno: ListaPaginada<EmpresaTrabalhador>) => {
        this.listaEmpresaTrabalhador = retorno.list;
        this.paginacao = this.getPaginacao(this.paginacao, retorno);
        if (retorno.quantidade === 0 && isPesquisa) {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  private verificarCampos() {
    let verificador: Boolean = true;

    if (!this.isVazia(this.filtro.cpf)) {
      if (this.filtro.cpf.length < 14) {
        this.mensagemError(MensagemProperties.app_rst_labels_cpf_incompleto);
        verificador = false;
      }
    }
    return verificador;
  }
}
