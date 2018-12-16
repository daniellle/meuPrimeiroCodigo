import { BaseComponent } from "app/componente/base.component";
import { Component, OnInit, Input, SimpleChanges, Output, EventEmitter } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { UsuarioService } from "app/servico/usuario.service";
import { UsuarioEntidadeService } from "app/servico/usuario-entidade.service";
import { BloqueioService } from "app/servico/bloqueio.service";
import { ToastyService } from "ng2-toasty";
import { UsuarioBarramentoComponent } from "../usuario-barramento.component";
import { UsuarioEntidade } from "app/modelo/usuario-entidade.model";


@Component({
    selector: 'app-cnpj-listar-sem-perfil',
    templateUrl: './cnpj-listar-sem-perfil.component.html',
    styleUrls: ['./cnpj-listar-sem-perfil.component.scss'],
})

export class CNPJListarSemPerfilComponent extends BaseComponent implements OnInit {

    @Input() usuariosEntidade: UsuarioEntidade[] = new Array<UsuarioEntidade>();
    usuarioEntidadeEmpresa: UsuarioEntidade[] = new Array<UsuarioEntidade>();
    usuarioBarramentoComponent: UsuarioBarramentoComponent;
    @Output() usuarioSelecionado: EventEmitter<UsuarioEntidade> = new EventEmitter<UsuarioEntidade>();
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

    ngOnChanges(changes: SimpleChanges) {
        if (changes['usuariosEntidade']) {
            this.createUsuarioEntidadeEmpresa();
        }
    }

    createUsuarioEntidadeEmpresa() {
        this.usuariosEntidade.forEach((element) => {
            if (element.empresa && !element.perfil) {
                this.usuarioEntidadeEmpresa.push(element);
            }
        });
    }

    enviarUsuarioEntidade(usuarioEntidade: UsuarioEntidade) {
        this.usuarioSelecionado.emit(usuarioEntidade);
    }
}
