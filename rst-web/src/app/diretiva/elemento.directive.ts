import {ViewContainerRef} from '@angular/core';

export abstract class ElementoDirective {

    elementos = 0;

    constructor(public conteiner: ViewContainerRef) {

    }

}
