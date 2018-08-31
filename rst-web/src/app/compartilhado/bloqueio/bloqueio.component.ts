import {AfterViewInit, Component, Input, OnDestroy, OnInit} from '@angular/core';
import {BloqueioService} from '../../servico/bloqueio.service';

@Component({
  selector: 'app-bloqueio',
  templateUrl: './bloqueio.component.html',
  styleUrls: ['./bloqueio.component.scss'],
})
export class BloqueioComponent implements AfterViewInit, OnInit, OnDestroy {

  @Input() processado: boolean;

  constructor(private servico: BloqueioService) {
    this.processado = true;
  }

  ngAfterViewInit(): void {
  }

  ngOnInit(): void {
    this.servico.evento.subscribe((evento) => this.setBloqueado(evento));
  }

  ngOnDestroy(): void {
  }

  private setBloqueado(processado: boolean) {
    this.processado = processado;
  }

}
