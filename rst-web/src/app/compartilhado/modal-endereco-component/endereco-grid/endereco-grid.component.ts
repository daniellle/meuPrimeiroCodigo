
import { Input, Output } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { EnderecoModalComponent } from '../endereco-modal/endereco-modal.component';

@Component({
    selector: 'app-endereco-grid',
    templateUrl: './endereco-grid.component.html',
    styleUrls: ['./endereco-grid.component.scss'],
})
export class EnderecoGridComponent implements OnInit {

    @Input() @Output()
    list: any[];

    @Input()
    enderecoModalComponent: EnderecoModalComponent;

    @Input()
    modoConsulta: boolean;

    constructor() { }

    selecionar(item: any, index: any) {
        this.enderecoModalComponent.selecionarEndereco(item, index);
    }

    removeEndereco(item: any) {
        const index: number = this.list.indexOf(item);
        if (index !== -1) {
            this.list.splice(index, 1);
        }
    }

    ngOnInit() {
    }

}
