import { ToastyModule, ToastyService } from 'ng2-toasty';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DashboardComponent} from './dashboard.component';
import {RouterModule, Routes} from '@angular/router';
import {ChartsModule} from 'ng2-charts/ng2-charts';
import {BsDropdownModule} from 'ngx-bootstrap/dropdown';
import {BloqueioModule} from '../../compartilhado/bloqueio/bloqueio.module';
import {DashboardService} from '../../servico/dashboard.service';

const routes: Routes = [
  {path: '', component: DashboardComponent, data: {title: 'Dashboard'}},
];

@NgModule({
  exports: [DashboardComponent],
  declarations: [DashboardComponent],
  providers: [DashboardService],
  imports: [
    BloqueioModule,
    BsDropdownModule,
    ChartsModule,
    CommonModule,
    RouterModule.forChild(routes),
  ],
})
export class DashboardModule {
}
