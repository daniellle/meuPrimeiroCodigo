import { ToastyModule, ToastyService } from 'ng2-toasty';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {TemplateComponent} from './template.component';
import {RodapeModule} from '../rodape/rodape.module';
import {CabecalhoModule} from '../cabecalho/cabecalho.module';
import {MenuModule} from '../menu/menu.module';
import {RastroModule} from '../rastro/rastro.module';
import {BloqueioModule} from '../bloqueio/bloqueio.module';
import {DashboardService} from '../../servico/dashboard.service';

@NgModule({
  declarations: [TemplateComponent],
  exports: [TemplateComponent, RastroModule,
    BloqueioModule, ToastyModule],
  imports: [
    RouterModule,
    CommonModule,
    CabecalhoModule,
    MenuModule,
    RodapeModule,
    RastroModule,
    BloqueioModule,
    ToastyModule,
  ],
  providers: [DashboardService, ToastyService],
})
export class TemplateModule {
}
