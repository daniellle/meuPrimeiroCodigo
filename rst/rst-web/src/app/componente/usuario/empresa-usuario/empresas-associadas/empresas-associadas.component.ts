import {Paginacao} from 'app/modelo/paginacao.model';
import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';

@Component({
    selector: 'app-empresas-associadas',
    templateUrl: './empresas-associadas.component.html',
    styleUrls: ['./empresas-associadas.component.scss'],
})
export class EmpresasAssociadasComponent {

    @Input() listaEmpresas = [];
    @Input() titulo = '';
    @Input() paginacao = new Paginacao();
    @Output() onPageChanged = new EventEmitter<any>();
    @Output() onRemove = new EventEmitter<any>();
    @Input() perfil: any;
    @Input() temPaginacao: boolean;
    @Input() modoConsulta: boolean;

    constructor(
    ) {
    }

    removerEmpresa(item: any) {
        this.onRemove.emit(item);
    }

    pageChanged(event: any): void {
        const page = event.page;
        this.onPageChanged.emit({page, perfil: this.perfil});
    }

    isEmpty(list: any[]): boolean {
        return (!list || (list && list.length == 0));
    }
}
