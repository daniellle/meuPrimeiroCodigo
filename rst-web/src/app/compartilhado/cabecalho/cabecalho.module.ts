import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CabecalhoComponent} from './cabecalho.component';
import {RouterModule} from '@angular/router';
import {BsDropdownModule} from 'ngx-bootstrap/dropdown';

import {CABECALHO_DIRECTIVES} from '../../diretiva/cabecalho.directive';

@NgModule({
  imports: [RouterModule, CommonModule, BsDropdownModule.forRoot()],
  declarations: [CabecalhoComponent, CABECALHO_DIRECTIVES],
  exports: [CabecalhoComponent],
})
export class CabecalhoModule {

}
