import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { UsuarioEntidade } from './../../../modelo/usuario-entidade.model';
import { UsuarioEntidadeService } from 'app/servico/usuario-entidade.service';
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
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { BaseComponent } from './../../..//componente/base.component';
import { MensagemProperties } from './../../..//compartilhado/utilitario/recurso.pipe';
import { Usuario } from './../../../modelo/usuario.model';
import { UsuarioService } from './../../../servico/usuario.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { SistemaEnum } from 'app/modelo/enum/enum-sistema.model';

export interface IHash {
  [details: number]: boolean;
}

@Component({
  selector: 'app-manter-usuario',
  templateUrl: './manter-usuario.component.html',
  styleUrls: ['./manter-usuario.component.scss'],
})
export class ManterUsuarioComponent extends BaseComponent implements OnInit {

  public usuarioForm: FormGroup;

  id: number;
  usuario: Usuario;
  perfis: Perfil[];
  perfisPortal: Perfil[];
  perfisCadastro: Perfil[];
  perfisDw: Perfil[];
  perfisGirst: Perfil[];
  sistemas: Sistema[];
  perfisSistemas: UsuarioPerfilSistema[];
  sistemaSelecionado?: Sistema;
  perfisSelecionados: IHash = {};
  idSistemas: number;

  constructor(
    private router: Router,
    private usuarioService: UsuarioService,
    private route: ActivatedRoute,
    protected bloqueioService: BloqueioService,
    protected formBuilder: FormBuilder,
    protected dialogo: ToastyService,
    private dialogService: DialogService,
    private perfilService: PerfilService,
    private sistemaService: SistemaService,
    private usuarioEntidadeService: UsuarioEntidadeService,
  ) {
    super(bloqueioService, dialogo);
    this.idSistemas = null;
    this.sistemaSelecionado = null;
    this.buscarPerfis();
    this.buscarSistemas();
  }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.id = params['id'];

      this.usuario = new Usuario();
      this.usuario.perfisSistema = new Array<UsuarioPerfilSistema>();
      this.perfisSistemas = new Array<UsuarioPerfilSistema>();

      this.perfisPortal = new Array<Perfil>();
      this.perfisCadastro = new Array<Perfil>();
      this.perfisDw = new Array<Perfil>();
      this.perfisGirst = new Array<Perfil>();

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

      if (!this.usuarioForm.controls['login'].errors.required && this.usuarioForm.controls['login'].errors.validCPF) {
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

      if (!this.usuarioForm.controls['email'].errors.required && this.usuarioForm.controls['email'].errors.validEmail) {
        this.mensagemErroComParametros('app_rst_campo_invalido', this.usuarioForm.controls['email'],
          MensagemProperties.app_rst_labels_email);
        isValido = false;
      }
    }

    if (this.isListaVazia()) {
      this.mensagemError(MensagemProperties.app_rst_usuario_validacao_selecione_sistema_perfil);
      isValido = false;
    }

    return isValido;
  }

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
    if (this.idSistemas && this.perfisSistemas.length > 0) {
      this.sistemaSelecionado = this.sistemas.filter((s) => s.id === Number(this.idSistemas))[0];

      const perfisRemover: UsuarioPerfilSistema[] = this.usuario.perfisSistema
        .filter((ps) => ps.sistema.id === Number(this.sistemaSelecionado.id));
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
    } else {
      this.mensagemError(MensagemProperties.app_rst_usuario_validacao_selecione_sistema_perfil);
    }
  }

  converterModelParaForm(): void {
    this.usuarioForm.patchValue({
      nome: this.usuario.nome,
      login: this.usuario.login,
      email: this.usuario.email,
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
          Validators.maxLength(160),
        ]),
      ],
      login: [
        { value: null, disabled: this.modoAlterar || this.modoConsulta },
        Validators.compose([
          Validators.required,
          ValidateCPF,
        ]),
      ],
      email: [
        { value: null, disabled: this.modoConsulta },
        Validators.compose([
          Validators.required,
          Validators.maxLength(255),
          ValidateEmail,
        ]),
      ],
    });
  }

  voltar(): void {
    this.router.navigate([this.id ? `${environment.path_raiz_cadastro}/usuario/${this.id}` :
      `${environment.path_raiz_cadastro}/usuario`]);
  }

  buscarPerfis(): void {
    this.perfilService.buscarTodos().subscribe((retorno: any) => {
      this.perfis = retorno;
      this.filtrarPerfisSistemaPortal();
      this.filtrarPerfisSistemaCadastro();
      this.filtrarPerfisSistemaGirst();
      this.filtrarPerfisSistemaDw();
    }, (error) => {
      this.mensagemError(error);
    });
  }

  buscarSistemas(): void {
    this.sistemaService.buscarTodos().subscribe((retorno: any) => {
      if (this.usuarioLogado.papeis.find((papel) => papel === PerfilEnum.ADM)) {
        this.sistemas = retorno;
      } else if (this.usuarioLogado.papeis.find((papel) => papel === PerfilEnum.GDNA)) {
        this.sistemas = [];
        retorno.forEach((sistema) => {
          if (sistema.codigo.toUpperCase() === SistemaEnum.DW
            || sistema.codigo.toUpperCase() === SistemaEnum.CADASTRO) {
            this.sistemas.push(sistema);
          }
        });
      } else {
        this.sistemas = [retorno.find((sistema) => sistema.codigo.toUpperCase() === SistemaEnum.CADASTRO)];
      }
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
      const perfilRemover: UsuarioPerfilSistema = this.perfisSistemas.filter((e) => e.perfil.id === Number(perfil.id))[0];
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
    const sistemaPerfis: UsuarioPerfilSistema[] = this.usuario.perfisSistema.filter((ps) => ps.sistema.id === Number(idSistema));

    sistemaPerfis.forEach((element) => {
      const i = this.usuario.perfisSistema.indexOf(element, 0);
      if (i > -1) {
        this.usuario.perfisSistema.splice(i, 1);
      }
    });
  }

  selecionarSistema(): void {
    this.filtrarPerfis();
    this.selecionarSistemaPerfil(this.idSistemas);
  }

  filtrarPerfis() {
    const sistema = this.sistemas.filter((s) => s.id === Number(this.idSistemas))[0];

    if (sistema) {
      if (sistema.codigo.toUpperCase() === SistemaEnum.PORTAL) {
        this.perfis = this.perfisPortal;
      }
      if (sistema.codigo.toUpperCase() === SistemaEnum.CADASTRO) {
        this.perfis = this.perfisCadastro;
      }
      if (sistema.codigo.toUpperCase() === SistemaEnum.GIRST) {
        this.perfis = this.perfisGirst;
      }
      if (sistema.codigo.toUpperCase() === SistemaEnum.DW) {
        this.perfis = this.perfisDw;
      }
    }
  }

  filtrarPerfisSistemaPortal() {
    this.perfis.forEach((element) => {
      if (element.codigo !== PerfilEnum.GDNA && element.codigo !== PerfilEnum.GDRA
        && element.codigo !== PerfilEnum.GEEM && element.codigo !== PerfilEnum.GEPC
        && element.codigo !== PerfilEnum.GERC && element.codigo !== PerfilEnum.GESI
        && element.codigo !== PerfilEnum.TRA) {
        this.perfisPortal.push(element);
      }
    });
  }

  filtrarPerfisSistemaCadastro() {

    if (this.usuarioLogado.papeis.find((papel) => papel === PerfilEnum.GDRA)) {
      this.perfis.forEach((element) => {
        if (element.codigo === PerfilEnum.GEEM || element.codigo === PerfilEnum.GEPC
          || element.codigo === PerfilEnum.GERC || element.codigo === PerfilEnum.GESI) {
          this.perfisCadastro.push(element);
        }
      });
    }

    if (this.usuarioLogado.papeis.find((papel) => papel === PerfilEnum.GDNA)) {
      this.perfis.forEach((element) => {
        if (this.naoEPerfilPortal(element) && element.codigo !== PerfilEnum.ADM
          && element.codigo !== PerfilEnum.ATD && element.codigo !== PerfilEnum.GDNA && element.codigo !== PerfilEnum.TRA) {
          this.perfisCadastro.push(element);
        }
      });
    }

    if (this.usuarioLogado.papeis.find((papel) => papel === PerfilEnum.ADM)) {
      this.perfis.forEach((element) => {
        if (this.naoEPerfilPortal(element) && element.codigo !== PerfilEnum.TRA) {
          this.perfisCadastro.push(element);
        }
      });
    }
  }

  filtrarPerfisSistemaDw() {
    if (this.usuarioLogado.papeis.find((papel) => papel === PerfilEnum.GDRA)) {
      this.perfis.forEach((element) => {
        if (element.codigo === PerfilEnum.GEEM || element.codigo === PerfilEnum.GEPC
          || element.codigo === PerfilEnum.GERC || element.codigo === PerfilEnum.GESI) {
          this.perfisDw.push(element);
        }
      });
    }

    if (this.usuarioLogado.papeis.find((papel) => papel === PerfilEnum.GDNA)) {
      this.perfis.forEach((element) => {
        if (this.naoEPerfilPortal(element) && element.codigo !== PerfilEnum.ADM
          && element.codigo !== PerfilEnum.ATD && element.codigo !== PerfilEnum.GDNA && element.codigo !== PerfilEnum.TRA) {
          this.perfisDw.push(element);
        }
      });
    }
    if (this.usuarioLogado.papeis.find((papel) => papel === PerfilEnum.ADM)) {
      this.perfis.forEach((element) => {
        if (this.naoEPerfilPortal(element) && element.codigo !== PerfilEnum.TRA) {
          this.perfisDw.push(element);
        }
      });
    }
  }

  private naoEPerfilPortal(element: any): boolean {
    return element.codigo !== PerfilEnum.GDNP && element.codigo !== PerfilEnum.GDRP;
  }

  filtrarPerfisSistemaGirst() {
    this.perfis.forEach((element) => {
      if (element.codigo === PerfilEnum.ADM) {
        this.perfisGirst.push(element);
      }
    });
  }

  temPermissaoDesativar(): boolean {
    return !this.modoConsulta && Boolean(Seguranca.isPermitido(['usuario_desativar']));
  }

}
