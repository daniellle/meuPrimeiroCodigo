import { ResElemento } from './res-elemento.component';
import { OperationalTemplate, Element, DvDate, DvDateTime } from '@ezvida/adl-core';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
    selector: 'app-dv-data',
    template: `
        <div class='row'>
            <div class='col-lg-12'>
                <span>               <label class='mat-caption nobottom'> {{ titulo }}: </label>
               <label> {{ data | date:formato}} </label></span>
             </div>
       </div>
    `,
    encapsulation: ViewEncapsulation.None,
})
export class ResDataComponent implements OnInit, ResElemento {

    titulo: string;
    data: Date;
    formato: string;

    constructor() {
    }

    ngOnInit() {
    }

    processar(arquetipo: OperationalTemplate, dvDate: Element, showTime: boolean = false) {
        this.titulo = dvDate.name.value;
        if (this.titulo == null) {
            throw new Error(`Titulo não encontrado para o path ${dvDate} no arquetipo ${arquetipo}`);
        }

        if (dvDate.value instanceof DvDate || dvDate.value instanceof DvDateTime) {
            this.data = new Date(dvDate.value.value);
        }

        if (this.data == null || isNaN(this.data.getTime())) {
            throw new Error(`data inválida para o path ${JSON.stringify(dvDate)}`);
        }
        if (showTime) {
            this.formato = 'medium';
        } else {
            this.formato = 'longDate';
        }
    }

}
