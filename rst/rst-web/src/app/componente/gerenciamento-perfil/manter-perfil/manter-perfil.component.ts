import {Component, OnInit, ViewChild} from '@angular/core';
import {Usuario} from "../../../modelo/usuario.model";
import {BloqueioService} from "../../../servico/bloqueio.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UsuarioService} from "../../../servico/usuario.service";
import {ToastyService} from "ng2-toasty";
import {BaseComponent} from "../../base.component";
import {Seguranca} from "../../../compartilhado/utilitario/seguranca.model";
import {PermissoesEnum} from "../../../modelo/enum/enum-permissoes";
import {MensagemProperties} from "../../../compartilhado/utilitario/recurso.pipe";
import {ValidateCPF} from "../../../compartilhado/validators/cpf.validator";
import {ValidateEmail} from "../../../compartilhado/validators/email.validator";
import {MascaraUtil} from "../../../compartilhado/utilitario/mascara.util";
import {CropperSettings, ImageCropperComponent} from 'ng2-img-cropper';
import {environment} from "../../../../environments/environment";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {EstadoService} from "../../../servico/estado.service";
import {FiltroUsuario} from "../../../modelo/filtro-usuario.model";
import {ListaPaginada} from "../../../modelo/lista-paginada.model";

@Component({
    selector: 'app-manter-perfil',
    templateUrl: './manter-perfil.component.html',
    styleUrls: ['./manter-perfil.component.scss']
})
export class ManterPerfilComponent extends BaseComponent implements OnInit {

    public usuarioForm: FormGroup;

    id: number;
    login: string;
    usuario: Usuario;
    foto: any;
    cropperSettings: CropperSettings;
    @ViewChild('cropper')
    cropper: ImageCropperComponent;

    temImagem = false;

    @ViewChild('upload') upload;

    constructor(private router: Router,
                private activatedRoute: ActivatedRoute,
                private usuarioService: UsuarioService,
                private route: ActivatedRoute,
                protected bloqueioService: BloqueioService,
                protected estadoService: EstadoService,
                protected formBuilder: FormBuilder,
                protected dialogo: ToastyService,
                private modalService: NgbModal,
    ) {
        super(bloqueioService, dialogo, estadoService);
        this.inicializarImagem();
        this.criarForm();
    }

    ngOnInit() {
        this.setId();
        this.title = MensagemProperties.app_rst_usuario_title_perfil;
    }

    inicializarImagem() {
        this.cropperSettings = new CropperSettings();
        this.cropperSettings.width = 50;
        this.cropperSettings.height = 50;
        this.cropperSettings.croppedWidth = 200;
        this.cropperSettings.croppedHeight = 200;
        this.cropperSettings.canvasWidth = 400;
        this.cropperSettings.canvasHeight = 300;
        this.cropperSettings.dynamicSizing = true;
        this.foto = {};
    }

    setId(): void {
        let filtro = new FiltroUsuario();
        filtro.login = Seguranca.getUsuario().sub;
        this.paginacao.pagina = 1;
        this.usuarioService.pesquisarPaginado(filtro, this.paginacao).subscribe((retorno: ListaPaginada<Usuario>) => {
            this.id = retorno.list[0].id;
            if (retorno.quantidade === 0) {
                this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
            }
            if (this.id) {
                this.buscarUsuario();
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    buscarUsuario(): void {
        this.usuarioService.buscarUsuarioById(this.id).subscribe((retorno: Usuario) => {
            this.usuario = new Usuario();
            this.usuario = retorno;
            if (this.usuario) {
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
                    MensagemProperties.app_rst_labels_cpf);
                isValido = false;
            }

            if (!this.usuarioForm.controls['login'].errors.required && this.usuarioForm.controls['login'].errors.validCPF) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.usuarioForm.controls['login'],
                    MensagemProperties.app_rst_labels_cpf);
                isValido = false;
            }

        }

        if (this.usuarioForm.controls['email'].invalid) {
            if (this.usuarioForm.controls['email'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.usuarioForm.controls['email'],
                    MensagemProperties.app_rst_labels_email);
                isValido = false;
            }

            if (!this.usuarioForm.controls['email'].errors.required && this.usuarioForm.controls['email'].errors.validEmail) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.usuarioForm.controls['email'],
                    MensagemProperties.app_rst_labels_email);
                isValido = false;
            }
        }

        return isValido;
    }

    salvar(): void {
        if (this.validarCampos()) {
            this.converterFormParaModel();
            this.usuarioService.salvarPerfil(this.usuario).subscribe((retorno: Usuario) => {
                this.usuario = retorno;
                this.id = this.usuario.id;
                this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
                this.voltar();
            }, (error) => {
                this.mensagemError(error);
            });
        }
    }

    voltar(): void {
        this.router.navigate([`${environment.url_portal}`]);
    }

    converterModelParaForm(): void {
        this.usuarioForm.patchValue({
            nome: this.usuario.nome,
            login: this.usuario.login,
            email: this.usuario.email,
            apelido: this.usuario.apelido,
            exibirApelido: this.usuario.exibirApelido,
        });
        // imagem
        if (!this.isUndefined(this.usuario.foto)) {
            this.temImagem = true;
            this.foto.image = "data:image/png;base64," + this.usuario.foto;
        }
    }

    converterFormParaModel(): void {
        const formModel = this.usuarioForm.controls;
        this.usuario.nome = formModel.nome.value;
        this.usuario.email = formModel.email.value;
        this.usuario.login = MascaraUtil.removerMascara(formModel.login.value);
        if (!this.isUndefined(formModel.apelido.value) && formModel.apelido.value.toString().trim() != "") {
            this.usuario.apelido = formModel.apelido.value;
            if (!this.isUndefined(formModel.exibirApelido.value)) {
                this.usuario.exibirApelido = formModel.exibirApelido.value;
            }
        } else {
            this.usuario.apelido = undefined;
            this.usuario.exibirApelido = false;
        }
        if (this.temImagem) {
            let img = this.foto.image.replace(/^data:image\/\w+;base64,/, '');
            this.usuario.foto = img;
        }
    }


    criarForm(): void {
        this.usuarioForm = this.formBuilder.group({
            nome: [
                {value: null, disabled: true},
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(160),
                ]),
            ],
            login: [
                {value: null, disabled: true},
                Validators.compose([
                    Validators.required, ValidateCPF,
                ]),
            ],
            email: [
                {value: null, disabled: true},
                Validators.compose([
                    Validators.required,
                    Validators.maxLength(255),
                    ValidateEmail,
                ]),
            ],
            apelido: [
                {value: null, disabled: false},
                Validators.compose([
                    Validators.maxLength(100),
                ]),
            ],
            exibirApelido: [
                {value: null, disabled: false},
                Validators.compose([
                    Validators.maxLength(1),
                ]),
            ],
        });
    }


    adicionarImagem() {
        this.temImagem = true;
        let dados;
        const modalRef = this.modalService.open(this.upload);
        modalRef.result.then((result) => {
            dados = this.foto.image.split('base64,');
        }, (reason) => {
            if (!this.usuario.foto) {
                this.temImagem = false;
            }
            this.foto.image = this.usuario.foto;
        });

    }

    excluirImagem() {
        this.usuario.foto = undefined;
        this.foto = {};
        this.temImagem = false;
    }

}
