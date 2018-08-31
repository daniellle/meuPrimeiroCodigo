import { environment } from './../../../../environments/environment';
import { MascaraUtil } from './../../../compartilhado/utilitario/mascara.util';
import { RedeCredenciada } from './../../../modelo/rede-credenciada.model';
import { RedeCredenciadaService } from './../../../servico/rede-credenciada.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';

@Component({
  selector: 'app-rede-credenciada-intermediaria',
  templateUrl: './rede-credenciada-intermediaria.component.html',
  styleUrls: ['./rede-credenciada-intermediaria.component.scss'],
})
export class RedeCredenciadaIntermediariaComponent implements OnInit {

  public id: number;
  public redeCredenciada: RedeCredenciada;
  public existeRede: boolean;
  public cnpjFormatado: string;

  constructor(private router: Router, private service: RedeCredenciadaService,
              private activatedRoute: ActivatedRoute) {
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
    this.router.navigate([`${environment.path_raiz_cadastro}/redecredenciada`]);
  }

  buscar() {
    this.service.pesquisarPorId(this.id.toString()).subscribe((redeCredenciada) => {
      if (redeCredenciada) {
        this.redeCredenciada = redeCredenciada;
        this.existeRede = true;
        this.cnpjFormatado = MascaraUtil.formatarCnpj(this.redeCredenciada.numeroCnpj);
      }
    });
  }

  hasPermissaoDadosBasicos() {
    return Seguranca.isPermitido([PermissoesEnum.REDE_CREDENCIADA,
      PermissoesEnum.REDE_CREDENCIADA_ALTERAR,
      PermissoesEnum.REDE_CREDENCIADA_CONSULTAR,
      PermissoesEnum.REDE_CREDENCIADA_DESATIVAR]);
  }

  hasPermissaoProdutosEServicos() {
    return Seguranca.isPermitido([PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_CADASTRAR,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_ALTERAR,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_CONSULTAR,
      PermissoesEnum.REDE_CREDENCIADA_PRODUTO_SERVICO_DESATIVAR]);
  }
}
