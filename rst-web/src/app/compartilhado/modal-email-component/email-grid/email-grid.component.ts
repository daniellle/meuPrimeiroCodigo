import { EmailModalComponent } from './../email-modal/email-modal.component';
import { Input, Output } from '@angular/core';
import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'app-email-grid',
    templateUrl: './email-grid.component.html',
    styleUrls: ['./email-grid.component.scss'],
})
export class EmailGridComponent implements OnInit {

    @Input() @Output()
    list: any[];

    @Input()
    modalEmail: EmailModalComponent;

    @Input()
    modoConsulta: boolean;

    constructor() { }

    selecionar(item: any, index: any) {
        this.modalEmail.selecionarEmail(item, index);
    }

    removeEmail(item: any) {
        const index: number = this.list.indexOf(item);
        if (index !== -1) {
            this.list.splice(index, 1);
        }
    }

    ngOnInit() {
    }
}
