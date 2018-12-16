import { BaseComponent } from "app/componente/base.component";
import { Component, OnInit, Input, SimpleChanges } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { UsuarioService } from "app/servico/usuario.service";
import { UsuarioEntidadeService } from "app/servico/usuario-entidade.service";
import { BloqueioService } from "app/servico/bloqueio.service";
import { ToastyService } from "ng2-toasty";
import { UsuarioBarramentoComponent } from "../usuario-barramento.component";
import { Usuario } from "app/modelo";

@Component({
    selector: 'app-cnpj-perfis-associados',
    templateUrl: './cnpj-perfis-associados.component.html',
    styleUrls: ['./cnpj-perfis-associados.component.scss'],
})

export class CNPJPerfisAssociadosComponent extends BaseComponent implements OnInit {

    usuarioBarramentoComponent: UsuarioBarramentoComponent;

    @Input()  usuarioEnviado: Usuario = new Usuario();

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

    checkList(){
        return false;
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes['usuarioEnviado']) {
            console.log(this.usuarioEnviado);
        }
    }
}
