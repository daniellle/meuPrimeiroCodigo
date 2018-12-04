import { Component, Input, OnInit, Output, EventEmitter, OnChanges, SimpleChanges, ViewChild } from '@angular/core';
import { MdSelect } from '@angular/material';

import { Sistema, UsuarioPerfilSistema, Usuario, Perfil } from '../../../../modelo/index';
import { SistemaService } from 'app/servico/sistema.service';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { SistemaPerfil } from 'app/modelo/sistema-perfil.model';
import { PerfilEnum } from 'app/modelo/enum/enum-perfil';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';

const COD_SISTEMAS_RELACIONADOS = ['dw', 'indigev', 'portal'];
const COD_SISTEMA_CADASTRO = 'cadastro';

@Component({
  selector: 'app-associa-perfil',
  templateUrl: './associa-perfil.component.html',
  styleUrls: ['./associa-perfil.component.scss']
})
export class AssociaPerfilComponent extends BaseComponent implements OnInit, OnChanges{

  @ViewChild('sistemasSelect') sistemasSelect: MdSelect;

  @Input() modoConsulta: boolean;
  @Input() usuario: Usuario;

  @Output() associaPerfilEvent = new EventEmitter<any>();

  perfisSistemas: UsuarioPerfilSistema[];
  sistemaSelecionado: Sistema;
  sistemas: Sistema[] = [];
  perfisDoSistema: SistemaPerfil[] = [];

  constructor(private sistemaService: SistemaService,
              protected bloqueioService: BloqueioService,
              protected dialogo: ToastyService) {
      super(bloqueioService, dialogo);

  }

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
    console.log(sistemaNome)
    if(sistemaNome) {
      const sistema = this.sistemas.find(s => s.nome == sistemaNome);
      console.log(sistema)
      this.changeSistema(sistema);
      this.sistemasSelect.writeValue(sistema);
      this.sistemasSelect.focus();
    }
  }

  usuarioTemPerfilSistema(codigoPerfil: string): boolean {
    if(this.sistemaSelecionado) {
      const codigoSistema = this.sistemaSelecionado.codigo;
      return this.perfisSistemas.some(ps =>
        ps.sistema.codigo === codigoSistema && ps.perfil.codigo === codigoPerfil);
    }
    return false;
  }

  atualizaPerfilSistema(event: any, sistemaPerfil: any) {
    const sistema = { id: this.sistemaSelecionado.id, nome: this.sistemaSelecionado.nome, codigo:  this.sistemaSelecionado.codigo };
    if(event.checked) {
      this.addUsuarioPerfil(sistemaPerfil.perfil, sistema);
    } else {
      this.removeUsuarioPerfil(sistemaPerfil.perfil, sistema);
    }
  }

  associarPerfil() {
      let temCadastro: boolean;
      let associandoEpidemiologia: boolean;
      this.usuario.perfisSistema.forEach(sistemaPerfil => {
          if(sistemaPerfil.sistema.codigo == "2"){
              temCadastro = true;
          }
      });
      this.perfisSistemas.forEach(sistemaPerfil => {
          if(sistemaPerfil.sistema.codigo == "3"){
              associandoEpidemiologia = true;
      }
      });
      if(!temCadastro && associandoEpidemiologia){
          this.mensagemError("É necessário associar o perfil cadastro primeiro para associar o perfil epidemiologia");
          return;
      }
    this.usuario.perfisSistema = this.perfisSistemas;
    this.changeSistema(undefined);
    this.sistemasSelect.writeValue('');
  }

  isPerfilTrabalhador(perfil: Perfil): boolean {
    return perfil.codigo == PerfilEnum.TRA;
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
