import {Component, EventEmitter, Input, Output} from '@angular/core';

import { Usuario } from 'app/modelo/usuario.model';
import {Sistema} from "../../../../modelo/sistema.model";
import {PerfilSistema} from "../../../../modelo/ṕerfil-sistemas";
import {UsuarioPerfilSistema} from "../../../../modelo/usuario-perfil-sistema.model";

@Component({
  selector: 'app-perfis-associados',
  templateUrl: './perfis-associados.component.html'
})
export class PerfisAssociadosComponent {

  @Input() usuario: Usuario;
  @Output() sistemaEditar: EventEmitter<PerfilSistema> = new EventEmitter();


  getSistemaPerfil(): any {
    return [];
  }


  editarPerfil(perfilSistema: PerfilSistema){
      if(perfilSistema) {
          this.sistemaEditar.emit(perfilSistema);

      }

  }

}
