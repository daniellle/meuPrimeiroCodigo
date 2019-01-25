import {ToastyService} from 'ng2-toasty';
import {BloqueioService} from './../../../servico/bloqueio.service';
import {BaseComponent} from 'app/componente/base.component';
import {environment} from './../../../../environments/environment';
import {UsuarioService} from './../../../servico/usuario.service';
import {Usuario} from './../../../modelo/usuario.model';
import {MascaraUtil} from './../../../compartilhado/utilitario/mascara.util';
import {ActivatedRoute, Router} from '@angular/router';
import {Component, OnInit, ElementRef} from '@angular/core';
import {PerfilEnum} from 'app/modelo/enum/enum-perfil';
import { UsuarioEntidadeService } from 'app/servico/usuario-entidade.service';
import { UsuarioEntidade } from 'app/modelo/usuario-entidade.model';
import { log } from 'util';

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
    public temPerfilUnidade = false;
    public usuariosEntidade: UsuarioEntidade[] = new Array<UsuarioEntidade>();

    constructor(private router: Router,
                private service: UsuarioService,
                private activatedRoute: ActivatedRoute,
                protected bloqueioService: BloqueioService,
                protected dialogo: ToastyService,
                protected usuarioEntidadeService: UsuarioEntidadeService) {
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
        this.router.navigate([`${tipo}`], {relativeTo: this.activatedRoute});
    }

    buscar() {
        this.service.buscarUsuarioById(this.id).subscribe((user) => {
            if (user) {
                this.usuario = user;
                this.existeUsuario = true;
                this.loginFormatado = MascaraUtil.formatarCpf(this.usuario.login);
                if (this.usuario.origemDados || true) {
                    this.buscarUsuarioEntidade();
                }
               //console.log(this.usuario);
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
            this.temPerfilUnidade = this.temUnidPerfil();
        }
    }


    private temSindPerfil() {
        return false;
    }

    private buscarUsuarioEntidade(){
        this.usuarioEntidadeService.pesquisaUsuariosEntidade(this.usuario.login).subscribe(response => {
            this.usuariosEntidade = response;
            this.checkUsuariosEntidade();
        });
    }

    private checkUsuariosEntidade() {
        this.usuariosEntidade.forEach((element) => {
            if (element.empresa) {
                this.temPerfilEmpresa = true;
            }
            if (element.departamentoRegional) {
                this.temPerfilDR = true;
            }
            if (element.uat) {
                this.temPerfilUnidade = true;
            }
        });
    }

    private temEmpPerfil(): boolean {
        const isPermitido = this.temPapel(
            PerfilEnum.ADM,
            PerfilEnum.DIDN,
            PerfilEnum.GDNP,
            PerfilEnum.GDNA,
            PerfilEnum.GCDN,
            PerfilEnum.SUDR,
            PerfilEnum.DIDR,
            PerfilEnum.GDRM,
            PerfilEnum.GDRA,
            PerfilEnum.GDRP,
            PerfilEnum.GCDR,
            PerfilEnum.GCODR,
            PerfilEnum.GUS,
            PerfilEnum.GEEMM,
            PerfilEnum.GEEM, PerfilEnum.PFS, PerfilEnum.GCOI,
            PerfilEnum.ATD
             );
        const isPerfil = this.contemPerfil([PerfilEnum.GEEM, PerfilEnum.GEEMM, PerfilEnum.TRA, PerfilEnum.PFS, PerfilEnum.GCOI,
            PerfilEnum.ST, PerfilEnum.RH], this.usuario);
        return isPerfil && isPermitido;
    }

    private temDRPerfil(): boolean {
        return this.temPapel(
            PerfilEnum.ADM,
            PerfilEnum.DIDN,
            PerfilEnum.GDNP,
            PerfilEnum.GDNA,
            PerfilEnum.GCDN,
            PerfilEnum.SUDR,
            PerfilEnum.GDRM,
            PerfilEnum.GDRA,
            PerfilEnum.MTSDR,
            PerfilEnum.GCDR,
            PerfilEnum.GCODR,
            PerfilEnum.ATD)
            && this.contemPerfil([PerfilEnum.DIDR, PerfilEnum.GDRA, PerfilEnum.GDRM, PerfilEnum.SUDR,
                PerfilEnum.MTSDR, PerfilEnum.GCDR, PerfilEnum.GUS, PerfilEnum.GCODR], this.usuario);
    }

    private temUnidPerfil(): boolean {
        return this.temPapel( PerfilEnum.ADM,
            PerfilEnum.DIDN,
            PerfilEnum.GDNP,
            PerfilEnum.GDNA,
            PerfilEnum.GCDN,
            PerfilEnum.SUDR,
            PerfilEnum.GDRM,
            PerfilEnum.GDRA,
            PerfilEnum.MTSDR,
            PerfilEnum.GCDR,
            PerfilEnum.GCODR,
            PerfilEnum.ATD)
            && this.contemPerfil([PerfilEnum.GUS], this.usuario);
    }
}
