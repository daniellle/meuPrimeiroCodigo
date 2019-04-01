import { ProximaDoseVacina } from './../../../modelo/proximaDoseVacina.model';
import { Component, OnInit, Input, Output } from '@angular/core';
import { UnidadeModalComponent } from 'app/compartilhado/modal-unidade-component/unidade-modal/unidade-modal.component';
import { VacinaModalComponent } from '../vacina-modal/vacina-modal.component';
import { parse } from 'querystring';

@Component({
  selector: 'app-vacina-grid',
  templateUrl: './vacina-grid.component.html',
  styleUrls: ['./vacina-grid.component.scss']
})
export class VacinaGridComponent implements OnInit {


  @Input() @Output()
  list: any[];

  @Input()
  modal: VacinaModalComponent;

  @Input()
  nomeDaVacina: String;

  constructor() {
  }

  remover(item: any) {
    item.remover = true;
  }

  removido(item: any): Boolean {
    return item.remover === false || item.remover == null || item.remover === undefined;
  }

  ngOnInit() {
  }


}
