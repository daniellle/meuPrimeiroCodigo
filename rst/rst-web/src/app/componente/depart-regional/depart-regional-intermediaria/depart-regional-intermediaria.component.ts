import { Seguranca } from './../../../compartilhado/utilitario/seguranca.model';
import { environment } from './../../../../environments/environment';
import { MascaraUtil } from './../../../compartilhado/utilitario/mascara.util';
import { DepartRegionalService } from './../../../servico/depart-regional.service';
import { Router, ActivatedRoute } from '@angular/router';
import { DepartamentoRegional } from 'app/modelo/departamento-regional.model';
import { Component, OnInit } from '@angular/core';
import { PermissoesEnum } from 'app/modelo/enum/enum-permissoes';

@Component({
  selector: 'app-depart-regional-intermediaria',
  templateUrl: './depart-regional-intermediaria.component.html',
  styleUrls: ['./depart-regional-intermediaria.component.scss'],
})
export class DepartRegionalIntermediariaComponent implements OnInit {

  public id: number;
  public departamentoRegional: DepartamentoRegional;
  public existeDepartamento: boolean;
  public cnpjFormatado: string;

  constructor(
    private router: Router,
    private service: DepartRegionalService,
    private route: ActivatedRoute,
    private activatedRoute: ActivatedRoute,
   ) {
    this.route.params.subscribe( (params) => {
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
    this.service.pesquisarPorId(this.id.toString()).subscribe((departamentoRegional) => {
      this.departamentoRegional = departamentoRegional;
      this.existeDepartamento = true;
      this.cnpjFormatado = MascaraUtil.formatarCnpj(this.departamentoRegional.cnpj);
    });
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/departamentoregional`]);
  }

  hasPermissaoDadosBasicos() {
    return Seguranca.isPermitido([PermissoesEnum.DEPARTAMENTO_REGIONAL,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_ALTERAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_CONSULTAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_DESATIVAR]);
  }

  hasPermissaoProdutosServicos() {
    return Seguranca.isPermitido([PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CADASTRAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_ALTERAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_CONSULTAR,
      PermissoesEnum.DEPARTAMENTO_REGIONAL_PRODUTOS_SERVICOS_DESATIVAR]);
  }

}
