import { BaseComponent } from "app/componente/base.component";
import { Component, OnInit, Input, SimpleChanges } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { UsuarioService } from "app/servico/usuario.service";
import { UsuarioEntidadeService } from "app/servico/usuario-entidade.service";
import { BloqueioService } from "app/servico/bloqueio.service";
import { ToastyService } from "ng2-toasty";
import { UsuarioBarramentoComponent } from "../usuario-barramento.component";
import { Usuario } from "app/modelo";
import { UsuarioEntidade } from "app/modelo/usuario-entidade.model";

@Component({
    selector: 'app-cnpj-perfis-associados',
    templateUrl: './cnpj-perfis-associados.component.html',
    styleUrls: ['./cnpj-perfis-associados.component.scss'],
})

export class CNPJPerfisAssociadosComponent extends BaseComponent implements OnInit {

    usuarioBarramentoComponent: UsuarioBarramentoComponent;

    @Input()  usuarioEnviado: Usuario = new Usuario();
    @Input() usuarioEntidadeSelecionado: UsuarioEntidade = new UsuarioEntidade();
    nome;
    cnpj: string;

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
        if(this.usuarioEnviado && this.usuarioEnviado.perfisSistema){
            return this.usuarioEnviado.perfisSistema.length > 0;
        }
        return false;
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes['usuarioEnviado']) {

        }
        if (changes['usuarioEntidadeSelecionado']) {

        }
    }


    public getSistemaPerfil(): any {
        const sistemas = {};
        if(this.usuarioEnviado && this.usuarioEnviado.perfisSistema && this.usuarioEnviado.perfisSistema.length > 0) {
            this.usuarioEnviado.perfisSistema.forEach((ps) => {
                let id = ps.sistema.id;
                if (ps.sistema.codigo === 'portal' ||
                    ps.sistema.codigo === 'cadastro' ||
                    ps.sistema.codigo === 'dw' ||
                    ps.sistema.codigo === 'indigev') {
                    id = 1;
                    if(this.usuarioEntidadeSelecionado.departamentoRegional){
                        this.cnpj  = this.usuarioEntidadeSelecionado.departamentoRegional.cnpj;
                         this.nome = this.usuarioEntidadeSelecionado.departamentoRegional.nomeFantasia;
                    }
                    else if (this.usuarioEntidadeSelecionado.empresa){
                        this.cnpj = this.usuarioEntidadeSelecionado.empresa.cnpj;
                         this.nome = this.usuarioEntidadeSelecionado.empresa.nomeFantasia;
                    }
                    else if(this.usuarioEntidadeSelecionado.uat){
                        this.cnpj = this.usuarioEntidadeSelecionado.uat.cnpj;
                         this.nome = this.usuarioEntidadeSelecionado.uat.nomeFantasia;
                    }
                    if (!sistemas[id]) {
                        sistemas[id] = {id, sistema: 'Cadastro', perfil: '', cnpj: this.cnpj, nomeFantasia: this.nome };
                    }
                    if (sistemas[id].perfil.search(ps.perfil.nome.toString()) === -1) {
                        sistemas[id].perfil = ps.perfil.nome.toString().concat('; ').concat(sistemas[id].perfil);
                    }
                } else {
                    if (!sistemas[id]) {
                        sistemas[id] = {id, sistema: ps.sistema.nome, perfil: ''};
                    }
                    sistemas[id].perfil = ps.perfil.nome.toString().concat('; ').concat(sistemas[id].perfil);
                }
            });

            return Object.keys(sistemas).map((key) => {
                sistemas[key].perfil = sistemas[key].perfil.substr(0, sistemas[key].perfil.length - 2);
                return sistemas[key];
            });
        }
    }
}
