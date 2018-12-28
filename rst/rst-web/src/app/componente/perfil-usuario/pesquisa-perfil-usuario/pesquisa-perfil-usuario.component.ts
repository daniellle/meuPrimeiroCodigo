import { Component, OnInit } from '@angular/core';
import { DepartamentoRegional } from 'app/modelo/departamento-regional.model';
import { Perfil, Usuario } from 'app/modelo';
import { Empresa } from 'app/modelo/empresa.model';
import { Router, ActivatedRoute } from '@angular/router';
import { UsuarioService } from 'app/servico/usuario.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';
import { DepartRegionalService } from 'app/servico/depart-regional.service';
import { PerfilService } from 'app/servico/perfil.service';
import { UsuarioEntidadeService } from 'app/servico/usuario-entidade.service';
import { BaseComponent } from 'app/componente/base.component';
import { FiltroDepartRegional } from 'app/modelo/filtro-depart-regional.model';
import { FiltroUsuario } from 'app/modelo/filtro-usuario.model';

@Component({
  selector: 'app-pesquisa-perfil-usuario',
  templateUrl: './pesquisa-perfil-usuario.component.html',
  styleUrls: ['./pesquisa-perfil-usuario.component.scss']
})
export class PesquisaPerfilUsuarioComponent extends BaseComponent implements OnInit {
  public filtro: FiltroUsuario;
  public usuarios: Usuario[];
  public usuarioSelecionado: Usuario;
  public itensCarregados: number;
  public totalItens: number;
  public pesquisaUsuarioForm: FormGroup;
  public departamentos: DepartamentoRegional[];
  public perfis: Perfil[];
  public empresa: Empresa;
  public departamento: DepartamentoRegional;
  public semPerfilBarramento: Perfil;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private dialogService: DialogService,
    private activatedRoute: ActivatedRoute,
    private departamentoService: DepartRegionalService,
    private perfilService: PerfilService,
  ) {
    super(bloqueioService, dialogo);
    this.empresa = new Empresa();
    this.buscarDepartamentos();
    this.departamento = new DepartamentoRegional();
    this.buscarPerfis();
   }

  ngOnInit() {
    this.semPerfilBarramento = new Perfil();
    this.criandoPerfilVazio();
  this.filtro = new FiltroUsuario();
  this.usuarios = new Array<Usuario>();
  this.pesquisaUsuarioForm = this.formBuilder.group({});
  this.filtro.codigoPerfil = '';
  }

  buscarDepartamentos() {
    this.departamentoService.listarTodos(new FiltroDepartRegional()).subscribe((dados: any) => {
      this.departamentos = dados;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  buscarPerfis(): void {
    this.perfilService.buscarTodos().subscribe((retorno: any) => {
      this.perfis = retorno;
        this.perfis.push(this.semPerfilBarramento);
    }, (error) => {
      this.mensagemError(error);
    }, () => {
        this.orderByNome(this.perfis);
    });
  }

  criandoPerfilVazio(){
    this.semPerfilBarramento.nome = "Sem Perfil";
    this.semPerfilBarramento.codigo = 'SP';
}
orderByNome(list: any[]) {

  if (!this.listaUndefinedOuVazia(list)) {
    list.sort((left, right): number => {
      if (left.id > right.id) {
        return 1;
      }
      if (left.id < right.id) {
        return -1;
      }
      return 0;
    });
  }
}

}
