import { Component, OnInit, Input, ViewChild, NgModule } from '@angular/core';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { UatEquipamentoArea } from 'app/modelo/uat-equipamento-area';
import { UatEquipamentoDTO } from 'app/modelo/uat-equipamento-dto';
import { UatEquipamentoService } from 'app/servico/uat-equipamento.service';
import { UatEquipamentoTipo } from 'app/modelo/uat-equipamento-tipo';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { UatEquipamentoGroupedAreaDTO } from 'app/modelo/uat-equipamento-grouped-tipo-dto';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

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
  listUatEquipamentosGroupedByArea = new Array<UatEquipamentoGroupedAreaDTO>();
  private equipamentoSelecionadoParaRemover: UatEquipamentoDTO;

  constructor(
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private uatEquipamentoService: UatEquipamentoService,
    private modalService: NgbModal,
  ) { 
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.loadListUatEquipamentoArea();
    this.loadUatEquipamentoGroupedByTipo()
  }

  changeArea(idArea: Number) {
    this.uatEquipamentoService.listUatEquipamentoTipoPorArea(idArea).subscribe((res: UatEquipamentoTipo[]) => {
      this.createNewListUatEquipamento(res);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  remover(): void {
    this.uatEquipamentoService.excluir(this.equipamentoSelecionadoParaRemover.id, this.idUat).subscribe((res) => {
      this.loadUatEquipamentoGroupedByTipo()
    }, (error) => {
      this.mensagemError(error);
    })
  }

  cancelar(): void {
    this.resetForm();
  }

  salvar(): void {
    if (this.isValid()) {
      this.uatEquipamentoService.salvar(this.prepareSave()).subscribe((res) => {
        this.resetForm();
        this.loadUatEquipamentoGroupedByTipo();
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      }, (error) => {
        this.mensagemError(error);
      })
    }
  }

  isValid() {
    const listFiltered = this.listUatEquipamentos.filter(item => item.quantidade);
    if (!listFiltered.length) {
      this.mensagemErroComParametros('app_rst_msg_nenhuma_quantidade_informada');
      return false;
    }
    return true;
  }

  openModal(modal: any, uatEquipamento: UatEquipamentoDTO) {
    this.equipamentoSelecionadoParaRemover = uatEquipamento;
    this.modalService.open(modal, { size: 'sm' });
  }

  showGrid(): boolean {
    return this.listUatEquipamentosGroupedByArea && this.listUatEquipamentosGroupedByArea.length > 0;
  }

  private prepareSave(): any {
    return this.listUatEquipamentos.filter(item => item.quantidade > 0);
  }

  private loadUatEquipamentoGroupedByTipo(): void {
    this.uatEquipamentoService.listAllUatEquipamentosGroupedByArea(this.idUat).subscribe((res: UatEquipamentoGroupedAreaDTO[]) => {
      this.listUatEquipamentosGroupedByArea = res;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private resetForm(): void {
    this.listUatEquipamentos.map((item) => {
      item.quantidade = undefined;
    });
    this.idEquipamentoAreaSelecionado = undefined;
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
      uatEquipamentoDTO.idUat = this.idUat;
      uatEquipamentoDTO.idTipo = item.id;
      uatEquipamentoDTO.descricao = item.descricao;
      return uatEquipamentoDTO;
    });
  }
}
