import {Component, OnInit} from '@angular/core';
import {Location} from '@angular/common';
import {Usuario} from '../../modelo/usuario.model';
import {Seguranca} from '../utilitario/seguranca.model';

@Component({
  moduleId: module.id,
  selector: 'app-cabecalho',
  templateUrl: './cabecalho.component.html',
  styleUrls: ['./cabecalho.component.css'],
})
export class CabecalhoComponent implements OnInit {

  usuario: Usuario = Seguranca.getUsuario();

  localizacao: Location;

  constructor(localizacao: Location) {
    this.localizacao = localizacao;
  }

  ngOnInit() {

  }

}
