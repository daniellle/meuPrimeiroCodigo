import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { BaseComponent } from "../../base.component";
import { ProximaDoseDTO } from "../../../modelo/proximaDoseDTO";
import { ActivatedRoute, Router } from "@angular/router";
import { ImunizacaoService } from "../../../servico/imunizacao.service";
import { BloqueioService } from "../../../servico/bloqueio.service";
import { ToastyService } from "ng2-toasty";
import { environment } from "../../../../environments/environment";
import * as moment from 'moment';
import { IMyDpOptions, IMyDate, IMyMarkedDates, IMyOptions } from 'mydatepicker';

@Component({
    selector: 'app-tela-inicial-vacina',
    templateUrl: './tela-inicial-vacina.component.html',
    styleUrls: ['./tela-inicial-vacina.component.scss']
})
export class TelaInicialVacinaComponent extends BaseComponent implements OnInit {

    public idTrabalhador: number;
    public doses: ProximaDoseDTO[];
    public dataAtual: Date;
    public dataAtualCard: String;
    public dosesDoMes: ProximaDoseDTO[] = [];
    public myDatePickerOptions: IMyDpOptions = {
        openSelectorOnInputClick: false,
        indicateInvalidDate: true,
        editableDateField: false,
        dateFormat: 'dd/mm/yyyy',
        showClearDateBtn: false,
        inline: false,
        monthSelector: false,
        markDates: [],
      }


    constructor(
        private router: Router, private activatedRoute: ActivatedRoute,
        protected service: ImunizacaoService,
        protected bloqueioService: BloqueioService,
        protected dialogo: ToastyService
    ) {
        super(bloqueioService, dialogo);
    }

    ngOnInit() {
        this.dataAtual =  new Date();
      this.dataAtualCard = moment(this.dataAtual).locale("pt-br").format("MMMM YYYY");
        this.carregarTela();
        this.carregarProximasDoses();
        if(!this.carregarProximasDoses){
            this.proximasDosesNoCalendario();
        }
        this.doses = new Array<ProximaDoseDTO>();
        this.myDatePickerOptions.inline = true;
        this.myDatePickerOptions.selectorWidth = "100%";
        this.myDatePickerOptions.showTodayBtn = false;
        this.myDatePickerOptions.monthSelector = false;
        this.myDatePickerOptions.markDates = [];
    }

    novaVacina() {
        this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}/vacina-declarada/cadastrar`]);
    }

    verHistorico() {
        this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}/vacina-declarada/historico`]);
    }

    carregarProximasDoses() {
        this.service.ultimasProximasDoses().subscribe((retorno: ProximaDoseDTO[]) => {
            this.doses = retorno;
            if (retorno === null) {
                this.mensagemError("Não há nenhuma próxima dose cadastrada");
                return false;
            }else{
                return true;
            }
        }, (error) => {
            this.mensagemError(error);
        });
    }

    carregarTela() {
        this.activatedRoute.params.subscribe((params) => {
            this.idTrabalhador = params['id'];
        });
    }

    voltar() {
        this.router.navigate([`${environment.path_raiz_cadastro}/trabalhador/${this.idTrabalhador}`])
    }

    proximasDosesNoCalendario() {
        let mesAtual = this.dataAtual.getMonth() + 1;
        let anoAtual = this.dataAtual.getFullYear();
        this.service.proximasDosesDoMes(mesAtual, anoAtual).subscribe((retorno: ProximaDoseDTO[]) => {
            if(retorno != null && !retorno){
                this.dosesDoMes = retorno;
                let date = Array<IMyDate>();
                let lista = [];
                let color = '#5d0046';
                
                retorno.forEach(element => {
                    let myDate = {
                        year: moment(element.dataVacinacao).year(),
                        month: moment(element.dataVacinacao).month()+1,
                        day: moment(element.dataVacinacao).date()
                    }
                    lista.push(myDate);
                });
                let copy : IMyOptions = this.getCopyOfOptions(this.myDatePickerOptions);
                copy.markDates = [{dates: lista, color: color}];
                this.myDatePickerOptions = copy;
            }else {
                this.mensagemError("Não há nenhuma próxima dose cadastrada neste mês");
            }
        }, (error) => {
            this.mensagemError(error);
        })
    }

    ConstroiIMyDate(elemento: any): IMyDate{
        return {
            year: moment(elemento).year(),
            month: moment(elemento).month(),
            day: moment(elemento).day(),
          };
    }

    public getCopyOfOptions(options): IMyOptions {
        return JSON.parse(JSON.stringify(options));
    }

}
