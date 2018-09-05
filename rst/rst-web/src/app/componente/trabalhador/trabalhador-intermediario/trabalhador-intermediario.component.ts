import {FiltroTrabalhador} from './../../../modelo/filtro-trabalhador.model';
import {PermissoesEnum} from 'app/modelo/enum/enum-permissoes';
import {Seguranca} from './../../../compartilhado/utilitario/seguranca.model';
import {environment} from './../../../../environments/environment';
import {MascaraUtil} from './../../../compartilhado/utilitario/mascara.util';
import {Trabalhador} from './../../../modelo/trabalhador.model';
import {TrabalhadorService} from 'app/servico/trabalhador.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Component, OnInit} from '@angular/core';
import {Usuario} from 'app/modelo/usuario.model';

@Component({
  selector: 'app-trabalhador-intermediario',
  templateUrl: './trabalhador-intermediario.component.html',
  styleUrls: ['./trabalhador-intermediario.component.scss'],
})
export class TrabalhadorIntermediarioComponent implements OnInit {

  public id: number;
  public trabalhador: Trabalhador;
  public cpfFormatdo: string;
  public meusdados: boolean;
  public usuarioLogado: Usuario;

  constructor(
    private router: Router,
    private service: TrabalhadorService,
    private activatedRoute: ActivatedRoute,
  ) {
  }

  ngOnInit() {
    this.trabalhador = new Trabalhador();
    this.usuarioLogado = Seguranca.getUsuario();
    this.meusdados = this.activatedRoute.snapshot.params.id === 'meusdados';
    if (this.meusdados) {
      this.service.buscarMeusDados().subscribe((trabalhador) => {
        this.trabalhador = trabalhador;
        this.cpfFormatdo = MascaraUtil.formatarCpf(this.trabalhador.cpf);
      });
    } else {
      this.id = this.activatedRoute.snapshot.params['id'];
      if (this.id) {
        this.buscar();
      }
    }
  }

  hasPermissionSaude() {
    return Seguranca.isPermitido([PermissoesEnum.TRABALHADOR_MINHA_SAUDE,
        PermissoesEnum.TRABALHADOR_MINHA_SAUDE_CONSULTAR]);
  }

  hasAcessMinhaConta() {
    return (this.usuarioLogado.sub === this.trabalhador.cpf);
  }

  mudarPagina(tipo) {
    if (this.meusdados) {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados/${this.trabalhador.id}/${tipo}`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.trabalhador.id}/${tipo}`]);
    }
  }

  openPageMinhaSaude(page) {
    localStorage.setItem('trabalhador_cpf', this.trabalhador.cpf);
    this.router.navigate([page]);
  }

    openPage(page) {
        this.router.navigate([page]);
    }

  hasPermissaoDependente() {
    return Seguranca.isPermitido([PermissoesEnum.TRABALHADOR_DEPENDENTE,
    PermissoesEnum.TRABALHADOR_DEPENDENTE_CADASTRAR,
    PermissoesEnum.TRABALHADOR_DEPENDENTE_ALTERAR,
    PermissoesEnum.TRABALHADOR_DEPENDENTE_CONSULTAR,
    PermissoesEnum.TRABALHADOR_DEPENDENTE_DESATIVAR]);
  }

  hasPermissaoCertificado() {
    return Seguranca.isPermitido([PermissoesEnum.TRABALHADOR_CERTIFICADO,
    PermissoesEnum.TRABALHADOR_CERTIFICADO_CADASTRAR,
    PermissoesEnum.TRABALHADOR_CERTIFICADO_ALTERAR,
    PermissoesEnum.TRABALHADOR_CERTIFICADO_CONSULTAR,
    PermissoesEnum.TRABALHADOR_CERTIFICADO_DESATIVAR]);
  }

  hasPermissaoTrabalhador() {
    return Seguranca.isPermitido([PermissoesEnum.TRABALHADOR,
    PermissoesEnum.TRABALHADOR_ALTERAR,
    PermissoesEnum.TRABALHADOR_CONSULTAR,
    PermissoesEnum.TRABALHADOR_DESATIVAR]);
  }

  hasPermissaoIGEV() {
    return Seguranca.isPermitido([PermissoesEnum.TRABALHADOR,
    PermissoesEnum.TRABALHADOR_CADASTRAR,
    PermissoesEnum.TRABALHADOR_ALTERAR,
    PermissoesEnum.TRABALHADOR_CONSULTAR,
    PermissoesEnum.TRABALHADOR_DESATIVAR]);
  }

  hasPermissaoCallCenter() {
    return Seguranca.getUsuario().papeis.indexOf('ATD') > -1;
  }

  voltar() {
    this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador`]);
  }

  buscar() {
    const filtro = new FiltroTrabalhador();
    filtro.id = this.id.toString();
    filtro.aplicarDadosFilter = true;
    this.service.buscarPorId(filtro).subscribe((trabalhador) => {
      this.trabalhador = trabalhador;
      this.cpfFormatdo = MascaraUtil.formatarCpf(this.trabalhador.cpf);
    });
  }
}
