import { ResElemento } from './res-elemento.component';
import { OperationalTemplate, Element, DvBoolean } from '@ezvida/adl-core';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
    selector: 'app-dv-boolean',
    template: `
        <div class='row'>
            <div class='col-lg-12'>
                <span>               <label class='mat-caption nobottom'> {{ titulo }}: </label>
               <label> {{ escolha }} </label></span>
             </div>
       </div>
    `,
    encapsulation: ViewEncapsulation.None,
})
export class ResBooleanoComponent implements OnInit, ResElemento {

    titulo: string;
    escolha = '--';

    constructor() { }

    ngOnInit() {
    }

    processar(arquetipo: OperationalTemplate, dvBoolean: Element) {
        if (!arquetipo || !dvBoolean) {
            throw new Error(`não é possível processar dados nulos`);
        }
        this.escolha = (<DvBoolean>dvBoolean.value).value ? 'Sim' : (dvBoolean.value != null ? 'Não' : '--');
        this.titulo = dvBoolean.name.value;
        if (this.titulo == null) {
            throw new Error(`Titulo não encontrado para o path ${JSON.stringify(dvBoolean)} no arquetipo ${arquetipo.archetypeId}`);
        }
    }

}
