import { SegmentoModalComponent } from 'app/compartilhado/modal-segmento-component/segmento-modal/segmento-modal.component';
import { Segmento } from './../../../modelo/segmento.model';
import { BaseComponent } from 'app/componente/base.component';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { Component, OnInit, Input, Output } from '@angular/core';

@Component({
    selector: 'app-segmento-grid',
    templateUrl: './segmento-grid.component.html',
    styleUrls: ['./segmento-grid.component.scss'],
})
export class SegmentoGridComponent extends BaseComponent implements OnInit {

    @Input() @Output()
    segmento: Segmento;

    @Input() @Output()
    segmentoModalComponent: SegmentoModalComponent;

    @Input()
    modoConsulta: boolean;

    constructor(bloqueio: BloqueioService) {
        super(bloqueio);
    }

    selecionar() {
        if (this.segmento.id) {
            this.segmentoModalComponent.editar(this.segmento);
        }
    }

    removeSegmento(segmento: Segmento) {
        segmento = new Segmento();
        this.segmento = new Segmento();
        this.segmentoModalComponent.segmento = new Segmento();
        this.segmentoModalComponent.model = new Segmento();

        this.segmento.id = undefined;
        this.segmento.codigo = undefined;
        this.segmento.descricao = undefined;
        this.segmento.dataCriacao = undefined;
        this.segmento.dataAlteracao = undefined;
        this.segmento.dataExclusao = undefined;

    }

    ngOnInit() {
    }

}
