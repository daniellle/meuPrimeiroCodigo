import { LinhaService } from './../../../servico/linha.service';
import { UatProdutoServicoService } from './../../../servico/uat-produto-servico.service';
import { UatService } from 'app/servico/uat.service';
import { ProdutoServico } from './../../../modelo/produto-servico.model';
import { UatProdutoServico } from './../../../modelo/uat-produto-servico.model';
import { Uat } from 'app/modelo/uat.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { IHash } from './../../../compartilhado/modal-uat-component/uat-modal/uat-modal.component';
import { Linha } from './../../../modelo/linha.model';
import { MensagemProperties } from './../../../compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from './../../../modelo/lista-paginada.model';
import { BaseComponent } from './../../../componente/base.component';
import { Component, OnInit } from '@angular/core';
import { ProdutoServicoFilter } from './../../../modelo/filtro-produto-servico';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { ActivatedRoute, Router } from '@angular/router';
import { Paginacao } from './../../../modelo/paginacao.model';

@Component({
  selector: 'app-uat-estrutura-unidade',
  templateUrl: './uat-estrutura-unidade.component.html',
  styleUrls: ['./uat-estrutura-unidade.component.scss'],
})
export class UatEstruturaUnidadeComponent extends BaseComponent implements OnInit {

  idUat: number;
  uat: Uat;
  edicao: boolean;

  constructor(
    private router: Router,
    private uatService: UatService,
    private activatedRoute: ActivatedRoute,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService) {
    super(bloqueioService, dialogo);
    this.carregarTela();
  }

  ngOnInit() {
    this.title = this.activatedRoute.snapshot.data.title;
  }


  private carregarTela() {
    this.activatedRoute.params.subscribe((params) => {
      this.idUat = params['id'];
      if (this.idUat) {
        this.bucarUatPorId(this.idUat);
      }
    });
  }

  bucarUatPorId(id: any) {
    this.uatService.pesquisarPorId(id).subscribe((retorno) => { this.uat = retorno; }, (error) => {
      this.mensagemError(error);
    });
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/uat/${this.idUat}`]);
  }

}
