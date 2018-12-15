import {ModalDepartamentoComponentModule} from './../../modal/modal-departamento-component/modal-departamento-component.module';
import {ModalEmpresaComponentModule} from './../../modal/modal-empresa-component/modal-empresa-component.module';
import {EmpresaTrabalhadorService} from './../../servico/empresa-trabalhador.service';
import {UsuarioIntermediarioComponent} from './usuario-intermediario/usuario-intermediario.component';
// tslint:disable-next-line:max-line-length
import {CadastroDepartamentoUsuarioComponent} from './departamento-regional-usuario/cadastro-departamento-regional-usuario/cadastro-departamento-regional-usuario.component';
import {PesquisaDepartamentoUsuarioComponent} from './departamento-regional-usuario/pesquisa-departamento-regional-usuario/pesquisa-departamento-regional-usuario.component';
import {CadastroSindicatoUsuarioComponent} from './sindicato-usuario/cadastro-sindicato-usuario/cadastro-sindicato-usuario.component';
import {DepartRegionalService} from './../../servico/depart-regional.service';
import {SindicatoService} from './../../servico/sindicato.service';
import {PesquisaSindicatoUsuarioComponent} from './sindicato-usuario/pesquisa-sindicato-usuario/pesquisa-sindicato-usuario.component';
import {EmpresaService} from './../../servico/empresa.service';
import {CadastroEmpresaUsuarioComponent} from './empresa-usuario/cadastro-empresa-usuario/cadastro-empresa-usuario.component';
import {UsuarioEntidadeService} from './../../servico/usuario-entidade.service';
import {PesquisaEmpresaUsuarioComponent} from './empresa-usuario/pesquisa-empresa-usuario/pesquisa-empresa-usuario.component';
import {PerfilService} from './../../servico/perfil.service';
import {SistemaService} from './../../servico/sistema.service';
import {PaginationModule} from 'ngx-bootstrap';
import {UsuarioService} from './../../servico/usuario.service';
import {ToastyService} from 'ng2-toasty';
import {DialogService} from 'ng2-bootstrap-modal';
import {BloqueioService} from './../../servico/bloqueio.service';
import {CompartilhadoModule} from './../../compartilhado/compartilhado.module';
import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {PesquisaUsuarioComponent} from './pesquisa-usuario/pesquisa-usuario.component';
import {ParametroService} from '../../servico/parametro.service';
import {PesquisaUnidadeSESIUsuarioComponent} from "./unidade-sesi-usuario/pesquisa-unidade-sesi-usuario/pesquisa-unidade-sesi-usuario.component";
import {CadastroUnidadeSESIUsuarioComponent} from "./unidade-sesi-usuario/cadastro-unidade-sesi-usuario/cadastro-unidade-sesi-usuario.component";
import {UatService} from "../../servico/uat.service";
import { UsuarioRoutingModule } from './usuario-routing.module';
import { ManterUsuarioModule } from './manter-usuario/manter-usuario.module';
import { UsuarioBarramentoComponent } from './usuario-barramento/usuario-barramento.component';

@NgModule({
    imports: [
        UsuarioRoutingModule,
        CompartilhadoModule,
        RouterModule,
        PaginationModule.forRoot(),
        ModalEmpresaComponentModule,
        ModalDepartamentoComponentModule,
        ManterUsuarioModule
    ],
    declarations: [
        PesquisaUsuarioComponent,
        UsuarioIntermediarioComponent,
        PesquisaEmpresaUsuarioComponent,
        CadastroEmpresaUsuarioComponent,
        PesquisaSindicatoUsuarioComponent,
        CadastroSindicatoUsuarioComponent,
        PesquisaDepartamentoUsuarioComponent,
        CadastroDepartamentoUsuarioComponent,
        PesquisaUnidadeSESIUsuarioComponent,
        CadastroUnidadeSESIUsuarioComponent,
        UsuarioBarramentoComponent,
    ],
    providers: [
        UsuarioService,
        BloqueioService,
        DialogService,
        ToastyService,
        SistemaService,
        PerfilService,
        UsuarioEntidadeService,
        EmpresaService,
        SindicatoService,
        DepartRegionalService,
        EmpresaTrabalhadorService,
        ParametroService,
        UatService
    ],
})
export class UsuarioModule {
}
