import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
// tslint:disable-next-line:max-line-length
import { AssociacaoEmpresaTrabalhadorComponent } from './../../compartilhado/associacao-empresa-trabalhador/associacao-empresa-trabalhador.component';
import { CadastroEmpresaTrabalhadorComponent } from './empresa-trabalhador/cadastro-empresa-trabalhador-associado/cadastro-empresa-trabalhador.component';
// tslint:disable-next-line:max-line-length
import { PesquisaEmpresaTrabalhadorComponent } from './empresa-trabalhador/pesquisa-empresa-trabalhador/pesquisa-empresa-trabalhador.component';
import { TrabalhadorService } from 'app/servico/trabalhador.service';
import { EmpresaTrabalhadorService } from './../../servico/empresa-trabalhador.service';
import { EmpresaLotacaoService } from './../../servico/empresa-lotacao.service';
import { EmpresaSetorService } from './../../servico/empresa-setor.service';
import { EmpresaFuncaoService } from 'app/servico/empresa-funcao.service';
import { EmpresaSindicatoService } from './../../servico/empresa.-sindicato.service';
import { SetorService } from './../../servico/setor.service';
import { RamoEmpresaService } from './../../servico/ramo-empresa.service';
import { UnidadeObraService } from './../../servico/unidade-obra.service';
import { FuncaoService } from './../../servico/funcao.service';
import { EmpresaCboService } from './../../servico/empresa-cbo.service';
import { JornadaService } from './../../servico/jornada.service';
import { EmpresaJornadaService } from './../../servico/empresa-jornada.service';
import { UatService } from './../../servico/uat.service';
import { TipoEmpresaService } from './../../servico/tipo-empresa.service';
import { EstadoService } from 'app/servico/estado.service';
import { PorteEmpresaService } from 'app/servico/porte-empresa.service';
import { DialogService } from 'ng2-bootstrap-modal';
import { EmpresaService } from 'app/servico/empresa.service';
import { EmpresaFuncaoComponent } from './empresa-funcao/empresa-funcao.component';
import { TextMaskModule } from 'angular2-text-mask/dist/angular2TextMask';
import { PaginationModule, AlertModule } from 'ngx-bootstrap';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { NgModule } from '@angular/core';
import { EmpresaJornadaComponent } from './empresa-jornada/empresa-jornada.component';
import { EmpresaSindicatoComponent } from './empresa-sindicato/empresa-sindicato.component';
import { CadastroEmpresaComponent } from 'app/componente/empresa/cadastro-empresa/cadastro-empresa.component';
import { PesquisaEmpresaComponent } from 'app/componente/empresa/pesquisa-empresa/pesquisa-empresa.component';
import { Routes, RouterModule } from '@angular/router';
import { SindicatoService } from './../../servico/sindicato.service';
import { CboService } from '../../servico/cbo.service';
import { EmpresaCboComponent } from './empresa-cbo/empresa-cbo.component';
import { EmpresaSetorComponent } from './empresa-setor/empresa-setor.component';
import { EmpresaLotacaoComponent } from './empresa-lotacao/empresa-lotacao.component';
import { ModalSetorComponentModule } from 'app/modal/modal-setor-component/modal-setor-component.module';
import { ModalCargoComponentModule } from 'app/modal/modal-cargo-component/modal-cargo-component.module';
import { ModalFuncaoComponentModule } from 'app/modal/modal-funcao-component/modal-funcao-component.module';
import { EmpresaIntermediarioComponent } from './empresa-intermediario/empresa-intermediario.component';
import { LotacaoTrabalhadorComponent } from './lotacao-trabalhador/lotacao-trabalhador.component';
import { AutorizacaoGuard } from '../../seguranca/autorizacao.guard';
import { EmpresaContratoComponent } from './empresa-contrato/empresa-contrato.component';
import {EmpresaContratoService} from "../../servico/empresa-contrato.service";
import { CadastroEmpresaContratoComponent } from './empresa-contrato/cadastro-empresa-contrato/cadastro-empresa-contrato.component';
import {TipoProgramaService} from "../../servico/tipo-programa.service";
import {UsuarioEntidadeService} from "../../servico/usuario-entidade.service";
import {DepartRegionalService} from "../../servico/depart-regional.service";

const routes: Routes = [
    {
        path: '', component: PesquisaEmpresaComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Pesquisar Empresa',
            permissoes: [PermissoesEnum.EMPRESA,
            PermissoesEnum.EMPRESA_CADASTRAR,
            PermissoesEnum.EMPRESA_ALTERAR,
            PermissoesEnum.EMPRESA_CONSULTAR,
            PermissoesEnum.EMPRESA_DESATIVAR,
            PermissoesEnum.EMPRESA_SINDICATO,
            PermissoesEnum.EMPRESA_SINDICATO_CADASTRAR,
            PermissoesEnum.EMPRESA_SINDICATO_ALTERAR,
            PermissoesEnum.EMPRESA_SINDICATO_CONSULTAR,
            PermissoesEnum.EMPRESA_SINDICATO_DESATIVAR,
            PermissoesEnum.EMPRESA_JORNADA,
            PermissoesEnum.EMPRESA_JORNADA_CADASTRAR,
            PermissoesEnum.EMPRESA_JORNADA_ALTERAR,
            PermissoesEnum.EMPRESA_JORNADA_CONSULTAR,
            PermissoesEnum.EMPRESA_JORNADA_DESATIVAR,
            PermissoesEnum.EMPRESA_CARGO,
            PermissoesEnum.EMPRESA_CARGO_CADASTRAR,
            PermissoesEnum.EMPRESA_CARGO_ALTERAR,
            PermissoesEnum.EMPRESA_CARGO_CONSULTAR,
            PermissoesEnum.EMPRESA_CARGO_DESATIVAR,
            PermissoesEnum.EMPRESA_FUNCAO,
            PermissoesEnum.EMPRESA_FUNCAO_CADASTRAR,
            PermissoesEnum.EMPRESA_FUNCAO_ALTERAR,
            PermissoesEnum.EMPRESA_FUNCAO_CONSULTAR,
            PermissoesEnum.EMPRESA_FUNCAO_DESATIVAR,
            PermissoesEnum.EMPRESA_SETOR,
            PermissoesEnum.EMPRESA_SETOR_CADASTRAR,
            PermissoesEnum.EMPRESA_SETOR_ALTERAR,
            PermissoesEnum.EMPRESA_SETOR_CONSULTAR,
            PermissoesEnum.EMPRESA_SETOR_DESATIVAR,
            PermissoesEnum.EMPRESA_LOTACAO,
            PermissoesEnum.EMPRESA_LOTACAO_CADASTRAR,
            PermissoesEnum.EMPRESA_LOTACAO_ALTERAR,
            PermissoesEnum.EMPRESA_LOTACAO_CONSULTAR,
            PermissoesEnum.EMPRESA_LOTACAO_DESATIVAR,
            PermissoesEnum.EMPRESA_TRABALHADOR,
            PermissoesEnum.EMPRESA_TRABALHADOR_CADASTRAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_ALTERAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_CONSULTAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_DESATIVAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CADASTRAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_ALTERAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CONSULTAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_DESATIVAR],
        },
    },
    {
        path: ':id/cadastrar', component: CadastroEmpresaComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastrar Empresa',
            permissoes: [PermissoesEnum.EMPRESA,
            PermissoesEnum.EMPRESA_ALTERAR,
            PermissoesEnum.EMPRESA_CONSULTAR,
            PermissoesEnum.EMPRESA_DESATIVAR],
        },
    },
    {
        path: 'minhaempresa/:id/cadastrar', component: CadastroEmpresaComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastrar Empresa',
            permissoes: [PermissoesEnum.EMPRESA,
            PermissoesEnum.EMPRESA_ALTERAR,
            PermissoesEnum.EMPRESA_CONSULTAR,
            PermissoesEnum.EMPRESA_DESATIVAR],
        },
    },
    {
        path: 'cadastrar', component: CadastroEmpresaComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastrar Empresa',
            permissoes: [PermissoesEnum.EMPRESA, PermissoesEnum.EMPRESA_CADASTRAR],
        },
    },
    {
        path: ':id/contrato', component: EmpresaContratoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Contratos da Empresa',
            permissoes: [PermissoesEnum.EMPRESA, PermissoesEnum.EMPRESA_CONTRATO]
        }
    },

    {
        path: ':id/contrato/cadastrarcontrato', component: CadastroEmpresaContratoComponent,
        canActivate: [AutorizacaoGuard],
        data:{
            title: 'Novo Contrato',
            permissoes: [PermissoesEnum.EMPRESA_CONTRATO_CADASTRAR]
        }
    },

    {
        path: 'inicio', component: CadastroEmpresaComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: '',
            permissoes: [PermissoesEnum.EMPRESA, PermissoesEnum.EMPRESA_CADASTRAR]
        },
    },
    {
        path: ':id', component: EmpresaIntermediarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Tela intermediaria de empresa',
            permissoes: [PermissoesEnum.EMPRESA,
            PermissoesEnum.EMPRESA_ALTERAR,
            PermissoesEnum.EMPRESA_CONSULTAR,
            PermissoesEnum.EMPRESA_DESATIVAR,
            PermissoesEnum.EMPRESA_SINDICATO,
            PermissoesEnum.EMPRESA_SINDICATO_CADASTRAR,
            PermissoesEnum.EMPRESA_SINDICATO_ALTERAR,
            PermissoesEnum.EMPRESA_SINDICATO_CONSULTAR,
            PermissoesEnum.EMPRESA_SINDICATO_DESATIVAR,
            PermissoesEnum.EMPRESA_JORNADA,
            PermissoesEnum.EMPRESA_JORNADA_CADASTRAR,
            PermissoesEnum.EMPRESA_JORNADA_ALTERAR,
            PermissoesEnum.EMPRESA_JORNADA_CONSULTAR,
            PermissoesEnum.EMPRESA_JORNADA_DESATIVAR,
            PermissoesEnum.EMPRESA_CARGO,
            PermissoesEnum.EMPRESA_CARGO_CADASTRAR,
            PermissoesEnum.EMPRESA_CARGO_ALTERAR,
            PermissoesEnum.EMPRESA_CARGO_CONSULTAR,
            PermissoesEnum.EMPRESA_CARGO_DESATIVAR,
            PermissoesEnum.EMPRESA_FUNCAO,
            PermissoesEnum.EMPRESA_FUNCAO_CADASTRAR,
            PermissoesEnum.EMPRESA_FUNCAO_ALTERAR,
            PermissoesEnum.EMPRESA_FUNCAO_CONSULTAR,
            PermissoesEnum.EMPRESA_FUNCAO_DESATIVAR,
            PermissoesEnum.EMPRESA_SETOR,
            PermissoesEnum.EMPRESA_SETOR_CADASTRAR,
            PermissoesEnum.EMPRESA_SETOR_ALTERAR,
            PermissoesEnum.EMPRESA_SETOR_CONSULTAR,
            PermissoesEnum.EMPRESA_SETOR_DESATIVAR,
            PermissoesEnum.EMPRESA_LOTACAO,
            PermissoesEnum.EMPRESA_LOTACAO_CADASTRAR,
            PermissoesEnum.EMPRESA_LOTACAO_ALTERAR,
            PermissoesEnum.EMPRESA_LOTACAO_CONSULTAR,
            PermissoesEnum.EMPRESA_LOTACAO_DESATIVAR,
            PermissoesEnum.EMPRESA_TRABALHADOR,
            PermissoesEnum.EMPRESA_TRABALHADOR_CADASTRAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_ALTERAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_CONSULTAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_DESATIVAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CADASTRAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_ALTERAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CONSULTAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_DESATIVAR],
        },
    },
    {
        path: 'minhaempresa', component: EmpresaIntermediarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            permissoes: [PermissoesEnum.EMPRESA,
            PermissoesEnum.EMPRESA_ALTERAR,
            PermissoesEnum.EMPRESA_CONSULTAR,
            PermissoesEnum.EMPRESA_DESATIVAR,
            PermissoesEnum.EMPRESA_SINDICATO,
            PermissoesEnum.EMPRESA_SINDICATO_CADASTRAR,
            PermissoesEnum.EMPRESA_SINDICATO_ALTERAR,
            PermissoesEnum.EMPRESA_SINDICATO_CONSULTAR,
            PermissoesEnum.EMPRESA_SINDICATO_DESATIVAR,
            PermissoesEnum.EMPRESA_JORNADA,
            PermissoesEnum.EMPRESA_JORNADA_CADASTRAR,
            PermissoesEnum.EMPRESA_JORNADA_ALTERAR,
            PermissoesEnum.EMPRESA_JORNADA_CONSULTAR,
            PermissoesEnum.EMPRESA_JORNADA_DESATIVAR,
            PermissoesEnum.EMPRESA_CARGO,
            PermissoesEnum.EMPRESA_CARGO_CADASTRAR,
            PermissoesEnum.EMPRESA_CARGO_ALTERAR,
            PermissoesEnum.EMPRESA_CARGO_CONSULTAR,
            PermissoesEnum.EMPRESA_CARGO_DESATIVAR,
            PermissoesEnum.EMPRESA_FUNCAO,
            PermissoesEnum.EMPRESA_FUNCAO_CADASTRAR,
            PermissoesEnum.EMPRESA_FUNCAO_ALTERAR,
            PermissoesEnum.EMPRESA_FUNCAO_CONSULTAR,
            PermissoesEnum.EMPRESA_FUNCAO_DESATIVAR,
            PermissoesEnum.EMPRESA_SETOR,
            PermissoesEnum.EMPRESA_SETOR_CADASTRAR,
            PermissoesEnum.EMPRESA_SETOR_ALTERAR,
            PermissoesEnum.EMPRESA_SETOR_CONSULTAR,
            PermissoesEnum.EMPRESA_SETOR_DESATIVAR,
            PermissoesEnum.EMPRESA_LOTACAO,
            PermissoesEnum.EMPRESA_LOTACAO_CADASTRAR,
            PermissoesEnum.EMPRESA_LOTACAO_ALTERAR,
            PermissoesEnum.EMPRESA_LOTACAO_CONSULTAR,
            PermissoesEnum.EMPRESA_LOTACAO_DESATIVAR,
            PermissoesEnum.EMPRESA_TRABALHADOR,
            PermissoesEnum.EMPRESA_TRABALHADOR_CADASTRAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_ALTERAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_CONSULTAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_DESATIVAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CADASTRAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_ALTERAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CONSULTAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_DESATIVAR],
        },
    },
    {
        path: ':id/sindicato', component: EmpresaSindicatoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Empresa | Sindicato',
            permissoes: [PermissoesEnum.EMPRESA_SINDICATO,
            PermissoesEnum.EMPRESA_SINDICATO_CADASTRAR,
            PermissoesEnum.EMPRESA_SINDICATO_ALTERAR,
            PermissoesEnum.EMPRESA_SINDICATO_CONSULTAR,
            PermissoesEnum.EMPRESA_SINDICATO_DESATIVAR],
        },
    },
    {
        path: 'minhaempresa/:id/sindicato', component: EmpresaSindicatoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Empresa | Sindicato',
            permissoes: [PermissoesEnum.EMPRESA_SINDICATO,
            PermissoesEnum.EMPRESA_SINDICATO_CADASTRAR,
            PermissoesEnum.EMPRESA_SINDICATO_ALTERAR,
            PermissoesEnum.EMPRESA_SINDICATO_CONSULTAR,
            PermissoesEnum.EMPRESA_SINDICATO_DESATIVAR],
        },
    },
    {
        path: ':id/jornadas', component: EmpresaJornadaComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastrar Jornadas',
            permissoes: [PermissoesEnum.EMPRESA_JORNADA,
            PermissoesEnum.EMPRESA_JORNADA_CADASTRAR,
            PermissoesEnum.EMPRESA_JORNADA_ALTERAR,
            PermissoesEnum.EMPRESA_JORNADA_CONSULTAR,
            PermissoesEnum.EMPRESA_JORNADA_DESATIVAR],
        },
    },
    {
        path: 'minhaempresa/:id/jornadas', component: EmpresaJornadaComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastrar Jornadas',
            permissoes: [PermissoesEnum.EMPRESA_JORNADA,
            PermissoesEnum.EMPRESA_JORNADA_CADASTRAR,
            PermissoesEnum.EMPRESA_JORNADA_ALTERAR,
            PermissoesEnum.EMPRESA_JORNADA_CONSULTAR,
            PermissoesEnum.EMPRESA_JORNADA_DESATIVAR],
        },
    },
    {
        path: ':id/cbos', component: EmpresaCboComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastrar Cargos',
            permissoes: [PermissoesEnum.EMPRESA_CARGO,
            PermissoesEnum.EMPRESA_CARGO_CADASTRAR,
            PermissoesEnum.EMPRESA_CARGO_ALTERAR,
            PermissoesEnum.EMPRESA_CARGO_CONSULTAR,
            PermissoesEnum.EMPRESA_CARGO_DESATIVAR],
        },
    },
    {
        path: 'minhaempresa/:id/cbos', component: EmpresaCboComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastrar Cargos',
            permissoes: [PermissoesEnum.EMPRESA_CARGO,
            PermissoesEnum.EMPRESA_CARGO_CADASTRAR,
            PermissoesEnum.EMPRESA_CARGO_ALTERAR,
            PermissoesEnum.EMPRESA_CARGO_CONSULTAR,
            PermissoesEnum.EMPRESA_CARGO_DESATIVAR],
        },
    },
    {
        path: ':id/funcao', component: EmpresaFuncaoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastrar Funcoes',
            permissoes: [PermissoesEnum.EMPRESA_FUNCAO,
            PermissoesEnum.EMPRESA_FUNCAO_CADASTRAR,
            PermissoesEnum.EMPRESA_FUNCAO_ALTERAR,
            PermissoesEnum.EMPRESA_FUNCAO_CONSULTAR,
            PermissoesEnum.EMPRESA_FUNCAO_DESATIVAR],
        },
    },
    {
        path: 'minhaempresa/:id/funcao', component: EmpresaFuncaoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastrar Funcoes',
            permissoes: [PermissoesEnum.EMPRESA_FUNCAO,
            PermissoesEnum.EMPRESA_FUNCAO_CADASTRAR,
            PermissoesEnum.EMPRESA_FUNCAO_ALTERAR,
            PermissoesEnum.EMPRESA_FUNCAO_CONSULTAR,
            PermissoesEnum.EMPRESA_FUNCAO_DESATIVAR],
        },
    },
    {
        path: ':id/setor', component: EmpresaSetorComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastrar Setores',
            permissoes: [PermissoesEnum.EMPRESA_SETOR,
            PermissoesEnum.EMPRESA_SETOR_CADASTRAR,
            PermissoesEnum.EMPRESA_SETOR_ALTERAR,
            PermissoesEnum.EMPRESA_SETOR_CONSULTAR,
            PermissoesEnum.EMPRESA_SETOR_DESATIVAR],
        },
    },
    {
        path: 'minhaempresa/:id/setor', component: EmpresaSetorComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastrar Setores',
            permissoes: [PermissoesEnum.EMPRESA_SETOR,
            PermissoesEnum.EMPRESA_SETOR_CADASTRAR,
            PermissoesEnum.EMPRESA_SETOR_ALTERAR,
            PermissoesEnum.EMPRESA_SETOR_CONSULTAR,
            PermissoesEnum.EMPRESA_SETOR_DESATIVAR],
        },
    },
    {
        path: ':id/lotacao', component: EmpresaLotacaoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastrar Lotação',
            permissoes: [PermissoesEnum.EMPRESA_LOTACAO,
            PermissoesEnum.EMPRESA_LOTACAO_CADASTRAR,
            PermissoesEnum.EMPRESA_LOTACAO_ALTERAR,
            PermissoesEnum.EMPRESA_LOTACAO_CONSULTAR,
            PermissoesEnum.EMPRESA_LOTACAO_DESATIVAR],
        },
    },
    {
        path: 'minhaempresa/:id/lotacao', component: EmpresaLotacaoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastrar Lotação',
            permissoes: [PermissoesEnum.EMPRESA_LOTACAO,
            PermissoesEnum.EMPRESA_LOTACAO_CADASTRAR,
            PermissoesEnum.EMPRESA_LOTACAO_ALTERAR,
            PermissoesEnum.EMPRESA_LOTACAO_CONSULTAR,
            PermissoesEnum.EMPRESA_LOTACAO_DESATIVAR],
        },
    },
    {
        path: ':id/trabalhador', component: PesquisaEmpresaTrabalhadorComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Pesquisar Trabalhadores Associados',
            permissoes: [PermissoesEnum.EMPRESA_TRABALHADOR,
            PermissoesEnum.EMPRESA_TRABALHADOR_CADASTRAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_ALTERAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_CONSULTAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: 'minhaempresa/:id/trabalhador', component: PesquisaEmpresaTrabalhadorComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Pesquisar Trabalhadores Associados',
            permissoes: [PermissoesEnum.EMPRESA_TRABALHADOR,
            PermissoesEnum.EMPRESA_TRABALHADOR_CADASTRAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_ALTERAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_CONSULTAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: ':id/trabalhador/associar', component: CadastroEmpresaTrabalhadorComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastro de Trabalhador Associado',
            permissoes: [PermissoesEnum.EMPRESA_TRABALHADOR,
            PermissoesEnum.EMPRESA_TRABALHADOR_CADASTRAR],
        },
    },
    {
        path: 'minhaempresa/:id/trabalhador/associar', component: CadastroEmpresaTrabalhadorComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastro de Trabalhador Associado',
            permissoes: [PermissoesEnum.EMPRESA_TRABALHADOR,
            PermissoesEnum.EMPRESA_TRABALHADOR_CADASTRAR],
        },
    },
    {
        path: ':id/trabalhador/associar/:idEmpresaTrabalhador', component: CadastroEmpresaTrabalhadorComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastro de Trabalhador Associado',
            permissoes: [PermissoesEnum.EMPRESA_TRABALHADOR,
            PermissoesEnum.EMPRESA_TRABALHADOR_ALTERAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_CONSULTAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: 'minhaempresa/:id/trabalhador/associar/:idEmpresaTrabalhador', component: CadastroEmpresaTrabalhadorComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Cadastro de Trabalhador Associado',
            permissoes: [PermissoesEnum.EMPRESA_TRABALHADOR,
            PermissoesEnum.EMPRESA_TRABALHADOR_ALTERAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_CONSULTAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: ':id/trabalhador/lotacao/:idEmpresaTrabalhador', component: LotacaoTrabalhadorComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Lotacação Trabalhador',
            permissoes: [PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CADASTRAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_ALTERAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CONSULTAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_DESATIVAR],
        },
    },
    {
        path: 'minhaempresa/:id/trabalhador/lotacao/:idEmpresaTrabalhador', component: LotacaoTrabalhadorComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: 'Lotacação Trabalhador',
            permissoes: [PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CADASTRAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_ALTERAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CONSULTAR,
            PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_DESATIVAR],
        },
    },
];

@NgModule({
    imports: [
        RouterModule.forChild(routes),
        CompartilhadoModule,
        RouterModule,
        PaginationModule.forRoot(),
        TextMaskModule,
        ModalSetorComponentModule,
        ModalCargoComponentModule,
        ModalFuncaoComponentModule,
        AlertModule.forRoot(),
    ],
    exports: [PesquisaEmpresaComponent, CadastroEmpresaComponent],
    declarations: [PesquisaEmpresaComponent, CadastroEmpresaComponent, EmpresaJornadaComponent,
        EmpresaSindicatoComponent, EmpresaFuncaoComponent, EmpresaCboComponent, CadastroEmpresaTrabalhadorComponent,
        EmpresaSetorComponent, EmpresaLotacaoComponent, EmpresaIntermediarioComponent,
        PesquisaEmpresaTrabalhadorComponent, LotacaoTrabalhadorComponent, AssociacaoEmpresaTrabalhadorComponent, EmpresaContratoComponent, CadastroEmpresaContratoComponent],
    providers: [EmpresaService, DialogService, PorteEmpresaService, EstadoService, TipoEmpresaService, UatService, UnidadeObraService,
        RamoEmpresaService, EmpresaJornadaService, JornadaService, SindicatoService, EmpresaCboService, CboService, FuncaoService,
        SetorService, EmpresaLotacaoService, EmpresaSindicatoService, EmpresaFuncaoService, EmpresaSetorService, EmpresaTrabalhadorService,
        TrabalhadorService, EmpresaContratoService, TipoProgramaService, UsuarioEntidadeService, DepartRegionalService],
})
export class EmpresaModule { }
