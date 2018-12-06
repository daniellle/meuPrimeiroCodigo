import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { MascaraUtil } from './../../../compartilhado/utilitario/mascara.util';

import { Router, ActivatedRoute } from '@angular/router';
import { EmpresaService } from 'app/servico/empresa.service';
import { Empresa } from './../../../modelo/empresa.model';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-empresa-intermediario',
  templateUrl: './empresa-intermediario.component.html',
  styleUrls: ['./empresa-intermediario.component.scss'],
})
export class EmpresaIntermediarioComponent implements OnInit {
  public id: number;
  public empresa: Empresa;
  public existeEmpresa: boolean;
  public cnpjFormatado: string;

  public empresasUsuario: Empresa[];
  public minhaEmpresa: boolean;
  public empresaUnica = false;
  public minhaEmpresaSelecionada: Empresa;

  constructor(private router: Router, private service: EmpresaService, private activatedRoute: ActivatedRoute) {
    this.minhaEmpresa = this.activatedRoute.snapshot.params.id === 'minhaempresa';
    if (this.minhaEmpresa) {
      this.carregarTelaMinhaEmpresa();
    } else {
      this.activatedRoute.params.subscribe((params) => {
        this.id = params['id'];
        if (this.id) {
          this.buscar();
        }
      });
    }
  }

  ngOnInit() {

  }

  carregarTelaMinhaEmpresa() {
    this.empresasUsuario = new Array<Empresa>();
    this.service.pesquisarMinhaEmpresa().subscribe((usuariosEntidades) => {
      if (usuariosEntidades) {
        usuariosEntidades.forEach((item) => {
          if (item.empresa) {
            this.empresasUsuario.push(item.empresa);
          }
        });
        if (this.empresasUsuario && this.empresasUsuario.length === 1) {
          this.empresaUnica = true;
          this.id = this.empresasUsuario[0].id;
          this.buscar();
        } else {
          const id = Number(localStorage.getItem('minhaEmpresaSelecionada_id'));
          this.empresasUsuario.forEach((empresa) => {
            if (empresa.id === id) {
              this.minhaEmpresaSelecionada = empresa;
            }
          });
          if (id) {
            this.id = id;
            this.buscar();
          }
        }
      }
    });
  }

  selecionaMinhaEmpresa() {
    if (this.minhaEmpresaSelecionada) {
      this.id = this.minhaEmpresaSelecionada.id;
      localStorage.setItem('minhaEmpresaSelecionada_id', this.minhaEmpresaSelecionada.id.toString());
      this.buscar();
    } else {
      this.existeEmpresa = false;
      localStorage.removeItem('minhaEmpresaSelecionada_id');

    }
  }

  mudarPagina(tipo) {
    if (this.minhaEmpresa) {
      this.router.navigate([`${environment.path_raiz_cadastro}/empresa/minhaempresa/${this.empresa.id}/${tipo}`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/empresa/${this.empresa.id}/${tipo}`]);
    }
  }

  buscar() {
    this.service.pesquisarPorId(this.id).subscribe((empresa) => {
      this.empresa = empresa;
      if (this.empresa) {
        this.existeEmpresa = true;
        this.cnpjFormatado = MascaraUtil.formatarCnpj(this.empresa.cnpj);
      }
    });
  }

  voltar() {
    this.router.navigate([`${environment.path_raiz_cadastro}/empresa`]);
  }

  hasPermissaoCadastroBasico() {
    return Seguranca.isPermitido([PermissoesEnum.EMPRESA,
    PermissoesEnum.EMPRESA_ALTERAR,
    PermissoesEnum.EMPRESA_CONSULTAR,
    PermissoesEnum.EMPRESA_DESATIVAR]);
  }

  hasPermissaoCargo() {
    return Seguranca.isPermitido([PermissoesEnum.EMPRESA_CARGO,
    PermissoesEnum.EMPRESA_CARGO_CADASTRAR,
    PermissoesEnum.EMPRESA_CARGO_ALTERAR,
    PermissoesEnum.EMPRESA_CARGO_CONSULTAR,
    PermissoesEnum.EMPRESA_CARGO_DESATIVAR]);
  }

  hasPermissaoFuncao() {
    return Seguranca.isPermitido([PermissoesEnum.EMPRESA_FUNCAO,
    PermissoesEnum.EMPRESA_FUNCAO_CADASTRAR,
    PermissoesEnum.EMPRESA_FUNCAO_ALTERAR,
    PermissoesEnum.EMPRESA_FUNCAO_CONSULTAR,
    PermissoesEnum.EMPRESA_FUNCAO_DESATIVAR]);
  }

  hasPermissaoSetor() {
    return Seguranca.isPermitido([PermissoesEnum.EMPRESA_SETOR,
    PermissoesEnum.EMPRESA_SETOR_CADASTRAR,
    PermissoesEnum.EMPRESA_SETOR_ALTERAR,
    PermissoesEnum.EMPRESA_SETOR_CONSULTAR,
    PermissoesEnum.EMPRESA_SETOR_DESATIVAR]);
  }

  hasPermissaoJornada() {
    return Seguranca.isPermitido([PermissoesEnum.EMPRESA_JORNADA,
    PermissoesEnum.EMPRESA_JORNADA_CADASTRAR,
    PermissoesEnum.EMPRESA_JORNADA_ALTERAR,
    PermissoesEnum.EMPRESA_JORNADA_CONSULTAR,
    PermissoesEnum.EMPRESA_JORNADA_DESATIVAR]);
  }

  hasPermissaoLotacao() {
    return Seguranca.isPermitido([PermissoesEnum.EMPRESA_LOTACAO,
    PermissoesEnum.EMPRESA_LOTACAO_CADASTRAR,
    PermissoesEnum.EMPRESA_LOTACAO_ALTERAR,
    PermissoesEnum.EMPRESA_LOTACAO_CONSULTAR,
    PermissoesEnum.EMPRESA_LOTACAO_DESATIVAR]);
  }

  hasPermissaoSindicato() {
    return Seguranca.isPermitido([PermissoesEnum.EMPRESA_SINDICATO,
    PermissoesEnum.EMPRESA_SINDICATO_CADASTRAR,
    PermissoesEnum.EMPRESA_SINDICATO_ALTERAR,
    PermissoesEnum.EMPRESA_SINDICATO_CONSULTAR,
    PermissoesEnum.EMPRESA_SINDICATO_DESATIVAR]);
  }

  hasPermissaoTrabalhador() {
    return Seguranca.isPermitido([PermissoesEnum.EMPRESA_TRABALHADOR,
    PermissoesEnum.EMPRESA_TRABALHADOR_CADASTRAR,
    PermissoesEnum.EMPRESA_TRABALHADOR_ALTERAR,
    PermissoesEnum.EMPRESA_TRABALHADOR_CONSULTAR,
    PermissoesEnum.EMPRESA_TRABALHADOR_DESATIVAR,
    PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO,
    PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CADASTRAR,
    PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_ALTERAR,
    PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_CONSULTAR,
    PermissoesEnum.EMPRESA_TRABALHADOR_LOTACAO_DESATIVAR]);
  }

  hasPermissaoContrato(){
      return Seguranca.isPermitido([PermissoesEnum.EMPRESA_CONTRATO,
          PermissoesEnum.EMPRESA_CONTRATO_CADASTRAR]);
  }


}
