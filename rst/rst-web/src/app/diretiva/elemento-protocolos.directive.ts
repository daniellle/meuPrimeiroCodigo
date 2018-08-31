import {Directive, ViewContainerRef} from '@angular/core';
import {ElementoDirective} from './elemento.directive';

@Directive({
  selector: '[appElementoProtocolos]',
})
export class ElementoProtocolosDirective extends ElementoDirective {

  constructor(public conteiner: ViewContainerRef) {
    super(conteiner);
  }

}
