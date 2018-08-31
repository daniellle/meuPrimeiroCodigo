import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({ selector: '[appCabecalhoAlternar]' })
export class CabecalhoAlternarDirective {

    constructor(el: ElementRef) {

    }

    @HostListener('click', ['$event'])
    alternar() {
        document.querySelector('body').classList.toggle('sidebar-hidden');
    }

}

// tslint:disable-next-line:max-classes-per-file
@Directive({ selector: '[appCabecalhoExibir]' })
export class CabecalhoExibirDirective {

    constructor(el: ElementRef) {

    }

    // Check if element has class
    // tslint:disable-next-line:no-unused-variable
    private hasClass(target: any, elementClassName: string) {
        return new RegExp('(\\s|^)' + elementClassName + '(\\s|$)').test(target.className);
    }

    @HostListener('click')
    exibir() {
        document.querySelector('body').classList.toggle('sidebar-mobile-show');
    }

}

// tslint:disable-next-line:max-classes-per-file
@Directive({ selector: '[appCabecalhoMinimizar]' })
export class CabecalhoMinimizarDirective {

    constructor(el: ElementRef) {

    }

    @HostListener('click')
    minimizar() {
        document.querySelector('body').classList.toggle('sidebar-hidden');
    }

}

// tslint:disable-next-line:max-classes-per-file
@Directive({ selector: '[myHighlight]' })
export class HighlightDirective {

    constructor(private el: ElementRef) {
        el.nativeElement.style.color = '#fff';
        el.nativeElement.style.background = '#20a8d8';
    }

    @HostListener('mouseenter', ['$event']) onMouseEnter(event: any) {
        this.highlight('yellow', 'transparent !important');
    }

    @HostListener('mouseleave') onMouseLeave() {
        this.highlight('#fff', '#20a8d8');
    }

    private highlight(color: string, background: string) {
        this.el.nativeElement.style.color = color;
        this.el.nativeElement.style.background = background;
    }

}

export const CABECALHO_DIRECTIVES = [
    CabecalhoExibirDirective,
    CabecalhoAlternarDirective,
    CabecalhoMinimizarDirective,
    HighlightDirective,
];
