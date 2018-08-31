import { BaseComponent } from 'app/componente/base.component';
import { Component, Input, Output } from '@angular/core';
import { UatModalComponent } from 'app/compartilhado/modal-uat-component/uat-modal/uat-modal.component';
import { BloqueioService } from 'app/servico/bloqueio.service';

@Component({
  selector: 'app-uat-grid',
  templateUrl: './uat-grid.component.html',
  styleUrls: ['./uat-grid.component.scss'],
})
export class UatGridComponent extends BaseComponent {

  @Input() @Output()
  list: any[];

  @Input()
  uatModalComponent: UatModalComponent;

  @Input()
  modoConsulta: boolean;

  constructor(bloqueio: BloqueioService) {
    super(bloqueio);
  }

  removeUat(item: any) {
    const index: number = this.list.indexOf(item);
    if (index !== -1) {
      this.list.splice(index, 1);
    }
  }

}
