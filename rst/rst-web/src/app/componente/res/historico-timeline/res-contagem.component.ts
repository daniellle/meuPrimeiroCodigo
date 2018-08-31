import { ResElemento } from './res-elemento.component';
import { OperationalTemplate, Element, DvCount } from '@ezvida/adl-core';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';

@Component({
    selector: 'app-dv-text',
    template: `
        <div class='row' (click)='$event.stopPropagation()'>
            <div class='col-lg-12'>
               <span> <label class='mat-caption nobottom'> {{ titulo }}: </label>
               <label> {{ contagem }} </label>
               </span>
             </div>
       </div>
    `,
    encapsulation: ViewEncapsulation.None,
})
export class ResContagemComponent implements OnInit, ResElemento {

    titulo: string;
    contagem: number;

    constructor() {
    }

    ngOnInit() {
    }

    processar(arquetipo: OperationalTemplate, dvContagem: Element) {
        if (!arquetipo || !dvContagem) {
            throw new Error(`nao é possível processar com elementos nulos: arquetipo ${arquetipo} path ${dvContagem}`);
        }
        this.contagem = (<DvCount>dvContagem.value).magnitude;
        if (this.contagem == null) {
            throw new Error(`nenhum texto encontrado para o path ${dvContagem}`);
        }
        this.titulo = dvContagem.name.value;
        if (this.titulo == null) {
            throw new Error(`Titulo não encontrado para o path ${JSON.stringify(dvContagem)} no arquetipo ${arquetipo.archetypeId}`);
        }
    }

}
