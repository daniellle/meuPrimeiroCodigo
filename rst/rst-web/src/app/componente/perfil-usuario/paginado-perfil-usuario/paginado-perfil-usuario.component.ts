import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { PerfilUsuarioFilter } from 'app/modelo/filter-perfil-usuario.model';
import { BaseComponent } from 'app/componente/base.component';
import { Router } from '@angular/router';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { Usuario } from 'app/modelo';
import { UsuarioService } from 'app/servico/usuario.service';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { UsuarioRelatorio } from 'app/modelo/usuario-relatorio.model';

@Component({
  selector: 'app-paginado-perfil-usuario',
  templateUrl: './paginado-perfil-usuario.component.html',
  styleUrls: ['./paginado-perfil-usuario.component.scss']
})  

export class PaginadoPerfilUsuarioComponent extends BaseComponent implements OnInit {

  @Input() @Output() public filtro: PerfilUsuarioFilter;
  public usuarios: UsuarioRelatorio[];
  @Output() limparFiltro: EventEmitter<Boolean> = new EventEmitter<Boolean>();

  constructor(
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    protected usuarioService: UsuarioService,

  ) {
    super(bloqueioService, dialogo);

   }
  ngOnInit() {
    this.usuarios = new Array<UsuarioRelatorio>();
  }

  public pesquisar() {
    if (this.verificarCampos()) {
      console.log(this.filtro)
      this.usuarios = new Array<UsuarioRelatorio>();
      this.paginacao.pagina = 1;
      this.usuarioService.pesquisarPaginadoRelatorio(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<UsuarioRelatorio>) => {
            this.usuarios = retorno.list;
        this.paginacao = this.getPaginacao(this.paginacao, retorno);
        if (retorno.quantidade === 0 || this.usuarios.length == 0) {
          this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
        }
      }, (error) => {
        this.mensagemError(error);
      });
    }
  }
  
  public verificarCampos(): boolean {
    let verificador = true;
    if (this.filtrosEmBranco()) {
      this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
      verificador = false;
    }
  
    if (!this.isVazia(this.filtro.login)) {
      if (this.filtro.login.length < 14) {
        this.mensagemError(MensagemProperties.app_rst_labels_cpf_incompleto);
        verificador = false;
      }
  
    }
    return verificador;
  }
  
  private filtrosEmBranco(): boolean {
    return this.filtro && !this.filtro.login && !this.filtro.nome && this.isVazia(this.filtro.idUnidadeSesi) &&
      (this.filtro.empresa && !this.filtro.empresa.id) &&
      (this.filtro.departamentoRegional && !this.filtro.departamentoRegional.id) &&
      (!this.filtro.codigoPerfil || this.isUndefined(this.filtro.codigoPerfil));
  }
  
    public limpar(){
      this.usuarios = new Array<UsuarioRelatorio>();
      this.limparFiltro.emit(true);
    }

    public isListaVazia(): boolean {
      return !(this.usuarios && this.usuarios.length > 0);
    }

    public pageChanged(event: any): void {
      this.paginacao.pagina = event.page;
      this.usuarioService.pesquisarPaginadoRelatorio(this.filtro, this.paginacao).subscribe((retorno: ListaPaginada<UsuarioRelatorio>) => {
        this.usuarios = retorno.list;
      }, (error) => {
        this.mensagemError(error);
      });
    }

}
