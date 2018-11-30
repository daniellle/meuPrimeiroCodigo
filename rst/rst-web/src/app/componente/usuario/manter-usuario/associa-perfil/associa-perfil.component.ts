import { Component, Input, OnInit, Output, EventEmitter, OnChanges, SimpleChanges, ViewChild } from '@angular/core';
import { MdSelect } from '@angular/material';

import { Sistema } from 'app/modelo/sistema.model';
import { UsuarioPerfilSistema } from 'app/modelo/usuario-perfil-sistema.model';
import { SistemaService } from 'app/servico/sistema.service';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { SistemaPerfil } from 'app/modelo/sistema-perfil.model';
import { Usuario } from 'app/modelo/usuario.model';
import { Perfil } from 'app/modelo/perfil.model';
import {PerfilSistema} from "../../../../modelo/ṕerfil-sistemas";

const COD_SISTEMAS_RELACIONADOS = ['dw', 'indigev', 'portal'];
const COD_SISTEMA_CADASTRO = 'cadastro';

@Component({
  selector: 'app-associa-perfil',
  templateUrl: './associa-perfil.component.html'
})
export class AssociaPerfilComponent implements OnInit, OnChanges {

  @ViewChild('sistemasSelect') sistemasSelect: MdSelect;

  @Input() modoConsulta: boolean;
  @Input() usuario: Usuario;

  @Output() associaPerfilEvent = new EventEmitter<any>();

  perfisSistemas: UsuarioPerfilSistema[];
  sistemaSelecionado: Sistema;
  sistemas: Sistema[] = [];
  perfisDoSistema: SistemaPerfil[] = [];

  constructor(private sistemaService: SistemaService) {}

  ngOnInit() {
    if(this.usuario) {
      this.perfisSistemas = this.usuario.perfisSistema;
    }
    this.sistemaService.buscarSistemasPermitidos(Seguranca.getUsuario())
      .subscribe(sistemas => this.sistemas = sistemas);
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

  selecionaSistema(sistemaNome: string) {
    if(sistemaNome) {
      const sistema = this.sistemas.find(s => s.nome == sistemaNome);
      console.log(sistema)
      this.changeSistema(sistema);
      this.sistemasSelect.writeValue(sistema);
      this.sistemasSelect.focus();
    }
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
    console.log(this.usuario.perfisSistema)
    if(event.checked) {
      this.addUsuarioPerfil(sistemaPerfil.perfil, sistema);
    } else {
      this.removeUsuarioPerfil(sistemaPerfil.perfil, sistema);
    }
    console.log(this.usuario.perfisSistema)
  }

  associarPerfil() {
    this.usuario.perfisSistema = this.perfisSistemas;
    this.changeSistema(undefined);
    this.sistemasSelect.writeValue('');
  }

  private addUsuarioPerfil(perfil: Perfil, sistema: Sistema) {
    if(this.ehSistemaCadastroOuRelacionado(sistema)) {
      this.sistemas.filter(s => this.ehSistemaCadastroOuRelacionado(s))
        .forEach(s => {
          if(this.perfilTahAssociadoAoSistema(s, perfil)) {
            this.perfisSistemas.push({perfil, sistema: s})
          }
        });
    } else {
      this.perfisSistemas.push({perfil, sistema});
    }
  }

  private removeUsuarioPerfil(perfil: Perfil, sistema: Sistema) {
    if(this.ehSistemaCadastroOuRelacionado(sistema)) {
      this.sistemas.filter(s => this.ehSistemaCadastroOuRelacionado(s))
        .forEach(s => {
          if(this.perfilTahAssociadoAoSistema(s, perfil)) {
            const index = this.perfisSistemas
              .findIndex(ps => ps.perfil.codigo === perfil.codigo && ps.sistema.codigo === s.codigo);

            if(index > -1) this.perfisSistemas.splice(index, 1);
          }
        });
    } else {
      const index = this.perfisSistemas.findIndex(ps => ps.perfil.codigo === perfil.codigo && ps.sistema.codigo === sistema.codigo);
      this.perfisSistemas.splice(index, 1);
    }
  }

  private ehSistemaCadastroOuRelacionado(sistema: Sistema): boolean {
    return sistema.codigo === COD_SISTEMA_CADASTRO || COD_SISTEMAS_RELACIONADOS.includes(sistema.codigo);
  }

  private perfilTahAssociadoAoSistema(sistema: Sistema, perfil: Perfil): boolean {
    return sistema.sistemaPerfis.some(sistemaPerfil => sistemaPerfil.perfil.codigo === perfil.codigo);
  }

}
