import { Component, Input, Output, EventEmitter } from '@angular/core';

import { Usuario } from 'app/modelo/usuario.model';
import { UsuarioPerfilSistema } from 'app/modelo/usuario-perfil-sistema.model';
import { PerfilEnum } from 'app/modelo/enum/enum-perfil';
import { PerfilSistema } from 'app/modelo/ṕerfil-sistemas';

@Component({
  selector: 'app-perfis-associados',
  templateUrl: './perfis-associados.component.html'
})
export class PerfisAssociadosComponent {

  @Input() usuario: Usuario;
  @Output() sistemaEditar: EventEmitter<PerfilSistema> = new EventEmitter();

  public getSistemaPerfil(): any {
    const sistemas = {};
    this.usuario.perfisSistema.forEach((ps) => {
        let id = ps.sistema.id;
        if (ps.sistema.codigo === 'portal' ||
            ps.sistema.codigo === 'cadastro' ||
            ps.sistema.codigo === 'dw' ||
            ps.sistema.codigo === 'indigev') {
                id = 1;
                if (!sistemas[id]) {
                    sistemas[id] = { id, sistema: 'Cadastro', perfil: '' };
                }
                if (sistemas[id].perfil.search(ps.perfil.nome.toString()) === -1) {
                    sistemas[id].perfil = ps.perfil.nome.toString().concat('; ').concat(sistemas[id].perfil);
                }
        } else {
                if (!sistemas[id]) {
                    sistemas[id] = { id, sistema: ps.sistema.nome, perfil: '' };
                }
                sistemas[id].perfil = ps.perfil.nome.toString().concat('; ').concat(sistemas[id].perfil);
            }
    });

    return Object.keys(sistemas).map((key) => {
        sistemas[key].perfil = sistemas[key].perfil.substr(0, sistemas[key].perfil.length - 2);
        return sistemas[key];
    });
}

excluirAssociacaoPerfil(idSistema): void {
    let sistemaPerfis: UsuarioPerfilSistema[] = this.usuario.perfisSistema.filter((ps) => ps.sistema.id
        === Number(idSistema));
    if (idSistema === 1) {
        sistemaPerfis = this.usuario.perfisSistema.filter((ps) => ps.sistema.codigo === 'portal' ||
        ps.sistema.codigo === 'cadastro' ||
        ps.sistema.codigo === 'dw' ||
        ps.sistema.codigo === 'indigev');
    }
    sistemaPerfis.forEach((element) => {
        const i = this.usuario.perfisSistema.indexOf(element, 0);
        if (element.perfil.codigo !== PerfilEnum.TRA) {
            if (i > -1) {
                this.usuario.perfisSistema.splice(i, 1);
            }
        }
    });
}

    editarPerfil(perfilSistema: PerfilSistema) {
        if (perfilSistema) {
            this.sistemaEditar.emit(perfilSistema);
        }

    }

    isNotOnlyTrabalhador(perfis: any) {
        perfis = perfis.split('; ');
        if (perfis.length === 1 && perfis[0] === 'Trabalhador'){
            return false;
        }
        return true;
    }

    checkList(){
        console.log('oi');
        return this.usuario.perfisSistema === undefined ||
        this.usuario.perfisSistema === null ||
        this.usuario.perfisSistema === [] ||
        !this.usuario.perfisSistema;
    }


}
