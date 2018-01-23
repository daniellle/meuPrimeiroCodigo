import { Component, OnInit } from '@angular/core';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from '../../servico/bloqueio.service';
import { DashboardService } from '../../servico/dashboard.service';
import { TemplateComponent } from '../../compartilhado/template/template.component';
import { Usuario } from 'app/modelo/usuario.model';
import { UsuarioService } from 'app/servico/usuario.service';
import { environment } from 'environments/environment';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent extends TemplateComponent implements OnInit {

  usuario: string;

  constructor(protected service: DashboardService, protected bloqueio: BloqueioService, protected dialogo: ToastyService, protected serviceUsuario: UsuarioService) {
    super(bloqueio, dialogo);
  }

  ngOnInit(): void {
    super.ngOnInit();
    this.consultarDadosUsuario();
  }

  exibirWS() {
    return environment.exibirWS;
  }

  consultarDadosUsuario() {
    this.serviceUsuario.consultarDadosUsuario().subscribe((usuario: Usuario) => {
      this.usuario = JSON.stringify(usuario, null, 4);
    });
  }

}
