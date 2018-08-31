import {Directive, ElementRef, HostListener} from '@angular/core';

@Directive({
  selector: '[appShowDate]',
})
export class ShowDateDirective {

  constructor(private el: ElementRef) {

  }

  @HostListener('click', ['$event'])
  toggle() {
    this.el.nativeElement.querySelector('.btnpickerenabled').click();
  }

}


export const CALENDARIO_DIRECTIVES = [ShowDateDirective];
