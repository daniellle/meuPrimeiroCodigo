import { ResElemento } from './res-elemento.component';
import { OperationalTemplate, Element, DvInterval, DvQuantity } from '@ezvida/adl-core';
import { Component, Inject, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
    selector: 'app-dv-interval-quantity',
    template: `
        <div class='row'>
            <div class='col-lg-12'>
                <span>  <label class='mat-caption nobottom'> {{ titulo }}: </label>
               <label> {{ inferior?.magnitude}} {{inferior?.units  }} -
                        {{ superior?.magnitude }}
                         {{ superior?.units }} </label></span>
             </div>
       </div>
    `,
    encapsulation: ViewEncapsulation.None
})
export class ResIntervaloQuantidadeComponent implements OnInit, ResElemento {

    titulo: string;
    intervalo: DvInterval;
    inferior: DvQuantity;
    superior: DvQuantity;
    formato: string;

    constructor() {
    }

    ngOnInit() {
    }

    processar(arquetipo: OperationalTemplate, dvIntervalo: Element) {
        this.intervalo = (<DvInterval>dvIntervalo.value);

        if (!this.intervalo) {
            throw new Error(`Intervalo não encontrado para o path ${JSON.stringify(dvIntervalo)} no arquetipo ${arquetipo.archetypeId}`);
        }

        this.inferior = (<DvQuantity>this.intervalo.lower);
        this.superior = (<DvQuantity>this.intervalo.upper);
        this.titulo = dvIntervalo.name.value;
        if (this.titulo == null) {
            throw new Error(`Titulo não encontrado para o path ${JSON.stringify(dvIntervalo)} no arquetipo ${arquetipo}`);
        }
    }

}
