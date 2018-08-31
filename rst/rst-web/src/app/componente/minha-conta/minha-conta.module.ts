import {CompartilhadoModule} from './../../compartilhado/compartilhado.module';
import {RouterModule, Routes} from '@angular/router';
import {GrupoPerguntaService} from './../../servico/grupo-pergunta.service';
import {NgModule} from '@angular/core';
import {MinhaContaComponent} from './minha-conta.component';
import {ImageCropperModule} from "ng2-img-cropper";
import {UsuarioService} from "../../servico/usuario.service";
import {ParametroService} from "../../servico/parametro.service";

const routes: Routes = [
    {path: '', component: MinhaContaComponent},
];


@NgModule({
    imports: [
        RouterModule.forChild(routes),
        CompartilhadoModule,
        RouterModule,
        ImageCropperModule,
    ],
    declarations: [MinhaContaComponent],
    providers: [GrupoPerguntaService, UsuarioService, ParametroService],
    exports: [MinhaContaComponent]
})
export class MinhaContaModule {
}
