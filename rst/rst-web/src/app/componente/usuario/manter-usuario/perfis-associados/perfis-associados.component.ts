import {Component, Input, Output, EventEmitter, Optional} from '@angular/core';

import { Usuario } from 'app/modelo/usuario.model';
import { UsuarioPerfilSistema } from 'app/modelo/usuario-perfil-sistema.model';
import { PerfilEnum } from 'app/modelo/enum/enum-perfil';
import { PerfilSistema } from 'app/modelo/ṕerfil-sistemas';
import {Sistema} from "../../../../modelo/sistema.model";
import {ToastOptions, ToastyService} from "ng2-toasty";
import {AbstractControl} from "@angular/forms";

@Component({
  selector: 'app-perfis-associados',
  templateUrl: './perfis-associados.component.html'
})
export class PerfisAssociadosComponent {

  @Input() usuario: Usuario;
  @Output() sistemaEditar: EventEmitter<Sistema> = new EventEmitter();

  constructor(@Optional() protected dialogo?: ToastyService){

  }


  public getSistemaPerfil(): any {
    const sistemas = {};
    if(this.usuario.perfisSistema.length > 0) {
        this.usuario.perfisSistema.forEach((ps) => {
            let id = ps.sistema.id;
            if (ps.sistema.codigo === 'portal' ||
                ps.sistema.codigo === 'cadastro' ||
                ps.sistema.codigo === 'dw' ||
                ps.sistema.codigo === 'indigev') {
                id = 1;
                if (!sistemas[id]) {
                    sistemas[id] = {id, sistema: 'Cadastro', perfil: ''};
                }
                if (sistemas[id].perfil.search(ps.perfil.nome.toString()) === -1) {
                    sistemas[id].perfil = ps.perfil.nome.toString().concat('; ').concat(sistemas[id].perfil);
                }
            } else {
                if (!sistemas[id]) {
                    sistemas[id] = {id, sistema: ps.sistema.nome, perfil: ''};
                }
                sistemas[id].perfil = ps.perfil.nome.toString().concat('; ').concat(sistemas[id].perfil);
            }
        });

        return Object.keys(sistemas).map((key) => {
            sistemas[key].perfil = sistemas[key].perfil.substr(0, sistemas[key].perfil.length - 2);
            return sistemas[key];
        });
    }
}

excluirAssociacaoPerfil(idSistema): void {
    let sistemaPerfis: UsuarioPerfilSistema[] = this.usuario.perfisSistema.filter((ps) => ps.sistema.id
        === Number(idSistema));
    if (idSistema === 1) {
        sistemaPerfis = this.usuario.perfisSistema.filter((ps) => ps.sistema.codigo === 'portal' ||
        ps.sistema.codigo === 'cadastro' ||
        ps.sistema.codigo === 'dw' ||
        ps.sistema.codigo === 'indigev');
         if(!this.validarEpidemiologia()){
             return;
         }
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

    private validarEpidemiologia(): boolean {
        let temEpidemiologia: boolean;

        this.usuario.perfisSistema.forEach(perfilSistema =>{
            if(perfilSistema.sistema.codigo == "epidemiologia"){
                temEpidemiologia = true;
            }
        })

        if(temEpidemiologia){
            this.mensagemError("Não é possível apagar a associação com o sistema Cadastro enquanto houver associação com o sistema Epidemiologia");
            return false;
        }
        else{
            return true;
        }
    }


    private getMensagem(mensagem: string, controle?: AbstractControl): ToastOptions {

        const configuracoes: ToastOptions = {
            title: '',
            timeout: 5000,
            msg: mensagem,
            showClose: true,
            theme: 'bootstrap',
        };

        return configuracoes;

    }

    protected mensagemError(mensagem: string, controle?: AbstractControl) {
        if (this.dialogo) {
            this.dialogo.error(this.getMensagem(mensagem, controle));
        }
    }


    editarPerfil(sistema: Sistema) {
        if (sistema) {
            this.sistemaEditar.emit(sistema);
        }

    }

    isNotOnlyTrabalhador(perfis: any) {
        perfis = perfis.split('; ');
        if (perfis.length === 1 && perfis[0] === 'Trabalhador'){
            return false;
        }
        return true;
    }

    checkList() {
         if(this.usuario.perfisSistema){
             return this.usuario.perfisSistema.length > 0;
         }
      }


}
