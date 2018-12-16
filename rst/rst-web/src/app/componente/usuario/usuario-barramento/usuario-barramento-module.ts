import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { CompartilhadoModule } from '../../../compartilhado/compartilhado.module';
import { AssociaPerfilComponent } from '../manter-usuario/associa-perfil/associa-perfil.component';
import { PaginationModule } from 'ngx-bootstrap';
import { RecursoPipe } from 'app/compartilhado/utilitario/recurso.pipe';
import {UsuarioBarramentoComponent} from "./usuario-barramento.component";
import {CNPJListarSemPerfilComponent} from "./cnpj-listar-sem-perfil/cnpj-listar-sem-perfil.component";
import {CNPJPerfisAssociadosComponent} from "./cnpj-perfis-associados/cnpj-perfis-associados.component";
import {ManterUsuarioComponent} from "../manter-usuario/manter-usuario.component";
import {ManterUsuarioModule} from "../manter-usuario/manter-usuario.module";
import {ConcatenaPerfisSistemasCadastroPipe} from "../manter-usuario/concatena-perfis-sistemas-cadastro.pipe";
import {ConcatenaPerfisSistemasCadastroBarramentoPipe} from "./concatena-perfis-sistemas-cadastro-barramento.pipe";

@NgModule({
    imports: [
        ReactiveFormsModule,
        CompartilhadoModule,
        PaginationModule.forRoot(),
        ManterUsuarioModule
    ],
    declarations: [
        AssociaPerfilComponent,
        ManterUsuarioComponent,
        CNPJListarSemPerfilComponent,
        CNPJPerfisAssociadosComponent,
        ConcatenaPerfisSistemasCadastroPipe,
        ConcatenaPerfisSistemasCadastroBarramentoPipe

    ],
    exports: [
        UsuarioBarramentoComponent
    ],
    providers: [
        RecursoPipe
    ]
})
export class UsuarioBarramentoModule {}
