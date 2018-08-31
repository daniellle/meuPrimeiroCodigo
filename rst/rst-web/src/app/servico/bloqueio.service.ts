import {EventEmitter, Injectable} from '@angular/core';

@Injectable()
export class BloqueioService {

  public evento: EventEmitter<boolean>;

  constructor() {
    this.evento = new EventEmitter();
  }

  public bloquear() {
    this.evento.emit(false);
  }

  public desbloquear() {
    this.evento.emit(true);
  }

}
