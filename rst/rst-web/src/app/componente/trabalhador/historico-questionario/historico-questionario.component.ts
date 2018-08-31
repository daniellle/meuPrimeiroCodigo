import { environment } from './../../../../environments/environment';
import { QuestionarioTrabalhador } from './../../../modelo/questionario-trabalhador';
import { QuestionarioTrabalhadorFilter } from 'app/modelo/filter-questionario-trabalhador-filter';
import { Component, OnInit } from '@angular/core';
import { BaseComponent } from 'app/componente/base.component';
import { ActivatedRoute, Router } from '@angular/router';
import { CertificadoService } from 'app/servico/certificado.service';
import { BloqueioService } from 'app/servico/bloqueio.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastyService } from 'ng2-toasty';
import { ParametroService } from 'app/servico/parametro.service';
import { TrabalhadorService } from 'app/servico/trabalhador.service';
import { MensagemProperties } from 'app/compartilhado/utilitario/recurso.pipe';
import { ListaPaginada } from 'app/modelo/lista-paginada.model';
import * as moment from 'moment';

@Component({
  selector: 'app-historico-questionario',
  templateUrl: './historico-questionario.component.html',
  styleUrls: ['./historico-questionario.component.scss'],
})
export class HistoricoQuestionarioComponent extends BaseComponent implements OnInit {

  public questionarios: QuestionarioTrabalhador[];
  public questionarioSelecionado: QuestionarioTrabalhador;
  public filtro: QuestionarioTrabalhadorFilter;
  public temQuestionario = false;
  public meusdados: boolean;
  public rastroIgev: boolean;

  public lineChartData: any[] = [
    { data: [], label: '' },
  ];
  public lineChartLabels: string[] = [];
  public lineChartOptions: any = {
    tooltips: {
      enabled: true,
    },
    responsive: true,
    legend: {
      display: false,
    },
    elements: {
      line: {
        tension: 0, // disables bezier curves
      },
    },
    scales: {
      yAxes: [{
        gridLines: {
          display: true,
          drawBorder: true,
          lineWidth: 1.5,
          borderDash: [5, 15],
          color: ['rgba(200, 55, 55,0)', 'rgba(198, 210, 48,1)', 'rgba(0, 0, 0,0)',
            'rgba(198, 210, 48,0)', 'rgba(255, 201, 16,1)', 'rgba(255, 201, 16,0)',
            'rgba(255, 127, 42,1)', 'rgba(255, 127, 42,0)', 'rgba(200, 55, 55,1)',
            'rgba(200, 55, 55,0)', 'rgba(0,0,0,0)', 'rgba(0, 0, 0,1)'],
        },
        ticks: {
          beginAtZero: false,
          display: false,
          suggestedMax: 11,
          suggestedMin: 0,
          stepSize: 1,
        },
      }],
      xAxes: [
        {
          gridLines: {
            display: false,
            drawBorder: false,
          },
        },
      ],
    },
  };
  public lineChartColors: any[] = [
    { // grey
      backgroundColor: 'rgba(255,255,255,0)',
      borderColor: 'rgba(0,0,0,1)',
      pointBackgroundColor: 'rgba(0, 0, 0, 1)',
      pointBorderColor: 'rgba(0, 0, 0, 1)',
      pointRadius: '7',
      pointHoverRadius: '7',
    },
  ];
  public lineChartLegend  = true;
  public lineChartType  = 'line';

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private certificadoService: CertificadoService,
    protected bloqueioService: BloqueioService,
    private modalService: NgbModal,
    protected dialogo: ToastyService,
    private parametroService: ParametroService,
    private trabalhadorService: TrabalhadorService,
  ) {
    super(bloqueioService, dialogo);
    this.filtro = new QuestionarioTrabalhadorFilter();
    this.meusdados = this.activatedRoute.snapshot.url[0].toString() === 'meusdados';
    this.rastroIgev = this.activatedRoute.snapshot.url[0].toString() === 'igev';
    this.filtro.id = this.activatedRoute.snapshot.params['id'];
    if (this.filtro.id) {
      this.carregarTela();
    }
  }

  ngOnInit() {
    this.title = MensagemProperties.app_rst_trabalhador_title_historico;
  }

  carregarTela() {
    this.questionarios = new Array<QuestionarioTrabalhador>();
    this.questionarioSelecionado = null;
    this.paginacao.pagina = 1;
    this.trabalhadorService.questionarioPaginado(this.filtro, this.paginacao)
      .subscribe((retorno: ListaPaginada<QuestionarioTrabalhador>) => {
        this.questionarios = retorno.list;
        this.temQuestionario = true;
        this.paginacao = this.getPaginacao(this.paginacao, retorno);
        if (retorno.quantidade === 0) {
          this.temQuestionario = false;
        }
        this.criarGrafico();
      }, (error) => {
        this.mensagemError(error);
      });
  }

  public pageChanged(event: any): void {
    this.paginacao.pagina = event.page;
    this.trabalhadorService.questionarioPaginado(this.filtro, this.paginacao)
      .subscribe((retorno: ListaPaginada<QuestionarioTrabalhador>) => {
        this.questionarios = retorno.list;
      });
  }

  voltar() {
    if (this.meusdados) {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados/${this.filtro.id}/igev`]);
    } else if (this.rastroIgev) {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/igev`]);
    } else {
      this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.filtro.id}/igev`]);
    }
  }

  public chartClicked(e: any): void {
  }

  public chartHovered(e: any): void {
  }

  selecionar(questionario: any) {
    if (questionario && questionario.id) {
      if (this.meusdados) {
        // tslint:disable-next-line:max-line-length
        this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/meusdados/${this.filtro.id}/questionario/${questionario.id}/resultado`]);
      } else if (this.rastroIgev) {
        // tslint:disable-next-line:max-line-length
        this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/igev/${this.filtro.id}/questionario/${questionario.id}/resultado`]);
      } else {
        this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.filtro.id}/questionario/${questionario.id}/resultado`]);
      }
    }
  }

  criarGrafico() {
    if (this.temQuestionario) {
      const dados = [];
      const labelsComDatas: string[] = new Array();
      const temp = [];
      this.questionarios.forEach((questionario) => {
        if (questionario.quantidadePonto >= 0) {
          dados.push(10 - questionario.quantidadePonto);
          const dataLabel = moment(questionario.dataQuestionarioTrabalhador).utc().format('DD/MM/YYYY');
          labelsComDatas.push(dataLabel);
        }
      });

      for (let i = 0; i < 6; i++) {
        temp.push(dados[i]);
        if (dados[i] === undefined) {
          this.lineChartLabels.push(' ');
        } else {
          this.lineChartLabels.push(labelsComDatas[i]);
        }
      }

      this.lineChartData = [
        { data: temp, label: '' },
      ];
    }
  }
}
