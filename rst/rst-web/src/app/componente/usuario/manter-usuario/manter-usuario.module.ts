import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';

import { ManterUsuarioComponent } from './manter-usuario.component';
import { DadosGeraisComponent } from './dados-gerais/dados-gerais.component';
import { CompartilhadoModule } from '../../../compartilhado/compartilhado.module';
import { AssociaPerfilComponent } from './associa-perfil/associa-perfil.component';
import { PerfisAssociadosComponent } from './perfis-associados/perfis-associados.component';
import { PaginationModule } from 'ngx-bootstrap';
import { ConcatenaPerfisSistemasCadastroPipe } from './concatena-perfis-sistemas-cadastro.pipe';
import { RecursoPipe } from 'app/compartilhado/utilitario/recurso.pipe';

@NgModule({
  imports: [
    ReactiveFormsModule,
    CompartilhadoModule,
    PaginationModule.forRoot(),
  ],
  declarations: [
    ManterUsuarioComponent,
    DadosGeraisComponent,
    AssociaPerfilComponent,
    PerfisAssociadosComponent,
    ConcatenaPerfisSistemasCadastroPipe
  ],
  exports: [
    ManterUsuarioComponent
  ],
  providers: [
    RecursoPipe
  ]
})
export class ManterUsuarioModule {}
