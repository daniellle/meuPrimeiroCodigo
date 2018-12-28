import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { PerfilUsuarioComponent } from './perfil-usuario.component';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { PesquisaPerfilUsuarioComponent } from '../src/app/componente/perfil-usuario/pesquisa-perfil-usuario/pesquisa-perfil-usuario.component';

const routes: Routes = [
  {path: '', component: PerfilUsuarioComponent},
];


@NgModule({
  imports: [
      RouterModule.forChild(routes),
      CompartilhadoModule,
      RouterModule,
  ],
  declarations: [PerfilUsuarioComponent, PesquisaPerfilUsuarioComponent],
  providers: [],
  exports: [PerfilUsuarioComponent]
})
export class PerfilUsuarioModule { }
