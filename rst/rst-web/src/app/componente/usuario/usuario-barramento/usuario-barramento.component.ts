import { BaseComponent } from "app/componente/base.component";
import { Component, OnInit, Output, Input, SimpleChanges, ViewChild, ChangeDetectorRef } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { UsuarioService } from "app/servico/usuario.service";
import { UsuarioEntidadeService } from "app/servico/usuario-entidade.service";
import { BloqueioService } from "app/servico/bloqueio.service";
import { ToastyService } from "ng2-toasty";
import { CNPJListarSemPerfilComponent } from '../usuario-barramento/cnpj-listar-sem-perfil/cnpj-listar-sem-perfil.component';
import { CNPJPerfisAssociadosComponent } from "./cnpj-perfis-associados/cnpj-perfis-associados.component";
import { UsuarioEntidade } from "app/modelo/usuario-entidade.model";
import {AssociaPerfilBarramentoComponent} from "./associa-perfil-barramento/associa-perfil-barramento.component";
import {Perfil, Usuario, UsuarioPerfilSistema} from "../../../modelo";
import {MensagemProperties} from "../../../compartilhado/utilitario/recurso.pipe";

@Component({
    selector: 'app-usuario-barramento',
    templateUrl: './usuario-barramento.component.html',
    styleUrls: ['./usuario-barramento.component.scss'],
})

export class UsuarioBarramentoComponent extends BaseComponent implements OnInit {
    @Output() public usuariosEntidade: UsuarioEntidade[] = new Array<UsuarioEntidade>();
    @Input() usuario: Usuario;
    @Output() @Input() usuarioEntidadeSelecionado: UsuarioEntidade;
    @Output() usuarioEnviado: Usuario;


    @ViewChild ('associaPerfilBarramentoComponent') associaPerfilBarramentoComponent: AssociaPerfilBarramentoComponent;
    @ViewChild ('cnpjListarSemPerfilComponent') cnpjListarSemPerfil: CNPJListarSemPerfilComponent;
    @ViewChild ('cnpjPerfisAssociadosComponent') cnpjPerfisAssociados: CNPJPerfisAssociadosComponent;

    id: number;
    perfisSistemas: UsuarioPerfilSistema[];
    perfisUsuario: Perfil[] = [];


    constructor(
        private router: Router,
        private usuarioService: UsuarioService,
        private activatedRoute: ActivatedRoute,
        private usuarioEntidadeService: UsuarioEntidadeService,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private changeDetector: ChangeDetectorRef
      ) {
        super(bloqueioService, dialogo);
      }

      ngOnInit(): void {
    }


    ngOnChanges(changes: SimpleChanges) {
        if (changes['usuario']) {
            this.buscarUsuarioEntidade(this.usuario);
        }
        if (changes['usuarioEntidadeSelecionado']) {
            console.log(this.usuarioEntidadeSelecionado);
        }
    }

    private buscarUsuarioEntidade(usuario){
        if(usuario){
            this.usuarioEntidadeService.pesquisaUsuariosEntidade(usuario.login).subscribe(response => {
                this.usuariosEntidade = response;
            });
        }
    }

    private _isEmptyListaPerfilSistema() {
        return this.usuario.perfisSistema.length === 0;
    }


    onUsuarioSelecionado(usuarioSelecionado: UsuarioEntidade):void {
        console.log(usuarioSelecionado);
        this.usuarioEntidadeSelecionado = usuarioSelecionado;
        this.ngAfterViewChecked();
      }

      //RECEBE O USUARIO APÓS SUAS ASSOCIAÇÕES COM OS PERFIS SEREM FEITAS
      usuarioEnviadoAssociacao(usuarioEnviado: Usuario) {
            this.usuarioEnviado = usuarioEnviado;
            this.ngAfterViewChecked();
      }

      salvar(){
          if(this._isEmptyListaPerfilSistema()) {
              this.mensagemErroComParametros('app_rst_usuario_validacao_selecione_sistema_perfil');
              return;
          }
          let enviaEmail;
          if(this.usuario.perfisSistema == undefined || this.usuario.perfisSistema == null){
              enviaEmail = true;
          }
          else{
              enviaEmail = false;
          }
          this.usuarioService.salvarUsuario(this.usuarioEnviado).subscribe((retorno: Usuario) => {
                  this.usuario = retorno;
                  this.id = this.usuario.id;
                  //this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
              }, error =>
                  this.mensagemError(error)
          );
           var usuariosEntidadePorCnpj: UsuarioEntidade[] = [];
          this.usuarioEnviado.perfisSistema.forEach(ps => {
              let usuarioEntidadePorCnpj = this.usuarioEntidadeSelecionado;
              usuarioEntidadePorCnpj.perfil = ps.perfil.codigo;
              usuariosEntidadePorCnpj.push(usuarioEntidadePorCnpj);
          });
          this.usuarioEntidadeService.salvar(usuariosEntidadePorCnpj).subscribe((retorno: UsuarioEntidade) => {
              this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
          }, error =>
                  this.mensagemError(error)
          );

          if(enviaEmail){
              //Chamar serviço do girst de enviar email
          }

      }

    editarEvent(sistema: string) {
        if(sistema)
            this.associaPerfilBarramentoComponent.selecionaSistema(sistema)
    }

    ngAfterViewChecked(){
        this.changeDetector.detectChanges();
      }

}
