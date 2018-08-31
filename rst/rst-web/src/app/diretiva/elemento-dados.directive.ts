import {Directive, ViewContainerRef} from '@angular/core';
import {ElementoDirective} from './elemento.directive';

@Directive({
    selector: '[appElementoDados]',
})
export class ElementoDadosDirective extends ElementoDirective {

    constructor(public conteiner: ViewContainerRef) {
        super(conteiner);
    }

}
