import { UsuarioEntidade } from 'app/modelo/usuario-entidade.model';
import { PerfilSistema } from 'app/modelo/perfil-sistemas';
import {Component, OnInit, ViewChild} from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { UsuarioEntidadeService } from './../../../servico/usuario-entidade.service';
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
import { UsuarioPerfilSistema, Perfil, Usuario, Sistema } from './../../../modelo/index';
import {MascaraUtil} from "../../../compartilhado/utilitario/mascara.util";
import { PerfilEnum } from 'app/modelo/enum/enum-perfil';
import { SistemaEnum } from 'app/modelo/enum/enum-sistema.model';
import { element } from 'protractor';
import { ignoreElements } from 'rxjs/operator/ignoreElements';
import { log } from 'util';
import { forEach } from '@angular/router/src/utils/collection';

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
    contemPortalApenas: boolean;

    constructor(
        private router: Router,
        private usuarioService: UsuarioService,
        private route: ActivatedRoute,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private usuarioEntidadeService: UsuarioEntidadeService
    ) {
        super(bloqueioService, dialogo);
    }

    editarSistemaPerfil(event){

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
       ;
    }

    salvar(): void {
        if(this._isEmptyListaPerfilSistema()) {
            this.mensagemErroComParametros('app_rst_usuario_validacao_selecione_sistema_perfil');
            return;
        }

        if(this.dadosGeraisComponent.validarCampos()) {
            const { nome, login, email } = this.dadosGeraisComponent.getFormValue();
            this.usuario.nome = nome;
            this.usuario.id = this.id;
            this.usuario.login = MascaraUtil.removerMascara(login);
            this.usuario.email = email;
            this.usuario.dados = undefined;
            this.adicionarGestorDRPortal(this.usuario);

            this.usuarioService.salvarUsuario(this.usuario).subscribe((retorno: Usuario) => {
                console.log(this.usuario.perfisSistema);
                
                this.usuario.id = retorno.id;
                this.id = this.usuario.id;
                this.excluirCardsPerfisDesassociadas(this.usuario, [PerfilEnum.GDRA, PerfilEnum.GDRM, PerfilEnum.GDRP,], 'departamentoRegional');
                this.excluirCardsPerfisDesassociadas(this.usuario, [PerfilEnum.GEEM, PerfilEnum.GEEMM], 'empresa');
                this.excluirCardsPerfisDesassociadas(this.usuario, [PerfilEnum.GUS], 'unidadesesi');
                this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
                this.voltar();
            }, error =>
            this.mensagemError(error)
            );
        }
    }

    adicionarGestorDRPortal(usuario: Usuario){
        if((this.ehGCDN() || this.ehGDNA() || this.ehGDNP() || this.ehSUDR()) || this.ehGDRM() && !this.contemPerfil([PerfilEnum.GDRP], usuario) && this.contemPerfil([PerfilEnum.GDRM, PerfilEnum.GDRA], usuario)){
            let sistemaEnums = ['', SistemaEnum.PORTAL, SistemaEnum.CADASTRO];
            let sistemaNomes = ['', 'Portal', 'Cadastro'];
           this.cadastrarPerfisSistemasGDRPortal(sistemaNomes, sistemaEnums, usuario);

        }
        if(this.contemPerfil([PerfilEnum.GDRP], usuario) && (!this.contemPerfil([PerfilEnum.GDRM], usuario)&& !this.contemPerfil([PerfilEnum.GDRA], usuario))){

            for(let i =0; i<2; i++){
                usuario.perfisSistema.forEach((perfilSistema: UsuarioPerfilSistema) => {
                    if(perfilSistema.perfil.codigo == PerfilEnum.GDRP){
                        const index = usuario.perfisSistema.indexOf(perfilSistema, 0);
                        if (index > -1) {
                            usuario.perfisSistema.splice(index, 1);
                        }
                    }

                });
            }
        }
    }

    editarEvent(sistema: string) {
        if(sistema)
            this.associaPerfilComponent.selecionaSistema(sistema)
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

    temOrigemDadosOuTemModoConsulta(): boolean {
        if (this.usuario) {
            return !this.modoConsulta;
        }
        
    }
    cadastrarPerfisSistemasGDRPortal(sistemaNomes, sistemaEnums, usuario){
        for(let i =1 ;i <=2;i++){
            let perfil = new Perfil();
            let sistema = new Sistema();
            perfil.codigo = PerfilEnum.GDRP;
            perfil.nome = 'Gestor DR Portal';
            perfil.hierarquia = 7;
                perfil.id=8;
                sistema.id = i;
                sistema.codigo = sistemaEnums[i];
                sistema.nome = sistemaNomes[i];
                let perfilSistema = new UsuarioPerfilSistema(perfil, sistema);
                usuario.perfisSistema.push(perfilSistema);
            }
    }
    excluirCardsPerfisDesassociadas(usuario: Usuario, perfisDesassociados: PerfilEnum[], tipoPerfil: string){
        
        if(!this.contemPerfil(perfisDesassociados, usuario)){
            this.usuarioEntidadeService.pesquisaUsuariosEntidade(usuario.login).subscribe((usuariosEntidade: UsuarioEntidade[]) => {
                if(usuariosEntidade){
                    usuariosEntidade.forEach(usuarioEntidade => {
                       this.checarTipoPerfilPassado(usuarioEntidade, tipoPerfil);
                    });
                }
            }, err => this.mensagemError(err))
        }
    }
    private checarTipoPerfilPassado(usuarioEntidade: UsuarioEntidade, tipoPerfil: string){
        if(tipoPerfil.toLowerCase().includes('empresa')){
            if(usuarioEntidade.empresa){
                this.usuarioEntidadeService.desativar(usuarioEntidade)
                    .subscribe()
            }
        }
        else if(tipoPerfil.toLowerCase().includes('departamentoregional')){
            if(usuarioEntidade.departamentoRegional){
                this.usuarioEntidadeService.desativar(usuarioEntidade)
                    .subscribe()
            }
        }
        else if(tipoPerfil.toLowerCase().includes('unidadesesi')){
            if(usuarioEntidade.uat){
                this.usuarioEntidadeService.desativar(usuarioEntidade)
                    .subscribe()
            }
        }
    }

    ehGDNA(){
        return this.usuarioLogado.papeis.includes(PerfilEnum.GDNA);
    }
    ehGDNP(){
        return this.usuarioLogado.papeis.includes(PerfilEnum.GDNP);
    }
    ehGCDN(){
        return this.usuarioLogado.papeis.includes(PerfilEnum.GCDN);
    }
    // ehGCODN(){
    //     return this.usuarioLogado.papeis.includes(PerfilEnum.GCODN);
    // }
    ehSUDR(){
        return this.usuarioLogado.papeis.includes(PerfilEnum.SUDR);

    }
    ehGDRM(){
        return this.usuarioLogado.papeis.includes(PerfilEnum.GDRM);
    }
}
