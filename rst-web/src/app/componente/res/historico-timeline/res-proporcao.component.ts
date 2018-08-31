import { ResElemento } from './res-elemento.component';
import { OperationalTemplate, Element, DvProportion } from '@ezvida/adl-core';
import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'app-dv-quantity',
    template: `
        <div class='row '>
            <div class='col-lg-12'>
                <span><label class='mat-caption nobottom'> {{ titulo.toUpperCase() }} </label>
                <label class='mat-body-2'>{{ valor }}</label>
               <label class='mat-body-1'>%</label></span>
            </div>
        </div>
    `,
})
export class ResProporcaoComponent implements OnInit, ResElemento {

    titulo: string;
    valor: number;

    constructor() {
    }

    ngOnInit() {
    }

    processar(arquetipo: OperationalTemplate, dvProportion: Element) {
        this.valor = (<DvProportion>dvProportion.value).numerator;
        if (this.valor == null) {
            throw new Error(`nenhum valor encontrado para o path ${dvProportion}`);
        }
        this.titulo = dvProportion.name.value;
        if (this.titulo == null) {
            throw new Error(`Titulo nao encontradado para path ${dvProportion} no arquetipo ${arquetipo}`);
        }
    }

}
