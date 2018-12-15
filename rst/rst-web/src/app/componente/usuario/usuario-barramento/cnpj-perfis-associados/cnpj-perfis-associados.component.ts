import { BaseComponent } from "app/componente/base.component";
import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { UsuarioService } from "app/servico/usuario.service";
import { UsuarioEntidadeService } from "app/servico/usuario-entidade.service";
import { BloqueioService } from "app/servico/bloqueio.service";
import { ToastyService } from "ng2-toasty";

@Component({
    selector: 'app-cnpj-perfis-associados',
    templateUrl: './cnpj-perfis-associados.component.html',
    styleUrls: ['./cnpj-perfis-associados.component.scss'],
})

export class CNPJPerfisAssociadosComponent extends BaseComponent implements OnInit {

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
