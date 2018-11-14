import {Component, OnInit, ViewChild} from '@angular/core';
import {BaseComponent} from 'app/componente/base.component';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastyService} from 'ng2-toasty';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {DialogService} from 'ng2-bootstrap-modal';
import {BloqueioService} from 'app/servico/bloqueio.service';
import {EstadoService} from 'app/servico/estado.service';
import {environment} from './../../../environments/environment';
import {MensagemProperties} from 'app/compartilhado/utilitario/recurso.pipe';
import {AutenticacaoService} from 'app/servico/autenticacao.service';
import {CropperSettings, ImageCropperComponent} from "ng2-img-cropper";
import {Usuario} from "../../modelo/usuario.model";
import {UsuarioService} from "../../servico/usuario.service";
import {ValidateEmail} from "../../compartilhado/validators/email.validator";
import {ValidateCPF} from "../../compartilhado/validators/cpf.validator";
import {MascaraUtil} from "../../compartilhado/utilitario/mascara.util";
import {isNullOrUndefined} from "util";
import {Trabalhador} from "../../modelo/trabalhador.model";
import {TrabalhadorService} from "../../servico/trabalhador.service";
import {PerfilEnum} from "../../modelo/enum/enum-perfil";
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';


@Component({
    selector: 'app-minha-conta',
    templateUrl: './minha-conta.component.html',
    styleUrls: ['./minha-conta.component.scss']
})
export class MinhaContaComponent extends BaseComponent implements OnInit {

    public usuarioPerfilForm: FormGroup;
    id: number;
    idTrab: number;
    login: string;
    usuario: Usuario;
    senhaConfirmacao: string;
    senhaAtual: string;
    meusdados: boolean;
    trabalhador: Trabalhador;
    foto: any;
    cropperSettings: CropperSettings;
    @ViewChild('cropper')
    cropper: ImageCropperComponent;

    temImagem = false;

    @ViewChild('upload') upload;

    constructor(
        private servico: AutenticacaoService,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private formBuilder: FormBuilder,
        protected estadoService: EstadoService,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService,
        private modalService: NgbModal,
        private dialogService: DialogService,
        private usuarioService: UsuarioService,
        private trabalhadorService: TrabalhadorService,
    ) {
        super(bloqueioService, dialogo, estadoService);
    }

    ngOnInit() {
        this.inicializarImagem();
        this.createForm();
        this.title = MensagemProperties.app_rst_usuario_title_minha_conta;
        this.buscarUsuario();
        this.dadosTrabalhador();
    }

    dadosTrabalhador(){
        console.log(this.activatedRoute.snapshot.params);
        this.meusdados = this.activatedRoute.snapshot.params.id === 'meusdados';
        this.trabalhadorService.buscarMeusDados().subscribe((trabalhador) => {
                this.trabalhador = trabalhador;
                this.idTrab = this.trabalhador.id;
            });
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

    buscarUsuario(): void {
        this.usuarioService.buscarPerfil().subscribe((retorno: Usuario) => {
            this.usuario = new Usuario();
            this.usuario = retorno;
            if (this.usuario) {
                this.converterModelParaForm();
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    createForm() {
        this.usuarioPerfilForm = this.formBuilder.group({
            senhaAtual: [
                {value: null, disabled: false}
            ],
            senhaNova: [
                {value: null, disabled: false},
                Validators.compose([
                    Validators.required,
                    Validators.pattern('(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$')
                ])],
            senhaConfirmacao: [
                {value: null, disabled: false},
                Validators.required],
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
        }, {validator: this.passwordMatchValidator});
    }

    passwordMatchValidator(g: FormGroup) {
        if (!(g.get('senhaNova').value === g.get('senhaConfirmacao').value)) {
            g.get('senhaConfirmacao').setErrors({MatchPassword: true})
        }
        return g.get('senhaNova').value === g.get('senhaConfirmacao').value
            ? null : {'mismatch': true};
    }

    validarCampos(): Boolean {
        let isValido = true;
        if (this.usuarioPerfilForm.controls['senhaAtual'].invalid) {
            if (this.usuarioPerfilForm.controls['senhaAtual'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.usuarioPerfilForm.controls['senhaAtual'],
                    "Erro ao Trocar Senha");
                isValido = false;
            }

        }

        if (this.usuarioPerfilForm.controls['nome'].invalid) {
            if (this.usuarioPerfilForm.controls['nome'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.usuarioPerfilForm.controls['nome'],
                    MensagemProperties.app_rst_labels_nome);
                isValido = false;
            }
        }

        if (this.usuarioPerfilForm.controls['login'].invalid) {

            if (this.usuarioPerfilForm.controls['login'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.usuarioPerfilForm.controls['login'],
                    MensagemProperties.app_rst_labels_cpf);
                isValido = false;
            }

            if (!this.usuarioPerfilForm.controls['login'].errors.required && this.usuarioPerfilForm.controls['login'].errors.validCPF) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.usuarioPerfilForm.controls['login'],
                    MensagemProperties.app_rst_labels_cpf);
                isValido = false;
            }

        }

        if (this.usuarioPerfilForm.controls['email'].invalid) {
            if (this.usuarioPerfilForm.controls['email'].errors.required) {
                this.mensagemErroComParametros('app_rst_campo_obrigatorio', this.usuarioPerfilForm.controls['email'],
                    MensagemProperties.app_rst_labels_email);
                isValido = false;
            }

            if (!this.usuarioPerfilForm.controls['email'].errors.required && this.usuarioPerfilForm.controls['email'].errors.validEmail) {
                this.mensagemErroComParametros('app_rst_campo_invalido', this.usuarioPerfilForm.controls['email'],
                    MensagemProperties.app_rst_labels_email);
                isValido = false;
            }
        }
        return isValido;
    }

    salvar(): void {
        if (this.validarCampos()) {
            this.servico.salvarPerfil(this.converterFormParaModel())
                .subscribe((user: Usuario) => {
                    if (user) {
                        this.mensagemSucesso(MensagemProperties.app_rst_minhaconta);
                        this.voltar();
                    }
                }, (error) => {
                    this.mensagemError(error);
                });
        }
    }

    converterModelParaForm(): void {
        this.usuarioPerfilForm.patchValue({
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

    converterFormParaModel(): {} {
        const formModel = this.usuarioPerfilForm.controls;
        this.usuario.nome = formModel.nome.value;
        this.usuario.email = formModel.email.value;
        this.usuario.login = MascaraUtil.removerMascara(formModel.login.value);
        if (this.isNotVazia(formModel.senhaConfirmacao.value) && this.isNotVazia(formModel.senhaAtual.value)) {
            this.senhaConfirmacao = formModel.senhaConfirmacao.value;
            this.senhaAtual = formModel.senhaAtual.value;
        }
        if (this.isNotVazia(formModel.apelido.value) && this.isNotVazia(formModel.apelido.value.toString().trim())) {
            this.usuario.apelido = formModel.apelido.value;
            if (this.isNotVazia(formModel.exibirApelido.value)) {
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

        let credencial = {
            usuario: this.usuario.login,
            senha: this.senhaConfirmacao,
            senhaAtual: this.senhaAtual,
        };
        const userCredencial = this.objToStrMap(this.usuario);
        userCredencial.set('credencial', credencial);
        return this.strMapToObj(userCredencial);
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

    podeVoltar(){
        return this.usuarioLogado.permissoes.includes(PermissoesEnum.TRABALHADOR);
    }

    voltar(): void {
        if (this.usuarioLogado.permissoes.includes(PermissoesEnum.TRABALHADOR)) {
            this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados`], {
                queryParams: { "fromMinhaConta": "true" }
            });
        }
    }

}
