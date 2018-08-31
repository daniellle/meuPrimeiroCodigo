import { environment } from './../../../environments/environment';
import { Component, OnInit } from '@angular/core';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from '../../servico/bloqueio.service';
import { BaseComponent } from '../../componente/base.component';

@Component({
  moduleId: module.id,
  selector: 'app-template',
  templateUrl: './template.component.html',
  styleUrls: ['./template.component.css'],
})
export class TemplateComponent extends BaseComponent implements OnInit {

  constructor(protected bloqueio: BloqueioService, protected dialogo: ToastyService) {
    super(bloqueio, dialogo);
  }

  ngOnInit(): void {
    this.exibeMenu();
  }

  exibirMenu() {
    if (!environment.exibirMenu) {
      return 0;
    }
  }
  exibeMenu() {
    if (!environment.exibirMenu) {
      document.querySelector('body').classList.toggle('sidebar-hidden');
      document.querySelector('header').remove();
      document.getElementById('menu').remove();
    }
  }

}
