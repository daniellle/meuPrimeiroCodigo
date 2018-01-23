import {ResOrdinalComponent} from './historico-timeline/res-ordinal.component';
import { ResHistoricoComponent } from './res-historico/res-historico.component';
import { ComposicaoDirective } from './../../diretiva/composicao.directive';
import { ElementoIndefinidoDirective } from 'app/diretiva/elemento-indefinido.directive';
import { ElementoProtocolosDirective } from 'app/diretiva/elemento-protocolos.directive';
import { ElementoEstadosDirective } from './../../diretiva/elemento-estados.directive';
import { ElementoDadosDirective } from './../../diretiva/elemento-dados.directive';
import { TimelineItemComponent } from './historico-timeline/timeline-item.component';
import { ResItemComponent } from './historico-timeline/res-item.component';
import { ResItemRowComponent } from './historico-timeline/res-item-row.component';
import { ResQuantidadeComponent } from './historico-timeline/res-quantidade.component';
import { ResTextoComponent } from './historico-timeline/res-texto.component';
import { ResBooleanoComponent } from './historico-timeline/res-booleano.component';
import { ResDataComponent } from './historico-timeline/res-data.component';
import { ResProporcaoComponent } from './historico-timeline/res-proporcao.component';
import { ResURIComponent } from './historico-timeline/res-uri.component';
import { ResMultiMidiaComponent } from './historico-timeline/res-midia.component';
import { ResContagemComponent } from './historico-timeline/res-contagem.component';
import { ResIntervaloQuantidadeComponent } from './historico-timeline/res-intervalo-quantidade.component';
import { ResTextoCodificadoComponent } from './historico-timeline/res-texto-codificado.component';
import { ResService } from './../../servico/res-service.service';
import { ResHomeResolve } from './res-home/res-home.resolver';
import { AutorizacaoGuard } from './../../seguranca/autorizacao.guard';
import { Routes, RouterModule } from '@angular/router';
import { NgModule, LOCALE_ID } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CompartilhadoModule } from 'app/compartilhado/compartilhado.module';
import { PaginationModule, TypeaheadModule } from 'ngx-bootstrap';
import { ResHomeComponent } from './res-home/res-home.component';

const routes: Routes = [
    {
        path: 'minhasaude', component: ResHistoricoComponent,
        canLoad: [AutorizacaoGuard],
        data: {
            permissoes: ['trabalhador_cadastrar', 'trabalhador_alterar', 'trabalhador_consultar', 'trabalhador_desativar'],
        },
    },
    {
        path: 'home', component: ResHomeComponent,
        canLoad: [AutorizacaoGuard],
        data: {
            permissoes: ['trabalhador_cadastrar', 'trabalhador_alterar', 'trabalhador_consultar', 'trabalhador_desativar'],
        },
        resolve: {
            basicos: ResHomeResolve,
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
    ],
    declarations: [
        ResHomeComponent,
        TimelineItemComponent,
        ResItemComponent,
        ResItemRowComponent,
        ResOrdinalComponent,
        ResTextoComponent,
        ResQuantidadeComponent,
        ResBooleanoComponent,
        ResDataComponent,
        ResProporcaoComponent,
        ResURIComponent,
        ResMultiMidiaComponent,
        ResContagemComponent,
        ResIntervaloQuantidadeComponent,
        ResTextoCodificadoComponent,
        ElementoDadosDirective,
        ElementoEstadosDirective,
        ElementoProtocolosDirective,
        ElementoIndefinidoDirective,
        ComposicaoDirective,
        ResHistoricoComponent,
    ],
    providers: [
        ResHomeResolve, ResService, {
            provide: LOCALE_ID,
            useValue: 'pt-BR',
        }],
    entryComponents: [
        TimelineItemComponent,
        ResItemComponent,
        ResItemRowComponent,
        ResOrdinalComponent,
        ResQuantidadeComponent,
        ResTextoComponent,
        ResBooleanoComponent,
        ResDataComponent,
        ResProporcaoComponent,
        ResURIComponent,
        ResMultiMidiaComponent,
        ResContagemComponent,
        ResIntervaloQuantidadeComponent,
        ResTextoCodificadoComponent,
    ],
})
export class ResModule { }
