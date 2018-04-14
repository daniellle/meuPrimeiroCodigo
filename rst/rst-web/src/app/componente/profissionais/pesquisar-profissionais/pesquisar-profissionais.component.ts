import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { Component, OnInit, Input } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { Router, ActivatedRoute } from '@angular/router';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { FormBuilder } from '@angular/forms';
import { FiltroProfissionais } from '../../../modelo/filtro-profissionais.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { MascaraUtil } from '../../../compartilhado/utilitario/mascara.util';
import { Profissional } from '../../../modelo/profissional.model';
import { ProfissionalService } from '../../../servico/profissional.service';
import { ListaPaginada } from '../../../modelo/lista-paginada.model';
import { Situacao } from 'app/modelo/enum/enum-situacao.model';
import { TipoProfissional } from 'app/modelo/enum/enum-tipo-profissional';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
  selector: 'app-profissionais-pesquisar',
  templateUrl: './pesquisar-profissionais.component.html',
  styleUrls: ['./pesquisar-profissionais.component.scss'],
})
export class PesquisarProfissionaisComponent extends BaseComponent implements OnInit {

  @Input() public filtro: FiltroProfissionais;
  public profissionais: Profissional[];

  public situacoes = Situacao;
  public keysSituacao: string[];
  public tipoProfissional = TipoProfissional;
  public keysTipoProfissional: string[];
  teste = new Map<number, number>();
  constructor(private router: Router, private fb: FormBuilder,
              protected bloqueioService: BloqueioService,
              private service: ProfissionalService,
              protected dialogo: ToastyService,
              private dialogService: DialogService,
              private activatedRoute: ActivatedRoute) {
    super(bloqueioService, dialogo);
    this.keysSituacao = Object.keys(this.situacoes);
    this.keysTipoProfissional = Object.keys(this.tipoProfissional);
  }

  ngOnInit() {
    this.filtro = new FiltroProfissionais();
    this.modoConsulta = !Seguranca.isPermitido([PermissoesEnum.PROFISSIONAL,
    PermissoesEnum.PROFISSIONAL_CADASTRAR,
    PermissoesEnum.PROFISSIONAL_ALTERAR,
    PermissoesEnum.PROFISSIONAL_DESATIVAR]);
  }

  pesquisar() {
    if (this.validarCampos()) {
      this.paginacao.pagina = 1;
      this.removerMascara();
      this.service.pesquisar(this.filtro, this.paginacao)
        .subscribe((retorno: ListaPaginada<Profissional>) => {
          this.profissionais = retorno.list;
          this.paginacao = this.getPaginacao(this.paginacao, retorno);
          if (retorno.quantidade === 0) {
            this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
          }
        }, (error) => {
          this.mensagemError(error);
        });
    }
  }

  validarCampos(): Boolean {
    let verificador: Boolean = true;

    if (this.isVazia(this.filtro.cpf) && this.isVazia(this.filtro.nome) && this.isVazia(this.filtro.registro)) {
      this.mensagemError(MensagemProperties.app_rst_msg_pesquisar_todos_vazios);
      verificador = false;
    }
    return verificador;
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.service.pesquisar(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<Profissional>) => {
      this.profissionais = retorno.list;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  incluir() {
    this.router.navigate(['cadastrar'], { relativeTo: this.activatedRoute });
  }

  selecionar(model: any) {
    if (model && model.id) {
      this.router.navigate([model.id], { relativeTo: this.activatedRoute });
    }
  }

  removerMascara() {
    if (this.filtro.cpf !== undefined) {
      this.filtro.cpf = MascaraUtil.removerMascara(this.filtro.cpf);
    }
  }

  hasPermissaoCadastrar() {
    return this.hasPermissao(PermissoesEnum.PROFISSIONAL)
      ||  this.hasPermissao(PermissoesEnum.PROFISSIONAL_CADASTRAR);
  }
}
