import { Output } from '@angular/core';
import { Component, OnInit, Input } from '@angular/core';
import { UnidadeModalComponent } from '../unidade-modal/unidade-modal.component';

@Component({
    selector: 'app-unidade-grid',
    templateUrl: './unidade-grid.component.html',
})
export class UnidadeGridComponent implements OnInit {

    @Input() @Output()
    list: any[];

    @Input()
    modal: UnidadeModalComponent;

    @Input()
    modoConsulta: boolean;

    constructor() { }

    selecionar(item: any, index: any) {
        this.modal.selecionarTel(item, index);
    }

    removeUnidade(item: any) {
        const index: number = this.list.indexOf(item);
        if (index !== -1) {
            this.list.splice(index, 1);
        }
    }

    ngOnInit() {
    }

}
