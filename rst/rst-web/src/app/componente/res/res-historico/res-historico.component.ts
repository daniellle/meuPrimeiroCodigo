import {OperationalTemplate} from '@ezvida/adl-core';
import {BloqueioService} from '../../../servico/bloqueio.service';
import {Seguranca} from '../../../compartilhado/utilitario/seguranca.model';
import {Observable} from 'rxjs/Observable';
import {ResService} from '../../../servico/res-service.service';
import {ResHomeComponent} from '../res-home/res-home.component';
import {ActivatedRoute, Router} from '@angular/router';
import {AfterViewInit, Component, OnInit} from '@angular/core';
import {TrabalhadorService} from "../../../servico/trabalhador.service";
import {Trabalhador} from "../../../modelo/trabalhador.model";
import {ImunizacaoService} from '../../../servico/imunizacao.service';
import {Vacina} from 'app/modelo/vacina.model';
import {Imunizacao} from '../../../modelo/imunizacao.model';

const moment = require('moment');

@Component({
    selector: 'app-res-historico',
    templateUrl: './res-historico.component.html',
    styleUrls: ['./res-historico.component.scss']
})
export class ResHistoricoComponent extends ResHomeComponent implements OnInit, AfterViewInit {

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
    public QUANTIDADE_PESQUISA_VACINA = 3;
    encontrosTimeline: any = null;
    carregandoTimeline = false;
    paginaTimeline: any[];
    numeroEncontros = 0;
    tamanhoPagina = 5;
    paginaAtual = 0;
    cpf = null;
    vacinas: string[] = [];
    alergias: string[] = [];
    medicamentos: string[] = [];
    form: OperationalTemplate;
    filtrarInformacoes = false;
    descricaoMedicamentos: string;
    descricaoAlergias: string;
    descricaoVacinas: string;
    dataRegistro: string;
    trabalhador: Trabalhador;
    imunizacao: Imunizacao[] = [];
    vacinasAutodeclaradas: Vacina[] = new Array<Vacina>();

    periodoEscolhido = 'Última semana';
    periodoEscolhidoDate: Date;
    periodos = ['Último mês', '03 meses', '06 meses', '09 meses', '12 meses', 'Todos'];


    constructor(protected trabalhadorService: TrabalhadorService,
                protected route: ActivatedRoute,
                protected router: Router,
                protected service: ResService,
                protected imunizacaoService: ImunizacaoService,
                protected bloqueioService: BloqueioService) {
        super(route, router, service, bloqueioService);
    }

    ngOnInit() {
        this.buscaDadosHistoricos(this.periodoEscolhidoDate);
        this.buscarDadosTrabalhador();
        this.buscarVacinasAutodeclaradas();
    }

    ngAfterViewInit() {
    }

    nomeTrabalhador(): string {
        if (this.cpf !== Seguranca.getUsuario().login && this.paciente) {
            return this.paciente.name ? this.paciente.name.toUpperCase() : null;
        }
        return null;
    }

    tratarImunizacao(imunizacao: any) {
        if (imunizacao) {
            imunizacao.forEach(element => {
                //console.log(element);
                let imunizar = new Imunizacao();
                imunizar.nome = element.informacao.value;
                if (element.data) {
                    imunizar.data = new Date(element.data.value)
                }
                //console.log(imunizar);
                this.imunizacao.push(imunizar);
            });
        }
    }

    tratarVacinas(vacinas: any) {
        if (vacinas) {
            vacinas.forEach((elem) => {
                if (elem.resultado && elem.resultado.value) {
                    this.vacinas.push(elem.nome);
                }
            });
        }
    }

    tratarMedicamentos(medicamentos: any) {
        if (medicamentos) {
            medicamentos.forEach((elem) => {
                if (elem && elem.informacao && elem.informacao.value) {
                    this.medicamentos.push(...(elem.informacao.value as string).split(','));
                }
            });
        }
    }

    tratarAlergias(alergias: any) {
        if (alergias) {
            alergias.forEach((elem) => {
                if (elem && elem.informacao && elem.informacao.value) {
                    this.alergias.push(...(elem.informacao.value as string).split(','));
                }
            });
        }
    }

    carregarTimeline(encontrosMedicos: any) {
        if (encontrosMedicos && encontrosMedicos.resultado.result) {
            this.filtrarInformacoes = encontrosMedicos.filtrarInformacoes;
            this.encontrosTimeline = {};
            this.encontrosTimeline[0] = encontrosMedicos.resultado.result;
            this.dataRegistro = moment(encontrosMedicos.resultado.result[0].created).format("DD/MM/YYYY");
            this.paginaTimeline = this.encontrosTimeline[0];
            this.numeroEncontros = encontrosMedicos.resultado.max;
        }
        else {
            this.encontrosTimeline = null;
            this.paginaTimeline = [];
        }
        this.carregandoTimeline = false;
    }

    navegar($event) {
        const pagina = $event.page - 1;
        if (this.encontrosTimeline[pagina]) {
            this.paginaTimeline = this.encontrosTimeline[pagina];
        }
        else {
            this.service.buscarHistorico(this.paciente.cpf, this.periodoEscolhidoDate, $event.page - 1).subscribe((resultado) => {
                this.filtrarInformacoes = resultado.filtrarInformacoes;

                if (resultado.resultado.result) {
                    this.encontrosTimeline[pagina] = resultado.resultado.result;
                    this.paginaTimeline = this.encontrosTimeline[pagina];
                }
            }, (error) => {
                this.mensagemError(error);
            });
        }
    }

    private buscaDadosHistoricos(periodo?: Date) {
        this.cpf = (Seguranca.getUsuario() as any).sub;
        if (localStorage.getItem('trabalhador_cpf')) {
            this.cpf = localStorage.getItem('trabalhador_cpf');
            localStorage.removeItem('trabalhador_cpf');
        }

        this.carregandoTimeline = true;
        const chamadas: any[] = this.DADOS_BASICOS.map((dado) => this.service.buscarValorParaInformacaoSaude(dado,
            this.cpf)
            .catch((err) => Observable.of(null)));
        chamadas.push(...this.DADOS_HISTORICOS.map((dado) => this.service.buscarHistoricoParaInformacaoSaude(dado,
            this.cpf)
            .catch((err) => Observable.of(null))));
        chamadas.push(this.service.buscarHistorico(this.cpf)
            .catch((err) => Observable.of(null)));
        chamadas.push(this.service.buscarPaciente(this.cpf));
        chamadas.push(this.service.buscaAlergias(this.cpf, 'ALERGIA')
            .catch((err) => Observable.of(null)));
        chamadas.push(this.service.buscaMedicamentos(this.cpf, 'MEDICAMENTO')
            .catch((err) => Observable.of(null)));
        chamadas.push(this.service.buscaVacinas(this.cpf, 'VACINA_HEPATITE_B')
            .catch((err) => Observable.of(null)));
        chamadas.push(this.service.buscaVacinas(this.cpf, 'VACINA_TRIPLICE_VIRAL')
            .catch((err) => Observable.of(null)));
        chamadas.push(this.service.buscaVacinas(this.cpf, 'VACINA_DUPLA_ADULTO')
            .catch((err) => Observable.of(null)));
        chamadas.push(this.service.buscaVacinas(this.cpf, 'VACINA_FEBRE_AMARELA')
            .catch((err) => Observable.of(null)));
        chamadas.push(this.service.buscaVacinas(this.cpf, 'VACINA_INFLUENZA')
            .catch((err) => Observable.of(null)));
        chamadas.push(this.service.buscaVacinas(this.cpf, 'VACINA_OUTRAS')
            .catch((err) => Observable.of(null)));
        chamadas.push(this.service.buscarImunizacao(['IMUNIZACAO_NOME', 'IMUNIZACAO_DATA'], this.cpf)
            .catch((err) => Observable.of(null)));
        Observable.forkJoin(chamadas).subscribe((result) => {
            this.tratarResultado({
                peso: result[0],
                altura: result[1],
                imc: result[2],
                idade: result[3],
                sistolica: result[4],
                diastolica: result[5],
                circunferenciaAbdominal: result[6],
                encontrosMedicos: result[7],
                paciente: result[8],
                alergias: result[9],
                medicamentos: result[10],
                imunizacao: result[17],
            });
        }, (error) => {
            this.mensagemError(error);
        });
    }


    private buscaDadosHistoricosComPeriodo(periodo?: Date) {
        this.cpf = (Seguranca.getUsuario() as any).sub;
        if (localStorage.getItem('trabalhador_cpf')) {
            this.cpf = localStorage.getItem('trabalhador_cpf');
            localStorage.removeItem('trabalhador_cpf');
        }
        let encontrosMedico: any;
        this.carregandoTimeline = true;
        this.service.buscarHistorico(this.cpf, periodo).subscribe((result) => {
            encontrosMedico = result;
            if (encontrosMedico) {
                this.carregarTimeline(encontrosMedico);
            }
            else {
                this.carregandoTimeline = false;
            }
            this.bloqueioService.desbloquear();
        }, (error) => {
            this.carregandoTimeline = false;
            this.bloqueioService.desbloquear();
            this.mensagemError(error);
        });
    }


    private tratarResultado(dados) {
        this.bloqueioService.bloquear();
        if (dados) {
            const encontrosMedicos = dados.encontrosMedicos;
            this.tratarDadosBasicos(dados);
            this.tratarMedicamentos(dados.medicamentos);
            this.tratarAlergias(dados.alergias);
            this.tratarVacinas(dados.vacinas);
            this.tratarImunizacao(dados.imunizacao);
            if (encontrosMedicos) {
                this.carregarTimeline(encontrosMedicos);
            }
            else {
                this.carregandoTimeline = false;
            }
        }
        else {
            this.carregandoTimeline = false;
            this.mensagemError('Não existem informações médicas para este trabalhador');
        }
        this.bloqueioService.desbloquear();
    }

    private buscarDadosTrabalhador() {
        if (this.cpf !== null) {
            this.trabalhadorService.buscarVacinasAlergiasMedicamentosAutoDeclarados(this.cpf).subscribe(trabalhador => {
                this.trabalhador = trabalhador;
            }, error => {
                this.mensagemError(error);
            });
        }
    }

    buscarVacinasAutodeclaradas() {
        if (this.cpf !== null) {
            this.imunizacaoService.buscaVacinasAutodeclaradas(this.QUANTIDADE_PESQUISA_VACINA, this.cpf).subscribe((retorno: Vacina[]) => {
                this.vacinasAutodeclaradas = retorno;
            }, error => {
                this.mensagemError(error);
            });
        }
    }

    atualizarPeriodo(periodo: string) {

        this.limparPaginacao();

        if (periodo == "Último mês") {
            this.periodoEscolhido = periodo;
            this.periodoEscolhidoDate = moment(new Date()).subtract(30, 'days');
            this.buscaDadosHistoricosComPeriodo(this.periodoEscolhidoDate);


        }
        else if (periodo == "03 meses") {
            this.periodoEscolhido = periodo;
            this.periodoEscolhidoDate = moment(new Date()).subtract(90, 'days');
            this.buscaDadosHistoricosComPeriodo(this.periodoEscolhidoDate);


        }
        else if (periodo == "06 meses") {
            this.periodoEscolhido = periodo;
            this.periodoEscolhidoDate = moment(new Date()).subtract(180, 'days');
            this.buscaDadosHistoricosComPeriodo(this.periodoEscolhidoDate);


        }
        else if (periodo == "09 meses") {
            this.periodoEscolhido = periodo;
            this.periodoEscolhidoDate = moment(new Date()).subtract(270, 'days');
            this.buscaDadosHistoricosComPeriodo(this.periodoEscolhidoDate);


        }
        else if (periodo == "12 meses") {
            this.periodoEscolhido = periodo;
            this.periodoEscolhidoDate = moment(new Date()).subtract(365, 'days');
            this.buscaDadosHistoricosComPeriodo(this.periodoEscolhidoDate);


        }
        else if (periodo == "Todos") {
            this.periodoEscolhido = periodo;
            this.periodoEscolhidoDate = new Date(0);
            this.buscaDadosHistoricosComPeriodo(this.periodoEscolhidoDate);
        }
    }

    private limparPaginacao() {
        this.numeroEncontros = 0;
        this.tamanhoPagina = 5;
        this.paginaAtual = 0;
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
