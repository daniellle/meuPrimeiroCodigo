import { Component, OnInit } from '@angular/core';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from '../../servico/bloqueio.service';
import { DashboardService } from '../../servico/dashboard.service';
import { TemplateComponent } from '../../compartilhado/template/template.component';
import { Usuario } from 'app/modelo/usuario.model';
import { UsuarioService } from 'app/servico/usuario.service';
import { environment } from 'environments/environment';
import { BaseComponent } from '../base.component';
import { EstadoService } from '../../servico/estado.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent extends BaseComponent implements OnInit {

  usuario: string;

  constructor(protected service: DashboardService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    protected serviceUsuario: UsuarioService,
    protected estadoService: EstadoService) {
    
      super(bloqueioService, dialogo, estadoService);
  }

  ngOnInit(): void {
    this.consultarDadosUsuario();
  }

  exibirWS() {
    return environment.exibirWS;
  }

  consultarDadosUsuario() {
    this.serviceUsuario.consultarDadosUsuario(this.usuarioLogado.sub).subscribe((usuario: Usuario) => {
      this.usuario = JSON.stringify(usuario, null, 4);
    });
  }

}
