import { Component, OnInit, Input } from '@angular/core';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { UatEquipamentoArea } from 'app/modelo/uat-equipamento-area';
import { UatEquipamentoDTO } from 'app/modelo/uat-equipamento-dto';
import { UatEquipamentoService } from 'app/servico/uat-equipamento.service';
import { UatEquipamentoTipo } from 'app/modelo/uat-equipamento-tipo';

@Component({
  selector: 'app-uat-equipamento',
  templateUrl: './uat-equipamento.component.html',
  styleUrls: ['./uat-equipamento.component.scss']
})
export class UatEquipamentoComponent extends BaseComponent implements OnInit {

  @Input() idUat: Number;
  @Input() modoConsulta: boolean;
  @Input() hasPermissaoCadastrarAlterar: boolean;
  @Input() hasPermissaoDesativar: boolean;

  idEquipamentoAreaSelecionado: Number;
  listUatEquipamentoArea = new Array<UatEquipamentoArea>();
  listUatEquipamentos = new Array<UatEquipamentoDTO>()
  private equipamentoAreaSelecionado: UatEquipamentoArea;

  constructor(
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private uatEquipamentoService: UatEquipamentoService,
  ) { 
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.loadListUatEquipamentoArea();
  }

  changeArea(idArea: Number) {
    this.equipamentoAreaSelecionado = this.listUatEquipamentoArea.filter(item => item.id == idArea)[0];
    this.uatEquipamentoService.listUatEquipamentoTipoPorArea(idArea).subscribe((res: UatEquipamentoTipo[]) => {
      console.log(res);
      this.createNewListUatEquipamento(res);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private loadListUatEquipamentoArea(): void {
    this.uatEquipamentoService.listUatEquipamentoArea().subscribe((res) => {
      this.listUatEquipamentoArea = res;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private createNewListUatEquipamento(res: UatEquipamentoTipo[]): void {
    this.listUatEquipamentos = res.map((item) => {
      const uatEquipamentoDTO = new UatEquipamentoDTO()
      uatEquipamentoDTO.idTipo = item.id;
      uatEquipamentoDTO.descricao = item.descricao;
      return uatEquipamentoDTO;
    });
  }
}
