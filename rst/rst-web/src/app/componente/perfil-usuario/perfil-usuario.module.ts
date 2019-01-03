import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { PerfilUsuarioComponent } from './perfil-usuario.component';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { PesquisaPerfilUsuarioComponent } from './pesquisa-perfil-usuario/pesquisa-perfil-usuario.component';
import { PaginationModule } from 'ngx-bootstrap';
import { ModalEmpresaComponentModule } from 'app/modal/modal-empresa-component/modal-empresa-component.module';
import { ModalDepartamentoComponentModule } from 'app/modal/modal-departamento-component/modal-departamento-component.module';
import { UsuarioService } from 'app/servico/usuario.service';
import { PerfilService } from 'app/servico/perfil.service';
import { PesquisaSesiService } from 'app/servico/pesquisa-sesi.service';
import { PaginadoPerfilUsuarioComponent } from './paginado-perfil-usuario/paginado-perfil-usuario.component';

const routes: Routes = [
  {path: '', component: PerfilUsuarioComponent},
];

@NgModule({
  imports: [
      RouterModule.forChild(routes),
      CompartilhadoModule,
      RouterModule,
      RouterModule,
      PaginationModule.forRoot(),
      ModalEmpresaComponentModule,
      ModalDepartamentoComponentModule,
  ],
  declarations: [PerfilUsuarioComponent, PesquisaPerfilUsuarioComponent, PaginadoPerfilUsuarioComponent],
  providers: [UsuarioService, PerfilService, PesquisaSesiService],
  exports: [PerfilUsuarioComponent]
})
export class PerfilUsuarioModule { }
