import { SegmentoService } from './../../../servico/segmento.service';
import { FiltroRedeCredenciada } from './../../../modelo/filtro-rede-credenciada';
import { RedeCredenciada } from './../../../modelo/rede-credenciada.model';
import { RedeCredenciadaService } from './../../../servico/rede-credenciada.service';
import { Segmento } from './../../../modelo/segmento.model';
import { Component, OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { Paginacao } from 'app/modelo/paginacao.model';
import { Router, ActivatedRoute } from '@angular/router';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { FormBuilder } from '@angular/forms';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { MascaraUtil } from '../../../compartilhado/utilitario/mascara.util';
import { ListaPaginada } from '../../../modelo/lista-paginada.model';
import { Situacao } from 'app/modelo/enum/enum-situacao.model';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
  selector: 'app-pesquisa-rede-credenciada',
  templateUrl: './pesquisa-rede-credenciada.component.html',
  styleUrls: ['./pesquisa-rede-credenciada.component.scss'],
})
export class RedeCredenciadaPesquisarComponent extends BaseComponent implements OnInit {
  public situacoes = Situacao;
  public filtro: FiltroRedeCredenciada;
  public segmento: Segmento[];
  public situacao: Situacao;
  public paginacao: Paginacao = new Paginacao(1, 10);
  public mascaraCnpj = MascaraUtil.mascaraCnpj;
  public redeCredenciada: RedeCredenciada[];
  public totalItens: number;
  public itensCarregados: number;
  public redeCredenciadaPorPagina: RedeCredenciada[];
  public keysSituacao: string[];
  public idSelecionada;

  constructor(private router: Router, private fsudob: FormBuilder, private activatedRoute: ActivatedRoute,
              protected bloqueioService: BloqueioService, private service: RedeCredenciadaService,
              protected dialogo: ToastyService, private dialogService: DialogService, private serviceSegmento: SegmentoService) {
    super(bloqueioService, dialogo);
    serviceSegmento.pesquisarSegmentos().subscribe((segmentos) => {
      this.segmento = segmentos;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  ngOnInit() {
    this.filtro = new FiltroRedeCredenciada();
    this.keysSituacao = Object.keys(this.situacoes);
  }

  pesquisar() {
    if (this.validarCampos()) {
      this.paginacao.pagina = 1;
      this.removerMascara();
      this.service.pesquisarPorFiltro(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<RedeCredenciada>) => {
        this.redeCredenciada = retorno.list;
        this.paginacao = this.getPaginacao(this.paginacao, retorno);
        if (retorno.quantidade === 0) {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  removerMascara() {
    if (this.filtro.cnpj !== undefined) {
      this.filtro.cnpj = MascaraUtil.removerMascara(this.filtro.cnpj);
    }
  }

  formatarCampos(rede: RedeCredenciada[]): void {
    rede.forEach((redeCredenciada) => {
      typeof redeCredenciada.dataDesligamento === 'undefined' ? redeCredenciada.situacao = Situacao.A :
        redeCredenciada.situacao = Situacao.I;
    });
  }

  validarCampos() {
    let verificador: Boolean = true;

    if (this.isVazia(this.filtro.cnpj) && this.isVazia(this.filtro.razaoSocial)
      && !this.filtro.segmento) {
      this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
      verificador = false;
    }

    if (!this.isVazia(this.filtro.cnpj)) {
      if (MascaraUtil.removerMascara(this.filtro.cnpj).length < 14) {
        this.mensagemError(MensagemProperties.app_rst_labels_cnpj_incompleto);
        verificador = false;
      }
    }
    return verificador;

  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.service.pesquisarPorFiltro(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<RedeCredenciada>) => {
      this.redeCredenciada = retorno.list;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  incluir() {
    this.router.navigate(['cadastrar'], {relativeTo: this.activatedRoute});
  }

  selecionar(model: any) {
    if (model && model.id) {
      this.router.navigate([model.id], { relativeTo: this.activatedRoute });
    }
  }

  isListaVazia() {
    return !this.isNotEmpty(this.redeCredenciada);
  }

  orderByRazaoSocial(list: any) {
    list.sort((left, right): number => {
      if (left.razaoSocial > right.razaoSocial) {
        return 1;
      }
      if (left.razaoSocial < right.razaoSocial) {
        return -1;
      }
      return 0;
    });
  }

  hasPermissaoCadastrar() {
    return Seguranca.isPermitido([PermissoesEnum.REDE_CREDENCIADA,
      PermissoesEnum.REDE_CREDENCIADA_CADASTRAR]);
  }
}
