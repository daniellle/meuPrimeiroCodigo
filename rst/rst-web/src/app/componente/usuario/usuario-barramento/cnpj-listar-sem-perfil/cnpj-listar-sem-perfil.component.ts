import { BaseComponent } from "app/componente/base.component";
import { Component, OnInit, Input, SimpleChanges, Output, EventEmitter } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { UsuarioService } from "app/servico/usuario.service";
import { UsuarioEntidadeService } from "app/servico/usuario-entidade.service";
import { BloqueioService } from "app/servico/bloqueio.service";
import { ToastyService } from "ng2-toasty";
import { UsuarioBarramentoComponent } from "../usuario-barramento.component";
import { UsuarioEntidade } from "app/modelo/usuario-entidade.model";
import { Usuario } from "app/modelo";
import { element } from "protractor";


@Component({
    selector: 'app-cnpj-listar-sem-perfil',
    templateUrl: './cnpj-listar-sem-perfil.component.html',
    styleUrls: ['./cnpj-listar-sem-perfil.component.scss'],
})

export class CNPJListarSemPerfilComponent extends BaseComponent implements OnInit {

    usuariosEntidade: UsuarioEntidade[] = new Array<UsuarioEntidade>();
    @Input() usuario: Usuario = new Usuario();
    usuarioEntidadeEmpresa: UsuarioEntidade[] = new Array<UsuarioEntidade>();
    usuarioEntidadeDepartamentoRegional: UsuarioEntidade[] = new Array<UsuarioEntidade>();
    usuarioEntidadeUnidadeSesi: UsuarioEntidade[] = new Array<UsuarioEntidade>();
    usuarioBarramentoComponent: UsuarioBarramentoComponent;
    @Output() usuarioSelecionado: EventEmitter<UsuarioEntidade> = new EventEmitter<UsuarioEntidade>();
    tipoCnpj: string;
    strEmpresa: string = "empresa";
    strDR: string = "departamentoregional";
    strUat: string = "unidadesesi";

    constructor(
        private router: Router,
        private usuarioService: UsuarioService,
        private activatedRoute: ActivatedRoute,
        private usuarioEntidadeService: UsuarioEntidadeService,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
      ) {
        super(bloqueioService, dialogo);

        if(this.router.url.includes(this.strEmpresa)){
            this.tipoCnpj = this.strEmpresa;
        }
        else if(this.router.url.includes(this.strDR)){
            this.tipoCnpj = this.strDR;
        }
        else if(this.router.url.includes(this.strUat)){
            this.tipoCnpj = this.strUat;
        }

      }

    ngOnInit(){ }

    ngOnChanges(changes: SimpleChanges) {
        if (changes['usuario']) {
            this.buscarUsuarioEntidade(this.usuario);
        }
        if (changes['usuariosEntidade']) {
            this.createUsuarioEntidade();
        }
    }

    private buscarUsuarioEntidade(usuario){
        if(usuario){
            this.usuarioEntidadeService.pesquisaUsuariosEntidade(usuario.login).subscribe((retorno: UsuarioEntidade[])  => {
                this.usuariosEntidade = retorno;
                this.createUsuarioEntidade();
            });
        }
    }

    createUsuarioEntidade() {
        console.log(this.usuariosEntidade);
        this.usuariosEntidade.forEach((element) => {
            if (element.empresa && !element.perfil) {
                this.usuarioEntidadeEmpresa.push(element);
            }
            if (element.departamentoRegional && !element.perfil) {
                this.usuarioEntidadeDepartamentoRegional.push(element);
            }
            if (element.uat && !element.perfil) {
                this.usuarioEntidadeUnidadeSesi.push(element);
            }
        });
        console.log(this.usuarioEntidadeUnidadeSesi);
        console.log(this.usuarioEntidadeDepartamentoRegional);
    }

    checkUsuarioEntidade(usuariosEntidade: UsuarioEntidade[]): boolean{
        return usuariosEntidade.length > 0;
    }


    enviarUsuarioEntidade(usuarioEntidade: UsuarioEntidade) {
        this.usuarioSelecionado.emit(usuarioEntidade);
    }
}
