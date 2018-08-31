import { DialogService } from 'ng2-bootstrap-modal';
import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CompartilhadoModule} from '../../compartilhado/compartilhado.module';
import {AutenticacaoService} from '../../servico/autenticacao.service';
import {FORMULARIO_DIRECTIVES} from '../../diretiva/formulario.directive';
import {AutenticacaoComponent} from './autenticacao.component';

const routes: Routes = [
  {
    path: 'login',
    component: AutenticacaoComponent,
    data: {title: 'Autenticação'},
  },
];

@NgModule({
  exports: [AutenticacaoComponent],
  providers: [AutenticacaoService, DialogService],
  declarations: [AutenticacaoComponent, FORMULARIO_DIRECTIVES],
  imports: [FormsModule, ReactiveFormsModule, CompartilhadoModule, RouterModule.forChild(routes)],
})
export class AutenticacaoModule {

}
