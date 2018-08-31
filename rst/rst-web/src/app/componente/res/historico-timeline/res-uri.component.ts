import { ResElemento } from './res-elemento.component';
import { OperationalTemplate, Element, DvUri } from '@ezvida/adl-core';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
    selector: 'app-dv-text',
    template: `
        <div class='row'>
            <div class='col-lg-12'>
               <span> <label class='mat-caption nobottom'> {{ titulo }}: </label>
              <a href="{{uri}}"> {{ uri }} </a>
               </span>
             </div>
       </div>
    `,
    encapsulation: ViewEncapsulation.None,
})
export class ResURIComponent implements OnInit, ResElemento {

    titulo: string;
    uri: string;

    constructor() {
    }

    ngOnInit() {
    }

    processar(arquetipo: OperationalTemplate, dvUri: Element) {
        this.uri = (<DvUri>dvUri.value).value;
        if (this.uri == null) {
            throw new Error(`nenhum texto encontrado para o path ${dvUri}`);
        }
        this.titulo = dvUri.name.value;
        if (this.titulo == null) {
            throw new Error(`Titulo não encontrado para o path ${dvUri} no arquetipo ${arquetipo}`);
        }
    }

}
