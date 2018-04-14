import { UatService } from 'app/servico/uat.service';
import { Uat } from './../../../modelo/uat.model';
import { environment } from './../../../../environments/environment';
import { MascaraUtil } from './../../../compartilhado/utilitario/mascara.util';
import { Router, ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-uat-intermediaria',
  templateUrl: './uat-intermediaria.component.html',
  styleUrls: ['./uat-intermediaria.component.scss'],
})
export class UatIntermediariaComponent implements OnInit {

  public id: number;
  public uat: Uat;
  public existeUat: boolean;
  public cnpjFormatado: string;

  constructor(
    private router: Router,
    private service: UatService,
    private route: ActivatedRoute,
    private activatedRoute: ActivatedRoute,
  ) {
    this.route.params.subscribe((params) => {
      this.id = params['id'];
      if (this.id) {
        this.buscar();
      }
    });
  }

  ngOnInit() {

  }

  mudarPagina(tipo) {
    this.router.navigate([`${tipo}`], { relativeTo: this.activatedRoute });
  }

  buscar() {
    this.service.pesquisarPorId(this.id.toString()).subscribe((uat) => {
      this.uat = uat;
      this.existeUat = true;
      this.cnpjFormatado = MascaraUtil.formatarCnpj(this.uat.cnpj);
    });
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/uat`]);
  }

}
