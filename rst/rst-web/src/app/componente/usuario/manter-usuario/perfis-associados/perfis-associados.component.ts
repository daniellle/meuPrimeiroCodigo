import {Component, EventEmitter, Input, Output} from '@angular/core';

import { Usuario } from 'app/modelo/usuario.model';
import {Sistema} from "../../../../modelo/sistema.model";
import {PerfilSistema} from "../../../../modelo/ṕerfil-sistemas";
import { UsuarioPerfilSistema } from 'app/modelo/usuario-perfil-sistema.model';
import { PerfilEnum } from 'app/modelo/enum/enum-perfil';

@Component({
  selector: 'app-perfis-associados',
  templateUrl: './perfis-associados.component.html'
})
export class PerfisAssociadosComponent {

  @Input() usuario: Usuario;
  @Output() sistemaEditar: EventEmitter<PerfilSistema> = new EventEmitter();


  public getSistemaPerfil(): any {
    console.log(this.usuario);
    const sistemas = {};
    this.usuario.perfisSistema.forEach((ps) => {
        const id = ps.sistema.id;

        if (!sistemas[id]) {
            sistemas[id] = { id, sistema: ps.sistema.nome, perfil: '' };
        }
        sistemas[id].perfil = ps.perfil.nome.toString().concat('; ').concat(sistemas[id].perfil);
    });

    return Object.keys(sistemas).map((key) => {
        sistemas[key].perfil = sistemas[key].perfil.substr(0, sistemas[key].perfil.length - 2);
        return sistemas[key];
    });
}

excluirAssociacaoPerfil(idSistema: number): void {
    const sistemaPerfis: UsuarioPerfilSistema[] = this.usuario.perfisSistema.filter((ps) => ps.sistema.id
        === Number(idSistema));
    sistemaPerfis.forEach((element) => {
        this.usuario.perfisSistema;
        const i = this.usuario.perfisSistema.indexOf(element, 0);
        if (element.perfil.codigo !== PerfilEnum.TRA) {
            if (i > -1) {
                this.usuario.perfisSistema.splice(i, 1);
            }
        }
    });
}


  editarPerfil(perfilSistema: PerfilSistema){
      if(perfilSistema) {
          this.sistemaEditar.emit(perfilSistema);

      }

  }

}
