import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { UnidadeObraService } from './../../../servico/unidade-obra.service';
import { Component, OnInit} from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { BaseComponent } from 'app/componente/base.component';
import { FormBuilder } from '@angular/forms';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { MascaraUtil } from '../../../compartilhado/utilitario/mascara.util';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { Empresa } from 'app/modelo/empresa.model';
import { EmpresaService } from 'app/servico/empresa.service';
import { FiltroEmpresa } from 'app/modelo/filtro-empresa.model';
import { UnidadeObra } from 'app/modelo/unidade-obra.model';
import { Situacao } from 'app/modelo/enum/enum-situacao.model';

@Component({
  selector: 'app-pesquisa-empresa',
  templateUrl: './pesquisa-empresa.component.html',
  styleUrls: ['./pesquisa-empresa.component.scss'],
})
export class PesquisaEmpresaComponent extends BaseComponent implements OnInit {
  public filtroEmpresa: FiltroEmpresa;
  public empresas: Empresa[];
  public empresasPorPagina: Empresa[];
  public itensCarregados: number;
  public totalItens: number;
  public mascaraCnpj = MascaraUtil.mascaraCnpj;
  public unidadesObras = Array<UnidadeObra>();

  public situacoes = Situacao;
  public keysSituacao: string[];

  constructor(
    private router: Router, private activatedRoute: ActivatedRoute, private service: EmpresaService, private fb: FormBuilder,
    protected bloqueioService: BloqueioService, protected dialogo: ToastyService,
    private dialogService: DialogService, private unidadeObraService: UnidadeObraService) {
    super(bloqueioService, dialogo);
    this.keysSituacao = Object.keys(this.situacoes);
    this.buscarUnidadesObras();
  }

  ngOnInit() {
    this.filtroEmpresa = new FiltroEmpresa();
    this.filtroEmpresa.unidadeObra = undefined;
    this.title = MensagemProperties.app_rst_empresa_title_pesquisar;
  }

  buscarUnidadesObras() {
    this.unidadeObraService.pesquisarTodos().subscribe((dados: any) => {
      this.unidadesObras = dados;
    });
  }

  pesquisar() {
    this.empresas = new Array<Empresa>();
    if (this.validarCampos()) {
      this.removerMascara();
      this.paginacao.pagina = 1;
      this.service.pesquisar(this.filtroEmpresa, this.paginacao).subscribe((retorno: ListaPaginada<Empresa>) => {
        this.empresas = retorno.list;
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
    if (this.filtroEmpresa.cnpj !== undefined) {
      this.filtroEmpresa.cnpj = MascaraUtil.removerMascara(this.filtroEmpresa.cnpj);
    }
  }

  selecionar(model: any) {
    if (model && model.id) {
      this.router.navigate([ model.id], {relativeTo: this.activatedRoute});
    }
  }

  isListaVazia(): boolean {
    return this.empresasPorPagina === undefined || this.empresasPorPagina.length === 0;
  }

  pageChanged(event: any): void {
    this.removerMascara();
    this.paginacao.pagina = event.page;
    this.service.pesquisar(this.filtroEmpresa, this.paginacao).subscribe((retorno: ListaPaginada<Empresa>) => {
      this.empresas = retorno.list;
    });
  }

  validarCampos(): Boolean {
    let verificador: Boolean = true;

    if (this.isVazia(this.filtroEmpresa.cnpj) && this.isVazia(this.filtroEmpresa.nomeFantasia)
      && this.isVazia(this.filtroEmpresa.razaoSocial)
      && (!this.filtroEmpresa.unidadeObra || this.filtroEmpresa.unidadeObra.toString() === 'undefined')) {
      this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
      verificador = false;
    }
    if (!this.isVazia(this.filtroEmpresa.cnpj)) {
      if (MascaraUtil.removerMascara(this.filtroEmpresa.cnpj).length < 14) {
        this.mensagemError(MensagemProperties.app_rst_labels_cnpj_incompleto);
        verificador = false;
      }
      this.filtroEmpresa.cnpj = MascaraUtil.removerMascara(this.filtroEmpresa.cnpj);
    }
    return verificador;
  }

  incluir() {
    this.router.navigate(['cadastrar'], {relativeTo: this.activatedRoute});
  }

  hasPermissaoCadastro() {
    return Seguranca.isPermitido([PermissoesEnum.EMPRESA, PermissoesEnum.EMPRESA_CADASTRAR]);
  }

}
