import { UatService } from 'app/servico/uat.service';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { ProdutoServicoService } from './../../servico/produto-servico.service';
import { LinhaService } from './../../servico/linha.service';
import { PesquisaSesiService } from './../../servico/pesquisa-sesi.service';
import { EstadoService } from './../../servico/estado.service';
import { PaginationModule } from 'ngx-bootstrap';
import { CompartilhadoModule } from './../../compartilhado/compartilhado.module';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { AutorizacaoGuard } from './../../seguranca/autorizacao.guard';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PesquisaSesiComponent } from './pesquisa-sesi/pesquisa-sesi.component';

const routes: Routes = [
  {
    path: '', component: PesquisaSesiComponent,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_pesquisa_sesi_title,
      permissoes: [PermissoesEnum.PESQUISA_SESI_CONSULTAR],
    },
  },
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    CompartilhadoModule,
    RouterModule,
    PaginationModule.forRoot(),
  ],
  declarations: [PesquisaSesiComponent],
  providers: [EstadoService, PesquisaSesiService, LinhaService, ProdutoServicoService, UatService],
})
export class PesquisaSesiModule { }
