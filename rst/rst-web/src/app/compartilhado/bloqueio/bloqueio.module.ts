import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {BloqueioComponent} from './bloqueio.component';
import {BloqueioService} from '../../servico/bloqueio.service';

@NgModule({
  imports: [CommonModule],
  declarations: [BloqueioComponent],
  providers: [BloqueioService],
  exports: [BloqueioComponent],
})
export class BloqueioModule {

}
