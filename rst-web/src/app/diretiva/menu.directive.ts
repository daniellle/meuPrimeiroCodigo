import {Directive, ElementRef, HostListener} from '@angular/core';

@Directive({
  selector: '[appMenu]',
})
export class MenuDirective {

  constructor(private el: ElementRef) {

  }

  toggle() {
    this.el.nativeElement.classList.toggle('open');
  }

}

// tslint:disable-next-line:max-classes-per-file
@Directive({
  selector: '[appMenuAlternar]',
})
export class MenuAlternarDirective {

  constructor(private dropdown: MenuDirective) {

  }

  @HostListener('click', ['$event'])
  alternar($event: any) {
    $event.preventDefault();
    this.dropdown.toggle();
  }

}

export const MENU_DIRECTIVES = [MenuDirective, MenuAlternarDirective];
