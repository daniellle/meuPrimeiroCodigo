import { environment } from './../../../../environments/environment.prod';
import {BloqueioService} from './../../../servico/bloqueio.service';
import {BaseComponent} from 'app/componente/base.component';
import {ResService} from './../../../servico/res-service.service';
import {Observable} from 'rxjs/Observable';
import {Seguranca} from './../../../compartilhado/utilitario/seguranca.model';
import {Path} from '@ezvida/adl-core';
import {ActivatedRoute, Router} from '@angular/router';
import {Component, OnInit} from '@angular/core';
import * as moment from 'moment';
import 'chartjs-plugin-datalabels';

@Component({
    selector: 'app-res-home',
    templateUrl: './res-home.component.html',
    styleUrls: ['./res-home.component.scss'],
})
export class ResHomeComponent extends BaseComponent implements OnInit {

    DADOS_HISTORICOS = [
        'PRESSAO_SISTOLICA',
        'PRESSAO_DIASTOLICA',
        'CIRCUNFERENCIA_ABDOMINAL',
    ];

    DADOS_BASICOS = [
        'PESO',
        'ALTURA',
        'TEMPERATURA',
        'IMC',
    ];

    hoje = new Date();
    idade: number;

    sistolica: DadoBasico;
    diastolica: DadoBasico;
    peso: DadoBasico;
    altura: DadoBasico;
    imc: DadoBasico;
    temperatura: DadoBasico;
    circunferenciaAbdominal: DadoBasico;
    recomendacaoASO: DadoBasico;
    recomendacaoObservacao: DadoBasico;
    deficiente: DadoBasico;
    deficiencia: DadoBasico;
    graficoPressaoSanguinea: any;
    graficoPeso: any;
    paciente: any;

    encontrosTimeline: any[];
    paginaTimeline: any[];
    numeroEncontros = 0;
    tamanhoPagina = 10;
    proximaDataTimeline: Date;

    semDadosPesoSuficiente = true;
    semDadosPressaoSuficiente = true;

    constructor(
        protected route: ActivatedRoute,
        protected router: Router,
        protected service: ResService,
        protected bloqueioService: BloqueioService) {
        super(bloqueioService);
    }

    ngOnInit() {
        this.resolve();
    }

    private resolve() {
        const cpf = (Seguranca.getUsuario() as any).sub;
        const chamadas: any[] = this.DADOS_BASICOS.map((dado) => this.service.buscarValorParaDado(dado, cpf, false)
            .catch((err) => Observable.of(null)));
        chamadas.push(...this.DADOS_HISTORICOS.map((dado) => this.service.buscarValorParaDado(dado, cpf, false, 3)
            .catch((err) => Observable.of(null))));
        chamadas.push(this.service.buscarPaciente());
        Observable.forkJoin(chamadas).subscribe((result) => {
            this.tratarDadosBasicos({
                peso: result[0],
                altura: result[1],
                temperatura: result[2],
                imc: result[3],
                sistolica: result[4],
                diastolica: result[5],
                circunferenciaAbdominal: result[6],
                paciente: result[7],
            });
        });
    }

    tratarDadosBasicos(dados) {
        if (dados.paciente) {
            this.idade = this.calculaIdade(new Date(dados.paciente.birthDate));
            this.paciente = dados.paciente;
        }

        this.criarGraficoDeCircunferencia(dados.circunferenciaAbdominal);

        if (dados.altura) {
            this.altura = new DadoBasico({ magnitude: dados.altura.magnitude as number, units: dados.altura.units });
        }

        if (dados.imc) {
            this.imc = new DadoBasico({ magnitude: dados.imc.magnitude as number, units: dados.imc.units });
        }

        if (dados.peso) {
            this.peso = new DadoBasico({ magnitude: dados.peso.magnitude as number, units: dados.peso.units });
        }

        if (dados.recomendacoesEducativoASO) {
            this.recomendacaoASO = new DadoBasico({
                magnitude: dados.recomendacoesEducativoASO.magnitude as string,
                units: dados.recomendacoesEducativoASO.units,
            });
        }

        if (dados.recomendacoesEducativoObservacoes) {
            this.recomendacaoObservacao = new DadoBasico({ magnitude: dados.recomendacoesEducativoObservacoes.magnitude as string });
        }

        this.criarGraficoDePressao(dados.sistolica || [], dados.diastolica || []);
    }

    calculaIdade(dataNascimento: Date): number {
        if (dataNascimento == null || isNaN(dataNascimento.getTime())) {
            return null;
        }
        return Math.floor((Math.abs(Date.now() -
            moment(dataNascimento, 'YYYY-MM-DDTHH:mm:ss.Z')
                .toDate().getTime()) / (1000 * 3600 * 24)) / 365);
    }

    criarGraficoDeCircunferencia(circunferencias: Array<{ data: string, informacao: Path }>) {
        if (!circunferencias || circunferencias.length === 0) {
            this.semDadosPesoSuficiente = true;
        } else {
            this.semDadosPesoSuficiente = false;
            let dados = [];
            let labelsComDatas = [];
            circunferencias.forEach((circunferencia) => {
                if (circunferencia.informacao) {
                    dados.push((circunferencia.informacao as any).magnitude);
                    const data = new Date(circunferencia.data);
                    labelsComDatas.push(`${data.getDate()}/${data.getMonth() + 1}/${data.getFullYear()}`);
                }
            });
            if (dados.length > 3) {
                dados = dados.splice(0, 3);
                labelsComDatas = labelsComDatas.splice(0, 3);
            }
            this.graficoPeso = {
                tooltips: false,
                dados: [
                    {
                        data: dados,
                        label: '',
                    },
                ],
                cores: [
                    {
                        backgroundColor: 'rgba(175, 99, 152, 0.2)',
                        borderColor: 'rgba(175, 99, 152,0.2)',
                        pointBackgroundColor: 'rgba(175, 99, 152, 1)',
                        pointBorderColor: 'rgba(175, 99, 152, 1)',
                        pointRadius: '5',
                        pointHoverRadius: '5',
                    },
                ],
                labels: labelsComDatas,
                opcoes: {
                    responsive: true,
                    // tooltips: { enabled: false },
                    scales: {
                        yAxes: [{
                            gridLines: {
                                drawBorder: false,
                            },
                            stacked: true,
                            ticks: {
                                suggestedMax: 150,
                                suggestedMin: 45,
                                stepSize: 50,
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
                    legend: {
                        display: false,
                    },
                },
            };
        }
    }

    criarGraficoDePressao(sistolicas: Array<{ data: string, informacao: Path }>, diastolicas: Array<{ data: string, informacao: Path }>) {
        if ((!sistolicas && !diastolicas) || (sistolicas.length === 0 && diastolicas.length === 0)) {
            this.semDadosPressaoSuficiente = true;
        } else {
            this.semDadosPressaoSuficiente = false;
            const mapaDeDatas = {};
            const referencia = {};

            sistolicas.forEach((sistolica) => {
                if (sistolica.informacao) {
                    mapaDeDatas[sistolica.data] = [(sistolica.informacao as any).magnitude];
                    referencia[sistolica.data] = [120];
                }
            });
            diastolicas.forEach((diastolica) => {
                mapaDeDatas[diastolica.data] ?
                mapaDeDatas[diastolica.data].push((diastolica.informacao as any).magnitude) :
                mapaDeDatas[diastolica.data] = [0, (diastolica.informacao as any).magnitude];

                referencia[diastolica.data] ? referencia[diastolica.data].push(80) : referencia[diastolica.data] = [0, 80];
            });

            const labelsComData = [];
            const serieSistolica = [];
            const serieDiastolica = [];
            const referenciaSistolica = [120];
            const referenciaDiastolica = [80];
            let i = 0;
            for (const key in mapaDeDatas) {
                if (mapaDeDatas.hasOwnProperty(key)) {
                    const data = new Date(key);
                    labelsComData.push(`${data.getDate()}/${data.getMonth() + 1}/${data.getFullYear()}`);
                    serieSistolica.push(mapaDeDatas[key][0]);
                    serieDiastolica.push(mapaDeDatas[key][1]);
                    referenciaSistolica.push(120);
                    referenciaDiastolica.push(80);
                    i++;
                    if (i > 2) {
                        break;
                    }
                }
            }

            this.graficoPressaoSanguinea = {
                dados: [
                    {
                        yAxisID: 'A',
                        label: 'Sistólica',
                        data: serieSistolica,
                    },
                    {
                        yAxisID: 'A',
                        label: 'Diastólica',
                        data: serieDiastolica,
                    },
                ],
                cores: [
                    {
                        backgroundColor: '#af6398',
                    },
                    {
                        backgroundColor: '#6dcdf5',
                    },
                ],
                labels: labelsComData,
                opcoes: {
                    scaleShowVerticalLines: false,
                    scales: {
                        yAxes: [{
                            id: 'B',
                            scaleLabel: {
                                display: true,
                                padding: 5,
                                labelString: 'Valor Referência',
                                fontColor: '#9a718f',
                            },
                            gridLines: {
                                drawBorder: false,
                                color: '#9a718f',
                            },
                            ticks: {
                                display: true,
                                fontColor: '#9a718f',
                                fontSize: 10,
                                max: 180,
                                min: 0,
                                maxTicksLimit: 1,
                                stepSize: 5,
                                callback: (value, index, values) => {
                                    if (value === 130) {
                                        return 'Sistólica ' + value;
                                    }
                                    if (value === 85) {
                                        return 'Diastólica ' + value;
                                    }
                                },
                            },
                        }, {
                            id: 'A',
                            gridLines: {
                                drawBorder: false,
                                color: ['#006400', '#006400'],
                                borderDash: [4, 3],
                                borderDashOffset: 0,
                            },
                            ticks: {
                                suggestedMax: 140,
                                suggestedMin: 90,
                                stepSize: 90,
                            },
                        }],
                        xAxes: [
                            {
                                barThickness: 20,
                                categoryPercentage: 0.16,
                                barPercentage: 1.0,
                                gridLines: {
                                    display: false,
                                    drawBorder: false,
                                },
                            },
                        ],
                    },
                },
            };
        }
    }

    getClasseImagemCorpo(): string {
        return 'homem';
    }

    getClasseNivelIMC(): string {
        const imc = this.imc ? this.imc.valor : null;
        if (imc) {
            if (imc < 18.5) {
                return 'baixo-peso';
            } else if (25 <= imc && imc < 30) {
                return 'sobrepeso';
            } else if (imc >= 30) {
                return 'obeso';
            }
        }
        return 'normal';
    }

    referenciaAbdominal(): string {
        if (this.paciente) {
            if (this.paciente.gender) {
                if (this.paciente.gender.code === 'male') {
                    return '94 cm';
                } else if (this.paciente.gender.code === 'female') {
                    return '60 cm';
                }
            }
        }
        return '';
    }
}
// tslint:disable-next-line:max-classes-per-file
export class DadoBasico {
    valor: any;
    unidade: string;

    constructor(valor: { magnitude: any, units: string } | any) {
        this.valor = valor.magnitude;
        this.unidade = valor.units;
    }
}
