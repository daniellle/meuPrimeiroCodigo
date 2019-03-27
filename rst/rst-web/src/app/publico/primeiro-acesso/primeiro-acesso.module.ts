import { SistemaService } from './../../servico/sistema.service';
import { PerfilService } from './../../servico/perfil.service';
import { ParametroService } from './../../servico/parametro.service';
import { TrabalhadorService } from 'app/servico/trabalhador.service';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { PaginationModule } from 'ngx-bootstrap';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { PrimeiroAcessoComponent } from './primeiro-acesso/primeiro-acesso.component';
import { TermoUsoModalComponent } from './termo-uso-modal/termo-uso-modal.component';

const routes: Routes = [
  {
    path: '', component: PrimeiroAcessoComponent,
    data: { title: MensagemProperties.app_rst_primeiro_acesso_title_primeiro_acesso_trabalhador },
  },
];

@NgModule({
  imports: [
    RouterModule.forChild(routes),
    CompartilhadoModule,
    RouterModule,
    PaginationModule.forRoot(),
  ],
  exports: [
    TermoUsoModalComponent,
  ],
  declarations: [PrimeiroAcessoComponent, TermoUsoModalComponent],
  providers: [
    BloqueioService,
    DialogService,
    ToastyService,
    TrabalhadorService,
    ParametroService,
    PerfilService,
    SistemaService,
  ],
})
export class PrimeiroAcessoModule { }
