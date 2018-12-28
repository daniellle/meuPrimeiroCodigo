import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { PerfilUsuarioComponent } from './perfil-usuario.component';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';

const routes: Routes = [
  {path: '', component: PerfilUsuarioComponent},
];


@NgModule({
  imports: [
      RouterModule.forChild(routes),
      CompartilhadoModule,
      RouterModule,
  ],
  declarations: [PerfilUsuarioComponent],
  providers: [],
  exports: [PerfilUsuarioComponent]
})
export class PerfilUsuarioModule { }
