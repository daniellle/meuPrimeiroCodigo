import { Component, OnInit, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastyService } from 'ng2-toasty';
import { UatVeiculoTipo } from 'app/modelo/uat-veiculo-tipo';
import { UatVeiculoService } from 'app/servico/uat-veiculo.service';
import { UatVeiculo } from 'app/modelo/uat-veiculo';
import { UatVeiculoTipoAtendimento } from 'app/modelo/uat-veiculo-tipo-atendimento';
import { UnidadeAtendimentoTrabalhador } from 'app/modelo/unid-atend-trabalhador.model';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { UatVeiculoGroupedTipoDTO } from 'app/modelo/uat-veiculo-grouped-tipo-dto';
import { UatVeiculoDTO } from 'app/modelo/uat-veiculo-dto';
import { ModalConfirmarService } from 'app/compartilhado/modal-confirmar/modal-confirmar.service';

@Component({
  selector: 'app-uat-veiculo',
  templateUrl: './uat-veiculo.component.html',
  styleUrls: ['./uat-veiculo.component.scss']
})
export class UatVeiculoComponent extends BaseComponent implements OnInit {

  @Input() idUat: Number;
  @Input() modoConsulta: boolean;
  @Input() hasPermissaoCadastrarAlterar: boolean;
  @Input() hasPermissaoDesativar: boolean;

  listUatVeiculoTipo = new Array<UatVeiculoTipo>();
  listVeiculoWithAtendimento = new Array<UatVeiculo>();
  listUatVeiculoGroupedByTipo = new Array<UatVeiculoGroupedTipoDTO>();
  veiculoPasseio: UatVeiculo;
  quantidadeVeiculoPasseio: Number;
  idVeiculoTipoSelecionado: Number;
  private tipoSelecionado = new UatVeiculoTipo();
  private veiculoSelecionadoParaRemover: UatVeiculoDTO;


  constructor(
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private uatVeiculoService: UatVeiculoService,
    private modalConfirmarSerivce: ModalConfirmarService,
  ) { 
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.loadVeiculoTipo();
    this.loadVeiculoTipoAtendimento();
    this.loadUatVeiculoGroupedByTipo();
  }

  salvar(): void {
    if (this.isValid()) {
      this.uatVeiculoService.salvar(this.prepareSave()).subscribe((res) => {
        this.resetForm();
        this.loadUatVeiculoGroupedByTipo();
        this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
      }, (error) => {
        this.mensagemError(error);
      })
    }
  }

  openConfirmationDialog(veiculo: UatVeiculoDTO) {
    this.modalConfirmarSerivce.confirm('Excluir Veículo', 'Tem certeza que deseja excluir este veículo?')
    .then((confirmed) => {
      if(confirmed) {
        this.remover(veiculo.id, this.idUat);
      }
    });
  }

  remover(idVeiculo: Number, idUat: Number): void {
    this.uatVeiculoService.excluir(idVeiculo, idUat).subscribe(() => {
      this.loadUatVeiculoGroupedByTipo();
      this.mensagemSucesso(MensagemProperties.app_rst_operacao_sucesso);
    }, (error) => {
      this.mensagemError(error);
    });
  }

  cancelar(): void {
    this.resetForm();
  }

  changeTipo(id: Number): void {
    this.tipoSelecionado = this.listUatVeiculoTipo.filter(item => item.id == id)[0];
  }

  isValid(): boolean {
    if (this.hasTipoAtendimento()) {
      return this.validateUnidadeMovel();
    } else {
      return this.validateVeiculoPasseioAtendimento();
    }
  }

  hasTipoAtendimento(): boolean {
    if(this.idVeiculoTipoSelecionado) {
      return this.tipoSelecionado.atendimento;
    }
    return false;
  }

  showGrid(): boolean {
    return this.listUatVeiculoGroupedByTipo && this.listUatVeiculoGroupedByTipo.length > 0;
  }

  private resetForm(): void {
    this.idVeiculoTipoSelecionado = undefined;
    this.listVeiculoWithAtendimento.map((item) => {
      item.quantidade = undefined;
    });
    this.quantidadeVeiculoPasseio = undefined;
  }

  private prepareSave(): any[] {
    if(this.hasTipoAtendimento()) {
      return this.listVeiculoWithAtendimento
        .filter(item => item.quantidade)
        .map((item) => {
          return {
            idTipo: this.idVeiculoTipoSelecionado,
            quantidade: item.quantidade,
            idUat: this.idUat,
            descricao: item.uatVeiculoTipoAtendimento.descricao,
            idVeiculoTipoAtendimento: item.uatVeiculoTipoAtendimento.id
          }
        });
    } else {
      const list = new Array<any>();
      const uatVeiculo = {
        idTipo: this.idVeiculoTipoSelecionado,
        quantidade: Number(this.quantidadeVeiculoPasseio),
        idUat: this.idUat
      };
      list.push(uatVeiculo);
      return list;
    }
  }

  private validateVeiculoPasseioAtendimento(): boolean {
    if(!this.quantidadeVeiculoPasseio) {
      this.mensagemErroComParametrosModel('app_rst_campo_obrigatorio', MensagemProperties.app_rst_labels_quantidade);
      return false;
    }
    return true;
  }

  private validateUnidadeMovel(): boolean {
    const listUnidadesMoveisParaSalvar = this.listVeiculoWithAtendimento.filter(item => item.quantidade);
    if (!listUnidadesMoveisParaSalvar.length) {
      this.mensagemErroComParametros('app_rst_msg_nenhuma_quantidade_informada');
      return false;
    }
    return true;
  }

  private loadVeiculoTipo(): void {
    this.uatVeiculoService.listUatVeiculoTipo().subscribe((res: UatVeiculoTipo[]) => {
      this.listUatVeiculoTipo = res;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private loadVeiculoTipoAtendimento(): void {
      this.uatVeiculoService.listUatVeiculoTipoAtendimento().subscribe((res: UatVeiculoTipoAtendimento[]) => {
        res.forEach((item) => {
          const uatVeiculo = new UatVeiculo();
          uatVeiculo.uatVeiculoTipoAtendimento = item;
          const unidadeAtendimentoTrabalhador = new UnidadeAtendimentoTrabalhador();
          unidadeAtendimentoTrabalhador.id = this.idUat;
          uatVeiculo.unidadeAtendimentoTrabalhador = unidadeAtendimentoTrabalhador;
          this.listVeiculoWithAtendimento.push(uatVeiculo);
        });
    }, (error) => {
      this.mensagemError(error);
    });
  }

  private loadUatVeiculoGroupedByTipo(): void {
    this.uatVeiculoService.listUatVeiculoByIdUatAndGroupedByTipo(this.idUat).subscribe((res: UatVeiculoGroupedTipoDTO[]) => {
      this.listUatVeiculoGroupedByTipo = res;
    }, (error) => {
      this.mensagemError(error);
    });
  }
}
