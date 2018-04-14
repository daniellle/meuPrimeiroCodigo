import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {MenuComponent} from './menu.component';
import {CompartilhadoModule} from '../compartilhado.module';
import {MENU_DIRECTIVES} from '../../diretiva/menu.directive';


@NgModule({
  imports: [RouterModule, CommonModule, CompartilhadoModule],
  declarations: [MenuComponent, MENU_DIRECTIVES],
  exports: [MenuComponent, MENU_DIRECTIVES],
})
export class MenuModule {
}
