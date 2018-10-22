import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ParametroService } from 'app/servico/parametro.service';
import { Parametro } from 'app/modelo/parametro.model';
import { BaseComponent } from 'app/componente/base.component';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { DialogService } from 'ng2-bootstrap-modal';

@Component({
  selector: 'app-acesso-negado',
  templateUrl: './acesso-negado.component.html',
  styleUrls: ['./acesso-negado.component.scss'],
})
export class AcessoNegadoComponent extends BaseComponent implements OnInit {

  public telefone = '';

  constructor(
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private parametroService: ParametroService,
    private location: Location,
  ) {
    super(bloqueioService, dialogo);
  }

  voltar() {
    this.location.back();
  }

  ngOnInit() {
    this.buscarTelefone();
  }

  buscarTelefone() {
    this.parametroService.buscarTelefoneCentralRelacionamento().subscribe((response: Parametro) => {
      this.telefone = response.valor;
    }, (error) => {
      this.mensagemError(error);
    });
  }

}
