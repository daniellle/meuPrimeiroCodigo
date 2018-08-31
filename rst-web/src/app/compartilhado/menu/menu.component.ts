import { AutenticacaoService } from 'app/servico/autenticacao.service';
import {Component} from '@angular/core';

@Component({
  moduleId: module.id,
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
})
export class MenuComponent {

  constructor() {

  }

  public logout() {
    AutenticacaoService.sair();
  }
}
