import {Directive, ViewContainerRef} from '@angular/core';

@Directive({
  selector: '[appComposicao]'
})
export class ComposicaoDirective {

  constructor(public conteiner: ViewContainerRef) {

  }

}
