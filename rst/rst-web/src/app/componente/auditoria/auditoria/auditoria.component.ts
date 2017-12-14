import { CompareDataBefore } from 'app/compartilhado/validators/data.validator';
import { AuditoriaService } from './../../../servico/auditoria-service';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { Paginacao } from 'app/modelo/paginacao.model';
import { AuditoriaFilter } from './../../../modelo/filtro-auditoria';
import { AuditoriaModel } from './../../../modelo/auditoria';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastyService } from 'ng2-toasty';
import { BaseComponent } from 'app/componente/base.component';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { Funcionalidade } from 'app/modelo/enum/enum-funcionalidade';
import { TipoOperacaoAuditoria } from 'app/modelo/enum/enum-tipo-operacao-auditoria';

@Component({
  selector: 'app-auditoria',
  templateUrl: './auditoria.component.html',
  styleUrls: ['./auditoria.component.scss'],
})
export class AuditoriaComponet extends BaseComponent implements OnInit {

  public funcionalidade = Funcionalidade;
  public keyFuncionalide: string[];
  public dataInicial: any;
  public dataFinal: any;
  public tipoOperacao = TipoOperacaoAuditoria;
  public keyTipoOperacao: string[];
  public filtro: AuditoriaFilter;
  public listaAuditoria: AuditoriaModel[];
  public temAuditoria = false;
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    protected auditoriaService: AuditoriaService,
  ) {
    super(bloqueioService, dialogo);
    this.keyFuncionalide = Object.keys(this.funcionalidade);
    this.keyTipoOperacao = Object.keys(this.tipoOperacao);
    this.filtro = new AuditoriaFilter();
    this.filtro.funcionalidade = 'TDS';
    this.filtro.tipoOperacaoAuditoria = 'TDS';
    this.title = MensagemProperties.app_rst_labels_auditoria;
  }

  ngOnInit() {
  }

  validarPesquisa(): boolean {
    if (!this.dataInicial || !this.dataFinal) {
      this.mensagemError(MensagemProperties.app_rst_msg_validacao_data);
      return false;
    }

    if (this.convertDateToString(this.dataInicial.date) !== this.convertDateToString(this.dataFinal.date)
      && !CompareDataBefore(this.dataInicial.jsdate, this.dataFinal.jsdate)) {
      this.mensagemError(MensagemProperties.app_rst_auditoria_data_inicio_maior_que_fim);
      return false;
    }

    return true;
  }

  pesquisar(): void {
    this.listaAuditoria = new Array<AuditoriaModel>();
    this.paginacao.pagina = 1;
    this.temAuditoria = false;
    if (this.validarPesquisa()) {
      this.pesquisarAuditoria(this.filtro, this.paginacao);
    }
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarAuditoria(this.filtro, this.paginacao);
  }

  converterDatas() {
    if (this.dataInicial) {
      this.filtro.dataInicial = this.convertDateToString(this.dataInicial.date);
    }
    if (this.dataFinal) {
      this.filtro.dataFinal = this.convertDateToString(this.dataFinal.date);
    }

  }
public getFuncionalidade(item: Funcionalidade): string {
  return Funcionalidade[item];
}

public getOperacao(item: TipoOperacaoAuditoria): string {
  return TipoOperacaoAuditoria[item];
}

public getNavegadorMovel(descricao: string): string {
  if (descricao.toLowerCase(). search('mobile') === -1) {
    return 'Não';
  } else {
    return 'Sim';
  }
}
  private pesquisarAuditoria(filtro: AuditoriaFilter, paginacao: Paginacao): void {
    this.converterDatas();
    this.auditoriaService.pesquisarPaginado(filtro, paginacao).
      subscribe((retorno: ListaPaginada<AuditoriaModel>) => {
        if (retorno.quantidade !== 0) {
          this.listaAuditoria = retorno.list;
          this.paginacao = this.getPaginacao(paginacao, retorno);
          this.temAuditoria = true;
        } else {
          this.temAuditoria = false;
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.temAuditoria = false;
        this.mensagemError(error);
      });
  }

}
