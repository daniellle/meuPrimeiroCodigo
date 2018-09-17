import {ToastyService} from 'ng2-toasty';
import {BloqueioService} from './../../../servico/bloqueio.service';
import {BaseComponent} from 'app/componente/base.component';
import {environment} from './../../../../environments/environment';
import {UsuarioService} from './../../../servico/usuario.service';
import {Usuario} from './../../../modelo/usuario.model';
import {MascaraUtil} from './../../../compartilhado/utilitario/mascara.util';
import {ActivatedRoute, Router} from '@angular/router';
import {Component, OnInit} from '@angular/core';
import {PerfilEnum} from 'app/modelo/enum/enum-perfil';

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
                protected dialogo: ToastyService) {
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
        return false;
    }

    private temEmpPerfil(): boolean {
        const isPermitido = this.temPapel(PerfilEnum.ADM, PerfilEnum.GDNA, PerfilEnum.MTSDN, PerfilEnum.GDRA, PerfilEnum.GDRM,
            PerfilEnum.SUDR, PerfilEnum.DIDN, PerfilEnum.DIDR, PerfilEnum.ATD, PerfilEnum.GEEMM, PerfilEnum.MTSDR, PerfilEnum.GCDR);
        const isPerfil = this.contemPerfil([PerfilEnum.GEEM, PerfilEnum.GEEMM, PerfilEnum.TRA, PerfilEnum.PFS,
            PerfilEnum.ST, PerfilEnum.RH], this.usuario);
        return isPerfil && isPermitido;
    }

    private temDRPerfil(): boolean {
        return this.temPapel(PerfilEnum.ADM, PerfilEnum.GDNA, PerfilEnum.MTSDN, PerfilEnum.DIDN, PerfilEnum.ATD, PerfilEnum.GDRM)
            && this.contemPerfil([PerfilEnum.DIDR, PerfilEnum.GDRA, PerfilEnum.GDRM, PerfilEnum.SUDR,
                PerfilEnum.MTSDR, PerfilEnum.GCDR, PerfilEnum.GUS], this.usuario);
    }

    private temUnidPerfil(): boolean {
        return this.temPapel(PerfilEnum.ADM, PerfilEnum.GDNA, PerfilEnum.MTSDN, PerfilEnum.DIDN, PerfilEnum.ATD, PerfilEnum.GDRM)
            && this.contemPerfil([PerfilEnum.GUS], this.usuario);
    }
}
