import { PesquisaSesiDTO } from './../../../modelo/pesquisa-sesi-dto.model';
import { FiltroEndereco } from './../../../modelo/filtro-endereco.model';
import { UatService } from 'app/servico/uat.service';
import { PesquisaSesiProdutoServico } from './../../../modelo/pesquisa-sesi-produto-servico.model';
import { Linha } from './../../../modelo/linha.model';
import { MascaraUtil } from 'app/compartilhado/utilitario/mascara.util';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { ProdutoServicoService } from './../../../servico/produto-servico.service';
import { LinhaService } from './../../../servico/linha.service';
import { Estado } from './../../../modelo/estado.model';
import { IOption } from 'ng-select';
import { PesquisaSesiService } from './../../../servico/pesquisa-sesi.service';
import { EstadoService } from 'app/servico/estado.service';
import { Uat } from 'app/modelo/uat.model';
import { FiltroPesquisaSesi } from './../../../modelo/filtro-pesquisa-sesi.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ToastyService } from 'ng2-toasty';
import { BaseComponent } from 'app/componente/base.component';
import { ActivatedRoute, Router } from '@angular/router';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-pesquisa-sesi',
  templateUrl: './pesquisa-sesi.component.html',
  styleUrls: ['./pesquisa-sesi.component.scss'],
})
export class PesquisaSesiComponent extends BaseComponent implements OnInit {

  public filtroPesquisaSesi: FiltroPesquisaSesi;
  public unidadesSesiOption = Array<IOption>();
  public unidadeSesiSelecionada: Uat;
  public estadoSesiSelecionado: number;
  public estados: Estado[];
  public linhasOption = Array<IOption>();
  public produtosOption = Array<IOption>();

  public unidadesSesi: PesquisaSesiDTO[];
  public habilitaMunicipio: boolean;
  public habilitaEstado: boolean;
  public habilitaBairro: boolean;
  public habilitaLinha: boolean;
  public habilitaProduto: boolean;

  public linhas: any;
  public produtos: any;

  public listaPesquisaSesiProdutoServico: PesquisaSesiProdutoServico[];

  constructor(
    private router: Router, private activatedRoute: ActivatedRoute, protected uatService: UatService,
    protected bloqueioService: BloqueioService, protected dialogo: ToastyService,
    protected estadoService: EstadoService, protected pesquisaSesiService: PesquisaSesiService,
    protected linhaService: LinhaService, protected produtoServicoService: ProdutoServicoService) {
    super(bloqueioService, dialogo, estadoService);
    this.buscarUnidadesSesi();
    this.buscarLinhas();
    this.buscarProdutos();
    this.buscarEstados();
  }

  ngOnInit() {
    this.title = MensagemProperties.app_rst_pesquisa_sesi_title;
    this.filtroPesquisaSesi = new FiltroPesquisaSesi();

    this.habilitaEstado = true;
    this.habilitaMunicipio = false;
    this.habilitaBairro = false;
    this.habilitaLinha = true;
    this.habilitaProduto = true;

    this.listaPesquisaSesiProdutoServico = new Array<PesquisaSesiProdutoServico>();
  }

  formatarCep(value): string {
    return MascaraUtil.formatarCep(value);
  }

  adicionarLinhasProdutosFiltro() {
    if (this.linhas) {
      this.filtroPesquisaSesi.idLinha = new Array<number>();
      this.linhas.forEach((element) => {
        this.filtroPesquisaSesi.idLinha.push(Number.parseInt(element));
      });
    } else {
      this.filtroPesquisaSesi.idLinha = null;
    }

    if (this.produtos) {
      this.filtroPesquisaSesi.idProduto = new Array<number>();
      this.produtos.forEach((element) => {
        this.filtroPesquisaSesi.idProduto.push(Number.parseInt(element));
      });
    } else {
      this.filtroPesquisaSesi.idProduto = null;
    }
  }

  pesquisar() {
    this.unidadesSesi = [];
    if (this.verificarCampos()) {
      this.paginacao.pagina = 1;
      this.adicionarLinhasProdutosFiltro();
      this.pesquisaSesiService.pesquisarPaginado(this.filtroPesquisaSesi, this.paginacao)
        .subscribe((retorno: ListaPaginada<PesquisaSesiDTO>) => {
          this.unidadesSesi = retorno.list;
          this.paginacao = this.getPaginacao(this.paginacao, retorno);
          if (retorno.quantidade === 0) {
            this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
          }
        }, (error) => {
          this.mensagemError(error);
        });
    }
  }

  verificarCampos(): Boolean {
    let verificador: Boolean = true;
    if (this.isVazia(this.filtroPesquisaSesi.idUnidadeSesi) && this.isVazia(this.filtroPesquisaSesi.idEstado) &&
      this.isVazia(this.filtroPesquisaSesi.idMunicipio) && this.isVazia(this.filtroPesquisaSesi.bairro) &&
      !this.isNotEmpty(this.linhas) && !this.isNotEmpty(this.produtos)) {
      this.mensagemError(MensagemProperties.app_rst_pesquisar_todos_vazios);
      verificador = false;
    }
    return verificador;
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.pesquisaSesiService.pesquisarPaginado(this.filtroPesquisaSesi, this.paginacao)
      .subscribe((retorno: ListaPaginada<PesquisaSesiDTO>) => {
        this.unidadesSesi = retorno.list;
      });
  }

  buscarUnidadesSesi() {
    this.pesquisaSesiService.buscarUnidadesSesi().subscribe((dados: any) => {
      let listaOption: IOption[];
      listaOption = [];
      if (dados) {
        dados.forEach((element) => {
          const item = new Option();
          item.value = element.id;
          item.label = element.razaoSocial;
          listaOption.push(item);
        });
      }
      this.unidadesSesiOption = listaOption;
    });
  }

  pesquisarProdutoServicoPorEndereco() {
    // buscar uats atraves do endereco
    const filtroEndereco = new FiltroEndereco();
    filtroEndereco.idEstado = this.filtroPesquisaSesi.idEstado;
    filtroEndereco.idMunicipio = this.filtroPesquisaSesi.idMunicipio;
    filtroEndereco.bairro = this.filtroPesquisaSesi.bairro;
    this.uatService.pesquisarPorEndereco(filtroEndereco).subscribe((retorno: Uat[]) => {
      // buscar linhas e produtos atraves das uats
      const uats = retorno;
      this.buscarLinhasPorIdUat(uats);
      this.buscarProdutoServicoPorIdUat(uats);
    });
  }

  buscarLinhas() {
    this.linhaService.buscarTodas().subscribe((dados: any) => {
      let listaOption: IOption[];
      listaOption = [];
      if (dados) {
        dados.forEach((element) => {
          const item = new Option();
          item.value = element.id;
          item.label = element.descricao;
          listaOption.push(item);
        });
      }
      this.linhasOption = listaOption;
    });
  }

  buscarLinhasPorIdUat(uats: Uat[]) {
    const ids = [];
    uats.forEach((element) => {
      ids.push(element.id);
    });
    this.linhaService.buscarLinhasPorIdUat(ids).subscribe((dados: any) => {
      let listaOption: IOption[];
      listaOption = [];
      if (dados) {
        dados.forEach((element) => {
          const item = new Option();
          item.value = element.id;
          item.label = element.descricao;
          listaOption.push(item);
        });
      }
      this.linhasOption = listaOption;
    });
  }

  buscarProdutoServicoPorIdUat(uats: Uat[]) {
    const ids = [];
    uats.forEach((element) => {
      ids.push(element.id);
    });
    this.produtoServicoService.buscarProdutoServicoPorIdUat(ids).subscribe((dados: any) => {
      let listaOption: IOption[];
      listaOption = [];
      if (dados) {
        dados.forEach((element) => {
          const item = new Option();
          item.value = element.id;
          item.label = element.nome;
          listaOption.push(item);
        });
      }
      this.produtosOption = listaOption;
    });
  }

  buscarProdutos() {
    this.produtoServicoService.pesquisarSemPaginacao().subscribe((dados: any) => {
      let listaOption: IOption[];
      listaOption = [];
      if (dados) {
        dados.forEach((element) => {
          const item = new Option();
          item.value = element.id;
          item.label = element.nome;
          listaOption.push(item);
        });
      }
      this.produtosOption = listaOption;
    });
  }

  buscarEstados() {
    this.estadoService.buscarEstados().subscribe((dados: Estado[]) => {
      this.estados = dados;
      this.estadoSesiSelecionado = 0;
    });
  }

  estadoAlterado() {
    this.filtroPesquisaSesi.idMunicipio = '';
    if (this.estadoSesiSelecionado) {
      this.filtroPesquisaSesi.idEstado = this.estadoSesiSelecionado.toString();
      if (this.filtroPesquisaSesi.idEstado && this.filtroPesquisaSesi.idEstado !== '') {
        this.pesquisarProdutoServicoPorEndereco();
        this.pesquisarMunicipiosPorEstado(Number.parseInt(this.filtroPesquisaSesi.idEstado));
        this.habilitaMunicipio = true;
      }
    } else {
      this.filtroPesquisaSesi.idEstado = '';
      this.habilitaMunicipio = false;
    }
  }

  unidadeSesiAlterado() {
    this.limparCampos();
    if (this.filtroPesquisaSesi.idUnidadeSesi && this.filtroPesquisaSesi.idUnidadeSesi !== '') {
      this.pesquisaSesiService.buscarEnderecoUnidadeSesi(this.filtroPesquisaSesi.idUnidadeSesi).subscribe((dados: Uat) => {
        this.unidadeSesiSelecionada = dados;
        this.preencherEnderecoUnidadeSesi();
        this.habilitaLinha = false;
        this.habilitaProduto = false;
      });
    }
  }

  limparCampos() {
    this.estadoSesiSelecionado = 0;
    this.filtroPesquisaSesi.idEstado = '';
    this.filtroPesquisaSesi.idMunicipio = '';
    this.filtroPesquisaSesi.bairro = '';
    this.filtroPesquisaSesi.idLinha = [];
    this.filtroPesquisaSesi.idProduto = [];
    this.linhas = '';
    this.produtos = '';
    this.linhasOption = new Array<IOption>();
    this.produtosOption = new Array<IOption>();
  }

  preencherEnderecoUnidadeSesi() {
    this.habilitaEstado = false;
    this.habilitaMunicipio = false;
    this.habilitaBairro = false;
    if (this.unidadeSesiSelecionada.endereco) {
      this.estadoSesiSelecionado = this.unidadeSesiSelecionada.endereco[0].endereco.municipio.estado.id;
      this.filtroPesquisaSesi.idEstado = this.unidadeSesiSelecionada.endereco[0].endereco.municipio.estado.id.toString();
      this.filtroPesquisaSesi.idMunicipio = this.unidadeSesiSelecionada.endereco[0].endereco.municipio.id.toString();
      this.filtroPesquisaSesi.bairro = this.unidadeSesiSelecionada.endereco[0].endereco.bairro;
      this.pesquisarMunicipiosPorEstado(this.estadoSesiSelecionado);
    }
  }

  unidadeSesiRemovido() {
    this.limparCampos();
    this.buscarLinhas();
    this.buscarProdutos();
    this.habilitaEstado = true;
    this.habilitaMunicipio = false;
    this.habilitaBairro = false;
    this.habilitaLinha = true;
    this.habilitaProduto = true;
  }

  municipioRemovido() {
    this.pesquisarProdutoServicoPorEndereco();
    this.filtroPesquisaSesi.bairro = '';
    this.habilitaBairro = false;
  }

  municipioAlterado() {
    if (this.filtroPesquisaSesi.idMunicipio && this.filtroPesquisaSesi.idMunicipio !== '') {
      this.filtroPesquisaSesi.bairro = '';
      this.habilitaBairro = true;
      this.pesquisarProdutoServicoPorEndereco();
    }
  }

  verMapa(unidade: PesquisaSesiDTO) {
    const link = 'https://www.google.com.br/maps/place/';

    if (unidade.endereco[0].endereco) {
      const rua = unidade.endereco[0].endereco.descricao ? unidade.endereco[0].endereco.descricao.replace(' ', '+') : '';
      const numero = unidade.endereco[0].endereco.numero ? unidade.endereco[0].endereco.numero : '';
      const bairro = unidade.endereco[0].endereco.bairro ? unidade.endereco[0].endereco.bairro.replace(' ', '+') : '';
      const municipio = unidade.endereco[0].endereco.municipio.descricao ?
        unidade.endereco[0].endereco.municipio.descricao.replace(' ', '+') : '';
      const estado = unidade.endereco[0].endereco.municipio.estado.descricao ?
        unidade.endereco[0].endereco.municipio.estado.descricao.replace(' ', '+') : '';
      const cep = unidade.endereco[0].endereco.cep ? unidade.endereco[0].endereco.cep : '';

      return link.concat(rua + '+' + numero + '+' + bairro + '+' + municipio + '+' + estado + '+' + cep);
    }
    return link;
  }

  exibirTelefone(unidade: PesquisaSesiDTO): string {
    let telefones = '';

    if (unidade.telefone) {
      if (unidade.telefone.length > 1) {
        unidade.telefone.forEach((element, index) => {
          telefones += MascaraUtil.formatarTelefoneNoveDigitos(element.telefone.numero);

          if (unidade.telefone.length - 1 !== index) {
            telefones += ' / ';
          }
        });
      } else {
        telefones = MascaraUtil.formatarTelefoneNoveDigitos(unidade.telefone[0].telefone.numero);
      }
    }
    return telefones;
  }

}
