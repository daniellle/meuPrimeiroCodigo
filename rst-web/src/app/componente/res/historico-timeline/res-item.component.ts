import {ElementoDirective} from 'app/diretiva/elemento.directive';
import {ElementoIndefinidoDirective} from 'app/diretiva/elemento-indefinido.directive';
import {InformacoesTemplate} from './timeline-item.component';
import {ResItemRowComponent} from './res-item-row.component';
import {ElementoProtocolosDirective} from 'app/diretiva/elemento-protocolos.directive';
import {ElementoEstadosDirective} from 'app/diretiva/elemento-estados.directive';
import {ElementoDadosDirective} from 'app/diretiva/elemento-dados.directive';
import {Element, OperationalTemplate,} from '@ezvida/adl-core';
import {Component, ComponentFactoryResolver, ViewChild,} from '@angular/core';

@Component({
    selector: 'app-res',
    templateUrl: './res-item.component.html',
    styleUrls: ['./res-item.component.scss'],
})
export class ResItemComponent {
    @ViewChild(ElementoDadosDirective) diretivaDados: ElementoDadosDirective;
    @ViewChild(ElementoIndefinidoDirective) diretivaOutros: ElementoEstadosDirective;
    @ViewChild(ElementoEstadosDirective) diretivaEstados: ElementoEstadosDirective;
    @ViewChild(ElementoProtocolosDirective) diretivaProtocolos: ElementoProtocolosDirective;

    pares: any[] = [];
    titulo: string;
    subTitulo: string;
    expandido = false;

    informacoesTemplate: InformacoesTemplate;
    opt: OperationalTemplate;
    itemExterno: boolean;

    constructor(private resolver: ComponentFactoryResolver) {}

    processar(informacoesTemplate: InformacoesTemplate, opt: OperationalTemplate, mostrarDivisoes: boolean = true): boolean {
        this.informacoesTemplate = informacoesTemplate;
        this.itemExterno = mostrarDivisoes;
        this.opt = opt;
        this.processarTitulo(opt, informacoesTemplate);
        informacoesTemplate.informacoesInternas.forEach((info) => {
            this.processarPaths(info.pathsComValor, info.informacoesInternas, this.diretivaDados, info);
        });

        return this.diretivaDados.conteiner.length > 0 ||
            this.diretivaEstados.conteiner.length > 0 ||
            this.diretivaProtocolos.conteiner.length > 0 ||
            this.diretivaOutros.conteiner.length > 0;
    }

    private processarPaths(valores: Element[], internos: InformacoesTemplate[], diretiva: ElementoDirective, info: InformacoesTemplate) {
        const pares = this.parearDados(valores);
        this.processarTituloPressaoSanguinea(info, pares);
        pares.forEach((path) => this.adicionaDadoNaDiretiva(diretiva, path));
        internos.forEach((info) => this.adicionaItemNaDiretiva(diretiva, info));
    }

    private processarTituloPressaoSanguinea(template: InformacoesTemplate, pares: any[][]) {
        if(template.arquetipoReferencia === 'openEHR-EHR-OBSERVATION.ovl-ficha_clinica_ocupacional-blood_pressure-001.v1.0.0') {
            pares.forEach(par => {
                par.forEach(element => {
                    if(element.name.value === 'Sistólica' || element.name.value === 'Diastólica') {
                        element.name.value = template.titulo + ' ' + element.name.value;
                    }
                });
            });
        }
    }

    private processarTitulo(opt: OperationalTemplate, informacoesTemplate: InformacoesTemplate) {
        this.titulo = informacoesTemplate.titulo;
    }

    private parearDados(vetor: any[]): any[][] {
        return vetor.map((value: Element, index: number, array: Element[]) => {
            if (index % 2 === 0) {
                const result = [value];
                if (array[index + 1]) {
                    result.push(array[index + 1]);
                }
                return result;
            }
        }).filter((elem) => elem != null);

    }

    private adicionaDadoNaDiretiva(diretiva: ElementoDirective, path: Element[]) {
        const component = diretiva.conteiner.createComponent(this.resolver.resolveComponentFactory(ResItemRowComponent)).instance;
        const vazio = component.processar(path, this.informacoesTemplate.arquetipoReferencia, this.opt);

        if (vazio) {
            diretiva.conteiner.remove();
        }
    }

    private adicionaItemNaDiretiva(diretiva: ElementoDirective, info: InformacoesTemplate) {
        try {
            const component = diretiva.conteiner.createComponent(this.resolver.resolveComponentFactory(ResItemComponent)).instance;
            if (!component.processar(info, this.opt, false)) {
                diretiva.conteiner.remove();
            }
        } catch (error) {
            diretiva.conteiner.remove();
        }
    }
}
