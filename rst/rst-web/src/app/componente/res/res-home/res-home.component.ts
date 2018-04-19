import {BloqueioService} from './../../../servico/bloqueio.service';
import {BaseComponent} from 'app/componente/base.component';
import {ResService} from './../../../servico/res-service.service';
import {Observable} from 'rxjs/Observable';
import {Path} from '@ezvida/adl-core';
import {ActivatedRoute, Router} from '@angular/router';
import {Component, OnInit} from '@angular/core';
import * as moment from 'moment';
import 'chartjs-plugin-datalabels';
import {Seguranca} from '../../../compartilhado/utilitario/seguranca.model';

@Component({
    selector: 'app-res-home',
    templateUrl: './res-home.component.html',
    styleUrls: ['./res-home.component.scss']
})
export class ResHomeComponent extends BaseComponent implements OnInit {

    DADOS_HISTORICOS = [
        'PRESSAO_SISTOLICA',
        'PRESSAO_DIASTOLICA',
        'CIRCUNFERENCIA_ABDOMINAL'
    ];

    DADOS_BASICOS = [
        'PESO',
        'ALTURA',
        'IMC'
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

    constructor(protected route: ActivatedRoute,
        protected router: Router,
        protected service: ResService,
        protected bloqueioService: BloqueioService) {
        super(bloqueioService);
    }

    ngOnInit() {
        this.resolve();
    }

    tratarDadosBasicos(dados) {
        if ( dados.paciente ) {
            this.idade = this.calculaIdade(new Date(dados.paciente.birthDate));
            this.paciente = dados.paciente;
        }

        this.criarGraficoDeCircunferencia(dados.circunferenciaAbdominal);

        if ( dados.altura ) {
            this.altura = new DadoBasico({
                magnitude: dados.altura.value.magnitude as number,
                units: dados.altura.value.units
            });
        }

        if ( dados.imc ) {
            this.imc = new DadoBasico({magnitude: dados.imc.value.magnitude as number, units: dados.imc.value.units});
        }

        if ( dados.peso ) {
            this.peso = new DadoBasico({
                magnitude: dados.peso.value.magnitude as number,
                units: dados.peso.value.units
            });
        }

        this.criarGraficoDePressao(dados.sistolica || [], dados.diastolica || []);
    }

    calculaIdade(dataNascimento: Date): number {
        if ( dataNascimento == null || isNaN(dataNascimento.getTime()) ) {
            return null;
        }
        return Math.floor((Math.abs(Date.now() -
                                    moment(dataNascimento, 'YYYY-MM-DDTHH:mm:ss.Z')
                                        .toDate().getTime()) / (1000 * 3600 * 24)) / 365);
    }

    criarGraficoDeCircunferencia(circunferencias: Array<{ data: string, informacao: any }>) {
        if (!circunferencias || circunferencias.length === 0 || circunferencias.every(c => Number(c.informacao!.magnitude) <= 0)) {
            this.semDadosPesoSuficiente = true;
        } else {
            this.semDadosPesoSuficiente = false;
            let setLabelsComDatas = new Set();
            let labelsComDatas: any[];
            let datasJaInseridas = [];
            let i = 0;
            let dados = [
                {data: []}
            ];

            circunferencias.forEach((circunferencia) => {
                if (circunferencia.informacao) {
                    const data = new Date(circunferencia.data);
                    setLabelsComDatas.add(`${data.getDate()}/${data.getMonth() + 1}/${data.getFullYear()}`);
                }
            });

            labelsComDatas = Array.from(setLabelsComDatas);

            for(const circunferencia of circunferencias) {
                const data = new Date(circunferencia.data);
                const dataFormatada = `${data.getDate()}/${data.getMonth() + 1}/${data.getFullYear()}`;

                //Verificação se o registro está entre os 3 registros que vão ser exibidos.
                if(labelsComDatas.indexOf(dataFormatada) > -1) {

                    //Verificar se a data do registro já foi inserida para colocar ele em outro dataset.
                    const index = datasJaInseridas.indexOf(dataFormatada);
                    if(index != -1) {
                        let list = [];

                        for (let i = 0; i < labelsComDatas.length; i++) {
                            list.push(undefined);
                        }

                        list[index] = circunferencia.informacao.magnitude;

                        dados.push({
                            data: list
                        });
                    } else {
                        dados[0].data.push(circunferencia.informacao.magnitude);
                        datasJaInseridas.push(dataFormatada);
                    }
                }

                i++;
                if (i > 2) {
                    break;
                }
            }

            this.graficoPeso = {
                tooltips: false,
                dados: dados,
                cores: this.obterObjetoDeCoresPorDataset(dados.length),
                labels: labelsComDatas,
                opcoes: {
                    responsive: true,
                    scales: {
                        yAxes: [{
                            gridLines: {
                                drawBorder: false
                            },
                            stacked: false,
                            ticks: {
                                suggestedMax: 150,
                                suggestedMin: 45,
                                stepSize: 50
                            }
                        }],
                        xAxes: [
                            {
                                gridLines: {
                                    display: false,
                                    drawBorder: false
                                }
                            }
                        ]
                    },
                    legend: {
                        display: false
                    }
                }
            };
        }
    }

    obterObjetoDeCoresPorDataset(quantidadeDeDataSets: number) {
        let retorno = [];

        for (let i = 0; i < quantidadeDeDataSets; i++) {
            retorno.push({
                backgroundColor: 'rgba(175, 99, 152, 0.2)',
                borderColor: 'rgba(175, 99, 152,0.2)',
                pointBackgroundColor: 'rgba(175, 99, 152, 1)',
                pointBorderColor: 'rgba(175, 99, 152, 1)',
                pointRadius: '5',
                pointHoverRadius: '5'
                });
        }

        return retorno;
    }

    criarGraficoDePressao(sistolicas: Array<{ data: string, informacao: Path }>, diastolicas: Array<{ data: string, informacao: Path }>) {
        if ( (!sistolicas && !diastolicas) || (sistolicas.length === 0 && diastolicas.length === 0) ) {
            this.semDadosPressaoSuficiente = true;
            return;
        }

        let labelsComData = [];
        let serieSistolica = [];
        let serieDiastolica = [];
        let referenciaSistolica = [];
        let referenciaDiastolica = [];
        let i = 0;

        for (let sistolica of sistolicas) {
            if (sistolica.informacao) {
                const data = new Date(sistolica.data);

                //Procura a pressão diastolica para fazer o match com a sistolica.
                //O parametro de busca é a data do atendimento.
                //Caso o paciente tenha 2 atendimentos no mesmo horário, o que é impossível, utiliza o primeiro.
                const diastolica = diastolicas.filter(element => {
                    return new Date(element.data).getTime() === new Date(sistolica.data).getTime();
                })[0];

                //Caso o paciente não tenha valor de pressão diastolica, impossivel só ter 1 dos 2,  não monta as barras.
                if(diastolica !== undefined) {
                    this.semDadosPressaoSuficiente = false;

                    //Adiciona as medições em par nas listas.
                    serieSistolica.push((sistolica.informacao as any).magnitude);
                    serieDiastolica.push((diastolica.informacao as any).magnitude);

                    //Adiciona as referencias base de pressão normal.
                    referenciaSistolica.push(120);
                    referenciaDiastolica.push(80);

                    //Monta a label do grafico
                    labelsComData.push(`${data.getDate()}/${data.getMonth() + 1}/${data.getFullYear()}`);

                    //Remove da lista para caso exista pressões repetidas.
                    diastolicas = diastolicas.splice(diastolicas.indexOf(diastolica), 1);//TODO TESTAR ESSE SPLICE.

                    i++;
                    if (i > 2) {
                        break;
                    }
                }
            }
        }

        this.graficoPressaoSanguinea = {
            dados: [
                {
                    yAxisID: 'A',
                    label: 'Sistólica',
                    data: serieSistolica
                },
                {
                    yAxisID: 'A',
                    label: 'Diastólica',
                    data: serieDiastolica
                }
            ],
            cores: [
                {
                    backgroundColor: '#af6398'
                },
                {
                    backgroundColor: '#6dcdf5'
                }
            ],
            labels: labelsComData,
            opcoes: {
                scaleShowVerticalLines: false,
                tooltips: {
                    mode: 'index'
                },
                scales: {
                    yAxes: [{
                        id: 'B',
                        scaleLabel: {
                            display: true,
                            padding: 5,
                            labelString: 'Valor Referência',
                            fontColor: '#9a718f'
                        },
                        gridLines: {
                            drawBorder: false,
                            color: '#9a718f'
                        },
                        ticks: {
                            display: true,
                            fontColor: '#9a718f',
                            fontSize: 10,
                            max: 190,
                            min: 55,
                            maxTicksLimit: 1,
                            stepSize: 15,
                            callback: (value, index, values) => {
                                console.log(value);
                                if ( value === 130 ) {
                                    return 'Sistólica ' + value;
                                }
                                if ( value === 85 ) {
                                    return 'Diastólica ' + value;
                                }
                            }
                        }
                    }, {
                        id: 'A',
                        gridLines: {
                            drawBorder: false,
                            color: ['#006400', '#006400'],
                            borderDash: [4, 3],
                            borderDashOffset: 0
                        },
                        ticks: {
                            max: 190,
                            min: 55,
                            stepSize: 50, display: true
                        }
                    }],
                    xAxes: [
                        {
                            barThickness: 20,
                            categoryPercentage: 0.16,
                            barPercentage: 1.0,
                            gridLines: {
                                display: false,
                                drawBorder: false
                            }
                        }
                    ]
                }
            }
        };
    }

    getClasseImagemCorpo(): string {
        return 'homem';
    }

    getClasseNivelIMC(): string {
        const imc = this.imc ? this.imc.valor : null;
        if ( imc ) {
            if ( imc < 18.5 ) {
                return 'baixo-peso';
            }
            else if ( 25 <= imc && imc < 30 ) {
                return 'sobrepeso';
            }
            else if ( imc >= 30 ) {
                return 'obeso';
            }
        }
        return 'normal';
    }

    referenciaAbdominal(): string {
        if ( this.paciente ) {
            if ( this.paciente.gender ) {
                if ( this.paciente.gender.code === 'male' ) {
                    return '94 cm';
                }
                else if ( this.paciente.gender.code === 'female' ) {
                    return '60 cm';
                }
            }
        }
        return '';
    }

    private resolve() {
        const cpf = (Seguranca.getUsuario() as any).sub;
        const chamadas: any[] = this.DADOS_BASICOS.map((dado) => this.service
                                                                     .buscarValorParaInformacaoSaude(dado, cpf)
                                                                     .catch((err) => Observable.of(null)));
        chamadas.push(...this.DADOS_HISTORICOS.map((dado) => this.service
                                                                 .buscarHistoricoParaInformacaoSaude(dado, cpf)
                                                                 .catch((err) => Observable.of(null))));
        chamadas.push(this.service.buscarPaciente());
        Observable.forkJoin(chamadas).subscribe((result) => {
            this.tratarDadosBasicos({
                peso: result[0],
                altura: result[1],
                imc: result[2],
                sistolica: result[3],
                diastolica: result[4],
                circunferenciaAbdominal: result[5],
                paciente: result[6]
            });
        });
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
