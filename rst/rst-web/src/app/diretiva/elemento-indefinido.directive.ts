import {Directive, ViewContainerRef} from '@angular/core';
import {ElementoDirective} from './elemento.directive';

@Directive({
    selector: '[appElementoIndefinido]',
})
export class ElementoIndefinidoDirective extends ElementoDirective {

    constructor(public conteiner: ViewContainerRef) {
        super(conteiner);
    }

}
