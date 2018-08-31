import {Directive, ElementRef, HostListener} from '@angular/core';

@Directive({selector: '[appFormularioCamposErro]'})
export class FormularioCampoErroDirective {

  private campo: ElementRef;

  constructor(campo: ElementRef) {
    this.campo = campo;
  }

  @HostListener('focus', ['$event']) focus(evento: FocusEvent) {
    this.campo.nativeElement.classList.remove('form-control-danger');
    this.campo.nativeElement.parentElement.classList.remove('has-danger');
  }

}

export const FORMULARIO_DIRECTIVES = [
  FormularioCampoErroDirective,
];
