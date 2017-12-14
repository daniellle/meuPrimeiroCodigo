import { Component, Input, Output, EventEmitter } from '@angular/core';

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

  constructor(
  ) {
  }

  mudarPagina() {
    this.onNavegar.emit();
  }
}
