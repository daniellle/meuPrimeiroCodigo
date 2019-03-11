import { UatService } from 'app/servico/uat.service';
import { Uat } from 'app/modelo/uat.model';
import { environment } from './../../../../environments/environment';
import { BaseComponent } from './../../../componente/base.component';
import { Component, OnInit } from '@angular/core';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { ActivatedRoute, Router } from '@angular/router';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
  selector: 'app-uat-estrutura-unidade',
  templateUrl: './uat-estrutura-unidade.component.html',
  styleUrls: ['./uat-estrutura-unidade.component.scss'],
})
export class UatEstruturaUnidadeComponent extends BaseComponent implements OnInit {

  idUat: Number;
  uat: Uat;
  edicao: boolean;
  hasPermissaoCadastrarAlterar: any;
  hasPermissaoDesativar: any;

  constructor(
    private router: Router,
    private uatService: UatService,
    private activatedRoute: ActivatedRoute,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.title = this.activatedRoute.snapshot.data.title;
    this.emModoConsulta();
    this.verificaPermissoes()
    this.carregarTela();
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/uat/${this.idUat}`]);
  }

  verificaPermissoes() {
    this.hasPermissaoCadastrarAlterar = Seguranca.isPermitido (
      [PermissoesEnum.CAT_ESTRUTURA_CADASTRAR,
        PermissoesEnum.CAT_ESTRUTURA_ALTERAR]);

    this.hasPermissaoDesativar = Seguranca.isPermitido (
      [PermissoesEnum.CAT_ESTRUTURA_DESATIVAR]);
  }

  private emModoConsulta() {
    this.modoConsulta = !Seguranca.isPermitido (
        [PermissoesEnum.CAT_ESTRUTURA_CADASTRAR,
          PermissoesEnum.CAT_ESTRUTURA_ALTERAR,
          PermissoesEnum.CAT_ESTRUTURA_DESATIVAR]);
  }

  private carregarTela() {
    this.activatedRoute.params.subscribe((params) => {
      this.idUat = params['id'];
      if (this.idUat) {
        this.bucarUatPorId(this.idUat);
      }
    });
  }

  private bucarUatPorId(id: any) {
    this.uatService.pesquisarPorId(id).subscribe((retorno) => { this.uat = retorno; }, (error) => {
      this.mensagemError(error);
    });
  }

}
