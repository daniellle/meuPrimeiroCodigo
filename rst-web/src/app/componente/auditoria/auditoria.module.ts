import { PermissoesEnum } from './../../modelo/enum/enum-permissoes';
import { AuditoriaService } from './../../servico/auditoria-service';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { PaginationModule } from 'ngx-bootstrap';
import { AuditoriaComponet } from './auditoria/auditoria.component';
import { AutorizacaoGuard } from 'app/seguranca/autorizacao.guard';

const routes: Routes = [
  {
    path: '', component: AuditoriaComponet,
    canActivate: [AutorizacaoGuard],
    data: {
      title: MensagemProperties.app_rst_produto_servico_title_pesquisar,
      permissoes: [PermissoesEnum.AUDITORIA,
      PermissoesEnum.AUDITORIA_CONSULTAR],
    },
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(routes),
    CompartilhadoModule,
    RouterModule,
    PaginationModule.forRoot(),
  ],
  declarations: [AuditoriaComponet],
  providers: [AuditoriaService],
})
export class AuditoriaModule { }
