import {Seguranca} from 'app/compartilhado/utilitario/seguranca.model';
import {PermissoesEnum} from 'app/modelo/enum/enum-permissoes';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder} from '@angular/forms';

import {ToastyService} from 'ng2-toasty';
import {DialogService} from 'ng2-bootstrap-modal';
import {BaseComponent} from 'app/componente/base.component';

import {BloqueioService} from 'app/servico/bloqueio.service';
import {MensagemProperties} from 'app/compartilhado/utilitario/recurso.pipe';
import {FiltroTrabalhador} from './../../../modelo/filtro-trabalhador.model';
import {TrabalhadorService} from './../../../servico/trabalhador.service';
import {ListaPaginada} from './../../../modelo/lista-paginada.model';
import {Trabalhador} from './../../../modelo/trabalhador.model';
import {Situacao} from 'app/modelo/enum/enum-situacao.model';

import {Estado} from './../../../modelo/estado.model';
import {EstadoService} from 'app/servico/estado.service';
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-pesquisa-trabalhador',
  templateUrl: './pesquisa-trabalhador.component.html',
  styleUrls: ['./pesquisa-trabalhador.component.scss'],
})
export class PesquisaTrabalhadorComponent extends BaseComponent implements OnInit {

  public filtro: FiltroTrabalhador;
  public trabalhadores: Trabalhador[];
  public trabalhadorSelecionado: Trabalhador;
  public situacoes = Situacao;
  public keysSituacao: string[];
  public estados: Estado[];
  public estadoSesiSelecionado: number;

  constructor(
    private router: Router,
    private service: TrabalhadorService,
    private formBuilder: FormBuilder,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private dialogService: DialogService,
    private activatedRoute: ActivatedRoute,
    protected estadoService: EstadoService
  ) {
    super(bloqueioService, dialogo);
    this.keysSituacao = Object.keys(this.situacoes);
    this.buscarEstados();
  }

  ngOnInit() {
    this.filtro = new FiltroTrabalhador();
    this.trabalhadores = new Array<Trabalhador>();
    this.title = MensagemProperties.app_rst_trabalhador_title_pesquisar;
  }

  public pesquisar() {
    if (this.verificarCampos()) {
      this.trabalhadores = new Array<Trabalhador>();
      this.trabalhadorSelecionado = null;
      this.paginacao.pagina = 1;
      this.service.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Trabalhador>) => {
        this.trabalhadores = retorno.list;
        this.paginacao = this.getPaginacao(this.paginacao, retorno);
        if (retorno.quantidade === 0) {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  public verificarCampos() {
    let verificador: Boolean = true;

    if (!this.isVazia(this.filtro.cpf)) {
      if (this.filtro.cpf.length < 14) {
        this.mensagemError(MensagemProperties.app_rst_labels_cpf_incompleto);
        verificador = false;
      }
    }

    if (!this.isVazia(this.filtro.nit)) {
      if (this.filtro.nit.length < 14) {
        this.mensagemError(MensagemProperties.app_rst_labels_nit_incompleto);
        verificador = false;
      }
    }

    return verificador;
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.service.pesquisarPaginado(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Trabalhador>) => {
      this.trabalhadores = retorno.list;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  selecionar(model: any) {
    if (model && model.id) {
      this.router.navigate([model.id], { relativeTo: this.activatedRoute });
    }
  }

  novo(): void {
    this.router.navigate(['cadastrar'], { relativeTo: this.activatedRoute });
  }

  getSituacaoTrabalhador(dataExclusao: any, dataFalecimento: any) {
    if (!(dataExclusao === undefined || dataFalecimento === undefined) || dataExclusao || dataFalecimento) {
      return Situacao.I;
    } else {
      return Situacao.A;
    }
  }

  hasPermissaoCadastrar() {
    return Seguranca.isPermitido([PermissoesEnum.TRABALHADOR, PermissoesEnum.TRABALHADOR_CADASTRAR]);
  }
  buscarEstados() {
    this.estadoService.buscarEstados().subscribe((dados: Estado[]) => {
      this.estados = dados;
      this.estadoSesiSelecionado = 0;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  isProducao(): Boolean {
     return environment.isProduction;
  }
}
