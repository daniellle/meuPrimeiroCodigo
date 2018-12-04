import {Component, OnInit, ViewChild} from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { BaseComponent } from './../../..//componente/base.component';
import { MensagemProperties } from './../../..//compartilhado/utilitario/recurso.pipe';
import { UsuarioService } from './../../../servico/usuario.service';
import { AssociaPerfilComponent } from './associa-perfil/associa-perfil.component';
import { DadosGeraisComponent } from './dados-gerais/dados-gerais.component';
import { UsuarioPerfilSistema, Perfil, Usuario } from './../../../modelo/index';

@Component({
    selector: 'app-manter-usuario',
    templateUrl: './manter-usuario.component.html',
    styleUrls: ['./manter-usuario.component.scss']
})
export class ManterUsuarioComponent extends BaseComponent implements OnInit {

    @ViewChild('associaPerfilComponent') associaPerfilComponent: AssociaPerfilComponent;
    @ViewChild('dadosGeraisComponent') dadosGeraisComponent: DadosGeraisComponent;

    usuarioForm: FormGroup;

    id: number;
    usuario: Usuario;
    perfisSistemas: UsuarioPerfilSistema[];
    perfisUsuario: Perfil[] = [];

    constructor(
        private router: Router,
        private usuarioService: UsuarioService,
        private route: ActivatedRoute,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
    ) {
        super(bloqueioService, dialogo);
    }

    ngOnInit() {
        this.route.params.subscribe((params) => {
            this.id = params['id'];

            this.usuario = new Usuario();
            this.usuario.perfisSistema = new Array<UsuarioPerfilSistema>();
            this.perfisSistemas = new Array<UsuarioPerfilSistema>();

            if (this.id) {
                this.modoAlterar = true;
                this.buscarUsuario();
            }
        });
        this.modoConsulta = !Seguranca.isPermitido([PermissoesEnum.USUARIO, PermissoesEnum.USUARIO_CADASTRAR,
            PermissoesEnum.USUARIO_ALTERAR, PermissoesEnum.USUARIO_DESATIVAR]);
        this.title = MensagemProperties.app_rst_usuario_title_cadastrar;
    }

    buscarUsuario(): void {
        this.usuarioService.buscarUsuarioById(this.id)
            .subscribe((retorno: Usuario) =>  this.usuario = retorno, 
            error => this.mensagemError(error));
    }

    salvar(): void {
        if(this._isEmptyListaPerfilSistema()) {
            this.mensagemErroComParametros('app_rst_usuario_validacao_selecione_sistema_perfil');
            return;
        }

        if(this.dadosGeraisComponent.validarCampos()) {
            const { nome, login, email } = this.dadosGeraisComponent.getFormValue();
            this.usuario.nome = nome;
            this.usuario.login = login;
            this.usuario.email = email;
            this.usuario.dados = undefined;
            this.usuarioService.salvarUsuario(this.usuario).subscribe((retorno: Usuario) => {
                this.usuario = retorno;
                this.id = this.usuario.id;
                this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
                this.voltar();
            }, error => 
                this.mensagemError(error)
            );
        }
    }

    private _isEmptyListaPerfilSistema() {
        return this.usuario.perfisSistema.length === 0;
    }

    voltar(): void {
        this.router.navigate([this.id ? `${environment.path_raiz_cadastro}/usuario/${this.id}` :
            `${environment.path_raiz_cadastro}/usuario`]);
    }

    isListaVazia(): boolean {
        return !(this.usuario && this.usuario.perfisSistema && 
            this.usuario.perfisSistema.length > 0);
    }

    temPermissaoDesativar(): boolean {
        return !this.modoConsulta && Boolean(Seguranca.isPermitido(['usuario_desativar']));
    }

    getPerfil(id: number): Perfil {
        return this.perfisUsuario.find(perfil => perfil.id === id);
    }
}
