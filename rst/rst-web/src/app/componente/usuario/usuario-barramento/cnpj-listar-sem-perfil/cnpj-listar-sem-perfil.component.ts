import { BaseComponent } from "app/componente/base.component";
import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { UsuarioService } from "app/servico/usuario.service";
import { UsuarioEntidadeService } from "app/servico/usuario-entidade.service";
import { BloqueioService } from "app/servico/bloqueio.service";
import { ToastyService } from "ng2-toasty";
import { UsuarioBarramentoComponent } from "../usuario-barramento.component";

@Component({
    selector: 'app-cnpj-listar-sem-perfil',
    templateUrl: './cnpj-listar-sem-perfil.component.html',
    styleUrls: ['./cnpj-listar-sem-perfil.component.scss'],
})

export class CNPJListarSemPerfilComponent extends BaseComponent implements OnInit {

    usuarioBarramentoComponent: UsuarioBarramentoComponent;
    constructor(
        private router: Router,
        private usuarioService: UsuarioService,
        private activatedRoute: ActivatedRoute,
        private usuarioEntidadeService: UsuarioEntidadeService,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
      ) {
        super(bloqueioService, dialogo);
      }

    ngOnInit(){ }
}
