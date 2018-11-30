import { Component, Input, OnInit, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';

import { Observable } from 'rxjs';

import { Sistema } from 'app/modelo/sistema.model';
import { UsuarioPerfilSistema } from 'app/modelo/usuario-perfil-sistema.model';
import { SistemaService } from 'app/servico/sistema.service';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { SistemaPerfil } from 'app/modelo/sistema-perfil.model';
import { Usuario } from 'app/modelo/usuario.model';
import {PerfilSistema} from "../../../../modelo/ṕerfil-sistemas";

const COD_SISTEMAS_RELACIONADOS = ['dw', 'indigev', 'portal'];
const COD_SISTEMA_CADASTRO = 'cadastro';

@Component({
  selector: 'app-associa-perfil',
  templateUrl: './associa-perfil.component.html'
})
export class AssociaPerfilComponent implements OnInit, OnChanges {

  @Input() modoConsulta: boolean;
  @Input() usuario: Usuario;

  @Output() associaPerfilEvent = new EventEmitter<any>();

  perfisSistemas: UsuarioPerfilSistema[];
  sistemaSelecionado: Sistema;
  sistemas$: Observable<Sistema[]>;
  perfisDoSistema: SistemaPerfil[] = [];

  constructor(private sistemaService: SistemaService) {}

  ngOnInit() {
    if(this.usuario) {
      this.perfisSistemas = this.usuario.perfisSistema;
    }
    this.sistemas$ = this.sistemaService.buscarSistemasPermitidos(Seguranca.getUsuario());
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['usuario'])
      this.perfisSistemas = this.usuario.perfisSistema;
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

    private editarUsuario(perfilSistema: PerfilSistema){

    }

  temPerfilSistema(codigoPerfil: string): boolean {
    if(this.sistemaSelecionado) {
      const codigoSistema = this.sistemaSelecionado.codigo;
      return this.perfisSistemas.some(ps =>
        ps.sistema.codigo === codigoSistema && ps.perfil.codigo === codigoPerfil);
    }
    return false;
  }

  atualizaPerfilSistema(event: any, sistemaPerfil: any) {
    const sistema = { id: this.sistemaSelecionado.id, nome: this.sistemaSelecionado.nome, codigo:  this.sistemaSelecionado.codigo };
    const usuarioPerfil: UsuarioPerfilSistema = { perfil: sistemaPerfil.perfil, sistema };
    if(event.checked) {
      this.addUsuarioPerfil(usuarioPerfil, sistema);
    } else {
      this.removeUsuarioPerfil(usuarioPerfil, sistema);
    }
  }

  associarPerfil() {
  }

  private addUsuarioPerfil(usuarioPerfil: UsuarioPerfilSistema, sistema: Sistema) {
    if(this.ehSistemaCadastro(sistema)) {
      this.perfisSistemas
        .filter(ps => this.ehSistemaCadastro(ps.sistema))
    } else {
      this.perfisSistemas.push(usuarioPerfil);
    }
  }

  private removeUsuarioPerfil(usuarioPerfil: UsuarioPerfilSistema, sistema: Sistema) {
    if(this.ehSistemaCadastro(this.sistemaSelecionado)) {
      this.perfisSistemas.filter(ps => ps.sistema)
    } else {
      const index = this.perfisSistemas.findIndex(ps => JSON.stringify(ps) === JSON.stringify(usuarioPerfil));
      this.perfisSistemas.splice(index, 1);
    }
  }

  private ehSistemaCadastro(sistema: Sistema): boolean {
    return sistema.codigo === COD_SISTEMA_CADASTRO || COD_SISTEMAS_RELACIONADOS.includes(sistema.codigo);
  }


}
