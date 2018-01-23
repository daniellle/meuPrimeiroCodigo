import {ResOrdinalComponent} from './res-ordinal.component';
import { ResElemento } from './res-elemento.component';
import { ResQuantidadeComponent } from './res-quantidade.component';
import { ResTextoComponent } from './res-texto.component';
import { ResBooleanoComponent } from './res-booleano.component';
import { ResDataComponent } from './res-data.component';
import { ResProporcaoComponent } from './res-proporcao.component';
import { ResURIComponent } from './res-uri.component';
import { ResMultiMidiaComponent } from './res-midia.component';
import { ResContagemComponent } from './res-contagem.component';
import { ResIntervaloQuantidadeComponent } from './res-intervalo-quantidade.component';
import { ResTextoCodificadoComponent } from './res-texto-codificado.component';
import { ElementoDirective } from 'app/diretiva/elemento.directive';
import {
    OperationalTemplate,
    Element,
    DvQuantity,
    DvText,
    DvCodedText,
    DvBoolean,
    DvDate,
    DvDateTime,
    DvProportion,
    DvUri,
    DvMultimedia,
    DvCount,
    DvInterval,
    DvOrdinal,
} from '@ezvida/adl-core';
import { ElementoDadosDirective } from 'app/diretiva/elemento-dados.directive';
import { Component, ViewChild, ComponentFactoryResolver} from '@angular/core';

@Component({
    selector: 'app-res-item-row', template: `
<div class='row'>
    <div class='col-lg-6 col-md-4'>
        <ng-template #dadoEsquerda appElementoDados></ng-template>
    </div>
    <div class='col-lg-6 col-md-4'>
        <ng-template #dadoDireita appElementoDados></ng-template>
    </div>
</div>`,
    styleUrls: ['./res-item.component.scss'],
})
export class ResItemRowComponent {

    @ViewChild('dadoEsquerda', { read: ElementoDadosDirective }) dadoEsquerda: ElementoDadosDirective;
    @ViewChild('dadoDireita', { read: ElementoDadosDirective }) dadoDireita: ElementoDadosDirective;

    arquetipo: OperationalTemplate;

    constructor(private resolver: ComponentFactoryResolver) { }

    processar(dados: Element[], reference: string, arquetipo: OperationalTemplate): boolean {
        if (dados != null && arquetipo != null) {
            this.arquetipo = arquetipo;
            const diretivas = [this.dadoDireita, this.dadoEsquerda];
            dados.forEach((dado) => this.adicionaDadoNaDiretiva(diretivas, dado));
            return diretivas.length === 2;
        }
    }

    private adicionaDadoNaDiretiva(diretivas: ElementoDirective[], path: Element) {
        if (diretivas.length === 0) {
            return;
        }
        const diretiva = diretivas.pop();
        try {
            const component = this.criarComponent(diretiva, path);
            if (component) {
                component.processar(this.arquetipo, path);
            } else {
                throw new Error(`Não foi encotrado componente para o ${path.amPath}`);
            }
        } catch (error) {
            diretiva.conteiner.remove();
            diretivas.push(diretiva);
        }
    }

    private criarComponent(diretiva: ElementoDirective, elemento: Element): ResElemento | null {
        if (elemento.value instanceof DvOrdinal) {
            return <ResQuantidadeComponent>diretiva.conteiner
                .createComponent(this.resolver.resolveComponentFactory(ResOrdinalComponent)).instance;
        } else if (elemento.value instanceof DvCodedText) {
            return <ResTextoCodificadoComponent>diretiva.conteiner
                .createComponent(this.resolver.resolveComponentFactory(ResTextoCodificadoComponent)).instance;
        } else if (elemento.value instanceof DvQuantity) {
            return <ResQuantidadeComponent>diretiva.conteiner
                .createComponent(this.resolver.resolveComponentFactory(ResQuantidadeComponent)).instance;
        } else if (elemento.value instanceof DvText) {
            return <ResTextoComponent>diretiva.conteiner
                .createComponent(this.resolver.resolveComponentFactory(ResTextoComponent)).instance;
        } else if (elemento.value instanceof DvBoolean) {
            return <ResBooleanoComponent>diretiva.conteiner
                .createComponent(this.resolver.resolveComponentFactory(ResBooleanoComponent)).instance;
        } else if (elemento.value instanceof DvDate || elemento.value instanceof DvDateTime) {
            return <ResDataComponent>diretiva.conteiner
                .createComponent(this.resolver.resolveComponentFactory(ResDataComponent)).instance;
        } else if (elemento.value instanceof DvProportion)  {
            return <ResProporcaoComponent>diretiva.conteiner
                .createComponent(this.resolver.resolveComponentFactory(ResProporcaoComponent)).instance;
        } else if (elemento.value instanceof DvUri)  {
            return <ResURIComponent>diretiva.conteiner
                .createComponent(this.resolver.resolveComponentFactory(ResURIComponent)).instance;
        } else if (elemento.value instanceof DvMultimedia)  {
            return <ResMultiMidiaComponent>diretiva.conteiner
                .createComponent(this.resolver.resolveComponentFactory(ResMultiMidiaComponent)).instance;
        } else if (elemento.value instanceof DvCount)  {
            return <ResContagemComponent>diretiva.conteiner
                .createComponent(this.resolver.resolveComponentFactory(ResContagemComponent)).instance;
        } else if (elemento.value instanceof DvInterval)  {
            if (elemento.value.upper instanceof DvQuantity && elemento.value.lower instanceof DvQuantity) {
                return <ResIntervaloQuantidadeComponent>diretiva.conteiner
                    .createComponent(this.resolver.resolveComponentFactory(ResIntervaloQuantidadeComponent)).instance;
            }
        } else {
            return null;
        }
    }
}
