import { ResElemento } from './res-elemento.component';
import { OperationalTemplate, Element, DvQuantity } from '@ezvida/adl-core';
import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'app-dv-quantity',
    template: `
        <div class='row '>
            <div class='col-lg-12'>
            <span>
                <label class='mat-caption nobottom'> {{ titulo.toUpperCase() }}: </label>
                <label class='mat-body-2'> {{ valor }} </label>
               <label class='mat-body-1'> {{ unidade }} </label>
               </span>
            </div>
        </div>
    `,
})
export class ResQuantidadeComponent implements ResElemento {

    titulo: string;
    unidade: string;
    valor: string;

    constructor() {
    }

    processar(arquetipo: OperationalTemplate, mQuantity: Element) {
        const quantidade = <DvQuantity>mQuantity.value;
        this.unidade = quantidade.units;
        if (quantidade.magnitude == null) {
            throw new Error(`nenhum valor encontrado para o path ${mQuantity}`);
        }
        this.titulo = mQuantity.name.value;
        if (this.titulo == null) {
            throw new Error(`Titulo nao encontradado para path ${mQuantity} no arquetipo ${arquetipo}`);
        }
        this.valor = quantidade.magnitude.toString();
    }

}
