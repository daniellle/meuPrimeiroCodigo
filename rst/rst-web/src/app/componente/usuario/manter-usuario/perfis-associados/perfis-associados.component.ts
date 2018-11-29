import { Component, Input } from '@angular/core';

import { Usuario } from 'app/modelo/usuario.model';

@Component({
  selector: 'app-perfis-associados',
  templateUrl: './perfis-associados.component.html'
})
export class PerfisAssociadosComponent {

  @Input() usuario: Usuario;

  getSistemaPerfil(): any {
    return [];
  }

}
