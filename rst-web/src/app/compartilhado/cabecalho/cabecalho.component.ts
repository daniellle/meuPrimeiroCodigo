import {Component, OnInit} from '@angular/core';
import {Location} from '@angular/common';
import {Usuario} from '../../modelo/usuario.model';
import {Seguranca} from '../utilitario/seguranca.model';
import {isUndefined} from "util";

@Component({
  moduleId: module.id,
  selector: 'app-cabecalho',
  templateUrl: './cabecalho.component.html',
  styleUrls: ['./cabecalho.component.css'],
})
export class CabecalhoComponent implements OnInit {

  usuario: Usuario = Seguranca.getUsuario();
  nome = '';

  localizacao: Location;

  constructor(localizacao: Location) {
    this.localizacao = localizacao;
  }

  ngOnInit() {
    this.nomePerfil()
  }

  nomePerfil(){
      if(!isUndefined(this.usuario) && this.usuario.exibirApelido){
          this.nome = this.usuario.apelido;
      }else{
          this.nome = this.usuario.nome;
      }
  }

}
