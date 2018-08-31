import { Component, Input, Output, OnInit } from '@angular/core';
import { TelefoneModalComponent } from 'app/compartilhado/modal-telefone-component/telefone-modal.component';
import { TipoTelefone } from 'app/modelo/enum/enum-tipo-telefone.model';

@Component({
    selector: 'telefone-grid',
    templateUrl: './telefone-grid.component.html',
  })

  export class TelefoneGridComponent implements OnInit {
    @Input() @Output()
    list: any[];

    @Input()
    modal: TelefoneModalComponent;

    @Input()
    modoConsulta: boolean;

    public tiposTelefone = TipoTelefone;
    public keysTiposTelefone: string[];

    constructor() {

      this.keysTiposTelefone = Object.keys(this.tiposTelefone);
    }

    selecionar(item: any, index: any) {
      if (!this.modoConsulta) {
        this.modal.selecionarTel(item, index, this.modoConsulta);
      }
    }

    removeTel(item: any) {
      const index: number = this.list.indexOf(item);
      if (index !== -1) {
        this.list.splice(index, 1);
      }
    }

    ngOnInit() {
    }
  }
