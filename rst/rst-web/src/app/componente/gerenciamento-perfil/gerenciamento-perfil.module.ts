import {CompartilhadoModule} from './../../compartilhado/compartilhado.module';
import {RouterModule, Routes} from '@angular/router';
import {GrupoPerguntaService} from './../../servico/grupo-pergunta.service';
import {NgModule} from '@angular/core';
import {GerenciamentoPerfilComponent} from './gerenciamento-perfil.component';
import {ManterPerfilComponent} from "./manter-perfil/manter-perfil.component";
import {ImageCropperModule} from "ng2-img-cropper";
import {UsuarioService} from "../../servico/usuario.service";

const routes: Routes = [
    {path: '', component: ManterPerfilComponent},
    {path: 'trocarsenha', component: GerenciamentoPerfilComponent},
];


@NgModule({
    imports: [
        RouterModule.forChild(routes),
        CompartilhadoModule,
        RouterModule,
        ImageCropperModule,
    ],
    declarations: [GerenciamentoPerfilComponent, ManterPerfilComponent],
    providers: [GrupoPerguntaService, UsuarioService],
    exports: [GerenciamentoPerfilComponent]
})
export class GerenciamentoPerfilModule {
}
