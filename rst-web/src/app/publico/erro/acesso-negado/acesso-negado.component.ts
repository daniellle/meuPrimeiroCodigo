import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-acesso-negado',
  templateUrl: './acesso-negado.component.html',
  styleUrls: ['./acesso-negado.component.scss'],
})
export class AcessoNegadoComponent implements OnInit {

  constructor(private location: Location) {
  }

  voltar() {
      this.location.back();
  }

  ngOnInit() {
  }

}
