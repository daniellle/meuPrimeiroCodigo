import { element } from 'protractor';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from './../../../servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { environment } from './../../../../environments/environment.prod';
import { UsuarioService } from './../../../servico/usuario.service';
import { Usuario } from './../../../modelo/usuario.model';
import { MascaraUtil } from './../../../compartilhado/utilitario/mascara.util';
import { Router, ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { PerfilEnum } from 'app/modelo/enum/enum-perfil';
import { UsuarioEntidadeService } from 'app/servico/usuario-entidade.service';

@Component({
  selector: 'app-usuario-intermediario',
  templateUrl: './usuario-intermediario.component.html',
  styleUrls: ['./usuario-intermediario.component.scss'],
})
export class UsuarioIntermediarioComponent extends BaseComponent implements OnInit {
  public id: number;
  public usuario: Usuario;
  public existeUsuario: boolean;
  public loginFormatado: string;
  public temPerfilDR = false;
  public temPerfilEmpresa = false;
  public temPerfilSindicato = false;

  constructor(private router: Router,
    private service: UsuarioService,
    private activatedRoute: ActivatedRoute,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private usuarioEntidadeService: UsuarioEntidadeService) {
    super(bloqueioService, dialogo);
    this.usuario = new Usuario();
    this.activatedRoute.params.subscribe((params) => {
      this.id = params['id'];
      if (this.id) {
        this.buscar();
      }
    });
  }

  ngOnInit() {

  }

  mudarPagina(tipo) {
    this.router.navigate([`${tipo}`], { relativeTo: this.activatedRoute });
  }

  buscar() {
    this.service.buscarUsuarioById(this.id).subscribe((user) => {
      if (user) {
        this.usuario = user;
        this.existeUsuario = true;
        this.loginFormatado = MascaraUtil.formatarCpf(this.usuario.login);
        this.setTipoPerfil();
      }
    });
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/usuario/`]);
  }

  setTipoPerfil() {
    if (this.usuario.perfisSistema) {
      this.temPerfilDR = this.temDRPerfil();
      this.temPerfilSindicato = this.temSindPerfil();
      this.temPerfilEmpresa = this.temEmpPerfil();
    }
  }
  private temSindPerfil() {
    // return this.usuario.perfisSistema.find((element) => 
    // element.perfil.codigo === PerfilEnum.GESI) != null;
    return false;
  }

  private temPapel(papel: string): boolean {
    return this.usuarioLogado.papeis.indexOf(papel) > -1;
  }

  private temEmpPerfil(): boolean {
    const isPermitido = this.usuarioLogado.papeis.find((element) =>
      element === PerfilEnum.ADM
      || element === PerfilEnum.GDNA
      || element ===  PerfilEnum.GDRA
      || element === PerfilEnum.DIDN
      || element === PerfilEnum.DIDR
      || element === PerfilEnum.ATD) != null;

    const isPerfil =  this.usuario.perfisSistema.find((element) =>
      ((element.perfil.codigo === PerfilEnum.GEEM
        || element.perfil.codigo === PerfilEnum.TRA)
        && isPermitido)) != null;
    return isPerfil && isPermitido;
  }
  private temDRPerfil(): boolean {
    const isPermitido = this.usuarioLogado.papeis.find((element) =>
    element === PerfilEnum.ADM
    || element === PerfilEnum.GDNA
    || element === PerfilEnum.DIDN
    || element === PerfilEnum.ATD) != null;

    const isPerfil = this.usuario.perfisSistema.find((element) =>
      element.perfil.codigo === PerfilEnum.DIDR
      || element.perfil.codigo === PerfilEnum.GDRA) != null;

    return isPerfil && isPermitido;
  }
}
