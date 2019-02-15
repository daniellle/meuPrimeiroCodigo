import { PerfilEnum } from 'app/modelo/enum/enum-perfil';
import { Perfil } from './../../../../modelo/perfil.model';
import { environment } from './../../../../../environments/environment';
import { DepartRegionalService } from './../../../../servico/depart-regional.service';
import { FiltroDepartRegional } from 'app/modelo/filtro-depart-regional.model';
import { SimNao } from 'app/modelo/enum/enum-simnao.model';
import { EnumValues } from 'enum-values';
import { UsuarioPerfilSistema } from './../../../../modelo/usuario-perfil-sistema.model';
import { UsuarioService } from './../../../../servico/usuario.service';
import { UsuarioEntidade } from './../../../../modelo/usuario-entidade.model';
import { UsuarioEntidadeService } from './../../../../servico/usuario-entidade.service';
import { IHash } from './../../../empresa/empresa-jornada/empresa-jornada.component';
import { MascaraUtil } from './../../../../compartilhado/utilitario/mascara.util';
import { DepartamentoRegional } from './../../../../modelo/departamento-regional.model';
import { Usuario } from './../../../../modelo/usuario.model';
import { BloqueioService } from './../../../../servico/bloqueio.service';
import { ListaPaginada } from './../../../../modelo/lista-paginada.model';
import { MensagemProperties } from './../../../../compartilhado/utilitario/recurso.pipe';
import { Paginacao } from 'app/modelo/paginacao.model';
import { Router, ActivatedRoute } from '@angular/router';
import { OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { Component } from '@angular/core';
import { ToastyService } from 'ng2-toasty';

@Component({
  selector: 'app-cadastro-departamento-regional-usuario',
  templateUrl: './cadastro-departamento-regional-usuario.component.html',
  styleUrls: ['./cadastro-departamento-regional-usuario.component.scss'],
})
export class CadastroDepartamentoUsuarioComponent extends BaseComponent implements OnInit {

  idUsuario: number;
  idUsuarioTrabalhador: number;
  usuario: Usuario;
  filtro: FiltroDepartRegional;
  listaDepartamento: DepartamentoRegional[];
  listaSelecionados: DepartamentoRegional[];
  paginacao: Paginacao = new Paginacao(1, 10);
  public checks: IHash = {};
  perfis: Perfil[];
  public estados: any[];

  constructor(
    private router: Router,
    private usuarioService: UsuarioService,
    private departamentoService: DepartRegionalService,
    private activatedRoute: ActivatedRoute,
    private usuarioEntidadeService: UsuarioEntidadeService,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
  ) {
    super(bloqueioService, dialogo);
    this.buscarEstados();
  }

  ngOnInit() {
    this.listaSelecionados = new Array<DepartamentoRegional>();
    this.filtro = new FiltroDepartRegional();
    this.listaDepartamento = new Array<DepartamentoRegional>();
    this.setIdUsuario();
    this.buscarUsuario();
  }

  buscarEstados() {
    if (!this.modoConsulta) {
      this.departamentoService.buscarEstados().subscribe((dados: any) => {
        this.estados = dados;
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }

  buscarUsuario(): void {
    this.usuarioService.buscarUsuarioById(this.idUsuario).subscribe((retorno: Usuario) => {
      this.usuario = retorno;
            this.perfis = getPerfis.call(this, retorno);
      this.usuario = retorno;
      if (this.usuario && !this.usuario.perfisSistema) {
        this.usuario.perfisSistema = new Array<UsuarioPerfilSistema>();
      }
    }, (error) => {
      this.mensagemError(error);
    });

    function getPerfis(retorno: Usuario): Perfil[] {
      let map = new Map();
      retorno.perfisSistema.forEach(value => {
          map.set(value.perfil.codigo, value.perfil.nome);
      });
      let p = new Array<Perfil>();
      map.forEach((value, key) => {
          switch (PerfilEnum[<string>key]) {
              case PerfilEnum.GCDR:
                  p.push(new Perfil(null, value, key));
                  break;
              case PerfilEnum.SUDR:
                  p.push(new Perfil(null, value, key));
                  break;
              case PerfilEnum.GDRM:
                  p.push(new Perfil(null, value, key));
                  break;
              case PerfilEnum.GDRA:
                  p.push(new Perfil(null, value, key));
                  break;
              case PerfilEnum.GCODR:
                  p.push(new Perfil(null, value, key));
              // case PerfilEnum.DIDR:
              //     p.push(new Perfil(null, value, key));
              //     break;
          }
      });
      return p;
  }
  }

  pesquisar(): void {
    if (this.validarPesquisa()) {
      this.paginacao.pagina = 1;
      this.pesquisarDepartamentoPaginado();
    }
  }

  pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisarDepartamentoPaginado();
  }

  limpar() {
    if (this.idUsuarioTrabalhador) {
      this.router.navigate([`${environment.path_raiz_cadastro}/usuario/${this.idUsuario}/departamentoregional`]);
    }
  }

  selecionar(selecionado: DepartamentoRegional, event: any, i: number) {
    if (selecionado && event.checked) {
      this.listaSelecionados.push(selecionado);
      this.checks[i] = true;
    } else {
      const index: number = this.listaSelecionados.indexOf(selecionado);
      if (index !== -1) {
        this.listaSelecionados.splice(index, 1);
        this.checks[i] = false;
      }
    }
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/usuario/${this.idUsuario}/departamentoregional`]);
  }

  adicionarUsuarioEntidade() {
    this.salvar(this.prepareSave());
  }

  private prepareSave(): UsuarioEntidade[] {
    const lista = new Array<UsuarioEntidade>();
    this.listaSelecionados.forEach((item) => {
      const usuarioEntidade = new UsuarioEntidade();
      usuarioEntidade.nome = this.usuario.nome;
      usuarioEntidade.cpf = this.usuario.login;
      usuarioEntidade.email = this.usuario.email;
      usuarioEntidade.termo = EnumValues.getNameFromValue(SimNao, SimNao.true);
      usuarioEntidade.departamentoRegional = item;
      usuarioEntidade.perfil = this.filtro.perfil;
      lista.push(usuarioEntidade);
    });
    return lista;
  }

  private salvar(lista: any): void {
    if (this.validarSelecao(lista)) {
      this.usuarioEntidadeService.salvar(lista).subscribe((response: UsuarioEntidade) => {
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
        this.limpar();
      }, error => 
        this.mensagemError(error)
      );
    }
  }

  validarSelecao(lista) {
    if (this.listaUndefinedOuVazia(this.listaSelecionados)) {
      this.mensagemError(MensagemProperties.app_rst_selecione_um_item);
      return false;
    }
    let vazio = false;
    lista.forEach((element:UsuarioEntidade) => {
        if(element.perfil == undefined){
            vazio = true;
        }
    });
    if(vazio){
        this.mensagemError("É necessário selecionar pelo menos um perfil");
        return false;
    }
    return true;
  }

  private setIdUsuario() {
    this.idUsuario = this.activatedRoute.snapshot.params['id'];
  }

  private validarPesquisa(): boolean {
    let verificador = true;

    if (!this.filtro.cnpj && !this.filtro.razaoSocial) {
      this.mensagemErroComParametrosModel('app_rst_msg_pesquisar_todos_vazios');
      verificador = false;
    }

    if (!this.isVazia(this.filtro.cnpj)) {
      if (this.filtro.cnpj.length < 18) {
        this.mensagemError(MensagemProperties.app_rst_labels_cnpj_incompleto);
        verificador = false;
      }
    }
    return verificador;
  }

  private pesquisarDepartamentoPaginado(): void {
    this.filtro.cnpj = MascaraUtil.removerMascara(this.filtro.cnpj);
    if (this.isUndefined(this.filtro.idEstado)) {
      this.filtro.idEstado = '0';
    }
    this.departamentoService.pesquisar(this.filtro, this.paginacao).
      subscribe((retorno: ListaPaginada<DepartamentoRegional>) => {
        if (retorno.list && retorno.list.length > 0) {
          if (this.filtro.idEstado === '0') {
            this.filtro.idEstado = undefined;
          }
          this.listaDepartamento = retorno.list;
          this.paginacao = this.getPaginacao(this.paginacao, retorno);
        } else {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

}
