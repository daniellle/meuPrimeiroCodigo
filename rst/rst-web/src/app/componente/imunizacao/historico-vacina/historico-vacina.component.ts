import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal/modal';
import { Vacina } from './../../../modelo/vacina.model';
import { environment } from './../../../../environments/environment';
import { Component, OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { ToastyService } from 'ng2-toasty/src/toasty.service';
import { DialogService } from 'ng2-bootstrap-modal';
import { FiltroVacina } from '../../../modelo/filtro-vacina.model';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ImunizacaoService } from '../../../servico/imunizacao.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { enumFiltro } from '../../../modelo/enum/enum.filtroVacina.model';
import { Paginacao } from 'app/modelo/paginacao.model';

@Component({
  selector: 'app-historico-vacina',
  templateUrl: './historico-vacina.component.html',
  styleUrls: ['./historico-vacina.component.scss']
})
export class HistoricoVacinaComponent extends BaseComponent implements OnInit {

  public idTrabalhador: number;
  public idVacina: number;
  filtroVacina: FiltroVacina;
  public periodo = enumFiltro;
  public keysPeriodoVacina: string[];
  vacinas: Vacina[] = [];
  vacinaRemover: Vacina;
  loading: boolean;

  public modal;
  public modalRef;

  constructor(
    private router: Router, private activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected service: ImunizacaoService,
    public modalAtivo: NgbActiveModal,
    public activeModal: NgbActiveModal,
    private modalService: NgbModal,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private dialogService: DialogService) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.vacinas = this.vacinas = new Array<Vacina>();
    this.filtroVacina = new FiltroVacina();
    this.keysPeriodoVacina = Object.keys(this.periodo);
    this.vacinaRemover = new Vacina();
    this.carregarTela();
  }

  openModal(modal) {
    this.modalRef = this.modalService.open(modal, { size: 'sm' });
  }

  pesquisar() {
      this.vacinas = new Array<Vacina>();
      this.paginacao.pagina = 1;
      this.buscarHistoricoVacina(this.paginacao);
  }

  private carregarHistoricoVacinas() {
    this.paginacao.pagina = 1;
    this.buscarHistoricoVacina(this.paginacao, true);
  }

  private buscarHistoricoVacina(paginacao: Paginacao, showLoading?: boolean) {
    if(showLoading) { this.loading = true };
    this.service.pesquisar(this.filtroVacina, paginacao).subscribe((retorno: ListaPaginada<Vacina>) => {
      this.vacinas = retorno.list;
      this.paginacao = this.getPaginacao(this.paginacao, retorno);
      if (retorno.quantidade === 0) {
        this.mensagemError(MensagemProperties.app_rst_nenhum_registro_encontrado);
      }
      this.loading = false;
    }, (error) => {
      this.loading = false;
      this.mensagemError(error);
    });
  }

  carregarTela() {
    this.activatedRoute.params.subscribe((params) => {
      this.idTrabalhador = params['id'];
    });
    this.carregarHistoricoVacinas();
  }

  voltar(): void {
    this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}`])
  }

  remover() {
    this.service.remover(this.vacinaRemover.id).subscribe((retorno: any) => {
        if(this.vacinas.length > 1 ){
            this.pesquisar();
        }
        else{
            this.vacinas = new Array<Vacina>();
        }
      this.mensagemSucesso('Vacina deletada com sucesso');
    }, (error) => {
      this.mensagemError(error);
    });
  }

  selecionar(item: Vacina) {
    this.idVacina = item.id;
    this.router.navigate(
      [`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}/vacina-declarada/${this.idVacina}/cadastrar`]);
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.service.pesquisar(this.filtroVacina, this.paginacao).subscribe((retorno: ListaPaginada<Vacina>) => {
      this.vacinas = retorno.list;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  selecionarVacinaRemover(item: Vacina){
    this.vacinaRemover = item;
  }

}
