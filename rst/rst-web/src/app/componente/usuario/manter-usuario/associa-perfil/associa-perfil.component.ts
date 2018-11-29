import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';

import { Observable } from 'rxjs';

import { Sistema } from 'app/modelo/sistema.model';
import { UsuarioPerfilSistema } from 'app/modelo/usuario-perfil-sistema.model';
import { SistemaService } from 'app/servico/sistema.service';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { SistemaPerfil } from 'app/modelo/sistema-perfil.model';
import { Usuario } from 'app/modelo/usuario.model';

@Component({
  selector: 'app-associa-perfil',
  templateUrl: './associa-perfil.component.html'
})
export class AssociaPerfilComponent implements OnInit {

  @Input() modoConsulta: boolean;
  @Input() perfisSistemas: UsuarioPerfilSistema[];
  @Input() usuario: Usuario;
  
  @Output() associaPerfilEvent = new EventEmitter<any>();

  sistemaSelecionado: Sistema;
  sistemas$: Observable<Sistema[]>;
  perfisDoSistema: SistemaPerfil[] = [];

  constructor(private sistemaService: SistemaService) {}

  ngOnInit() {
    console.log(this.usuario)
    this.sistemas$ = this.sistemaService.buscarSistemasPermitidos(Seguranca.getUsuario());
  }

  changeSistema(sistema: Sistema) {
    if(sistema) {
      this.perfisDoSistema = sistema.sistemaPerfis;
      this.sistemaSelecionado = sistema;
    } else {
      this.perfisDoSistema = [];
      this.sistemaSelecionado = undefined;
    }
  }

  atualizaPerfilSistema(event: any, sistemaPerfil: any) {
    console.log(event.checked);
    console.log(sistemaPerfil);
  }

  associaPerfil() {
    this.associaPerfilEvent.emit();
  }

}
