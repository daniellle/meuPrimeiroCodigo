import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { PerfilEnum } from 'app/modelo/enum/enum-perfil';
import { environment } from './../../../../environments/environment';
import { MascaraUtil } from './../../..//compartilhado/utilitario/mascara.util';
import { IHash } from './../../empresa/empresa-funcao/empresa-funcao.component';
import { UsuarioPerfilSistema } from './../../../modelo/usuario-perfil-sistema.model';
import { Sistema } from './../../../modelo/sistema.model';
import { Perfil } from './../../../modelo/perfil.model';
import { SistemaService } from './../../../servico/sistema.service';
import { PerfilService } from './../../../servico/perfil.service';
import { DialogService } from 'ng2-bootstrap-modal';
import { ValidateEmail } from './../../..//compartilhado/validators/email.validator';
import { ValidateCPF } from './../../..//compartilhado/validators/cpf.validator';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { BaseComponent } from './../../..//componente/base.component';
import { MensagemProperties } from './../../..//compartilhado/utilitario/recurso.pipe';
import { Usuario } from './../../../modelo/usuario.model';
import { UsuarioService } from './../../../servico/usuario.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { SistemaEnum } from 'app/modelo/enum/enum-sistema.model';
import { PerfilSistema } from 'app/modelo/ṕerfil-sistemas';

export interface IHash {
    [details: number]: boolean;
}

@Component({
    selector: 'app-manter-usuario',
    templateUrl: './manter-usuario.component.html',
    styleUrls: ['./manter-usuario.component.scss']
})
export class ManterUsuarioComponent extends BaseComponent implements OnInit {

    public usuarioForm: FormGroup;

    id: number;
    usuario: Usuario;
    perfis: Perfil[] = [];
    sistemas: Sistema[];
    sistemasPossiveis: Sistema[];
    perfisSistemas: UsuarioPerfilSistema[];
    sistemaSelecionado?: Sistema;
    perfisSelecionados: IHash = {};
    idSistemas: number;
    perfilSistemas: PerfilSistema[] = [];

    constructor(
        private router: Router,
        private usuarioService: UsuarioService,
        private route: ActivatedRoute,
        protected bloqueioService: BloqueioService,
        protected formBuilder: FormBuilder,
        protected dialogo: ToastyService,
        private dialogService: DialogService,
        private perfilService: PerfilService,
        private sistemaService: SistemaService
    ) {
        super(bloqueioService, dialogo);
        this.idSistemas = null;
        this.sistemaSelecionado = null;
        this.buscarSistemas();
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
        this.criarForm();
    }

    buscarUsuario(): void {
        this.usuarioService.buscarUsuarioById(this.id).subscribe((retorno: Usuario) => {
            this.usuario = retorno;
            if (this.usuario) {
                if (!this.usuario.perfisSistema) {
                    this.usuario.perfisSistema = new Array<UsuarioPerfilSistema>();
                }
                this.converterModelParaForm();
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    validarCampos(): Boolean {
        let isValido: Boolean = true;

        if (this.usuarioForm.controls['nome'].invalid) {
            if (this.usuarioForm.controls['nome'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.usuarioForm.controls['nome'],
                    MensagemProperties.app_rst_labels_nome);
                isValido = false;
            }
        }

        if (this.usuarioForm.controls['login'].invalid) {
            if (this.usuarioForm.controls['login'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.usuarioForm.controls['login'],
                    MensagemProperties.app_rst_labels_login_cpf);
                isValido = false;
            }

            if (!this.usuarioForm.controls['login'].errors.required
                && this.usuarioForm.controls['login'].errors.validCPF) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.usuarioForm.controls['login'],
                    MensagemProperties.app_rst_labels_login_cpf);
                isValido = false;
            }
        }

        if (this.usuarioForm.controls['email'].invalid) {
            if (this.usuarioForm.controls['email'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.usuarioForm.controls['email'],
                    MensagemProperties.app_rst_labels_email);
                isValido = false;
            }

            if (!this.usuarioForm.controls['email'].errors.required
                && this.usuarioForm.controls['email'].errors.validEmail) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.usuarioForm.controls['email'],
                    MensagemProperties.app_rst_labels_email);
                isValido = false;
            }
        }

        // if (this.isListaVazia()) {
        //     this.mensagemError(MensagemProperties.app_rst_usuario_validacao_selecione_sistema_perfil);
        //     isValido = false;
        // }

        return isValido;
    }

    //TODO FAZER VALIDACAO NO BACKEND PARA PERMITIR APENAS PERFIS VINCULADOS AO SISTEMA

    salvar(): void {
        if (this.validarCampos()) {
            this.converterFormParaModel();
            this.usuarioService.salvarUsuario(this.usuario).subscribe((retorno: Usuario) => {
                this.usuario = retorno;
                this.id = this.usuario.id;
                this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
                this.voltar();
            }, (error) => {
                this.mensagemError(error);
            });
        }
    }

    associarPerfil(): void {
        this.sistemaSelecionado = this.sistemas.filter((s) => s.id === Number(this.idSistemas))[0];

        const perfisRemover: UsuarioPerfilSistema[] = this.usuario.perfisSistema
            .filter((ps) => ps.sistema.id
                === Number(this.sistemaSelecionado.id));
        if (perfisRemover.length > 0) {
            perfisRemover.forEach((pr) => {
                const index: number = this.usuario.perfisSistema.indexOf(pr);
                if (index > -1) {
                    this.usuario.perfisSistema.splice(index, 1);
                }
            });
        }

        this.perfisSistemas.forEach((element) => {
            element.sistema = this.sistemaSelecionado;
            this.usuario.perfisSistema.push(element);
        });

        this.sistemaSelecionado = null;
        this.idSistemas = null;
        this.perfisSelecionados = [];
        this.perfisSistemas = Array<UsuarioPerfilSistema>();

    }

    converterModelParaForm(): void {
        this.usuarioForm.patchValue({
            nome: this.usuario.nome,
            login: this.usuario.login,
            email: this.usuario.email
        });
    }

    converterFormParaModel(): void {
        const formModel = this.usuarioForm.controls;

        this.usuario.nome = formModel.nome.value;
        this.usuario.email = formModel.email.value;
        this.usuario.login = MascaraUtil.removerMascara(formModel.login.value);
        this.usuario.dados = undefined;
    }

    criarForm(): void {
        this.usuarioForm = this.formBuilder.group({
            nome: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(160)
                ])
            ],
            login: [
                { value: null, disabled: this.modoAlterar || this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    ValidateCPF
                ])
            ],
            email: [
                { value: null, disabled: this.modoConsulta },
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(255),
                    ValidateEmail
                ])
            ]
        });
    }

    voltar(): void {
        this.router.navigate([this.id ? `${environment.path_raiz_cadastro}/usuario/${this.id}` :
            `${environment.path_raiz_cadastro}/usuario`]);
    }


    buscarSistemas(): void {
        this.sistemaService.buscarSistemasPermitidos(Seguranca.getUsuario()).subscribe((retorno: any) => {
            this.sistemas = retorno;
            this.sistemasPossiveis = retorno.filter((item) => {
                return item.id != 7;
              });     
              this.sistemasPossiveis = this.sistemasPossiveis.filter((item) => {
                return item.id != 4;
              });      
              this.sistemasPossiveis = this.sistemasPossiveis.filter((item) => {
                return item.id != 1;
              });                                         
        }, (error) => {
            this.mensagemError(error);
        });
    }

    modificarPerfil(perfil?: Perfil, event?: any): void {
        if (perfil && event.checked) {
            const usuarioPerfilSistema = new UsuarioPerfilSistema(perfil);
            this.perfisSistemas.push(usuarioPerfilSistema);
            this.perfisSelecionados[perfil.id] = true;
        } else {
            const perfilRemover: UsuarioPerfilSistema = this.perfisSistemas.filter((e) => e.perfil.id
                === Number(perfil.id))[0];
            const index = this.perfisSistemas.indexOf(perfilRemover);
            if (index > -1) {
                this.perfisSistemas.splice(index, 1);
            }
            this.perfisSelecionados[perfil.id] = false;
        }
    }

    isListaVazia(): boolean {
        return !(this.usuario && this.usuario.perfisSistema && this.usuario.perfisSistema.length > 0);
    }

    selecionarSistemaPerfil(id: number): void {
        this.perfisSelecionados = [];
        this.perfisSistemas = [];
        const target_r: any = { checked: true };
        const event: any = { checked: target_r };
        this.idSistemas = id;
        this.filtrarPerfis();

        if (this.usuario.perfisSistema) {
            this.usuario.perfisSistema.forEach((element) => {
                if (Number(element.sistema.id) === Number(id)) {
                    this.sistemaSelecionado = element.sistema;
                    this.modificarPerfil(element.perfil, event);
                }
            });
        }
    }

    public getSistemaPerfil(): any {

        const sistemas = {};
        this.usuario.perfisSistema.forEach((ps) => {
            const id = ps.sistema.id;

            if (!sistemas[id]) {
                sistemas[id] = { id, sistema: ps.sistema.nome, perfil: '' };
            }
            sistemas[id].perfil = ps.perfil.nome.toString().concat('; ').concat(sistemas[id].perfil);
        });

        return Object.keys(sistemas).map((key) => {
            sistemas[key].perfil = sistemas[key].perfil.substr(0, sistemas[key].perfil.length - 2);
            return sistemas[key];
        });
    }

    excluirAssociacaoPerfil(idSistema: number): void {
        const sistemaPerfis: UsuarioPerfilSistema[] = this.usuario.perfisSistema.filter((ps) => ps.sistema.id
            === Number(idSistema));
        sistemaPerfis.forEach((element) => {
            this.usuario.perfisSistema;
            const i = this.usuario.perfisSistema.indexOf(element, 0);
            if (element.perfil.codigo !== PerfilEnum.TRA) {
                if (i > -1) {
                    this.usuario.perfisSistema.splice(i, 1);
                }
            }
        });
        console.log(sistemaPerfis);
    }

    isNotOnlyTrabalhador(perfis: any) {
        perfis = perfis.split("; ");
        return perfis.length > 1;
    }


    selecionarSistema(): void {
        this.filtrarPerfis();
        this.selecionarSistemaPerfil(this.idSistemas);
    }

    filtrarPerfis() {
        this.perfilSistemas = [];
        if(this.idSistemas == 2)
        {
            this.perfis = [];
            const sistemaCadastro = this.sistemas.filter((s) => s.id === 7 || s.id === 4 || s.id === 1 || s.id === 2);
            if (sistemaCadastro){
                sistemaCadastro.forEach(resposta => {
                    resposta.sistemaPerfis.forEach(r =>{
                        let perfilSistema = this.checkPerfilSistemas(r.perfil);
                        if(perfilSistema.perfil.id !== undefined){
                            perfilSistema.sistemas.push(resposta.id);
                        }else{
                            if(r.perfil.codigo !== PerfilEnum.TRA){
                                let perfilSistema = new PerfilSistema();
                                perfilSistema.perfil = r.perfil;
                                perfilSistema.sistemas.push(resposta.id);
                                this.perfilSistemas.push(perfilSistema);
                            }
                        }
                    }); 
                })
            } 
            console.log(this.perfilSistemas);
        }
        else{
            const sistema = this.sistemas.filter((s) => s.id === Number(this.idSistemas))[0];
            if (sistema) {
                this.perfis = sistema.sistemaPerfis.map(value => value.perfil)
                    .filter(value => value.codigo != PerfilEnum.TRA);
            } else {
                this.perfis = [];
            }
        }
    }

   checkPerfilSistemas(perfil): PerfilSistema{
       let retorno = new PerfilSistema();
       this.perfilSistemas.forEach(perfilSistema => {
            if(perfilSistema.perfil.nome == perfil.nome){
                retorno = perfilSistema;
            }
        });
        //console.log(retorno);
        return retorno;
   }


    temPermissaoDesativar(): boolean {
        return !this.modoConsulta && Boolean(Seguranca.isPermitido(['usuario_desativar']));
    }

}
