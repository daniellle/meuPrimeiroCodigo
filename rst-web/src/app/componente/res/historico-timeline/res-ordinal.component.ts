import { ResElemento } from './res-elemento.component';
import { OperationalTemplate, Element, DvOrdinal } from '@ezvida/adl-core';
import { Component } from '@angular/core';

@Component({
    selector: 'app-dv-quantity',
    template: `
        <div class='row '>
            <div class='col-lg-12'>
            <span>
                <label class='mat-caption nobottom'> {{ titulo.toUpperCase() }}: </label>
                <label class='mat-body-2'> {{ valor }} </label>
               </span>
            </div>
        </div>
    `,
})
export class ResOrdinalComponent implements ResElemento {

    titulo: string;
    valor: string;

    constructor() {
    }

    processar(arquetipo: OperationalTemplate, mQuantity: Element) {
        const quantidade = <DvOrdinal>mQuantity.value;
        this.titulo = mQuantity.name.value;
        if (this.titulo == null) {
            throw new Error(`Titulo nao encontradado para path ${mQuantity} no arquetipo ${arquetipo}`);
        }
        this.valor = quantidade.value.toString();
    }

}
