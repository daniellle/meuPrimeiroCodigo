import { Trabalhador } from './../../../modelo/trabalhador.model';
import { FiltroTrabalhador } from './../../../modelo/filtro-trabalhador.model';
import { Seguranca } from 'app/compartilhado/utilitario/seguranca.model';
import { Observable } from 'rxjs/Observable';
import { TrabalhadorService } from 'app/servico/trabalhador.service';
import { QuestionarioTrabalhadorService } from './../../../servico/questionario-trabalhador.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ToastyService } from 'ng2-toasty';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { BaseComponent } from 'app/componente/base.component';
import { Parametro } from './../../../modelo/parametro.model';
import { environment } from './../../../../environments/environment';
import { ParametroService } from './../../../servico/parametro.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-igev',
  templateUrl: './igev.component.html',
  styleUrls: ['./igev.component.scss'],
})
export class IgevComponent extends BaseComponent implements OnInit {

  @ViewChild('modalResponder') modalResponder: NgbModal;
  public idTrabalhador: number;
  public parametro: Parametro;
  public igev: String;
  public meusdados: boolean;
  public rastroIgev: boolean;
  public trabalhador = new Trabalhador();

  constructor(
    private router: Router,
    private service: ParametroService,
    private activatedRoute: ActivatedRoute,
    protected bloqueioService: BloqueioService,
    protected dialogo: ToastyService,
    private modalService: NgbModal,
    private questionarioTrabalhadorService: QuestionarioTrabalhadorService,
    private trabalhadorService: TrabalhadorService,
  ) {
    super(bloqueioService, dialogo);
  }

  ngOnInit() {
    this.meusdados = this.activatedRoute.snapshot.url[0].toString() === 'meusdados';
    this.rastroIgev = this.activatedRoute.snapshot.url[0].toString() === 'igev';

    if (this.meusdados || this.rastroIgev) {
      this.trabalhadorService.buscarMeusDados().subscribe((retorno: Trabalhador) => {
        this.idTrabalhador = retorno.id;
        this.trabalhador = retorno;
      }, (error) => {
        this.mensagemError(error);
      });
    } else {
      this.idTrabalhador = this.activatedRoute.snapshot.params['id'];
      this.buscarTrabalhador();
    }
    this.title = MensagemProperties.app_rst_trabalhador_title_igev;
    this.buscarIgev();
  }

  buscarTrabalhador() {
    const filtroTrabalhador = new FiltroTrabalhador();
    filtroTrabalhador.id = this.idTrabalhador.toString();
    this.trabalhadorService.buscarPorId(filtroTrabalhador).subscribe((retorno: Trabalhador) => {
      this.trabalhador = retorno;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  buscarIgev() {
    this.service.buscarIgev().subscribe((retorno: Parametro) => {
      this.parametro = retorno;
      this.igev = retorno.valor;
    }, (error) => {
      this.mensagemError(error);
    });
  }

  habilitarBtnPreencherIgev(): boolean {
    const usuario = Seguranca.getUsuario();
    return usuario.papeis.indexOf('TRA') > -1 && usuario.sub === this.trabalhador.cpf;
  }

  openModalResponder() {
    this.modalService.open(this.modalResponder).result
      .then((result) => {
      }, (reason) => {
      });
  }

  voltar() {
    if (this.meusdados) {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}`]);
    }
  }

  preencherIgev() {
    this.questionarioTrabalhadorService.consultarPeriodicidade(this.idTrabalhador)
      .subscribe((retorno: boolean) => {
        if (retorno) {
          if (this.meusdados) {
            this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados/${this.idTrabalhador}/questionario/responder`]);
          } else if (this.rastroIgev) {
            this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/igev/${this.idTrabalhador}/questionario/responder`]);
          } else {
            this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}/questionario/responder`]);
          }
        } else {
          this.openModalResponder();
        }
      }, (error) => {
        this.mensagemError(error);
      });
  }

  consultarHistorico() {
    if (this.meusdados) {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados/${this.idTrabalhador}/historico`]);
    } else if (this.rastroIgev) {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/igev/${this.idTrabalhador}/historico`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}/historico`]);
    }
  }
}
