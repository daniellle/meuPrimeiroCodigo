import { PermissoesEnum } from './../../modelo/enum/enum-permissoes';
import { PerfilEnum } from './../../modelo/enum/enum-perfil';
import { IgevComponent } from './igev/igev.component';
import { RespostaQuestionarioTrabalhadorService } from './../../servico/resposta-questionario-trabalhador.service';
import { QuestionarioTrabalhadorService } from './../../servico/questionario-trabalhador.service';
import { MontarQuestionarioService } from './../../servico/montar-questionario.service';
import { ParametroService } from './../../servico/parametro.service';
import { CertificadoService } from './../../servico/certificado.service';
import { TipoCursoService } from './../../servico/tipo-curso.service';
import { ImageCropperModule } from 'ng2-img-cropper';
import { AutorizacaoGuard } from './../../seguranca/autorizacao.guard';
import { TrabalhadorDependenteService } from './../../servico/trabalhador-dependente.service';
import { TrabalhadorIntermediarioComponent } from './trabalhador-intermediario/trabalhador-intermediario.component';
import { PaisService } from './../../servico/pais.service';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { PaginationModule, TypeaheadModule } from 'ngx-bootstrap';
import { TrabalhadorService } from './../../servico/trabalhador.service';
import { CadastroTrabalhadorComponent } from './cadastro-trabalhador/cadastro-trabalhador.component';
import { PesquisaTrabalhadorComponent } from './pesquisa-trabalhador/pesquisa-trabalhador.component';
import { DialogService } from 'ng2-bootstrap-modal';
import { ProfissaoService } from 'app/servico/profissao.service';
import { TrabalhadorDependenteComponent } from './trabalhador-dependente/trabalhador-dependente.component';
import { TrabalhadorCertificadoComponent } from './trabalhador-certificado/trabalhador-certificado.component';
import { HistoricoQuestionarioComponent } from './historico-questionario/historico-questionario.component';
import { AplicarQuestionarioComponent } from 'app/componente/trabalhador/aplicar-questionario/aplicar-questionario.component';
import { ResultadoQuestionarioComponent } from './resultado-questionario/resultado-questionario.component';
import { CadastrarVacinaComponent } from '../imunizacao/cadastrar-vacina/cadastrar-vacina.component';
import { ImunizacaoService } from '../../servico/imunizacao.service';
import { HistoricoVacinaComponent } from '../imunizacao/historico-vacina/historico-vacina.component';
import {TelaInicialVacinaComponent} from "../imunizacao/tela-inicial-vacina/tela-inicial-vacina.component";


const routes: Routes = [
    {
        path: '', component: PesquisaTrabalhadorComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_pesquisar,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_CADASTRAR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_CADASTRAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_ALTERAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_DESATIVAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_CADASTRAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_ALTERAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_CONSULTAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_DESATIVAR],
        },
    },
    {
        path: ':id/cadastrar', component: CadastroTrabalhadorComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_cadastrar,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: 'meusdados/:id/cadastrar', component: CadastroTrabalhadorComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_cadastrar,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: 'cadastrar', component: CadastroTrabalhadorComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_cadastrar,
            permissoes: [PermissoesEnum.TRABALHADOR, PermissoesEnum.TRABALHADOR_CADASTRAR],
        },
    },
    {
        path: 'meuscertificados', component: TrabalhadorCertificadoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_labels_cadastro_certificado,
            permissoes: [PermissoesEnum.TRABALHADOR_CERTIFICADO,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_CADASTRAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_ALTERAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_CONSULTAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_DESATIVAR],
        },
    },
    {
        path: 'igev', component: IgevComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_igev,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_CADASTRAR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: 'igev/:id/historico', component: HistoricoQuestionarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_historico,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_CADASTRAR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: 'igev/:id/questionario/responder', component: AplicarQuestionarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_responder_quest,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_CADASTRAR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: 'igev/:id/questionario/:idQuest/resultado', component: ResultadoQuestionarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_quest_resultado,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_CADASTRAR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: ':id/historico', component: HistoricoQuestionarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_historico,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_CADASTRAR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: ':id/questionario/responder', component: AplicarQuestionarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_responder_quest,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_CADASTRAR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: ':id/questionario/:idQuest/resultado', component: ResultadoQuestionarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_quest_resultado,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_CADASTRAR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR],
        },
    },

    {
        path: ':id/vacina-declarada',
        component: TelaInicialVacinaComponent,
        canActivate: [AutorizacaoGuard], data: {
            permissoes: [PermissoesEnum.VACINA_AUTODECLARADA,
                PermissoesEnum.VACINA_AUTODECLARADA_CONSULTAR,
                PermissoesEnum.VACINA_AUTODECLARADA_DESATIVAR]
        }
    },
    {
        path: ':id/vacina-declarada/cadastrar',
        component: CadastrarVacinaComponent,
        canActivate: [AutorizacaoGuard], data: {
            permissoes: [PermissoesEnum.VACINA_AUTODECLARADA,
                PermissoesEnum.VACINA_AUTODECLARADA_CADASTRAR],
        },
    },
    {
        path: ':id/vacina-declarada/:idVacina/cadastrar',
        component: CadastrarVacinaComponent,
        canActivate: [AutorizacaoGuard], data: {
            permissoes: [PermissoesEnum.VACINA_AUTODECLARADA,
                PermissoesEnum.VACINA_AUTODECLARADA_ALTERAR,
                PermissoesEnum.VACINA_AUTODECLARADA_CONSULTAR],
        },
    },
    {
        path: ':id/vacina-declarada/historico',
        component: HistoricoVacinaComponent,
        canActivate: [AutorizacaoGuard], data: {
            permissoes: [PermissoesEnum.VACINA_AUTODECLARADA,
                PermissoesEnum.VACINA_AUTODECLARADA_CONSULTAR,
                PermissoesEnum.VACINA_AUTODECLARADA_DESATIVAR],
        },
    },
    
    {
        path: ':id/igev', component: IgevComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_igev,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_CADASTRAR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: ':id', component: TrabalhadorIntermediarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_consultar,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_CADASTRAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_ALTERAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_DESATIVAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_CADASTRAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_ALTERAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_CONSULTAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_DESATIVAR],
        },
    },
    {
        path: 'meusdados', component: TrabalhadorIntermediarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_consultar,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_CADASTRAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_ALTERAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_DESATIVAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_CADASTRAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_ALTERAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_CONSULTAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_DESATIVAR],
        }
    },
    {
        path: ':id/dependente', component: TrabalhadorDependenteComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_consultar,
            permissoes: [PermissoesEnum.TRABALHADOR_DEPENDENTE,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_CADASTRAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_ALTERAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_DESATIVAR],
        },
    },
    {
        path: 'meusdados/:id/dependente', component: TrabalhadorDependenteComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_consultar,
            permissoes: [PermissoesEnum.TRABALHADOR_DEPENDENTE,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_CADASTRAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_ALTERAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DEPENDENTE_DESATIVAR],
        },
    },
    {
        path: ':id/certificado', component: TrabalhadorCertificadoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_labels_cadastro_certificado,
            permissoes: [PermissoesEnum.TRABALHADOR_CERTIFICADO,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_CADASTRAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_ALTERAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_CONSULTAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_DESATIVAR],
        },
    },
    {
        path: 'meusdados/:id/certificado', component: TrabalhadorCertificadoComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_labels_cadastro_certificado,
            permissoes: [PermissoesEnum.TRABALHADOR_CERTIFICADO,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_CADASTRAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_ALTERAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_CONSULTAR,
            PermissoesEnum.TRABALHADOR_CERTIFICADO_DESATIVAR],
        },
    },
    {
        path: 'meusdados/:id/igev', component: IgevComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_igev,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_CADASTRAR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: 'meusdados/:id/historico', component: HistoricoQuestionarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_historico,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_CADASTRAR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: 'meusdados/:id/questionario/responder', component: AplicarQuestionarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_responder_quest,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_CADASTRAR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: 'meusdados/:id/questionario/:idQuest/resultado', component: ResultadoQuestionarioComponent,
        canActivate: [AutorizacaoGuard],
        data: {
            title: MensagemProperties.app_rst_trabalhador_title_quest_resultado,
            permissoes: [PermissoesEnum.TRABALHADOR,
            PermissoesEnum.TRABALHADOR_CADASTRAR,
            PermissoesEnum.TRABALHADOR_ALTERAR,
            PermissoesEnum.TRABALHADOR_CONSULTAR,
            PermissoesEnum.TRABALHADOR_DESATIVAR],
        },
    },
    {
        path: 'meusdados/:id/vacina-declarada',
        component: TelaInicialVacinaComponent,
        canActivate: [AutorizacaoGuard], data: {
            permissoes: [PermissoesEnum.VACINA_AUTODECLARADA,
                PermissoesEnum.VACINA_AUTODECLARADA_CONSULTAR,
                PermissoesEnum.VACINA_AUTODECLARADA_DESATIVAR]
        }
    },
    {
        path: 'meusdados/:id/vacina-declarada/cadastrar',
        component: CadastrarVacinaComponent,
        canActivate: [AutorizacaoGuard], data: {
            permissoes: [PermissoesEnum.VACINA_AUTODECLARADA,
                PermissoesEnum.VACINA_AUTODECLARADA_CADASTRAR],
        },
    },
    {
        path: 'meusdados/:id/vacina-declarada/:idVacina/cadastrar',
        component: CadastrarVacinaComponent,
        canActivate: [AutorizacaoGuard], data: {
            permissoes: [PermissoesEnum.VACINA_AUTODECLARADA,
                PermissoesEnum.VACINA_AUTODECLARADA_ALTERAR,
                PermissoesEnum.VACINA_AUTODECLARADA_CONSULTAR],
        },
    },
    {
        path: 'meusdados/:id/vacina-declarada/historico',
        component: HistoricoVacinaComponent,
        canActivate: [AutorizacaoGuard], data: {
            permissoes: [PermissoesEnum.VACINA_AUTODECLARADA,
                PermissoesEnum.VACINA_AUTODECLARADA_CONSULTAR,
                PermissoesEnum.VACINA_AUTODECLARADA_DESATIVAR],
        },
    },
];

@NgModule({
    imports: [
        RouterModule.forChild(routes),
        CompartilhadoModule,
        RouterModule,
        PaginationModule.forRoot(),
        TypeaheadModule.forRoot(),
        NgbModule,
        ImageCropperModule,
    ],
    declarations: [PesquisaTrabalhadorComponent, CadastroTrabalhadorComponent, AplicarQuestionarioComponent,
        TrabalhadorDependenteComponent, TrabalhadorIntermediarioComponent, TrabalhadorCertificadoComponent, HistoricoQuestionarioComponent,
        ResultadoQuestionarioComponent, IgevComponent, CadastrarVacinaComponent, HistoricoVacinaComponent, TelaInicialVacinaComponent
    ],
    providers: [TrabalhadorService,
        TrabalhadorDependenteService,
        DialogService, ProfissaoService, PaisService, TipoCursoService,
        CertificadoService, ParametroService, MontarQuestionarioService, QuestionarioTrabalhadorService,
        RespostaQuestionarioTrabalhadorService, ImunizacaoService],
})
export class TrabalhadorModule { }
