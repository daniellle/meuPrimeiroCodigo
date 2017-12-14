import { environment } from './../../../../environments/environment';
import { ParceiroService } from './../../../servico/parceiro.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Parceiro } from './../../../modelo/parceiro.model';
import { Component, OnInit } from '@angular/core';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
  selector: 'app-parceiro-credenciado-intermediario',
  templateUrl: './parceiro-credenciado-intermediario.component.html',
  styleUrls: ['./parceiro-credenciado-intermediario.component.scss'],
})
export class ParceiroCredenciadoIntermediarioComponent implements OnInit {

  public id: number;
  public parceiro: Parceiro;
  public existeParceiro: boolean;
  public cnpjFormatado: string;
  public isCpf: boolean;
  constructor(private router: Router,
              private activatedRoute: ActivatedRoute, private service: ParceiroService) {
    this.activatedRoute.params.subscribe( (params) => {
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

  voltar() {
    this.router.navigate([`${environment.path_raiz_cadastro}/parceirocredenciado`]);
  }

  buscar() {
    this.service.pesquisarPorId(this.id).subscribe((redeCredenciada) => {
      this.parceiro = redeCredenciada;
      this.existeParceiro = true;
      if ( this.parceiro.numeroCnpjCpf.length > 11 ) {
        this.isCpf = false;
      } else {
        this.isCpf = true;
      }
    });
  }

  hasPermissaoDadosBasicos() {
    return Seguranca.isPermitido([PermissoesEnum.PARCEIRO_CREDENCIADA,
      PermissoesEnum.PARCEIRO_CREDENCIADA_ALTERAR,
      PermissoesEnum.PARCEIRO_CREDENCIADA_CONSULTAR,
      PermissoesEnum.PARCEIRO_CREDENCIADA_DESATIVAR]);
  }

  hasPermissaoProdutosEServicos() {
    return Seguranca.isPermitido([PermissoesEnum.PARCEIRO_PRODUTO_SERVICO,
      PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_CADASTRAR,
      PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_ALTERAR,
      PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_CONSULTAR,
      PermissoesEnum.PARCEIRO_PRODUTO_SERVICO_DESATIVAR]);
  }
}
