import { ResElemento } from './res-elemento.component';
import { OperationalTemplate, Element, DvText } from '@ezvida/adl-core';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
    selector: 'app-dv-text',
    template: `
        <div class='row' (click)='$event.stopPropagation()'>
            <div class='col-lg-12'>
               <span> <label class='mat-caption nobottom'> {{ titulo }}: </label>
               <label> {{ texto }} </label>
               </span>
             </div>
       </div>
    `,
    encapsulation: ViewEncapsulation.None,
})
export class ResTextoComponent implements OnInit, ResElemento {

    titulo: string;
    texto: string;

    constructor() {
    }

    ngOnInit() {
    }

    processar(arquetipo: OperationalTemplate, dvText: Element) {
        this.texto = (<DvText>dvText.value).value as string;
        if (this.texto == null) {
            throw new Error(`nenhum texto encontrado para o path ${dvText}`);
        }
        this.titulo = dvText.name.value;
        if (this.titulo == null) {
            throw new Error(`Titulo não encontrado para o path ${dvText} no arquetipo ${arquetipo}`);
        }
    }

}
