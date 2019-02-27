import { Component, OnInit, Input } from '@angular/core';
import { Uat } from 'app/modelo/uat.model';
import { UatVeiculoTipo } from 'app/modelo/uat-veiculo-tipo';
import { UatVeiculoService } from 'app/servico/uat-veiculo.service';
import { UatVeiculo } from 'app/modelo/uat-veiculo';
import { UatVeiculoTipoAtendimento } from 'app/modelo/uat-veiculo-tipo-atendimento';
import { UnidadeAtendimentoTrabalhador } from 'app/modelo/unid-atend-trabalhador.model';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty';
import { BaseComponent } from 'app/componente/base.component';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';

@Component({
  selector: 'app-uat-veiculo',
  templateUrl: './uat-veiculo.component.html',
  styleUrls: ['./uat-veiculo.component.scss']
})
export class UatVeiculoComponent extends BaseComponent implements OnInit {

  @Input() idUat: Number;
  @Input() modoConsulta: boolean;

  listUatVeiculoTipo = new Array<UatVeiculoTipo>();
  listVeiculoUnidMoveis = new Array<UatVeiculo>();
  veiculoPasseio: UatVeiculo;
  quantidadeVeiculoPasseio: Number;
  idVeiculoTipoSelecionado: Number;


  constructor(
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private uatVeiculoService: UatVeiculoService,
  ) { 
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.loadVeiculoTipo();
    this.loadVeiculoTipoAtendimento();
  }

  salvar(): void {
    if (this.isValid()) {
      console.log('Salvando...' , this.prepareSave());
      this.uatVeiculoService.salvar(this.prepareSave()).subscribe((res) => {
        console.log(res);
      })
    }
  }

  isValid() {
    if (this.isVeiculoUnidadeMovel()) {
      return this.unidadeMoveisIsValid();
    }

    if (this.isVeiculoAtendimentoPasseio()) {
      return this.veiculoPasseiAtendimentoIsValid();
    }

    return false;
  }

  isVeiculoAtendimentoPasseio(): boolean {
    return this.idVeiculoTipoSelecionado === 2;
  }

  isVeiculoUnidadeMovel(): boolean {
    return this.idVeiculoTipoSelecionado === 1;
  }

  private prepareSave(): any[] {
    if(this.isVeiculoUnidadeMovel()) {
      return this.listVeiculoUnidMoveis.map((item) => {
        return {
          quantidade: item.quantidade,
          idUat: this.idUat,
          idVeiculoTipoAtendimento: item.uatVeiculoTipoAtendimento.id
        }
      });
    }

    if(this.isVeiculoAtendimentoPasseio()) {
      const list = new Array<any>();
      const uatVeiculo = { 
        quantidade: Number(this.quantidadeVeiculoPasseio),
        idUat: this.idUat
      };
      list.push(uatVeiculo);
      return list;
    }
  }

  private veiculoPasseiAtendimentoIsValid(): boolean {
    if(!this.quantidadeVeiculoPasseio) {
      this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_quantidade);
      return false;
    }
    return true;
  }

  private unidadeMoveisIsValid(): boolean {
    const listUnidadesMoveisParaSalvar = this.listVeiculoUnidMoveis.filter(item => item.quantidade);
    if (!listUnidadesMoveisParaSalvar.length) {
      this.mensagemErroComParametros('app_rst_msg_nenhuma_quantidade_informada');
      return false;
    }
    return true;
  }

  private loadVeiculoTipo(): void {
    this.uatVeiculoService.listUatVeiculoTipo().subscribe((res: UatVeiculoTipo[]) => {
      this.listUatVeiculoTipo = res;
    });
  }

  private loadVeiculoTipoAtendimento() {
      this.uatVeiculoService.listUatVeiculoTipoAtendimento().subscribe((res: UatVeiculoTipoAtendimento[]) => {
        res.forEach((item) => {
          const uatVeiculo = new UatVeiculo();
          uatVeiculo.uatVeiculoTipoAtendimento = item;
          const unidadeAtendimentoTrabalhador = new UnidadeAtendimentoTrabalhador();
          unidadeAtendimentoTrabalhador.id = this.idUat;
          uatVeiculo.unidadeAtendimentoTrabalhador = unidadeAtendimentoTrabalhador;
          this.listVeiculoUnidMoveis.push(uatVeiculo);
        });
    });
  }
}
