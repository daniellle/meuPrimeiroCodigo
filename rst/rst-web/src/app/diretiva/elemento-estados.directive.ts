import {Directive, ViewContainerRef} from '@angular/core';
import {ElementoDirective} from './elemento.directive';

@Directive({
  selector: '[appElementoEstados]',
})
export class ElementoEstadosDirective extends ElementoDirective {

  constructor(public conteiner: ViewContainerRef) {
    super(conteiner);
  }

}
