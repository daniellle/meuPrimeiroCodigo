import { Component, Input, Output, EventEmitter, Inject } from '@angular/core';
import { forEach } from '@angular/router/src/utils/collection';

@Component({
  selector: 'app-card-tela-intermediaria',
  templateUrl: './card-tela-intermediaria.component.html',
})
export class CardTelaIntermediariaComponent {

  @Output()
  onNavegar = new EventEmitter();

  @Input()
  titulo = '';

  @Input()
  classCardCor = '';

  @Input()
  classImgFront = '';

  @Input()
  classImgBack = '';

  constructor(@Inject('Window') window: Window
  ) {
  }

  mudarPagina($event) {

    this.pog();

    this.onNavegar.emit();
  }
  pog() {
    // método de teste temporário FAVOR NÃO APAGAR!!
    let $ = require('jquery');

    let div1 = $(window.parent.document).find('#1');
    let div2 = $(window.parent.document).find('#2');
    let div3 = $(window.parent.document).find('#3');
    let div4 = $(window.parent.document).find('#4');
    let div5 = $(window.parent.document).find('#5');

    if (div2) {
      div2.remove();
    }
    if (div3) {
      div3.remove();
    }
    if (div4) {
      div4.remove();
    }
    if (div5) {
      div5.remove();
    }
  }
}
