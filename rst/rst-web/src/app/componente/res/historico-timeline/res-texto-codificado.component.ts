import { ResElemento } from './res-elemento.component';
import { OperationalTemplate, TerminologyQuery, DictionaryItem, Element, DvCodedText, Locatable } from '@ezvida/adl-core';
import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'app-dv-code-text',
    template: `
       <div class='row'>
            <div class='col-lg-12'>
               <span><label class='mat-caption nobottom'> {{ titulo }}: </label>
               <label > {{ texto.text }} </label></span>
             </div>
       </div>
    `,
})
export class ResTextoCodificadoComponent implements OnInit, ResElemento {

    titulo: string;
    texto: DictionaryItem;

    constructor() {
    }

    ngOnInit() {
    }

    processar(arquetipo: OperationalTemplate, dvText: Element) {
        this.titulo = dvText.name.value;

        if (typeof (<DvCodedText>dvText.value).value === 'string') {
            const root = this.getRoot(dvText);

            this.texto = TerminologyQuery.findTermDefinition(arquetipo, (<DvCodedText>dvText.value).value, {
                overlayId: root.archetypeNodeId,
            });
        } else {
            this.texto = {
                text: (dvText.value as any).code,
                description: (dvText.value as any).description,
                language: '',
            };
        }

        if (this.texto == null) {
            throw new Error(`nao foi possivel achar o texto para ${dvText}`);
        }
    }

    private getRoot(element: Locatable): Locatable {
        if (element.archetypeDetails !== null) {
            return element;
        } else {
            return this.getRoot(<Locatable>element.parent);
        }
    }

}
