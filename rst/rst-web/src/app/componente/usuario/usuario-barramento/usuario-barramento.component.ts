import { BaseComponent } from "app/componente/base.component";
import {Component, OnInit, ViewChild} from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { UsuarioService } from "app/servico/usuario.service";
import { UsuarioEntidadeService } from "app/servico/usuario-entidade.service";
import { BloqueioService } from "app/servico/bloqueio.service";
import { ToastyService } from "ng2-toasty";
import { CNPJListarSemPerfilComponent } from '../usuario-barramento/cnpj-listar-sem-perfil/cnpj-listar-sem-perfil.component';
import { CNPJPerfisAssociadosComponent } from "./cnpj-perfis-associados/cnpj-perfis-associados.component";
import {AssociaPerfilBarramentoComponent} from "./associa-perfil-barramento/associa-perfil-barramento.component";
import {Perfil, Usuario, UsuarioPerfilSistema} from "../../../modelo";

@Component({
    selector: 'app-usuario-barramento',
    templateUrl: './usuario-barramento.component.html',
    styleUrls: ['./usuario-barramento.component.scss'],
})

export class UsuarioBarramentoComponent extends BaseComponent implements OnInit {

    @ViewChild ('associaPerfilBarramentoComponent') associaPerfilBarramentoComponent: AssociaPerfilBarramentoComponent;
    @ViewChild ('cnpjListarSemPerfilComponent') cnpjListarSemPerfil: CNPJListarSemPerfilComponent;
    @ViewChild ('cnpjPerfisAssociadosComponent') cnpjPerfisAssociados: CNPJPerfisAssociadosComponent;

    id: number;
    usuario: Usuario;
    perfisSistemas: UsuarioPerfilSistema[];
    perfisUsuario: Perfil[] = [];


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

    ngOnInit(){

        this.activatedRoute.params.subscribe((params) => {
            this.id = params['id'];

            this.usuario = new Usuario();
            this.usuario.perfisSistema = new Array<UsuarioPerfilSistema>();
            this.perfisSistemas = new Array<UsuarioPerfilSistema>();

            if (this.id) {
                this.modoAlterar = true;
                this.buscarUsuario();
            }
        });

    }

    buscarUsuario(): void {
        this.usuarioService.buscarUsuarioById(this.id)
            .subscribe((retorno: Usuario) =>  this.usuario = retorno,
                error => this.mensagemError(error));
    }


    editarEvent(sistema: string) {
        if(sistema)
            this.associaPerfilBarramentoComponent.selecionaSistema(sistema)
    }


}
