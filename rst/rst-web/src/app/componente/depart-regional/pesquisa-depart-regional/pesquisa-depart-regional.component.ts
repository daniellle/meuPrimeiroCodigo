import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { Component, OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { Router, ActivatedRoute } from '@angular/router';
import { DepartRegionalService } from 'app/servico/depart-regional.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { FormBuilder } from '@angular/forms';
import { DialogService } from 'ng2-bootstrap-modal';
import { FiltroDepartRegional } from 'app/modelo/filtro-depart-regional.model';
import { DepartamentoRegional } from 'app/modelo/departamento-regional.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { Situacao } from '../../../modelo/enum/enum-situacao.model';

@Component({
  selector: 'app-pesquisa-depart-regional',
  templateUrl: './pesquisa-depart-regional.component.html',
  styleUrls: ['./pesquisa-depart-regional.component.scss'],
})
export class PesquisaDepartRegionalComponent extends BaseComponent implements OnInit {


  public filtroDepartRegional: FiltroDepartRegional;
  public estados: any[];
  public departamentoSelecionado: DepartamentoRegional;
  public departsRegionais: DepartamentoRegional[];

  public situacoes = Situacao;
  public keysSituacao: string[];

  constructor(private router: Router, private service: DepartRegionalService, private fb: FormBuilder,
    protected bloqueioService: BloqueioService, protected dialogo: ToastyService, private dialogService: DialogService,
    private activatedRoute: ActivatedRoute) {
    super(bloqueioService, dialogo);
    this.buscarEstados();
    this.keysSituacao = Object.keys(this.situacoes);
  }

  ngOnInit() {
    this.title = MensagemProperties.app_rst_depart_regional_pesquisar;
    this.filtroDepartRegional = new FiltroDepartRegional();
    this.filtroDepartRegional.situacao = '';
    this.modoConsulta = !Seguranca.isPermitido(['departamento_regional_cadastrar', 'departamento_regional_alterar',
      'departamento_regional_desativar']);
  }

  buscarEstados() {
    if (!this.modoConsulta) {
      this.service.buscarEstados().subscribe((dados: any) => {
        this.estados = dados;
      });
    }
  }

  selecionar(model: any) {
    if (model && model.id) {
      this.router.navigate([model.id], { relativeTo: this.activatedRoute });
    }
  }

  desativar(): void {
    if (this.departamentoSelecionado && this.departamentoSelecionado.id) {
      this.mensagemAtencao(MensagemProperties.app_rst_operacao_nao_realizada);
    } else {
      this.mensagemError(MensagemProperties.app_rst_selecione_um_item);
    }
  }

  getEndereco(departamentoRegional: DepartamentoRegional) {
    if (departamentoRegional.listaEndDepRegional && departamentoRegional.listaEndDepRegional.length > 0) {
      return departamentoRegional.listaEndDepRegional[0].endereco.municipio.descricao;
    }
  }

  pesquisar() {
    this.departsRegionais = new Array<DepartamentoRegional>();
    this.departamentoSelecionado = null;
    this.removerMascara();
    if (this.validarCampos()) {
      const estadoSelecinado = this.filtroDepartRegional.idEstado;
      if (!this.filtroDepartRegional.idEstado || this.filtroDepartRegional.idEstado === 'undefined') {
        this.filtroDepartRegional.idEstado = '0';
      }
      this.paginacao.pagina = 1;
      this.service.pesquisar(this.filtroDepartRegional, this.paginacao).subscribe((retorno: ListaPaginada<DepartamentoRegional>) => {
        this.departsRegionais = retorno.list;
        this.getPaginacao(this.paginacao, retorno);
        this.filtroDepartRegional.idEstado = estadoSelecinado;
        if (retorno.quantidade === 0) {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (erro) => {
        this.mensagemError(erro.error);
      });
    }
  }

  validarCampos(): Boolean {
    let verificador: Boolean = true;

    if (this.isVazia(this.filtroDepartRegional.cnpj)
      && this.isVazia(this.filtroDepartRegional.razaoSocial)
      && (!this.filtroDepartRegional.idEstado || this.filtroDepartRegional.idEstado.toString() === 'undefined')) {
      this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
      verificador = false;
    }

    if (!this.isVazia(this.filtroDepartRegional.cnpj)) {

      if (this.filtroDepartRegional.cnpj.length < 14) {
        this.mensagemError(MensagemProperties.app_rst_labels_cnpj_incompleto);
        verificador = false;
      }
    }
    return verificador;
  }

  removerMascara() {
    if (this.filtroDepartRegional.cnpj) {
      this.filtroDepartRegional.cnpj = MascaraUtil.removerMascara(this.filtroDepartRegional.cnpj);
    }
  }

  public pageChanged(event: any): void {
    this.removerMascara();
    this.paginacao.pagina = event.page;
    this.service.pesquisar(this.filtroDepartRegional, this.paginacao)
      .subscribe((retorno: ListaPaginada<DepartamentoRegional>) => {
        this.departsRegionais = retorno.list;
      });
  }
  incluir() {
    this.router.navigate(['cadastrar'], { relativeTo: this.activatedRoute });
  }

  hasPermissaoCadastro() {
    return Seguranca.isPermitido([PermissoesEnum.DEPARTAMENTO_REGIONAL,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_CADASTRAR]);
  }
}
