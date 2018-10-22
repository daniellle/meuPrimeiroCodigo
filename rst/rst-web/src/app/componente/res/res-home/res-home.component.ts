import {BloqueioService} from '../../../servico/bloqueio.service';
import {BaseComponent} from 'app/componente/base.component';
import {ResService} from '../../../servico/res-service.service';
import {Observable} from 'rxjs/Observable';
import {Path} from '@ezvida/adl-core';
import {ActivatedRoute, Router} from '@angular/router';
import {Component, OnInit} from '@angular/core';
import 'chartjs-plugin-datalabels';
import {Seguranca} from '../../../compartilhado/utilitario/seguranca.model';
import * as _ from 'lodash';
import {isNullOrUndefined} from "util";

const moment = require('moment');

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
        'IMC',
        'IDADE'
    ];

    disclaimerAferimento = 'Registro de: ';

    idade: any;

    sistolica: DadoBasico;
    diastolica: DadoBasico;
    peso: DadoBasico;
    altura: DadoBasico;
    imc: DadoBasico;
    circunferenciaAbdominal: DadoBasico;
    graficoPressaoSanguinea: any;
    graficoCircunferenciaAbdominal: any;
    paciente: any;

    semDadosCircunferenciaAbdominalSuficiente = true;
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
            this.paciente = dados.paciente;
        }

        this.criarGraficoDeCircunferencia(dados.circunferenciaAbdominal);

        if ( dados.altura ) {
            this.altura = new DadoBasico({
                magnitude: dados.altura.value.magnitude as number,
                units: dados.altura.value.units
            });
        }


        if ( dados.peso ) {
            this.peso = new DadoBasico({
                magnitude: dados.peso.value.magnitude as number,
                units: dados.peso.value.units
            });
        }

        this.calcularImc();

        this.criarGraficoDePressao(dados.sistolica || [], dados.diastolica || []);
        this.calcularIdade();
    }

    criarGraficoDeCircunferencia(circunferencias: Array<{ data: string, informacao: any }>) {

        if ( !circunferencias || circunferencias.length === 0
             || circunferencias.every(c => Number(c.informacao!.magnitude) <= 0) ) {
            this.semDadosCircunferenciaAbdominalSuficiente = true;
        } else {
            this.semDadosCircunferenciaAbdominalSuficiente = false;
            let setLabelsComDatas = new Set();
            let labelsComDatas: any[];
            let datasJaInseridas = [];
            let i = 0;
            let dados = [
                {data: []}
            ];

            this.ordenarArrayDeCircunferenciaAbdominal(circunferencias);

            let quantidade_de_datas_sem_repeticao_sem_zero = 0;
            circunferencias.forEach((circunferencia) => {
                if ( circunferencia.informacao
                     && circunferencia.informacao.magnitude !== 0
                     && circunferencia.informacao !== undefined
                     && circunferencia.informacao !== null
                     && circunferencia.informacao.magnitude !== undefined
                     && circunferencia.informacao.magnitude != null ) {
                    quantidade_de_datas_sem_repeticao_sem_zero++;
                }
            });

            let contador = 0;
            circunferencias.forEach((circunferencia) => {

                if ( circunferencia.informacao
                     && circunferencia.informacao.magnitude !== 0
                     && circunferencia.informacao !== undefined
                     && circunferencia.informacao !== null
                     && circunferencia.informacao.magnitude !== undefined
                     && circunferencia.informacao.magnitude !== null ) {
                    const data = new Date(circunferencia.data);
                    setLabelsComDatas.add(`${data.getDate()}/${data.getMonth() + 1}/${data.getFullYear()}`);
                    contador++;
                }
            });

            labelsComDatas = Array.from(setLabelsComDatas);
            setLabelsComDatas = new Set();

            const posicoesAIgnorar = labelsComDatas.length - 3;
            let contador2 = 0;
            labelsComDatas.forEach((data) => {
                if ( contador2 >= posicoesAIgnorar ) {
                    setLabelsComDatas.add(data);
                }
                contador2++;
            });

            labelsComDatas = Array.from(setLabelsComDatas);
            for (const circunferencia of circunferencias) {
                if ( circunferencia.informacao
                     && circunferencia.informacao.magnitude !== 0
                     && circunferencia.informacao !== undefined
                     && circunferencia.informacao !== null
                     && circunferencia.informacao.magnitude !== undefined
                     && circunferencia.informacao.magnitude !== null ) {

                    const data = new Date(circunferencia.data);
                    const dataFormatada = `${data.getDate()}/${data.getMonth() + 1}/${data.getFullYear()}`;

                    //Verificação se o registro está entre os 3 registros que vão ser exibidos.
                    if ( labelsComDatas.indexOf(dataFormatada) > -1 ) {

                        //Verificar se a data do registro já foi inserida para colocar ele em outro dataset.
                        const index = datasJaInseridas.indexOf(dataFormatada);
                        if ( index != -1 ) {
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
                    if ( i > labelsComDatas.length ) {
                        break;
                    }

                }
            }

            this.graficoCircunferenciaAbdominal = {
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

        this.ordenarArrayDePressaoSanguinea(sistolicas);
        this.ordenarArrayDePressaoSanguinea(diastolicas);

        let labelsComData = [];
        let serieSistolica = [];
        let serieDiastolica = [];
        let referenciaSistolica = [];
        let referenciaDiastolica = [];
        let i = 0;

        for (let sistolica of sistolicas) {
            if ( sistolica.informacao ) {
                const data = new Date(sistolica.data);

                //Procura a pressão diastolica para fazer o match com a sistolica.
                //O parametro de busca é a data do atendimento.
                //Caso o paciente tenha 2 atendimentos no mesmo horário, o que é impossível, utiliza o primeiro.
                const diastolica = diastolicas.filter(element => {
                    return new Date(element.data).getTime() === new Date(sistolica.data).getTime();
                })[0];

                if (isNullOrUndefined(diastolica)) {
                    continue;
                }

                //Se a pressão sistólica e diastólica forem 0 não adiciona no gráfico.
                if ( (sistolica.informacao as any).magnitude === undefined || (sistolica.informacao as any).magnitude
                     === null || (sistolica.informacao as any).magnitude === 0 &&

                     (diastolica.informacao as any).magnitude === undefined || (diastolica.informacao as any).magnitude
                     === null || (diastolica.informacao as any).magnitude === 0 ) {
                    continue;
                }

                //Caso o paciente não tenha valor de pressão diastolica, impossivel só ter 1 dos 2,  não monta as
                // barras.
                if ( diastolica !== undefined ) {
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
                    diastolicas.splice(diastolicas.indexOf(diastolica), 1);

                    i++;
                    if ( i > 2 ) {
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
                                //console.log(value);
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
                if ( this.paciente.gender === 'M' ) {
                    return '94 cm';
                } else if ( this.paciente.gender === 'F' ) {
                    return '60 cm';
                }
            }
        }
        return '';
    }

    private calcularIdade() {

        if ( _.isNil(this.idade) && !_.isNil(this.paciente) && !_.isNil(this.paciente.birthDate) ) {
            this.idade = moment().diff(moment(this.paciente.birthDate), 'years');
        }
    }

    private calcularImc() {

        if ( !_.isNil(this.altura) && !_.isNil(this.altura.valor) &&
             !_.isNil(this.peso) && !_.isNil(this.peso.valor) ) {
            const altura = Number(this.altura.valor) / 100;
            const imcMagnitude = (this.peso.valor / (altura * altura)).toFixed(2);
            this.imc = new DadoBasico({
                magnitude: imcMagnitude,
                units: this.peso.unidade + '/' + this.altura.unidade
            });
        }
    }

    private resolve() {
        const cpf = (Seguranca.getUsuario() as any).sub;
        const chamadas: any[] = this.DADOS_BASICOS.map((dado) => this.service
                                                                     .buscarValorParaInformacaoSaude(dado, cpf)
                                                                     .catch(() => Observable.of(null)));
        chamadas.push(...this.DADOS_HISTORICOS.map((dado) => this.service
                                                                 .buscarHistoricoParaInformacaoSaude(dado, cpf)
                                                                 .catch(() => Observable.of(null))));
        chamadas.push(this.service.buscarPaciente());
        Observable.forkJoin(chamadas).subscribe((result) => {
            this.tratarDadosBasicos({
                peso: result[0],
                altura: result[1],
                imc: result[2],
                idade: result[3],
                sistolica: result[4],
                diastolica: result[5],
                circunferenciaAbdominal: result[6],
                paciente: result[7]
            });
        });
    }

    private ordenarArrayDeCircunferenciaAbdominal(collection: Array<{ data: string, informacao: Path }>) {
        collection.sort(function (a, b) {
            if ( new Date(a.data) > new Date(b.data) ) {
                return 1;
            }

            if ( new Date(a.data) < new Date(b.data) ) {
                return -1;
            }

            return 0;
        });
    }

    private ordenarArrayDePressaoSanguinea(collection: Array<{ data: string, informacao: Path }>) {
        collection.sort(function (a, b) {
            if ( new Date(a.data) > new Date(b.data) ) {
                return 1;
            }

            if ( new Date(a.data) < new Date(b.data) ) {
                return -1;
            }

            return 0;
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
